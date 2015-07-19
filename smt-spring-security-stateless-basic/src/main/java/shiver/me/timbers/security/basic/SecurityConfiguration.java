/*
 * Copyright 2015 Karl Bennett
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package shiver.me.timbers.security.basic;

import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shiver.me.timbers.security.basic.security.servlet.HttpServletRequestBinder;
import shiver.me.timbers.security.basic.security.spring.SecurityContextHolder;
import shiver.me.timbers.security.basic.security.spring.StatelessAuthenticationFilter;
import shiver.me.timbers.security.basic.security.spring.StatelessAuthenticationSuccessHandler;
import shiver.me.timbers.security.basic.security.token.JwtTokenFactory;
import shiver.me.timbers.security.basic.security.token.TokenFactory;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private SecurityContextHolder securityContextHolder;

    @Autowired
    private HttpServletRequestBinder<Authentication> authenticationBinder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // The http.formLogin().defaultSuccessUrl("/path/") method is required when using stateless Spring Security
        // because the session cannot be used to redirect to the page that was requested while signed out. Unfortunately
        // using this configuration method will cause our custom success handler (below) to be overridden with the
        // default success handler. So to replicate the defaultSuccessUrl("/path/") configuration we will instead
        // correctly configure and delegate to the default success handler.
        final SimpleUrlAuthenticationSuccessHandler delegate = new SimpleUrlAuthenticationSuccessHandler();
        delegate.setDefaultTargetUrl("/spring/");

        // Make Spring Security stateless. This means no session will be created by Spring Security, nor will it use any
        // previously existing session.
        http.sessionManagement().sessionCreationPolicy(STATELESS);
        // Disable the CSRF prevention because it requires the session, which of course is not available in a
        // stateless application. It also greatly complicates the requirements for the sign in POST request.
        http.csrf().disable();
        // Viewing any page requires authentication.
        http.authorizeRequests().anyRequest().authenticated();
        http.formLogin()
            // Viewing the sign in page does not require authentication.
            .loginPage("/spring/signIn").permitAll()
            // Override the sign in success handler with our stateless implementation. This will update the response
            // with any headers and cookies that are required for subsequent authenticated requests.
            .successHandler(new StatelessAuthenticationSuccessHandler(authenticationBinder, delegate));
        http.logout().logoutUrl("/spring/signOut").logoutSuccessUrl("/spring/");
        // Add our stateless authentication filter before the default sign in filter. The default sign in filter is
        // still used for the initial sign in, but if a user is authenticated we need to acknowledge this before it is
        // reached.
        http.addFilterBefore(
            new StatelessAuthenticationFilter(authenticationBinder, securityContextHolder),
            UsernamePasswordAuthenticationFilter.class
        );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Set a custom user details service that reads the user credentials from an external data source. This will
        // only ever be used once within the Spring Security process, that is during the initial sign in. The
        // verification of all authenticated requests are stateless, that is it does not require access to any internal
        // or external state.
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    public TokenFactory tokenFactory() {
        return new JwtTokenFactory("some secret string", Jwts.builder(), Jwts.parser());
    }
}

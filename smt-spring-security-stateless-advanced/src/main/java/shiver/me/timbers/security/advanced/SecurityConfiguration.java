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

package shiver.me.timbers.security.advanced;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.servlet.configuration.EnableWebMvcSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import shiver.me.timbers.security.advanced.spring.UserAuthenticationFactory;
import shiver.me.timbers.security.advanced.token.JwtTokenFactory;
import shiver.me.timbers.security.data.UserRepository;
import shiver.me.timbers.security.spring.StatelessAuthenticationFilter;
import shiver.me.timbers.security.spring.StatelessAuthenticationSuccessHandler;
import shiver.me.timbers.security.token.TokenFactory;

import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebMvcSecurity
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenFactory tokenFactory;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.sessionManagement().sessionCreationPolicy(STATELESS);
        http.csrf().disable();
        http.authorizeRequests().anyRequest().authenticated();
        http.formLogin()
            .loginPage("/spring/signIn").permitAll()
            // The token factory is updated so that it now checks the expiry date.
            .successHandler(new StatelessAuthenticationSuccessHandler(tokenFactory, "/spring/"));
        http.logout().logoutUrl("/spring/signOut").logoutSuccessUrl("/spring/");
        http.addFilterBefore(
            // The authentication factory now returns the entire user as the principal.
            new StatelessAuthenticationFilter(tokenFactory, new UserAuthenticationFactory(userRepository)),
            UsernamePasswordAuthenticationFilter.class
        );
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }

    @Bean
    public JwtTokenFactory tokenFactory(@Value("${jwt.secret}") String secret, @Value("${jwt.expiry}") long expiry) {
        return new JwtTokenFactory(secret, expiry);
    }
}

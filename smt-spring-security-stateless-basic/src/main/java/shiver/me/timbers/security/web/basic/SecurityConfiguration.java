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

package shiver.me.timbers.security.web.basic;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import shiver.me.timbers.security.spring.StatelessWebSecurityConfigurerAdapter;

@Configuration
public class SecurityConfiguration extends StatelessWebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsService userDetailsService;

    @Override
    protected void configureFurther(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().authenticated();
        http.formLogin().loginPage("/spring/signIn").permitAll();
        http.logout().logoutUrl("/spring/signOut").logoutSuccessUrl("/spring/");
    }

    @Override
    protected String defaultSuccessUrl() {
        return "/spring/";
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        // Set a custom user details service that reads the user credentials from an external data source. This will
        // only ever be used once within the Spring Security process, that is during the initial sign in. The
        // verification of all authenticated requests are stateless, that is it does not require access to any internal
        // or external state.
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}

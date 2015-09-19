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

package shiver.me.timbers.security.web.advanced;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import shiver.me.timbers.security.spring.AuthenticationConverter;
import shiver.me.timbers.security.spring.StatelessWebSecurityConfigurerAdapter;
import shiver.me.timbers.security.token.TokenFactory;
import shiver.me.timbers.security.web.advanced.spring.UserAuthenticationConverter;
import shiver.me.timbers.security.web.advanced.token.ExpiringJwtTokenFactory;
import shiver.me.timbers.security.web.data.UserRepository;

import java.util.concurrent.TimeUnit;

@Configuration
public class SecurityConfiguration extends StatelessWebSecurityConfigurerAdapter<String> {

    @Value("${jwt.expiry.duration}")
    private long duration;

    @Value("${jwt.expiry.unit}")
    private TimeUnit unit;

    @Autowired
    private UserRepository userRepository;

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
    protected AuthenticationConverter<String> authenticationConverter() {
        return new UserAuthenticationConverter(userRepository);
    }

    @Bean
    @Override
    public TokenFactory<String> tokenFactory(String secret) {
        return new ExpiringJwtTokenFactory(secret, duration, unit);
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(NoOpPasswordEncoder.getInstance());
    }
}

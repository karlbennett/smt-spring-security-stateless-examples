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

package shiver.me.timbers.security.web.advanced.spring;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import shiver.me.timbers.security.web.domain.User;
import shiver.me.timbers.security.web.spring.UserDetails;

import javax.security.auth.Subject;
import java.util.Collection;

/**
 * @author Karl Bennett
 */
public class UserAuthentication implements Authentication {

    private final User user;
    private UserDetails userDetails;
    private boolean authenticated = true;

    public UserAuthentication(User user) {
        this.user = user;
        this.userDetails = new UserDetails(user);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userDetails.getAuthorities();
    }

    @Override
    public String getCredentials() {
        return user.getPassword();
    }

    @Override
    public UserDetails getDetails() {
        return userDetails;
    }

    @Override
    public String getPrincipal() {
        return user.getUsername();
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean authenticated) throws IllegalArgumentException {
        this.authenticated = authenticated;
    }

    @Override
    public String getName() {
        return user.getUsername();
    }

    public boolean implies(Subject subject) {
        return false;
    }
}

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

package shiver.me.timbers.security.basic.security.servlet;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import shiver.me.timbers.security.basic.domain.User;
import shiver.me.timbers.security.basic.security.spring.UserAuthentication;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author Karl Bennett
 */
@Component
public class UserAuthenticationFactory implements HttpServletRequestBinder<Authentication> {

    private final HttpServletRequestBinder<String> httpServletRequestBinder;

    @Autowired
    public UserAuthenticationFactory(HttpServletRequestBinder<String> httpServletRequestBinder) {
        this.httpServletRequestBinder = httpServletRequestBinder;
    }

    @Override
    public void add(HttpServletResponse response, Authentication authentication) {
        httpServletRequestBinder.add(response, authentication.getName());
    }

    @Override
    public UserAuthentication retrieve(HttpServletRequest request) {

        final String username = httpServletRequestBinder.retrieve(request);

        if (username != null) {

            return new UserAuthentication(new User(username, ""));
        }

        return null;
    }
}

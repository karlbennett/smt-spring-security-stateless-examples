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

package shiver.me.timbers.security.advanced.spring;

import org.springframework.security.core.Authentication;
import shiver.me.timbers.security.data.UserRepository;
import shiver.me.timbers.security.spring.AuthenticationFactory;

/**
 * @author Karl Bennett
 */
public class UserAuthenticationFactory implements AuthenticationFactory<String> {

    private final UserRepository userRepository;

    public UserAuthenticationFactory(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public Authentication create(String subject) {
        return new UserAuthentication(userRepository.findByUsername(subject));
    }
}

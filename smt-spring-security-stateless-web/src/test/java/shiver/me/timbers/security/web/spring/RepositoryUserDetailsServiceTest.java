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

package shiver.me.timbers.security.web.spring;

import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import shiver.me.timbers.security.web.data.UserRepository;
import shiver.me.timbers.security.web.domain.User;

import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertPropertyReflectionEquals;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class RepositoryUserDetailsServiceTest {

    @Test
    public void Can_load_user() {

        final String username = someString();
        final UserRepository userRepository = mock(UserRepository.class);

        final User expected = mock(User.class);

        // Given
        given(userRepository.findByUsername(username)).willReturn(expected);

        // When
        final UserDetails actual = new RepositoryUserDetailsService(userRepository).loadUserByUsername(username);

        // Then
        assertPropertyReflectionEquals("user", expected, actual);
    }

    @Test(expected = UsernameNotFoundException.class)
    public void Get_a_meaningful_error_when_the_user_cannot_be_loaded() {

        final String username = someString();
        final UserRepository userRepository = mock(UserRepository.class);

        // Given
        given(userRepository.findByUsername(username)).willReturn(null);

        // When
        new RepositoryUserDetailsService(userRepository).loadUserByUsername(username);
    }
}

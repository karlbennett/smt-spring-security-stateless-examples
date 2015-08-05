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

import org.junit.Test;
import org.springframework.security.core.Authentication;
import shiver.me.timbers.security.data.UserRepository;
import shiver.me.timbers.security.domain.User;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class UserAuthenticationConverterTest {

    @Test
    public void Can_create_an_authentication() {

        final UserRepository userRepository = mock(UserRepository.class);

        final String subject = someString();

        final User user = mock(User.class);

        // Given
        given(userRepository.findByUsername(subject)).willReturn(user);
        given(user.getUsername()).willReturn(subject);

        // When
        final Authentication actual = new UserAuthenticationConverter(userRepository).convert(subject);

        // Then
        assertThat(actual.getName(), equalTo(subject));
    }

    @Test
    public void Cannot_create_an_authentication_if_no_user_exists() {

        final UserRepository userRepository = mock(UserRepository.class);

        final String subject = someString();

        // Given
        given(userRepository.findByUsername(subject)).willReturn(null);

        // When
        final Authentication actual = new UserAuthenticationConverter(userRepository).convert(subject);

        // Then
        assertThat(actual, nullValue());
    }

    @Test
    public void Can_create_a_principal() {

        final String principal = someString();

        final Authentication authentication = mock(Authentication.class);

        // Given
        given(authentication.getName()).willReturn(principal);

        // When
        final String actual = new UserAuthenticationConverter(mock(UserRepository.class)).convert(authentication);

        // Then
        assertThat(actual, equalTo(principal));
    }
}
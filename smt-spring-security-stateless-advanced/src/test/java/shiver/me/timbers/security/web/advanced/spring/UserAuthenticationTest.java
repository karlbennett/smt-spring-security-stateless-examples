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

import org.junit.Test;
import org.springframework.security.core.userdetails.UserDetails;
import shiver.me.timbers.security.web.domain.User;

import javax.security.auth.Subject;

import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.unitils.reflectionassert.ReflectionAssert.assertPropertyReflectionEquals;
import static shiver.me.timbers.data.random.RandomBooleans.someBoolean;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class UserAuthenticationTest {

    @Test
    public void Can_create_a_user_authentication() {

        final User user = mock(User.class);
        final String username = someString();
        final String password = someString();
        final Boolean authenticated = someBoolean();

        // Given
        given(user.getUsername()).willReturn(username);
        given(user.getPassword()).willReturn(password);

        // When
        final UserAuthentication actual = new UserAuthentication(user);
        actual.setAuthenticated(authenticated);

        // Then
        assertPropertyReflectionEquals("user", user, actual);
        assertThat(actual.getName(), equalTo(username));
        assertThat(actual.getPrincipal(), equalTo(username));
        assertThat(actual.getCredentials(), equalTo(password));
        assertThat(actual.getAuthorities(), empty());
        assertThat(actual.isAuthenticated(), equalTo(authenticated));
        assertThat(actual.implies(new Subject()), equalTo(false));

        final UserDetails details = actual.getDetails();

        assertThat(details.getUsername(), equalTo(username));
        assertThat(details.getPassword(), equalTo(password));
        assertThat(details.getAuthorities(), empty());
        assertThat(details.isAccountNonExpired(), equalTo(true));
        assertThat(details.isAccountNonLocked(), equalTo(true));
        assertThat(details.isCredentialsNonExpired(), equalTo(true));
        assertThat(details.isEnabled(), equalTo(true));
    }
}

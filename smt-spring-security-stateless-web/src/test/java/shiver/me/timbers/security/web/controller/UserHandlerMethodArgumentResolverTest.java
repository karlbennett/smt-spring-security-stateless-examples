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

package shiver.me.timbers.security.web.controller;

import org.junit.Before;
import org.junit.Test;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.ModelAndViewContainer;
import shiver.me.timbers.security.web.data.UserRepository;
import shiver.me.timbers.security.web.domain.User;

import java.security.Principal;

import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class UserHandlerMethodArgumentResolverTest {

    private UserRepository userRepository;
    private UserHandlerMethodArgumentResolver resolver;

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() {
        userRepository = mock(UserRepository.class);

        resolver = new UserHandlerMethodArgumentResolver(userRepository);
    }

    @Test
    public void Can_resolver_a_user() throws Exception {

        final NativeWebRequest request = mock(NativeWebRequest.class);

        final Principal principal = mock(Principal.class);
        final String username = someString();

        final User expected = mock(User.class);

        // Given
        given(request.getUserPrincipal()).willReturn(principal);
        given(principal.getName()).willReturn(username);
        given(userRepository.findByUsername(username)).willReturn(expected);

        // When
        final User actual = resolver.resolveArgument(
            mock(MethodParameter.class),
            mock(ModelAndViewContainer.class),
            request,
            mock(WebDataBinderFactory.class)
        );

        // Then
        assertThat(actual, equalTo(expected));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Supports_user_parameters() {

        final MethodParameter parameter = mock(MethodParameter.class);

        final Class userClass = User.class;

        // Given
        given(parameter.getParameterType()).willReturn(userClass);

        // When
        final boolean actual = resolver.supportsParameter(parameter);

        // Then
        assertThat(actual, equalTo(true));
    }

    @Test
    @SuppressWarnings("unchecked")
    public void Does_not_support_user_sub_type_parameters() {

        final MethodParameter parameter = mock(MethodParameter.class);

        class SubUser extends User {
            public SubUser() {
                super(null, null);
            }
        }

        final Class userClass = SubUser.class;

        // Given
        given(parameter.getParameterType()).willReturn(userClass);

        // When
        final boolean actual = resolver.supportsParameter(parameter);

        // Then
        assertThat(actual, equalTo(false));
    }
}

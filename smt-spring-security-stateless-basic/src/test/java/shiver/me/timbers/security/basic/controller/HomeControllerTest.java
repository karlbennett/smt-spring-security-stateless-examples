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

package shiver.me.timbers.security.basic.controller;

import org.junit.Test;
import org.springframework.web.servlet.ModelAndView;
import shiver.me.timbers.security.basic.domain.User;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.mock;

public class HomeControllerTest {

    @Test
    public void Can_display_home_page() {

        final String username = "User Name";
        final User user = mock(User.class);

        // Given
        given(user.getUsername()).willReturn(username);

        // When
        final ModelAndView actual = new HomeController().display(user);

        // Then
        assertThat(actual.getViewName(), equalTo("home"));
        assertThat(actual.getModel().get("username").toString(), equalTo(username));
    }
}
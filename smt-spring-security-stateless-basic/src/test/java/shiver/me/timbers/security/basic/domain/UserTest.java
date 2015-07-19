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

package shiver.me.timbers.security.basic.domain;

import org.junit.Test;

import static java.lang.String.format;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;
import static shiver.me.timbers.data.random.RandomLongs.someLong;
import static shiver.me.timbers.data.random.RandomStrings.someAlphaNumericString;

public class UserTest {

    @Test
    public void Users_with_the_same_values_are_equal() {

        // Given
        final Long id = someLong();
        final String username = someAlphaNumericString();
        final String password = someAlphaNumericString();

        final User expected = new User(id, username, password);

        // When
        final User actual = new User(id, username, password);

        // Then
        assertThat(actual, equalTo(actual));
        assertThat(actual, equalTo(expected));
        assertThat(new User(null, username, password), equalTo(new User(null, username, password)));
        assertThat(new User(id, null, password), equalTo(new User(id, null, password)));
        assertThat(new User(id, username, null), equalTo(new User(id, username, null)));

        assertThat(actual.hashCode(), equalTo(expected.hashCode()));
    }

    @Test
    public void Users_are_not_equal_to_other_classes() {

        // When
        final User actual = new User(someAlphaNumericString(), someAlphaNumericString());

        // Then
        assertThat(actual, not(equalTo(new Object())));
    }

    @Test
    public void Users_with_the_different_ids_are_not_equal() {

        // Given
        final Long id = someLong();
        final String password = someAlphaNumericString();
        final String username = someAlphaNumericString();

        final User expected = new User(someLong(), username, password);

        // When
        final User actual = new User(id, username, password);

        // Then
        assertThat(actual, not(equalTo(expected)));
        assertThat(actual, not(equalTo(new User(null, username, password))));
        assertThat(new User(null, username, password), not(equalTo(actual)));

        assertThat(actual.hashCode(), not(equalTo(expected.hashCode())));
        assertThat(actual.hashCode(), not(equalTo(new User(null, username, password).hashCode())));
    }

    @Test
    public void Users_with_the_different_user_names_are_not_equal() {

        // Given
        final Long id = someLong();
        final String password = someAlphaNumericString();
        final String username = someAlphaNumericString();

        final User expected = new User(id, someAlphaNumericString(), password);

        // When
        final User actual = new User(id, username, password);

        // Then
        assertThat(actual, not(equalTo(expected)));
        assertThat(actual, not(equalTo(new User(id, null, password))));
        assertThat(new User(id, null, password), not(equalTo(actual)));

        assertThat(actual.hashCode(), not(equalTo(expected.hashCode())));
        assertThat(actual.hashCode(), not(equalTo(new User(id, null, password).hashCode())));
    }

    @Test
    public void Users_with_the_different_passwords_are_not_equal() {

        // Given
        final Long id = someLong();
        final String username = someAlphaNumericString();
        final String password = someAlphaNumericString();

        final User expected = new User(id, username, someAlphaNumericString());

        // When
        final User actual = new User(id, username, password);

        // Then
        assertThat(actual, not(equalTo(expected)));
        assertThat(actual, not(equalTo(new User(id, username, null))));
        assertThat(new User(id, username, null), not(equalTo(actual)));

        assertThat(actual.hashCode(), not(equalTo(expected.hashCode())));
        assertThat(actual.hashCode(), not(equalTo(new User(id, username, null).hashCode())));
    }

    @Test
    public void Can_to_string_a_user() {

        // Given
        final Long id = someLong();
        final String username = someAlphaNumericString();
        final String password = someAlphaNumericString();

        final String expected = format("User {\n  id=%d,\n  username='%s',\n  password='%s'\n}", id, username, password);

        // When
        final String actual = new User(id, username, password).toString();

        // Then
        assertThat(actual, equalTo(expected));
    }
}

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

package shiver.me.timbers.security.test.step;

import cucumber.api.java.en.Given;
import org.springframework.beans.factory.annotation.Autowired;
import shiver.me.timbers.security.data.UserRepository;
import shiver.me.timbers.security.domain.User;

public class BackgroundUserSteps extends SpringBootIntegrationSteps {

    @Autowired
    private UserRepository repository;

    @Given("^a user with the username \"([^\"]*)\" and password \"([^\"]*)\" exists$")
    public void a_user_with_the_username_and_password_exists(String username, String password) {
        repository.save(new User(username, password));
    }
}

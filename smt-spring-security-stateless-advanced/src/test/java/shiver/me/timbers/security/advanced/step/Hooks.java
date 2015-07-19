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

package shiver.me.timbers.security.advanced.step;

import cucumber.api.java.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import shiver.me.timbers.security.advanced.token.JwtTokenFactory;
import shiver.me.timbers.security.step.SpringBootIntegrationSteps;

import java.lang.reflect.Field;

public class Hooks extends SpringBootIntegrationSteps {

    private static final Field EXPIRY_FIELD = expiryField();

    private static Field expiryField() {
        try {
            final Field expiry = JwtTokenFactory.class.getDeclaredField("expiry");
            expiry.setAccessible(true);
            return expiry;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Value("${jwt.expiry}")
    private long expiry;

    @Autowired
    private JwtTokenFactory jwtTokenFactory;

    @Before
    public void setup() throws IllegalAccessException {
        setExpiry(jwtTokenFactory, expiry);
    }

    public static void setExpiry(JwtTokenFactory jwtTokenFactory, long expiry) throws IllegalAccessException {
        EXPIRY_FIELD.set(jwtTokenFactory, expiry);
    }
}

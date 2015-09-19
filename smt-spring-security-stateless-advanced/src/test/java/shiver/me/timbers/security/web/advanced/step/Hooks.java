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

package shiver.me.timbers.security.web.advanced.step;

import cucumber.api.java.Before;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import shiver.me.timbers.security.test.step.SpringBootIntegrationSteps;
import shiver.me.timbers.security.web.advanced.token.ExpiringJwtTokenFactory;

import java.lang.reflect.Field;
import java.util.concurrent.TimeUnit;

public class Hooks extends SpringBootIntegrationSteps {

    private static final Field DURATION_FIELD = getField(ExpiringJwtTokenFactory.class, "duration");
    private static final Field UNIT_FIELD = getField(ExpiringJwtTokenFactory.class, "unit");

    private static Field getField(Class type, String name) {
        try {
            final Field expiry = type.getDeclaredField(name);
            expiry.setAccessible(true);
            return expiry;
        } catch (NoSuchFieldException e) {
            throw new RuntimeException(e);
        }
    }

    @Value("${jwt.expiry.duration}")
    private long duration;

    @Value("${jwt.expiry.unit}")
    private TimeUnit unit;

    @Autowired
    private ExpiringJwtTokenFactory expiringJwtTokenFactory;

    @Before
    public void setup() throws IllegalAccessException {
        setDuration(duration);
        setUnit(unit);
    }

    public void setDuration(long duration) throws IllegalAccessException {
        DURATION_FIELD.set(expiringJwtTokenFactory, duration);
    }

    public void setUnit(TimeUnit unit) throws IllegalAccessException {
        UNIT_FIELD.set(expiringJwtTokenFactory, unit);
    }
}

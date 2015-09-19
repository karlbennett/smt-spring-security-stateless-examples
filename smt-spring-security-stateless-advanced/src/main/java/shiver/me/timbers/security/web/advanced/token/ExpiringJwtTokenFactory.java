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

package shiver.me.timbers.security.web.advanced.token;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import shiver.me.timbers.security.token.JwtTokenFactory;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author Karl Bennett
 */
public class ExpiringJwtTokenFactory extends JwtTokenFactory<String> {

    private final long duration;
    private final TimeUnit unit;

    public ExpiringJwtTokenFactory(String secret, long duration, TimeUnit unit) {
        super(String.class, secret);
        this.duration = duration;
        this.unit = unit;
    }

    @Override
    protected JwtBuilder configure(JwtBuilder jwtBuilder) {
        return jwtBuilder.setExpiration(expiry());
    }

    @Override
    public String parse(String token) {
        try {
            return super.parse(token);
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    private Date expiry() {
        return new Date(System.currentTimeMillis() + unit.toMillis(duration));
    }
}

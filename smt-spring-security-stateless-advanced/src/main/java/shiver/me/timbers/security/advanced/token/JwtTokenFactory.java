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

package shiver.me.timbers.security.advanced.token;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import shiver.me.timbers.security.token.TokenFactory;

import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;

/**
 * @author Karl Bennett
 */
public class JwtTokenFactory implements TokenFactory<String> {

    private static final long FIVE_MINUTES = 300000L;

    private final String secret;
    private final JwtBuilder jwtBuilder;
    private final JwtParser jwtParser;
    private final long expiry;

    public JwtTokenFactory(String secret, long expiry) {
        this(secret, Jwts.builder(), Jwts.parser(), expiry);
    }

    public JwtTokenFactory(String secret, JwtBuilder jwtBuilder, JwtParser jwtParser) {
        this(secret, jwtBuilder, jwtParser, FIVE_MINUTES);
    }

    public JwtTokenFactory(String secret, JwtBuilder jwtBuilder, JwtParser jwtParser, long expiry) {
        this.secret = secret;
        this.jwtBuilder = jwtBuilder;
        this.jwtParser = jwtParser;
        this.expiry = expiry;
    }

    @Override
    public String create(String subject) {
        return jwtBuilder.setSubject(subject).setExpiration(fiveMinutes()).signWith(HS512, secret).compact();
    }

    @Override
    public String parse(String token) {
        try {
            return jwtParser.setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
        } catch (ExpiredJwtException e) {
            return null;
        }
    }

    private Date fiveMinutes() {
        return new Date(System.currentTimeMillis() + expiry);
    }
}

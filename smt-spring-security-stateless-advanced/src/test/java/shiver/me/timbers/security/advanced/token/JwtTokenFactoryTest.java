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

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Header;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;

import static io.jsonwebtoken.SignatureAlgorithm.HS512;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.argThat;
import static org.mockito.Mockito.mock;
import static shiver.me.timbers.data.random.RandomStrings.someString;

public class JwtTokenFactoryTest {

    private String secret;
    private JwtBuilder jwtBuilder;
    private JwtParser jwtParser;
    private JwtTokenFactory factory;

    @Before
    public void setUp() {
        secret = someString();
        jwtBuilder = mock(JwtBuilder.class);
        jwtParser = mock(JwtParser.class);
        factory = new JwtTokenFactory(secret, jwtBuilder, jwtParser);
    }

    @Test
    public void Can_create_a_token() {

        final String subject = someString();

        final JwtBuilder subjectJwtBuilder = mock(JwtBuilder.class);
        final JwtBuilder expirationJwtBuilder = mock(JwtBuilder.class);
        final JwtBuilder signedJwtBuilder = mock(JwtBuilder.class);

        final String expected = someString();

        // Given
        given(jwtBuilder.setSubject(subject)).willReturn(subjectJwtBuilder);
        given(subjectJwtBuilder.setExpiration(argThat(greaterThan(new Date())))).willReturn(expirationJwtBuilder);
        given(expirationJwtBuilder.signWith(HS512, secret)).willReturn(signedJwtBuilder);
        given(signedJwtBuilder.compact()).willReturn(expected);

        // When
        final String actual = factory.create(subject);

        // Then
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void Can_parse_a_subject_from_a_token() {

        final String token = someString();

        final JwtParser signingKeyJwtParser = mock(JwtParser.class);
        @SuppressWarnings("unchecked")
        final Jws<Claims> jws = mock(Jws.class);
        final Claims body = mock(Claims.class);
        final String expected = someString();

        // Given
        given(jwtParser.setSigningKey(secret)).willReturn(signingKeyJwtParser);
        given(signingKeyJwtParser.parseClaimsJws(token)).willReturn(jws);
        given(jws.getBody()).willReturn(body);
        given(body.getExpiration()).willReturn(new Date(System.currentTimeMillis() + 60000));
        given(body.getSubject()).willReturn(expected);

        // When
        final String actual = factory.parse(token);

        // Then
        assertThat(actual, equalTo(expected));
    }

    @Test
    public void Cannot_parse_a_subject_from_an_expired_token() {

        final String token = someString();

        final JwtParser signingKeyJwtParser = mock(JwtParser.class);

        // Given
        given(jwtParser.setSigningKey(secret)).willReturn(signingKeyJwtParser);
        given(signingKeyJwtParser.parseClaimsJws(token))
            .willThrow(new ExpiredJwtException(mock(Header.class), mock(Claims.class), ""));

        // When
        final String actual = factory.parse(token);

        // Then
        assertThat(actual, nullValue());
    }
}
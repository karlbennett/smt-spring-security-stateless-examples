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

import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import shiver.me.timbers.security.basic.data.UserRepository;
import shiver.me.timbers.security.basic.domain.User;
import shiver.me.timbers.security.basic.security.servlet.HttpServletRequestBinder;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Karl Bennett
 */
public class UserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {

    private final HttpServletRequestBinder<String> httpServletRequestBinder;
    private final UserRepository userRepository;

    public UserHandlerMethodArgumentResolver(HttpServletRequestBinder<String> httpServletRequestBinder, UserRepository userRepository) {
        this.httpServletRequestBinder = httpServletRequestBinder;
        this.userRepository = userRepository;
    }

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return User.class.equals(parameter.getParameterType());
    }

    @Override
    public User resolveArgument(MethodParameter parameter, ModelAndViewContainer container, NativeWebRequest request,
                                WebDataBinderFactory binderFactory) throws Exception {
        return userRepository.findByUsername(httpServletRequestBinder.retrieve((HttpServletRequest) request.getNativeRequest()));
    }
}

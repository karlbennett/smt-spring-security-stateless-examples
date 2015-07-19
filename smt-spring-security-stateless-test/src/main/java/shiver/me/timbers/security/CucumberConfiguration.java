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

package shiver.me.timbers.security;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.opera.OperaDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import static java.lang.String.format;

@Configuration
@ComponentScan(basePackages = {"test.shiver.me.timbers", "shiver.me.timbers"})
public class CucumberConfiguration {

    @Bean(destroyMethod = "quit")
    public WebDriver webDriver(@Value("${web.driver:htmlunit}") String webDriver) {

        if ("htmlunit".equals(webDriver)) {
            return new HtmlUnitDriver();
        }

        if ("chrome".equals(webDriver)) {
            return new ChromeDriver();
        }

        if ("firefox".equals(webDriver)) {
            return new FirefoxDriver();
        }

        if ("ie".equals(webDriver)) {
            return new InternetExplorerDriver();
        }

        if ("safari".equals(webDriver)) {
            return new SafariDriver();
        }

        if ("opera".equals(webDriver)) {
            return new OperaDriver();
        }

        throw new IllegalArgumentException(format("Web driver %s not supported.", webDriver));
    }
}

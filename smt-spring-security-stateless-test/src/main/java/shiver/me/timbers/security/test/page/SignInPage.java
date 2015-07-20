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

package shiver.me.timbers.security.test.page;

import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SignInPage {

    private final WebDriver driver;
    private final BaseUrl baseUrl;
    private final Finders finders;
    private final Bys bys;

    @Autowired
    public SignInPage(WebDriver driver, BaseUrl baseUrl, Finders finders, Bys bys) {
        this.driver = driver;
        this.baseUrl = baseUrl;
        this.finders = finders;
        this.bys = bys;
    }

    public void enterUsername(String username) {
        finders.enterTextByLabel("User Name", username);
    }

    public void enterPassword(String password) {
        finders.enterTextByLabel("Password", password);
    }

    public HomePage signIn() {
        driver.findElement(bys.byValue("Sign In")).click();
        return new HomePage(driver, baseUrl);
    }

    public SignInPage signOut() {
        driver.manage().deleteAllCookies();
        return this;
    }

    public String getTitle() {
        return driver.getTitle();
    }
}

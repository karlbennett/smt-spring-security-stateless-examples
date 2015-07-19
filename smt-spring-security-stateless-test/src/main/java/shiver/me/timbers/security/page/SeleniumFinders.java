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

package shiver.me.timbers.security.page;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SeleniumFinders implements Finders {

    private final WebDriver driver;
    private final Bys bys;

    @Autowired
    public SeleniumFinders(WebDriver driver, Bys bys) {
        this.driver = driver;
        this.bys = bys;
    }

    @Override
    public String findIdByLabel(String text) {
        return driver.findElement(bys.byLabel(text)).getAttribute("for");
    }

    @Override
    public void enterTextByLabel(String labelName, String text) {
        driver.findElement(By.id(findIdByLabel(labelName))).sendKeys(text);
    }
}

/**
 *    Copyright 2010-2020 the original author or authors.
 *
 *    Licensed under the Apache License, Version 2.0 (the "License");
 *    you may not use this file except in compliance with the License.
 *    You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *    Unless required by applicable law or agreed to in writing, software
 *    distributed under the License is distributed on an "AS IS" BASIS,
 *    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *    See the License for the specific language governing permissions and
 *    limitations under the License.
 */
package org.mybatis.jpetstore;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.WebDriverWait;

public class SeleniumTest {

  // Variables globales
  String URLSite = "http://127.0.0.1:8090/jpetstore";
  WebDriver driver;

  @Before
  public void miseEnConditionsInitiales() throws InterruptedException {
    // logger.info("Deploiement des conditions initiales de test");

    // Instanciation driver
    System.setProperty("webdriver.gecko.driver", "src/test/resources/geckodriver.exe");
    driver = new FirefoxDriver();
    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

    // Instanciation pageIndex
    driver.get(URLSite);

  }

  @After
  public void cloture() {
    // logger.info("Test terminé ou interrompu - Fermeture du WebDriver");
    driver.quit();
  }

  @Test
  public void test1() throws InterruptedException {
    // logger.info("Exécution du test : ");

    // Accès à la page d'accueil
    driver.findElement(By.xpath("//a")).click();
    assertEquals(URLSite + "/actions/Catalog.action", driver.getCurrentUrl());

    // Accès à la page d'authentification
    driver.findElement(By.xpath("//div[@id=\"MenuContent\"]//a[2]")).click();
    Thread.sleep(2000);

    // Authentification
    WebElement champ_login = driver.findElement(By.xpath("//input[@name=\"username\"]"));
    champ_login.clear();
    champ_login.sendKeys("j2ee");

    WebElement champ_mdp = driver.findElement(By.xpath("//input[@name=\"password\"]"));
    champ_mdp.clear();
    champ_mdp.sendKeys("j2ee");

    driver.findElement(By.xpath("//input[@name=\"signon\"]")).click();
    assertTrue(driver.findElement(By.xpath("//div[@id=\"WelcomeContent\"]")).getText().contains("Welcome ABC"));

    // Faire ses courses : ajouter un reptile au panier
    driver.findElement(By.xpath("//img[@src=\"../images/sm_reptiles.gif\"]")).click();
    driver.findElement(By.xpath("//a[@href=\"/jpetstore/actions/Catalog.action?viewProduct=&productId=RP-SN-01\"]"))
        .click();
    driver.findElement(By.xpath("//a[@href=\"/jpetstore/actions/Cart.action?addItemToCart=&workingItemId=EST-11\"]"))
        .click();

    // Consulter le panier et modifier la quantitié
    WebElement champ_qte = driver.findElement(By.xpath("//input[@name=\"EST-11\"]"));
    champ_qte.clear();
    champ_qte.sendKeys("2");
    driver.findElement(By.xpath("//tbody/tr[3]/td[1]/input")).click();
    Thread.sleep(2000);
    assertTrue(driver.findElement(By.xpath("//tbody/tr[3]/td[1]")).getText().contains("$37,00"));

    // Procéder au paiement
    driver.findElement(By.xpath("//a[@href=\"/jpetstore/actions/Order.action?newOrderForm=\"]")).click();
    driver.findElement(By.xpath("//input[@name=\"newOrder\"]")).click();
    driver.findElement(By.xpath("//a[@href=\"/jpetstore/actions/Order.action?newOrder=&confirmed=true\"]")).click();

    assertEquals("Thank you, your order has been submitted.", driver.findElement(By.xpath("//ul/li")).getText());

    // Se déconnecter
    driver.findElement(By.xpath("//a[@href=\"/jpetstore/actions/Account.action?signoff=\"]")).click();
    assertTrue(
        driver.findElement(By.xpath("//a[@href=\"/jpetstore/actions/Account.action?signonForm=\"]")).isDisplayed());

  }

}

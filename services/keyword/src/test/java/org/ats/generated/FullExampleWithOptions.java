 

package org.ats.generated;

import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.Test;
import static org.testng.Assert.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.DataProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.concurrent.TimeUnit;
import java.util.Date;
import java.io.File;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.*;
import static org.openqa.selenium.OutputType.*;

public class FullExampleWithOptions {

  private RemoteWebDriver wd;

  @BeforeMethod
  public void setUp() throws Exception {
    wd = new FirefoxDriver();
    wd.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
    wd.manage().window().maximize();
  }
   
  @AfterMethod
  public void tearDown() {
    wd.quit();
  }
  
  
  @Test
  public void test9a8a1178() throws Exception {
    System.out.println("\n");
    System.out.println("[INFO] Perform get url \"http://saucelabs.com/test/guinea-pig/\"");
    wd.get("http://saucelabs.com/test/guinea-pig/");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform clickElement at \"i am a link\"");
    wd.findElement(By.linkText("i am a link")).click();

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform storeTitle with variable \"title\"");
    String title = wd.getTitle();

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyTitle title \"" + title + "\"");
    if (!wd.getTitle().equals(title)) {
      System.out.println("verifyTitle failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyTitle title \"asdf\"");
    if (wd.getTitle().equals("asdf")) {
      System.out.println("!verifyTitle failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertTitle title \"" + title + "\"");
    assertEquals(wd.getTitle(), title);

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertTitle title \"asdf\"");
    assertNotEquals(wd.getTitle(), "asdf");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform storeCurrentUrl with variable \"url\"");
    String url = wd.getCurrentUrl();

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyCurrentUrl url \"" + url + "\"");
    if (!wd.getCurrentUrl().equals(url)) {
      System.out.println("verifyCurrentUrl failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyCurrentUrl url \"http://google.com\"");
    if (wd.getCurrentUrl().equals("http://google.com")) {
      System.out.println("!verifyCurrentUrl failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertCurrentUrl url \"" + url + "\"");
    assertEquals(wd.getCurrentUrl(), url);

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertCurrentUrl url \"http://google.com\"");
    assertNotEquals(wd.getCurrentUrl(), "http://google.com");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform storeText at \"i_am_an_id\" with variable \"text\"");
    String text = wd.findElement(By.id("i_am_an_id")).getText();

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyText at \"i_am_an_id\" value \"" + text + "\"");
    if (!wd.findElement(By.id("i_am_an_id")).getText().equals(text)) {
      System.out.println("verifyText failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyText at \"i_am_an_id\" value \"not " + text + "\"");
    if (wd.findElement(By.id("i_am_an_id")).getText().equals("not \" + text + \"")) {
      System.out.println("!verifyText failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertText at \"i_am_an_id\" value \"" + text + "\"");
    assertEquals(wd.findElement(By.id("i_am_an_id")).getText(), text);

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertText at \"i_am_an_id\" value \"not " + text + "\"");
    assertNotEquals(wd.findElement(By.id("i_am_an_id")).getText(), "not \" + text + \"");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform storeTextPresent value \"I am another div\" with variable \"text_is_present\"");
    boolean text_is_present = wd.findElement(By.tagName("html")).getText().contains("I am another div");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform store value \"I am another div\" with variable \"text_present\"");
    String text_present = "I am another div";

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyTextPresent value \"" + text_present + "\"");
    if (!wd.findElement(By.tagName("html")).getText().contains(text_present)) {
      System.out.println("verifyTextPresent failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyTextPresent value \"not " + text_present + "\"");
    if (wd.findElement(By.tagName("html")).getText().contains("not \" + text_present + \"")) {
      System.out.println("!verifyTextPresent failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertTextPresent value \"" + text_present + "\"");
    assertTrue(wd.findElement(By.tagName("html")).getText().contains(text_present));

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertTextPresent value \"not " + text_present + "\"");
    assertFalse(wd.findElement(By.tagName("html")).getText().contains("not \" + text_present + \""));

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform storeBodyText with variable \"body_text\"");
    String body_text = wd.findElement(By.tagName("html")).getText();

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyBodyText value \"" + body_text + "\"");
    if (!wd.findElement(By.tagName("html")).getText().equals(body_text)) {
      System.out.println("verifyBodyText failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyBodyText value \"not " + body_text + "\"");
    if (wd.findElement(By.tagName("html")).getText().equals("not \" + body_text + \"")) {
      System.out.println("!verifyBodyText failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertBodyText value \"" + body_text + "\"");
    assertEquals(wd.findElement(By.tagName("html")).getText(), body_text);

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertBodyText value \"not " + body_text + "\"");
    assertNotEquals(wd.findElement(By.tagName("html")).getText(), "not \" + body_text + \"");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform storePageSource with variable \"page_source\"");
    String page_source = wd.getPageSource();

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyPageSource source \"" + page_source + "\"");
    if (!wd.getPageSource().equals(page_source)) {
      System.out.println("verifyPageSource failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyPageSource source \"<!-- --> " + page_source + "\"");
    if (wd.getPageSource().equals("<!-- --> \" + page_source + \"")) {
      System.out.println("!verifyPageSource failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertPageSource source \"" + page_source + "\"");
    assertEquals(wd.getPageSource(), page_source);

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertPageSource source \"<!-- --> " + page_source + "\"");
    assertNotEquals(wd.getPageSource(), "<!-- --> \" + page_source + \"");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform addCookie name \"test_cookie\" options \"path=/,max_age=100000000\" value \"this-is-a-cookie\"");
    wd.manage().addCookie(new Cookie.Builder("test_cookie", "this-is-a-cookie").path("/").expiresOn(new Date(new Date().getTime() + 100000000000l)).build());

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform storeCookiePresent name \"test_cookie\" with variable \"cookie_is_present\"");
    boolean cookie_is_present = (wd.manage().getCookieNamed("test_cookie") != null);

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform storeCookieByName name \"test_cookie\" with variable \"cookie\"");
    String cookie = wd.manage().getCookieNamed("test_cookie").getValue();

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform print value \"" + cookie + ";\"");
    System.out.println("\" + cookie + \";");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyCookiePresent name \"test_cookie\"");
    if (!(wd.manage().getCookieNamed("test_cookie") != null)) {
      System.out.println("verifyCookiePresent failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertCookiePresent name \"test_cookie\"");
    assertTrue((wd.manage().getCookieNamed("test_cookie") != null));

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyCookieByName name \"test_cookie\" value \"" + cookie + "\"");
    if (!wd.manage().getCookieNamed("test_cookie").getValue().equals(cookie)) {
      System.out.println("verifyCookieByName failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyCookieByName name \"test_cookie\" value \"not " + cookie + "\"");
    if (wd.manage().getCookieNamed("test_cookie").getValue().equals("not \" + cookie + \"")) {
      System.out.println("!verifyCookieByName failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertCookieByName name \"test_cookie\" value \"" + cookie + "\"");
    assertEquals(wd.manage().getCookieNamed("test_cookie").getValue(), cookie);

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertCookieByName name \"test_cookie\" value \"not " + cookie + "\"");
    assertNotEquals(wd.manage().getCookieNamed("test_cookie").getValue(), "not \" + cookie + \"");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform deleteCookie name \"test_cookie\"");
    if (wd.manage().getCookieNamed("test_cookie") != null) {
      wd.manage().deleteCookie(wd.manage().getCookieNamed("test_cookie"));
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyCookiePresent name \"test_cookie\"");
    if ((wd.manage().getCookieNamed("test_cookie") != null)) {
      System.out.println("!verifyCookiePresent failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertCookiePresent name \"test_cookie\"");
    assertFalse((wd.manage().getCookieNamed("test_cookie") != null));

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform refresh");
    wd.navigate().refresh();

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform goBack");
    wd.navigate().back();

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform goForward");
    wd.navigate().forward();

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform goBack");
    wd.navigate().back();

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform saveScreenshot file \"/tmp/screen.png\"");
    wd.getScreenshotAs(FILE).renameTo(new File("/tmp/screen.png"));

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform print value \"this is some debug text\"");
    System.out.println("this is some debug text");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform storeElementSelected at \"unchecked_checkbox\" with variable \"element_is_selected\"");
    boolean element_is_selected = (wd.findElement(By.id("unchecked_checkbox")).isSelected());

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform setElementSelected at \"unchecked_checkbox\"");
    if (!wd.findElement(By.id("unchecked_checkbox")).isSelected()) {
      wd.findElement(By.id("unchecked_checkbox")).click();
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyElementSelected at \"unchecked_checkbox\"");
    if (!(wd.findElement(By.id("unchecked_checkbox")).isSelected())) {
      System.out.println("verifyElementSelected failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertElementSelected at \"unchecked_checkbox\"");
    assertTrue((wd.findElement(By.id("unchecked_checkbox")).isSelected()));

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform setElementNotSelected at \"unchecked_checkbox\"");
    if (wd.findElement(By.id("unchecked_checkbox")).isSelected()) {
      wd.findElement(By.id("unchecked_checkbox")).click();
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyElementSelected at \"unchecked_checkbox\"");
    if ((wd.findElement(By.id("unchecked_checkbox")).isSelected())) {
      System.out.println("!verifyElementSelected failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertElementSelected at \"unchecked_checkbox\"");
    assertFalse((wd.findElement(By.id("unchecked_checkbox")).isSelected()));

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform storeElementAttribute at \"i am a link\" attribute name \"href\" with variable \"link_href\"");
    String link_href = wd.findElement(By.linkText("i am a link")).getAttribute("href");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyElementAttribute at \"i am a link\" attribute name \"href\" value \"" + link_href + "\"");
    if (!wd.findElement(By.linkText("i am a link")).getAttribute("href").equals(link_href)) {
      System.out.println("verifyElementAttribute failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertElementAttribute at \"i am a link\" attribute name \"href\" value \"" + link_href + "\"");
    assertEquals(wd.findElement(By.linkText("i am a link")).getAttribute("href"), link_href);

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform sendKeysToElement at \"comments\" value \"w00t\"");
    wd.findElement(By.id("comments")).click();
    wd.findElement(By.id("comments")).sendKeys("w00t");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyElementAttribute at \"i am a link\" attribute name \"href\" value \"not " + link_href + "\"");
    if (wd.findElement(By.linkText("i am a link")).getAttribute("href").equals("not \" + link_href + \"")) {
      System.out.println("!verifyElementAttribute failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertElementAttribute at \"i am a link\" attribute name \"href\" value \"not " + link_href + "\"");
    assertNotEquals(wd.findElement(By.linkText("i am a link")).getAttribute("href"), "not \" + link_href + \"");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform storeElementValue at \"comments\" with variable \"comments\"");
    String comments = wd.findElement(By.id("comments")).getAttribute("value");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyElementValue at \"comments\" value \"w00t\"");
    if (!wd.findElement(By.id("comments")).getAttribute("value").equals("w00t")) {
      System.out.println("verifyElementValue failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertElementValue at \"comments\" value \"w00t\"");
    assertEquals(wd.findElement(By.id("comments")).getAttribute("value"), "w00t");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyElementValue at \"comments\" value \"not w00t\"");
    if (wd.findElement(By.id("comments")).getAttribute("value").equals("not w00t")) {
      System.out.println("!verifyElementValue failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform assertElementValue at \"comments\" value \"not w00t\"");
    assertNotEquals(wd.findElement(By.id("comments")).getAttribute("value"), "not w00t");

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform submitElement at \"comments\"");
    wd.findElement(By.id("comments")).submit();

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
    System.out.println("[INFO] Perform verifyTextPresent value \"w00t\"");
    if (!wd.findElement(By.tagName("html")).getText().contains("w00t")) {
      System.out.println("verifyTextPresent failed");
    }

    System.out.println("[INFO] Perform  wait time \"5000\"s");
try { Thread.sleep(5000l); } catch (Exception e) { throw new RuntimeException(e); }
    System.out.println("\n");
  }

  public static boolean isAlertPresent(RemoteWebDriver wd) {
    try {
      wd.switchTo().alert();
      return true;
    } catch (NoAlertPresentException e) {
      return false;
    }
  }
}
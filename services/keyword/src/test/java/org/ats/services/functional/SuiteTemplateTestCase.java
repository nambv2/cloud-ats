/**
 * 
 */
package org.ats.services.functional;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.ats.services.DataDrivenModule;
import org.ats.services.KeywordServiceModule;
import org.ats.services.OrganizationServiceModule;
import org.ats.services.data.DatabaseModule;
import org.ats.services.data.MongoDBService;
import org.ats.services.event.EventModule;
import org.ats.services.event.EventService;
import org.ats.services.keyword.Case;
import org.ats.services.keyword.CaseFactory;
import org.ats.services.keyword.CaseReference;
import org.ats.services.keyword.CaseService;
import org.ats.services.keyword.Suite;
import org.ats.services.keyword.SuiteFactory;
import org.ats.services.organization.entity.fatory.ReferenceFactory;
import org.ats.services.organization.event.AbstractEventTestCase;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Apr 8, 2015
 */
public class SuiteTemplateTestCase extends AbstractEventTestCase {
  
  /** .*/
  private MongoDBService mongoService;
  
  /** .*/
  private EventService eventService;
  
  /** .*/
  private CaseFactory caseFactory;
  
  private SuiteFactory suiteFactory;
  
  private CaseService caseService;
  
  private ReferenceFactory<CaseReference> caseRefFactory;
  
  @BeforeClass
  public void init() throws Exception {
    Injector injector = Guice.createInjector(
        new DatabaseModule(), 
        new EventModule(),
        new OrganizationServiceModule(),
        new DataDrivenModule(),
        new KeywordServiceModule());
    
    
    this.caseService = injector.getInstance(CaseService.class);
    this.caseFactory = injector.getInstance(CaseFactory.class);
    this.suiteFactory = injector.getInstance(SuiteFactory.class);
    this.caseRefFactory = injector.getInstance(Key.get(new TypeLiteral<ReferenceFactory<CaseReference>>(){}));
    
    this.mongoService = injector.getInstance(MongoDBService.class);
    this.mongoService.dropDatabase();
    
    //start event service
    eventService = injector.getInstance(EventService.class);
    eventService.setInjector(injector);
    eventService.start();
  }

  @Test
  public void testFullExample() throws Exception {
    testBase("FullExample", "full_example.json");
  }
  
  @Test
  public void testVerifyEval() throws Exception {
    testBase("VerifyEval", "verifyEval.json");
  }
  
  @Test
  public void testAcceptAlert() throws Exception {
    testBase("AcceptAlert", "acceptAlert.json");
  }
  
  @Test
  public void testAnswerAlert() throws Exception {
    testBase("AnswerAlert", "answerAlert.json");
  }
  
  @Test
  public void testAssertAlertPresent() throws Exception {
    testBase("AssertAlertPresent", "assertAlertPresent.json");;
  }
  
  @Test
  public void testAssertAlertPresentFail() throws Exception {
    testBase("AssertAlertPresentFail", "assertAlertPresent_fail.json");
  }
  
  @Test
  public void testDismissAlert() throws Exception {
    testBase("DismissAlert", "dismissAlert.json");
  }
  
  @Test
  public void testNotAssertAlertPresent() throws Exception {
    testBase("NotAssertAlertPresent", "not_assertAlertPresent.json");
  }
  
  @Test
  public void testSwitchToFrame() throws Exception {
    testBase("SwitchToFrame", "switchToFrame.json");
  }
  
  @Test
  public void testSwitchToFrameByIndex() throws Exception {
    testBase("SwitchToFrameByIndex", "switchToFrameByIndex.json");
  }
  
  @Test
  public void testSwitchToWindow() throws Exception {
    testBase("SwitchToWindow", "switchToWindow.json");
  }
  
  @Test
  public void testJira() throws Exception {
    testBase("Jira", "jira.json");
  }
  
  @Test
  public void testSwitchToWindowWithOptions() throws Exception {
    testBaseWithMoreOptions("SwitchToWindowWithOptions", "switchToWindow.json");
  }
  
  @Test
  public void testJiraWithOptions() throws Exception {
    testBaseWithMoreOptions("JiraWithOptions", "jira.json");
  }
  
  @Test
  public void testFullExampleWithOptions() throws Exception {
    testBaseWithMoreOptions("FullExampleWithOptions", "full_example.json");
  }
  
  
  private void testBase(String testClass, String jsonFile) throws JsonProcessingException, IOException {

    ObjectMapper m = new ObjectMapper();
    JsonNode rootNode = m.readTree(new File("src/test/resources/" + jsonFile));
    
    JsonNode stepsNode = rootNode.get("steps");

    List<CaseReference> cases = new ArrayList<CaseReference>();
    Case caze = caseFactory.create("fake", "test", null);
    for (JsonNode json : stepsNode) {
      caze.addAction(json);
    }
    caseService.create(caze);
    
    cases.add(caseRefFactory.create(caze.getId()));
    
    Suite suite = suiteFactory.create("fake", testClass, SuiteFactory.DEFAULT_INIT_DRIVER, cases);
    
    try {
      String output = suite.transform();
      FileWriter writer = new FileWriter(new File("src/test/java/org/ats/generated/" + testClass +".java"));
      writer.write(output);
      writer.close();
      
      System.out.println(output);
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
  }
  
  private void testBaseWithMoreOptions(String testClass, String jsonFile) throws JsonProcessingException, IOException {

    ObjectMapper m = new ObjectMapper();
    JsonNode rootNode = m.readTree(new File("src/test/resources/" + jsonFile));
    
    JsonNode stepsNode = rootNode.get("steps");

    List<CaseReference> cases = new ArrayList<CaseReference>();
    Case caze = caseFactory.create("fake", "test", null);
    for (JsonNode json : stepsNode) {
      caze.addAction(json);
    }
    caseService.create(caze);
    
    cases.add(caseRefFactory.create(caze.getId()));
    
    Suite suite = suiteFactory.create("fake", testClass, SuiteFactory.DEFAULT_INIT_DRIVER, cases);
    
    try {
      String output = suite.transform(true,5);
      FileWriter writer = new FileWriter(new File("src/test/java/org/ats/generated/" + testClass +".java"));
      writer.write(output);
      writer.close();
      
      System.out.println(output);
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
  }
}

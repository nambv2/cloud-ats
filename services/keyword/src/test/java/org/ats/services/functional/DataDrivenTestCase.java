/**
 * 
 */
package org.ats.services.functional;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

import org.ats.common.StringUtil;
import org.ats.services.DataDrivenModule;
import org.ats.services.KeywordServiceModule;
import org.ats.services.OrganizationServiceModule;
import org.ats.services.data.DatabaseModule;
import org.ats.services.data.MongoDBService;
import org.ats.services.datadriven.DataDriven;
import org.ats.services.datadriven.DataDrivenFactory;
import org.ats.services.datadriven.DataDrivenReference;
import org.ats.services.datadriven.DataDrivenService;
import org.ats.services.event.EventModule;
import org.ats.services.event.EventService;
import org.ats.services.keyword.Case;
import org.ats.services.keyword.CaseFactory;
import org.ats.services.keyword.CaseReference;
import org.ats.services.keyword.CaseService;
import org.ats.services.keyword.Suite;
import org.ats.services.keyword.SuiteFactory;
import org.ats.services.organization.base.AuthenticationService;
import org.ats.services.organization.entity.Space;
import org.ats.services.organization.entity.Tenant;
import org.ats.services.organization.entity.User;
import org.ats.services.organization.entity.fatory.ReferenceFactory;
import org.ats.services.organization.event.AbstractEventTestCase;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Guice;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * May 6, 2015
 */
public class DataDrivenTestCase extends AbstractEventTestCase {
  
  private AuthenticationService<User> authService;
  
  private DataDrivenService drivenService;
  
  private DataDrivenFactory drivenFactory;
  
  private ReferenceFactory<DataDrivenReference> drivenRefFactory;
  
  private CaseService caseService;
  
  private CaseFactory caseFactory;
  
  private SuiteFactory suiteFactory;
  
  private ReferenceFactory<CaseReference> caseRefFactory;
  
  private Tenant tenant;

  private Space space;

  private User user;

  @BeforeClass
  public void init() throws Exception {
    this.injector = Guice.createInjector(
        new DatabaseModule(), 
        new EventModule(),
        new OrganizationServiceModule(),
        new DataDrivenModule(),
        new KeywordServiceModule());
    
    this.drivenService = injector.getInstance(DataDrivenService.class);
    this.drivenFactory = injector.getInstance(DataDrivenFactory.class);
    this.drivenRefFactory = injector.getInstance(Key.get(new TypeLiteral<ReferenceFactory<DataDrivenReference>>(){}));
    
    this.caseService = injector.getInstance(CaseService.class);
    this.caseFactory = injector.getInstance(CaseFactory.class);
    this.suiteFactory = injector.getInstance(SuiteFactory.class);
    this.caseRefFactory = injector.getInstance(Key.get(new TypeLiteral<ReferenceFactory<CaseReference>>(){}));
    
    this.authService = injector.getInstance(Key.get(new TypeLiteral<AuthenticationService<User>>(){}));
    
    this.mongoService = injector.getInstance(MongoDBService.class);
    this.mongoService.dropDatabase();

    //start event service
    this.eventService = injector.getInstance(EventService.class);
    this.eventService.setInjector(injector);
    this.eventService.start();

    initService();
  }
  
  @AfterClass
  public void shutdown() throws Exception {
    this.eventService.stop();
    this.mongoService.dropDatabase();
  } 
  
  @BeforeMethod
  public void setup() throws Exception {
    this.tenant = tenantFactory.create("Fsoft");
    this.tenantService.create(this.tenant);

    this.space = spaceFactory.create("FSU1.BU11");
    this.space.setTenant(tenantRefFactory.create(this.tenant.getId()));
    this.spaceService.create(this.space);

    this.user = userFactory.create("haint@cloud-ats.net", "Hai", "Nguyen");
    this.user.setTenant(tenantRefFactory.create(this.tenant.getId()));
    this.user.joinSpace(spaceRefFactory.create(this.space.getId()));
    this.user.setPassword("12345");
    this.userService.create(this.user);
  }
  
  @AfterMethod
  public void tearDown() {
    this.authService.logOut();
    this.mongoService.dropDatabase();
  }
  
  @Test
  public void test() throws Exception {
    ObjectMapper m = new ObjectMapper();
    
    this.authService.logIn("haint@cloud-ats.net", "12345");
    this.spaceService.goTo(spaceRefFactory.create(this.space.getId()));
    
    DataDriven data = drivenFactory.create("userSource", StringUtil.readStream(new FileInputStream("src/test/resources/data.json")));
    
    Assert.assertEquals(data.getCreator().getId(), "haint@cloud-ats.net");
    Assert.assertEquals(data.getSpace().getId(), this.space.getId());
    
    drivenService.create(data);
    
    DataDrivenReference dataRef = drivenRefFactory.create(data.getId());
    
    JsonNode rootNode = m.readTree(new File("src/test/resources/google.json"));
    
    JsonNode stepsNode = rootNode.get("steps");
    
    List<CaseReference> cases = new ArrayList<CaseReference>();
    
    Case caze = caseFactory.create("fake", "test", dataRef);
    for (JsonNode json : stepsNode) {
      caze.addAction(json);
    }
    caseService.create(caze);
    
    cases.add(caseRefFactory.create(caze.getId()));
    
    Suite suite = suiteFactory.create("fake", "Google", SuiteFactory.DEFAULT_INIT_DRIVER, cases);
    
    try {
      String output = suite.transform();
      FileWriter writer = new FileWriter(new File("src/test/java/org/ats/generated/Google.java"));
      writer.write(output);
      writer.close();
      
      System.out.println(output);
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
  }
  
  @Test
  public void testDataDrivenWithOptions() throws Exception {
    ObjectMapper m = new ObjectMapper();
    
    this.authService.logIn("haint@cloud-ats.net", "12345");
    this.spaceService.goTo(spaceRefFactory.create(this.space.getId()));
    
    DataDriven data = drivenFactory.create("userSource", StringUtil.readStream(new FileInputStream("src/test/resources/data.json")));
    
    Assert.assertEquals(data.getCreator().getId(), "haint@cloud-ats.net");
    Assert.assertEquals(data.getSpace().getId(), this.space.getId());
    
    drivenService.create(data);
    
    DataDrivenReference dataRef = drivenRefFactory.create(data.getId());
    
    JsonNode rootNode = m.readTree(new File("src/test/resources/google.json"));
    
    JsonNode stepsNode = rootNode.get("steps");
    
    List<CaseReference> cases = new ArrayList<CaseReference>();
    
    Case caze = caseFactory.create("fake", "test", dataRef);
    for (JsonNode json : stepsNode) {
      caze.addAction(json);
    }
    caseService.create(caze);
    
    cases.add(caseRefFactory.create(caze.getId()));
    
    Suite suite = suiteFactory.create("fake", "GoogleWithOptions", SuiteFactory.DEFAULT_INIT_DRIVER, cases);
    
    try {
      String output = suite.transform(false,3);
      FileWriter writer = new FileWriter(new File("src/test/java/org/ats/generated/GoogleWithOptions.java"));
      writer.write(output);
      writer.close();
      
      System.out.println(output);
    } catch (Exception e) {
      e.printStackTrace();
      Assert.fail();
    }
  }
}

/**
 * 
 */
package org.ats.services.keyword;

import java.util.logging.Logger;

import org.ats.services.OrganizationContext;
import org.ats.services.data.MongoDBService;
import org.ats.services.keyword.KeywordProject.Status;
import org.ats.services.organization.SpaceService;
import org.ats.services.organization.TenantService;
import org.ats.services.organization.UserService;
import org.ats.services.organization.base.AbstractMongoCRUD;

import com.google.inject.Inject;
import com.google.inject.Singleton;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * May 4, 2015
 */
@Singleton
public class KeywordProjectService extends AbstractMongoCRUD<KeywordProject>{

  /** .*/
  private final String COL_NAME = "keyword-project";
  
  @Inject
  private KeywordProjectFactory factory;
  
  @Inject
  private OrganizationContext context;
  
  @Inject
  private TenantService tenantService;
  
  @Inject
  private SpaceService spaceService;
  
  @Inject
  private UserService userService;
  
  @Inject
  private CustomKeywordFactory customKeyFactory;
  
  @Inject
  KeywordProjectService(MongoDBService mongo, Logger logger) {
    this.col = mongo.getDatabase().getCollection(COL_NAME);
    this.logger = logger;
    
    //create text index
    this.createTextIndex("name");
    
    //
    this.col.createIndex(new BasicDBObject("created_date", 1));
    this.col.createIndex(new BasicDBObject("creator._id", 1));
    this.col.createIndex(new BasicDBObject("tenant._id", 1));
    this.col.createIndex(new BasicDBObject("space._id", 1));
  }
  
  @Override
  public KeywordProject transform(DBObject source) {
    //rebuild context
    if (context.getTenant() == null) {
      BasicDBObject tenantSource = (BasicDBObject) source.get("tenant");
      context.setTenant(tenantService.get(tenantSource.getString("_id")));
    }
    if (context.getUser() == null) {
      BasicDBObject userSource = (BasicDBObject) source.get("creator");
      context.setUser(userService.get(userSource.getString("_id")));
    }
    if (context.getSpace() == null && source.get("space") != null) {
      BasicDBObject spaceSource = (BasicDBObject) source.get("space");
      context.setSpace(spaceService.get(spaceSource.getString("_id")));
    }
    
    KeywordProject project = factory.create(context, (String) source.get("name"));
    project.put("created_date", source.get("created_date"));
    project.put("active", source.get("active"));
    project.put("_id", source.get("_id"));
    project.setStatus(source.get("status") == null ? Status.READY : Status.valueOf((String) source.get("status")));
    project.setType(source.get("upload_project") == null ? "false" : source.get("upload_project").toString());
    //transform custom keywords
    if (source.get("custom_keywords") == null) return project;
    
    BasicDBList list = (BasicDBList) source.get("custom_keywords");
    for (Object obj : list) {
      BasicDBObject dbObj = (BasicDBObject) obj;
      String name = dbObj.getString("name");
      CustomKeyword customKeyword = customKeyFactory.create(project.getId(), name);
      customKeyword.put("_id", dbObj.get("_id"));
    }
    project.put("custom_keywords", source.get("custom_keywords"));
    return project;
  }

}

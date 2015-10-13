/**
 * 
 */
package org.ats.services.keyword;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.ats.services.OrganizationContext;
import org.ats.services.organization.entity.AbstractEntity;
import org.ats.services.organization.entity.User;
import org.ats.services.organization.entity.fatory.ReferenceFactory;
import org.ats.services.organization.entity.reference.SpaceReference;
import org.ats.services.organization.entity.reference.TenantReference;
import org.ats.services.organization.entity.reference.UserReference;

import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Apr 22, 2015
 */
@SuppressWarnings("serial")
public class KeywordProject extends AbstractEntity<KeywordProject> {
  
  private ReferenceFactory<TenantReference> tenantRefFactory;
  private ReferenceFactory<UserReference> userRefFactory;
  private ReferenceFactory<SpaceReference> spaceRefFactory;
  
  public static enum Status {
    READY, RUNNING
  }
  
  private Map<String, CustomKeyword> customKeywords = new HashMap<String, CustomKeyword>();
  
  @Inject
  KeywordProject(ReferenceFactory<TenantReference> tenantRefFactory, 
      ReferenceFactory<UserReference> userRefFactory,
      ReferenceFactory<SpaceReference> spaceRefFactory,
      @Assisted("context") OrganizationContext context,
      @Assisted("name") String name) {
    
    this.tenantRefFactory = tenantRefFactory;
    this.userRefFactory = userRefFactory;
    this.spaceRefFactory = spaceRefFactory;
    
    if (context == null || context.getUser() == null) 
      throw new IllegalStateException("You need logged in system to creat new functional project");
    
    User user = context.getUser();
    this.put("name", name);
    this.put("creator", new BasicDBObject("_id", user.getEmail()));
    
    if (context.getSpace() != null) {
      this.put("space", new BasicDBObject("_id", context.getSpace().getId()));
    }
    
    this.put("tenant", user.getTanent().toJSon());
    this.put("created_date", new Date());
    this.setActive(true);
    this.put("_id", UUID.randomUUID().toString());
    setStatus(Status.READY);
  }
  
  public String getId() {
    return this.getString("_id");
  }
  
  public Status getStatus() {
    return this.get("status") != null ? Status.valueOf(this.getString("status")) : Status.READY;
  }
  
  public void setStatus(Status status) {
    this.put("status", status.toString());
  }
  
  public UserReference getCreator() {
    return userRefFactory.create(((BasicDBObject)this.get("creator")).getString("_id"));
  }
  
  public SpaceReference getSpace() {
    if (this.get("space") == null) return null;
    BasicDBObject obj = (BasicDBObject) this.get("space");
    return spaceRefFactory.create(obj.getString("_id"));
  }
  
  public TenantReference getTenant() {
    return tenantRefFactory.create(((BasicDBObject)this.get("tenant")).getString("_id"));
  }
  
  public void setShowAction(boolean status) {
    this.put("show_action", status);
  }
  
  public void setValueDelay(int value) {
    this.put("value_delay", value);
  }
  
  public int getValueDelay() {
    return this.get("value_delay") != null ? this.getInt("value_delay") : 0;
  }
  
  public boolean getShowAction() {
    return this.get("show_action") != null ? this.getBoolean("show_action") : false;
  }
  
  /**
   * To add custom keywords into project. The keyword has same name that will be overridden.
   * @param keywords
   */
  public void addCustomKeyword(CustomKeyword... keywords) {
    BasicDBList list = new BasicDBList();
    for (CustomKeyword keyword : keywords) {
      this.customKeywords.put(keyword.getId(), keyword);
    }
    for (CustomKeyword keyword : customKeywords.values()) {
      list.add(keyword);
    }
    this.put("custom_keywords", list);
  }
  
  /**
   * TODO: need check
   * @param keywordName
   */
  public void removeCustomKeyword(String keywordName) {
    this.customKeywords.remove(keywordName);
    BasicDBList list = new BasicDBList();
    for (CustomKeyword keyword : customKeywords.values()) {
      list.add(keyword);
    }
    this.put("custom_keywords", list);
  }
  
  public void removeCustomKeyword(CustomKeyword keyword) {
    this.customKeywords.remove(keyword.getId());
  }
  
  public Collection<CustomKeyword> getCustomKeywords() {
    return Collections.unmodifiableCollection(customKeywords.values());
  }
}

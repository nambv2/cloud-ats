/**
 * 
 */
package org.ats.services.keyword;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import javax.annotation.Nullable;

import org.ats.common.StringUtil;
import org.ats.services.datadriven.DataDriven;
import org.ats.services.datadriven.DataDrivenReference;
import org.ats.services.keyword.action.AbstractAction;
import org.ats.services.organization.entity.fatory.ReferenceFactory;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.inject.Inject;
import com.google.inject.assistedinject.Assisted;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.util.JSON;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Apr 8, 2015
 */
@SuppressWarnings("serial")
public class Case extends AbstractTemplate {
  
  private List<JsonNode> actions = new ArrayList<JsonNode>();
  
  @Inject
  private ActionFactory actionFactory;
  
  @Inject
  private ReferenceFactory<DataDrivenReference> drivenRefFactory;
  
  @Inject
  private Case(@Assisted("projectId") String projectId, @Assisted("name") String name, @Nullable @Assisted("dataDriven") DataDrivenReference ref) {
    this.put("_id", UUID.randomUUID().toString());
    this.put("project_id", projectId);
    this.put("name", name);
    this.put("data_driven", ref != null ? ref.toJSon() : null);
    this.put("created_date", new Date());
    this.put("steps",  new BasicDBList());
  }
  
  public String getId() {
    return this.getString("_id");
  }
  
  public String getProjectId() {
    return this.getString("project_id");
  }
  
  public void setName(String name) {
    this.put("name", name);
  }
  
  public String getName() {
    return this.getString("name");
  }
  
  public Date getCreatedDate() {
    return this.getDate("created_date");
  }
  
  public void setDataDriven(DataDrivenReference driven) {
    this.put("data_driven", driven != null ? driven.toJSon() : null);
  }
  
  public DataDrivenReference getDataDriven() {
    Object obj = this.get("data_driven");
    if (obj == null) return null;
    return drivenRefFactory.create(((BasicDBObject) obj).getString("_id"));
  }
  
  public Case addAction(JsonNode... actions) {
    for (JsonNode action : actions) {
      if (action == null) continue;
      this.actions.add(action);
    }
    
    BasicDBList list = new BasicDBList();
    for (JsonNode action : this.actions) {
      list.add(JSON.parse(action.toString()));
    }
    this.put("steps",  list);
    return this;
  }
  
  public void clearActions() {
    this.remove("steps");
    this.actions.clear();
  }
  
  public List<JsonNode> getActions() {
    return Collections.unmodifiableList(actions);
  }
   
  public String transform() throws IOException {
    return transform(false,0);
  }
  
  public String transform(boolean showAction, int valueDelay) throws IOException {
    StringBuilder sb = new StringBuilder();
    boolean isUseDataProvider = getDataDriven() != null;
    int valueDelayTransform = valueDelay*1000;
    
    String delayTime = "";
    ObjectMapper mapper = new ObjectMapper(); 
    JsonNode nodePause = mapper.readTree("{\"type\":\"pause\",\"waitTime\":\""+valueDelayTransform+"\"}");
    AbstractAction actionPause = actionFactory.createAction(nodePause);
    delayTime = " Waiting delayTime "+"\\\""+valueDelayTransform+"\\\"s";
    
    if(isUseDataProvider) {
      
      DataDriven dataDriven = getDataDriven().get();
      
      ObjectMapper obj = new ObjectMapper();
      JsonNode rootNode;
      rootNode = obj.readTree(dataDriven.getDataSource());

      JsonNode data = rootNode.get(0);
      Iterator<Entry<String, JsonNode>> nodeIterator = data.fields();
      
      sb.append(getDataDriven().get().transform(getId()));
      sb.append("\n");
      sb.append("  @Test (dataProvider = \"").append(StringUtil.normalizeName(dataDriven.getName())).append(getId().substring(0, 8)).append("\")\n");
      sb.append("  public void ").append(StringUtil.normalizeName(getName())).append(getId().substring(0, 8)).append("(JsonNode data) throws Exception {\n");
      
      while (nodeIterator.hasNext()) {
        Map.Entry<String, JsonNode> entry = (Map.Entry<String, JsonNode>) nodeIterator.next();
        String a = "    String "+entry.getKey()+" = data.get(\""+entry.getKey().toString()+"\").toString().split(\"\\\"\")[1];\n";
        sb.append(a);
     }
      
    } else {
      sb.append("\n");
      sb.append("  @Test\n");
      sb.append("  public void ").append(StringUtil.normalizeName(getName())).append(getId().substring(0, 8)).append("() throws Exception {\n");
    }
    
    for (JsonNode json : actions) {
      AbstractAction action = actionFactory.createAction(json);
      
      if (action == null) continue;
      
      //If show action is true, show action in log
      if (showAction) {
        
        String type = json.get("type").asText();
        String locator = "";
        String text = "";
        String url = "";
        String targetLocator = "";
        String variable = "";
        String source = "";
        String title = "";
        String attributeName = "";
        String propertyName = "";
        String name = "";
        String options = "";
        String file = "";
        String identifier = "";
        String index = "";
        String script = "";
        String value = "";
        String temp = "";
        String waitTime = "";
        JsonNode locatorNode = null;
        
        if (json.get("locator") != null) {
          locatorNode = json.get("locator");
          temp = locatorNode.get("value").asText();
          if (temp.indexOf("\"") != -1) temp = temp.replace("\"", "\\\"");
          if(temp.indexOf("${") != -1 && temp.lastIndexOf("}") != -1 ) {
            int start = temp.indexOf("${");
            int end = temp.indexOf("}");
            String subTemp = temp.substring(start + 2, end);
            StringBuilder sbTemp = new StringBuilder(temp.substring(0, start)).append("\" + ");
            sbTemp.append(subTemp).append(" + \"").append(temp.substring(end + 1));
            temp = sbTemp.toString();
          }
          locator = " at " +"\\\"" +temp+"\\\"";
        }
        
        if (json.get("text") != null) {
          temp =  json.get("text").asText();
          if (temp.indexOf("\"") != -1) temp = temp.replace("\"", "\\\"");
          if(temp.indexOf("${") != -1 && temp.lastIndexOf("}") != -1 ) {
            int start = temp.indexOf("${");
            int end = temp.indexOf("}");
            String subTemp = temp.substring(start + 2, end);
            StringBuilder sbTemp = new StringBuilder(temp.substring(0, start)).append("\" + ");
            sbTemp.append(subTemp).append(" + \"").append(temp.substring(end + 1));
            temp = sbTemp.toString();
          }
          text = " value "+ "\\\""+temp+"\\\"";
        }
        
        if (json.get("waitTime") != null) {
          waitTime = " wait time "+"\\\""+json.get("waitTime").asLong()+"\\\"s";
        }
        
        if (json.get("url") != null) {
          temp = json.get("url").asText();
          if (temp.indexOf("\"") != -1) temp = temp.replace("\"", "\\\"");
          if(temp.indexOf("${") != -1 && temp.lastIndexOf("}") != -1 ) {
            int start = temp.indexOf("${");
            int end = temp.indexOf("}");
            String subTemp = temp.substring(start + 2, end);
            StringBuilder sbTemp = new StringBuilder(temp.substring(0, start)).append("\" + ");
            sbTemp.append(subTemp).append(" + \"").append(temp.substring(end + 1));
            temp = sbTemp.toString();
          }
          url = " url "+ "\\\""+temp+"\\\"";
        }
        
        if (json.get("targetLocator") != null) {
          locatorNode = json.get("targetLocator");
          temp = locatorNode.get("value").asText();
          if (temp.indexOf("\"") != -1) temp = temp.replace("\"", "\\\"");
          if(temp.indexOf("${") != -1 && temp.lastIndexOf("}") != -1 ) {
            int start = temp.indexOf("${");
            int end = temp.indexOf("}");
            String subTemp = temp.substring(start + 2, end);
            StringBuilder sbTemp = new StringBuilder(temp.substring(0, start)).append("\" + ");
            sbTemp.append(subTemp).append(" + \"").append(temp.substring(end + 1));
            temp = sbTemp.toString();
          }
          targetLocator = " to targetLocator "+ "\\\""+temp+"\\\"";
        }
        
        if (json.get("variable") != null) {
          temp = json.get("variable").asText();
          if (temp.indexOf("\"") != -1) temp = temp.replace("\"", "\\\"");
          if(temp.indexOf("${") != -1 && temp.lastIndexOf("}") != -1 ) {
            int start = temp.indexOf("${");
            int end = temp.indexOf("}");
            String subTemp = temp.substring(start + 2, end);
            StringBuilder sbTemp = new StringBuilder(temp.substring(0, start)).append("\" + ");
            sbTemp.append(subTemp).append(" + \"").append(temp.substring(end + 1));
            temp = sbTemp.toString();
          }
          variable = " with variable "+ "\\\""+temp+"\\\"";
        }
        
        if (json.get("source") != null) {
          temp = json.get("source").asText();
          if (temp.indexOf("\"") != -1) temp = temp.replace("\"", "\\\"");
          if(temp.indexOf("${") != -1 && temp.lastIndexOf("}") != -1 ) {
            int start = temp.indexOf("${");
            int end = temp.indexOf("}");
            String subTemp = temp.substring(start + 2, end);
            StringBuilder sbTemp = new StringBuilder(temp.substring(0, start)).append("\" + ");
            sbTemp.append(subTemp).append(" + \"").append(temp.substring(end + 1));
            temp = sbTemp.toString();
          }
          source = " source "+"\\\""+temp+"\\\"";
        }
        
        if (json.get("title") != null) {
          temp = json.get("title").asText();
          if (temp.indexOf("\"") != -1) temp = temp.replace("\"", "\\\"");
          if(temp.indexOf("${") != -1 && temp.lastIndexOf("}") != -1 ) {
            int start = temp.indexOf("${");
            int end = temp.indexOf("}");
            String subTemp = temp.substring(start + 2, end);
            StringBuilder sbTemp = new StringBuilder(temp.substring(0, start)).append("\" + ");
            sbTemp.append(subTemp).append(" + \"").append(temp.substring(end + 1));
            temp = sbTemp.toString();
          }
          title = " title "+"\\\""+temp+"\\\"";
        }
        
        if (json.get("attributeName") != null) {
          temp = json.get("attributeName").asText();
          if (temp.indexOf("\"") != -1) temp = temp.replace("\"", "\\\"");
          if(temp.indexOf("${") != -1 && temp.lastIndexOf("}") != -1 ) {
            int start = temp.indexOf("${");
            int end = temp.indexOf("}");
            String subTemp = temp.substring(start + 2, end);
            StringBuilder sbTemp = new StringBuilder(temp.substring(0, start)).append("\" + ");
            sbTemp.append(subTemp).append(" + \"").append(temp.substring(end + 1));
            temp = sbTemp.toString();
          }
          attributeName = " attribute name "+ "\\\""+temp+"\\\"";
        }
        
        if (json.get("propertyName") != null) {
          temp = json.get("propertyName").asText();
          if (temp.indexOf("\"") != -1) temp = temp.replace("\"", "\\\"");
          if(temp.indexOf("${") != -1 && temp.lastIndexOf("}") != -1 ) {
            int start = temp.indexOf("${");
            int end = temp.indexOf("}");
            String subTemp = temp.substring(start + 2, end);
            StringBuilder sbTemp = new StringBuilder(temp.substring(0, start)).append("\" + ");
            sbTemp.append(subTemp).append(" + \"").append(temp.substring(end + 1));
            temp = sbTemp.toString();
          }
          propertyName = " property name " + "\\\""+temp+"\\\"";
        }
        
        if (json.get("options") != null) {
          temp = json.get("options").asText();
          if (temp.indexOf("\"") != -1) temp = temp.replace("\"", "\\\"");
          if(temp.indexOf("${") != -1 && temp.lastIndexOf("}") != -1 ) {
            int start = temp.indexOf("${");
            int end = temp.indexOf("}");
            String subTemp = temp.substring(start + 2, end);
            StringBuilder sbTemp = new StringBuilder(temp.substring(0, start)).append("\" + ");
            sbTemp.append(subTemp).append(" + \"").append(temp.substring(end + 1));
            temp = sbTemp.toString();
          }
          options = " options "+ "\\\""+temp+"\\\"";
        }
        
        if (json.get("name") != null) {
          temp = json.get("name").asText();
          if (temp.indexOf("\"") != -1) temp = temp.replace("\"", "\\\"");
          if(temp.indexOf("${") != -1 && temp.lastIndexOf("}") != -1 ) {
            int start = temp.indexOf("${");
            int end = temp.indexOf("}");
            String subTemp = temp.substring(start + 2, end);
            StringBuilder sbTemp = new StringBuilder(temp.substring(0, start)).append("\" + ");
            sbTemp.append(subTemp).append(" + \"").append(temp.substring(end + 1));
            temp = sbTemp.toString();
          }
          name = " name "+"\\\""+temp+"\\\"";
        }
        
        if (json.get("file") != null) {
          temp = json.get("file").asText();
          if (temp.indexOf("\"") != -1) temp = temp.replace("\"", "\\\"");
          if(temp.indexOf("${") != -1 && temp.lastIndexOf("}") != -1 ) {
            int start = temp.indexOf("${");
            int end = temp.indexOf("}");
            String subTemp = temp.substring(start + 2, end);
            StringBuilder sbTemp = new StringBuilder(temp.substring(0, start)).append("\" + ");
            sbTemp.append(subTemp).append(" + \"").append(temp.substring(end + 1));
            temp = sbTemp.toString();
          }
          file = " file "+"\\\""+temp+"\\\"";
        }
        
        if (json.get("identifier") != null) {
          temp = json.get("identifier").asText();
          if (temp.indexOf("\"") != -1) temp = temp.replace("\"", "\\\"");
          if(temp.indexOf("${") != -1 && temp.lastIndexOf("}") != -1 ) {
            int start = temp.indexOf("${");
            int end = temp.indexOf("}");
            String subTemp = temp.substring(start + 2, end);
            StringBuilder sbTemp = new StringBuilder(temp.substring(0, start)).append("\" + ");
            sbTemp.append(subTemp).append(" + \"").append(temp.substring(end + 1));
            temp = sbTemp.toString();
          }
          identifier = " identifier "+"\\\""+temp+"\\\"";
        }
        
        if (json.get("index") != null) {
          index = " index "+"\\\""+json.get("index").asInt()+"\\\"";
        }
        
        if (json.get("script") != null) {
          temp = json.get("script").asText();
          if (temp.indexOf("\"") != -1) temp = temp.replace("\"", "\\\"");
          if(temp.indexOf("${") != -1 && temp.lastIndexOf("}") != -1 ) {
            int start = temp.indexOf("${");
            int end = temp.indexOf("}");
            String subTemp = temp.substring(start + 2, end);
            StringBuilder sbTemp = new StringBuilder(temp.substring(0, start)).append("\" + ");
            sbTemp.append(subTemp).append(" + \"").append(temp.substring(end + 1));
            temp = sbTemp.toString();
          }
          script = " script "+"\\\""+temp+"\\\"";
        }
        
        if(json.get("value") != null) {
          temp = json.get("value").asText();
          if (temp.indexOf("\"") != -1) temp = temp.replace("\"", "\\\"");
          if(temp.indexOf("${") != -1 && temp.lastIndexOf("}") != -1 ) {
            int start = temp.indexOf("${");
            int end = temp.indexOf("}");
            String subTemp = temp.substring(start + 2, end);
            StringBuilder sbTemp = new StringBuilder(temp.substring(0, start)).append("\" + ");
            sbTemp.append(subTemp).append(" + \"").append(temp.substring(end + 1));
            temp = sbTemp.toString();
          }
          value = " value "+"\\\""+temp+"\\\"";
        }
        
        sb.append("    System.out.println(\"\\n\");\n");
        sb.append("    System.out.println(\"[INFO] Perform ");
        sb.append(type);
        sb.append(locator);
        sb.append(targetLocator);
        sb.append(url);
        sb.append(text);
        sb.append(script);
        sb.append(name);
        sb.append(source);
        sb.append(title);
        sb.append(options);
        sb.append(propertyName);
        sb.append(attributeName);
        sb.append(variable);
        sb.append(value);
        sb.append(file);
        sb.append(waitTime);
        sb.append(identifier);
        sb.append(index);
        sb.append("\");\n");
      }
      
      sb.append("    ");
      sb.append(action.transform());
      sb.append("\n");
      if(valueDelayTransform != 0) {
        sb.append("    System.out.println(\"");
        sb.append(delayTime);
        sb.append("\");\n");
        sb.append(actionPause.transform());
      }
    }
    sb.append("    System.out.println(\"\\n\");\n");
    sb.append("  }");
    return sb.toString();
  }
}

/**
 * 
 */
package controllers.organization;

import interceptor.AuthenticationInterceptor;
import interceptor.Authorization;
import interceptor.WizardInterceptor;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.ats.component.usersmgt.EventExecutor;
import org.ats.component.usersmgt.UserManagementException;
import org.ats.component.usersmgt.feature.Feature;
import org.ats.component.usersmgt.feature.FeatureDAO;
import org.ats.component.usersmgt.feature.Operation;
import org.ats.component.usersmgt.group.Group;
import org.ats.component.usersmgt.group.GroupDAO;
import org.ats.component.usersmgt.role.Permission;
import org.ats.component.usersmgt.role.PermissionDAO;
import org.ats.component.usersmgt.role.Role;
import org.ats.component.usersmgt.role.RoleDAO;

import play.api.templates.Html;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.With;
import views.html.organization.index;
import views.html.organization.role.addrole;
import views.html.organization.role.editrole;

import com.mongodb.BasicDBObject;

import controllers.Application;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Aug 18, 2014
 */
@With({WizardInterceptor.class, AuthenticationInterceptor.class})
@Authorization(feature = "Organization", operation = "Administration")
public class RoleAction extends Controller {

  public static Result addRole() throws UserManagementException {
    Group current = Organization.setCurrentGroup(null);
    session().put("group_id", current.getId());
    
    Html body = addrole.render(current);
    return ok(index.render("role" , body, current.getId()));
  }
  
  public static Result doAddRole() throws UserManagementException {
    Group currentGroup = Organization.setCurrentGroup(null);
    session().put("group_id", currentGroup.getId());
    
    if (request().getQueryString("name") != null && request().getQueryString("operation") != null) {
      String name = request().getQueryString("name");
      String desc = request().getQueryString("desc");
      
      String[] op_ids = request().queryString().get("operation");
      Role role_ = new Role(Application.dbName, name, currentGroup.getId());
      role_.put("desc", desc);
      
      for (String op_id : op_ids) {
        Feature f = FeatureDAO.getInstance(Application.dbName).find(new BasicDBObject("operation_ids", Pattern.compile(op_id))).iterator().next();
        role_.addPermission(new Permission(Application.dbName, f.getId(), op_id));
      }
      RoleDAO.getInstance(Application.dbName).create(role_);
      currentGroup.addRole(role_);
      GroupDAO.getInstance(Application.dbName).update(currentGroup);
    }
    return redirect(routes.Organization.index() + "?nav=role");
  }
  
  public static Result editRole(String r) throws UserManagementException {
    Group currentGroup = Organization.setCurrentGroup(null);
    session().put("group_id", currentGroup.getId());
    
    Role role_ = RoleDAO.getInstance(Application.dbName).findOne(r);
    
    //Prevent system role or no right permission
    if (role_.getBoolean("system") || !currentGroup.getRoles().contains(role_)) return forbidden(views.html.forbidden.render()); 

    Html body = editrole.render(currentGroup, role_ );
    return ok(index.render("role" , body, currentGroup.getId()));
  }
  
  public static Result doEditRole(String r) throws UserManagementException {
    Group currentGroup = Organization.setCurrentGroup(null);
    session().put("group_id", currentGroup.getId());
    
    Role role_ = RoleDAO.getInstance(Application.dbName).findOne(r);
    
    //Prevent system role or no right permission
    if (role_.getBoolean("system") || !currentGroup.getRoles().contains(role_)) return forbidden(views.html.forbidden.render()); 
    
    if (request().getQueryString("name") != null && request().getQueryString("operation") != null) {
      String name = request().getQueryString("name");
      role_.put("name", name);
      
      Set<String> pers = role_.getString("permission_ids") != null ? role_.stringIDtoSet(role_.getString("permission_ids")) : new HashSet<String>();
      Set<String> op_ids = new HashSet<String>();
      Collections.addAll(op_ids, request().queryString().get("operation"));
      
      //Add more permission if not existed
      for (String op_id : op_ids) {
        if (!pers.contains(op_id)) {
          Feature f = FeatureDAO.getInstance(Application.dbName).find(new BasicDBObject("operation_ids", Pattern.compile(op_id))).iterator().next();
          role_.addPermission(new Permission(Application.dbName, f.getId(), op_id));
        }
      }
      
      //Remove not existed permission
      for (String per : pers) {
        if (!op_ids.contains(per)) {
           role_.removePermission(per);
           PermissionDAO.getInstance(Application.dbName).delete(per);
        }
      }
      
      if (request().getQueryString("desc") != null) role_.put("desc", request().getQueryString("desc"));
      
      RoleDAO.getInstance(Application.dbName).update(role_);
    }
    
    return redirect(routes.Organization.index() + "?nav=role");
  }
  
  public static Result deleteRole(String r) throws UserManagementException {
    Group currentGroup = Organization.setCurrentGroup(null);
    session().put("group_id", currentGroup.getId());
    
    Role role_ = RoleDAO.getInstance(Application.dbName).findOne(r);
    
    //Prevent system role or no right permission
    if (role_.getBoolean("system") || !currentGroup.getRoles().contains(role_)) return forbidden(views.html.forbidden.render());
    
    RoleDAO.getInstance(Application.dbName).delete(role_);
    
    while (EventExecutor.getInstance(Application.dbName).isInProgress()) {
    }
    
    return redirect(routes.Organization.index() + "?nav=role");
  }
  
  public static Result removePermission(String r, String p) throws UserManagementException {
    Group currentGroup = Organization.setCurrentGroup(null);
    session().put("group_id", currentGroup.getId());
    
    Role role_ = RoleDAO.getInstance(Application.dbName).findOne(r);
    Permission per = PermissionDAO.getInstance(Application.dbName).findOne(p);
    
    //Prevent system role or no right permission
    if (role_.getBoolean("system") 
        || !currentGroup.getRoles().contains(role_) 
        || !role_.getPermissions().contains(per)) 
      return forbidden(views.html.forbidden.render());
    
    role_.removePermission(per);
    PermissionDAO.getInstance(Application.dbName).delete(per);
    
    while (EventExecutor.getInstance(Application.dbName).isInProgress()) {
    }
    
    RoleDAO.getInstance(Application.dbName).update(role_);
    
    return redirect(routes.Organization.index() + "?nav=role");
  }
  
  public static boolean shouldChecked(Role role_, Operation op) throws UserManagementException {
    for (Permission per : role_.getPermissions()) {
      if (per.getOpertion().equals(op)) return true;
    }
    return false;
  }
}

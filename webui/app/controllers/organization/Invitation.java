/**
 * 
 */
package controllers.organization;

import org.ats.component.usersmgt.UserManagementException;
import org.ats.component.usersmgt.group.Group;
import org.ats.component.usersmgt.group.GroupDAO;
import org.ats.component.usersmgt.role.Role;
import org.ats.component.usersmgt.role.RoleDAO;
import org.ats.component.usersmgt.user.User;
import org.ats.component.usersmgt.user.UserDAO;

import play.mvc.Controller;
import play.mvc.Result;
import controllers.Application;
import controllers.routes;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Aug 14, 2014
 */
public class Invitation extends Controller {

  public static Result index(String u, String r, String g) throws UserManagementException {
    User user = UserDAO.getInstance(Application.dbName).findOne(u);
    Role role = RoleDAO.getInstance(Application.dbName).findOne(r);
    Group group = GroupDAO.getInstance(Application.dbName).findOne(g);
    
    if (verify(user, role, group)) {
      User currentUser = UserDAO.getInstance(Application.dbName).findOne(session("user_id"));
      
      if (!currentUser.getBoolean("joined") && currentUser.getGroups().isEmpty()) {
        currentUser.joinGroup(group);
        group.addUser(currentUser);
        
        GroupDAO.getInstance(Application.dbName).update(group);
        UserDAO.getInstance(Application.dbName).update(currentUser);
      }
    }
    
    return redirect(routes.Application.dashboard());
  }
  
  //Verify the user who sent invitation that has right permission
  private static boolean verify(User user, Role role, Group group) {
    if (!"Administration".equals(role.getName())) return false;
    if (!role.getGroup().equals(group)) return false;
    if (!role.getUsers().contains(user)) return false;
    if (!group.getUsers().contains(user)) return false;
    if (!user.getGroups().contains(group)) return false;
    return true;
  }
}

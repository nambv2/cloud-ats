/**
 * 
 */
package interceptor;

import helpervm.VMHelper;

import org.ats.component.usersmgt.user.User;
import org.ats.component.usersmgt.user.UserDAO;

import play.libs.F.Promise;
import play.mvc.Action.Simple;
import play.mvc.Http.Context;
import play.mvc.SimpleResult;
import views.html.vm.index;
import views.html.vm.wizard;
import controllers.Application;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Sep 18, 2014
 */
public class VMWizardIterceptor extends Simple {
  
  @Override
  public Promise<SimpleResult> call(Context ctx) throws Throwable {
    User currentUser = UserDAO.getInstance(Application.dbName).findOne(ctx.session().get("user_id"));
    if (currentUser == null) return Promise.<SimpleResult>pure(redirect("/"));
    
    if (VMHelper.getSystemProperties().size() == 0) {
      return currentUser.getBoolean("system") ? Promise.<SimpleResult>pure(ok(index.render(wizard.render()))) : Promise.<SimpleResult>pure(forbidden(views.html.forbidden.render()));
    }
    return delegate.call(ctx);
  }

}

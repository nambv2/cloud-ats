/**
 * 
 */
package org.ats.services.functional.action;

import java.io.IOException;

import org.ats.services.functional.Value;
import org.rythmengine.Rythm;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Apr 14, 2015
 */
public class VerifyCurrentUrl implements IAction {
  
  private Value url;
  
  private boolean negated;
  
  public VerifyCurrentUrl(Value url, boolean negated) {
    this.url = url;
    this.negated = negated;
  }

  public String transform() throws IOException {
    StringBuilder sb = new StringBuilder("if (").append(negated ? "" : "!");
    sb.append("wd.getCurrentUrl().equals(@url)) {\n");
    sb.append("System.out.println(\"").append(negated ? "!" : "").append("verifyCurrentUrl failed\");\n");
    sb.append("}\n");
    return Rythm.render(sb.toString(), url.transform());
  }

  public String getAction() {
    return "verifyCurrentUrl";
  }

}

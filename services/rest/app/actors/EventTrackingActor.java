/**
 * 
 */
package actors;

import org.ats.services.event.Event;
import org.ats.services.executor.job.AbstractJob;
import org.ats.services.executor.job.KeywordJob;
import org.ats.services.executor.job.PerformanceJob;
import org.ats.services.keyword.KeywordProject;
import org.ats.services.keyword.KeywordProjectService;
import org.ats.services.performance.PerformanceProject;
import org.ats.services.performance.PerformanceProjectService;
import org.ats.services.vmachine.VMachine;
import org.ats.services.vmachine.VMachineService;

import akka.actor.UntypedActor;

import com.google.inject.Inject;

import controllers.EventController;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Aug 8, 2015
 */
public class EventTrackingActor extends UntypedActor {

  @Inject EventController eventController;

  @Inject KeywordProjectService keywordService;
  
  @Inject PerformanceProjectService perfService;
  
  @Inject VMachineService vmachineService;
  
  @Override
  public void onReceive(Object obj) throws Exception {
    if (obj instanceof Event) {
      Event event = (Event) obj;
      try {
        if ("keyword-job-tracking".equals(event.getName())) {
          
          KeywordJob job = (KeywordJob) event.getSource();
          
          if (job.getStatus() == AbstractJob.Status.Queued) return;
          
          KeywordProject project = keywordService.get(job.getProjectId());
          
          if (job.getStatus() == AbstractJob.Status.Running) {
            VMachine jenkinsVM = vmachineService.getSystemVM(project.getTenant(), project.getSpace());
            VMachine testVM = vmachineService.get(job.getTestVMachineId());
            StringBuilder sb = new StringBuilder("http://").append(jenkinsVM.getPublicIp())
                .append(":8081/guacamole/#/client/c/vnc_node_").append(testVM.getPrivateIp());
            job.put("watch_url", sb.toString());
          }
          
          job.put("project_status", project.getStatus().toString());
          eventController.send(project.getCreator().get(), job);
          
        } else if ("performance-job-tracking".equals(event.getName())) {
          
          PerformanceJob job = (PerformanceJob) event.getSource();
          
          if (job.getStatus() ==  AbstractJob.Status.Queued) return;
          
          PerformanceProject project = perfService.get(job.getId());
          job.put("project_status", project.getStatus().toString());
          eventController.send(project.getCreator().get(), job);
          
        }
      } catch (Exception e) {
        e.printStackTrace();
        //TODO: should be send error
      }
    }
  }


}

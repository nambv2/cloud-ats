/**
 * 
 */
package controllers.vm;

import static java.util.concurrent.TimeUnit.SECONDS;
import helpervm.VMHelper;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import models.vm.VMModel;
import models.vm.VMModel.VMStatus;
import play.Logger;
import play.libs.Json;
import scala.concurrent.duration.Duration;
import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedActor;
import azure.AzureClient;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.microsoft.windowsazure.management.compute.models.RoleInstance;

/**
 * @author <a href="mailto:haithanh0809@gmail.com">Nguyen Thanh Hai</a>
 *
 * Sep 11, 2014
 */
public class VMStatusActor extends UntypedActor {
  
  private static ActorSystem system = null;
  
  static ActorRef actor = null;
  
  static ConcurrentHashMap<String, VMChannel> channels = new ConcurrentHashMap<String, VMChannel>();
  
  public static void start() {
    if (system == null) {
      system = ActorSystem.create("vm-status");
      actor = system.actorOf(Props.create(VMStatusActor.class));
      
      system.scheduler().schedule(Duration.create(3000, TimeUnit.MILLISECONDS), Duration.create(10, SECONDS),
          actor,
          "Check",
          system.dispatcher(),
          null);
    }
    Logger.info("Started Akka system has named vm-status");
  }
  
  public static void stop() {
    if (system != null) {
      system.shutdown();
    }
    Logger.info("Shutdown Akka system has named vm-status");
  }
  
  static void addChannel(VMChannel channel) {
    channels.put(channel.sessionId, channel);
  }
  
  static void removeChannel(String sessionId) {
    channels.remove(sessionId);
  }
  
  @Override
  public void onReceive(Object msg) throws Exception {
    if (msg instanceof VMChannel) {
      VMChannel channel = (VMChannel) msg;
      channels.put(channel.sessionId, channel);
      getSender().tell("OK", getSelf());
    } else if (msg.equals("Check")) {
      for (VMChannel channel : channels.values()) {
        
        AzureClient azureClient = VMHelper.getAzureClient();
        
        ObjectNode jsonObj = Json.newObject();
        ArrayNode array = jsonObj.arrayNode();
        
        for (VMModel sel : VMHelper.getVMsByGroupID(channel.groupId)) {
          RoleInstance vm = azureClient.getVirutalMachineByName(sel.getId());
          if (vm == null) return;
          String status = null;
          if ("ReadyRole".equals(vm.getInstanceStatus())) {
              status = sel.getStatus().toString();
          } else if ("StoppedDeallocated".equals(vm.getInstanceStatus()) || "StoppedVM".equals(vm.getInstanceStatus())) {
            if (sel.getStatus() != VMStatus.Stopped && sel.getStatus() != VMStatus.Stopping) {
              sel.setStatus(VMStatus.Stopped);
              VMHelper.updateVM(sel);
            }
            status = VMStatus.Stopped.toString();
          } else {
            status = sel.getStatus().toString();
          }          
          array.add(Json.newObject().put("id", vm.getRoleName()).put("status", status).put("ip", vm.getIPAddress().getHostAddress()));
        }
        
        jsonObj.put("vms", array);
        channel.out.write(jsonObj);
      }
    } else {
      unhandled(msg);
    }
  }
}

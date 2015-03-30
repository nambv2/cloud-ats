package org.ats.services.organization.event;

import org.ats.services.event.Event;
import org.ats.services.organization.entity.User;
import org.ats.services.organization.entity.fatory.ReferenceFactory;
import org.ats.services.organization.entity.reference.UserReference;

import akka.actor.UntypedActor;

import com.google.inject.Inject;

public class DeleteUserActor extends UntypedActor{


  @Inject
  private ReferenceFactory<UserReference> userRefFactory;
  @Override
  public void onReceive(Object message) throws Exception {
    
    if (message instanceof Event) {
      
      Event event = (Event) message;
      if ("delete-user".equals(event.getName())) {
        
        User user = (User) event.getSource();
        UserReference ref = userRefFactory.create(user.getEmail());
        
        process(ref);
      } else if ("delete-user-ref".equals(event.getName())) {
        
        UserReference ref = (UserReference) event.getSource();
        process(ref);
        
      } else unhandled(message);
    }
    
  }

  private void process (UserReference ref) {
    
    
  }
  
}
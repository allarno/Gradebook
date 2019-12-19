package edu.franklin.comp655.group5.gradebook;

import edu.franklin.comp655.group5.gradebook.resources.GradeBookResourceService;
import java.util.HashSet;
import java.util.Set;
import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

/**
 * Configures JAX-RS for the application.
 * 
 * @author Alcir David
 * @author Allan Akhonya
 * @author Anirudha Samudrala
 */
@ApplicationPath("/services")
public class GradeBookApplicationJAXRSConfiguration extends Application {
    
   private Set<Object> singletons = new HashSet<Object>();
   private Set<Class<?>> empty = new HashSet<Class<?>>();

   public GradeBookApplicationJAXRSConfiguration() {
      singletons.add(new GradeBookResourceService());
   }

   @Override
   public Set<Class<?>> getClasses() {
      return empty;
   }

   @Override
   public Set<Object> getSingletons() {
      return singletons;
   }

}

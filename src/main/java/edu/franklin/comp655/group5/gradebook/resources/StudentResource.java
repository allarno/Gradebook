/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.franklin.comp655.group5.gradebook.resources;

import java.io.InputStream;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 *
 * @author Alcir David
 */
@Path("/student")
public interface StudentResource {
   
   @POST
   @Path("{name}/grade/{grade}")
   @Consumes("application/x-www-form-urlencoded")
   public Response createStudent(@PathParam("name") String name, 
           @PathParam("grade") String letter);
   
   @GET
   @Path("{name}")
   @Produces("text/xml;charset=utf-8")
   public StreamingOutput getStudent(@PathParam("name") String name);

   @PUT
   @Path("{name}/grade/{grade}")
   @Consumes("application/x-www-form-urlencoded")
   public Response updateStudent(@PathParam("name") String name, 
           @PathParam("grade") String letter);
   
   @GET
   @Produces("text/xml;charset=utf-8")
   public StreamingOutput getAllStudents( );
   
   @DELETE
   @Path("{name}")
   @Consumes("application/x-www-form-urlencoded")
   public Response deleteStudent(@PathParam("name") String name);
}

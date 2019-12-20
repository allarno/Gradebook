/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.franklin.comp655.group5.gradebook.resources;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Response;

/**
 *
 * @author Alcir David
 * @author Allan Akhonya
 * @author Anirudha Samudrala
 */
@Path("/")
public interface GradeBookResource {

    @PUT
    @Path("gradebook/{name}")
    @Consumes("application/x-www-form-urlencoded")
    public Response createGradeBook(@PathParam("name") String name);

    @POST
    @Path("gradebook/{name}")
    @Consumes("application/x-www-form-urlencoded")
    public Response updateGradeBook(@PathParam("name") String name);

    @GET
    @Path("gradebook")
    @Produces("text/xml;charset=utf-8")
    public Response getAllGradeBooks();

    @DELETE
    @Path("gradebook/{id}")
    @Consumes("application/x-www-form-urlencoded")
    public Response deleteGradeBook(
            @PathParam(value = "id") Long gradeBookId);

    @PUT
    @Path("secondary/{id}")
    @Consumes("application/x-www-form-urlencoded")
    public Response createSecondaryGradeBook(
            @PathParam(value = "id") Long gradeBookId);

    @POST
    @Path("secondary/{id}")
    @Consumes("application/x-www-form-urlencoded")
    public Response updateSecondaryGradeBook(
            @PathParam(value = "id") Long gradeBookId);

    @DELETE
    @Path("secondary/{id}")
    @Consumes("application/x-www-form-urlencoded")
    public Response deleteSecondaryGradeBook(
            @PathParam(value = "id") Long gradeBookId);

    @PUT
    @Path("gradebook/{id}/student/{name}/grade/{grade}")
    @Consumes("application/x-www-form-urlencoded")
    public Response createStudent(
            @PathParam(value = "id") Long gradeBookId,
            @PathParam("name") String name,
            @PathParam("grade") String grade);

    @GET
    @Path("gradebook/{id}/student/{name}")
    @Produces("text/xml;charset=utf-8")
    public Response getStudent(
            @PathParam(value = "id") Long gradeBookId,
            @PathParam(value = "name") String name);

    @POST
    @Path("gradebook/{id}/student/{name}/grade/{grade}")
    @Consumes("application/x-www-form-urlencoded")
    public Response updateStudent(
            @PathParam(value = "id") Long gradeBookId,
            @PathParam("name") String name,
            @PathParam("grade") String grade);

    @GET
    @Path("gradebook/{id}/student")
    @Produces("text/xml;charset=utf-8")
    public Response getAllStudents(
            @PathParam(value = "id") Long gradeBookId);

    @DELETE
    @Path("gradebook/{id}/student/{name}")
    @Consumes("application/x-www-form-urlencoded")
    public Response deleteStudent(
            @PathParam(value = "id") Long gradeBookId,
            @PathParam("name") String name);
}

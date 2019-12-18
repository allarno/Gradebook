/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.franklin.comp655.group5.gradebook.resources;

import edu.franklin.comp655.group5.gradebook.model.GradeBook;
import edu.franklin.comp655.group5.gradebook.model.GradeBookList;
import edu.franklin.comp655.group5.gradebook.model.Student;
import edu.franklin.comp655.group5.gradebook.model.StudentList;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;
import javax.ws.rs.core.StreamingOutput;

/**
 *
 * @author Alcir David
 */
public class GradeBookResourceService implements GradeBookResource {

    GradeBookList gradeBookList;
    GradeBookList secondaryGradeBookList;

    public GradeBookResourceService() {
        gradeBookList = new GradeBookList();
        secondaryGradeBookList = new GradeBookList();
    }

    // Method to encode a string value using `UTF-8` encoding scheme
    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }

    String regex = "([A-D]|[a-d])[+-]?|E|F|I|W|Z|e|f|i|w|z";

    private boolean isValideGrade(String grade) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(grade);
        return matcher.matches();
    }

    private boolean isValideGradeBookName(String name) {
        // GradeBoook title which must be a character string 
        //that begins with a non-whitespace character.
        return !name.isEmpty() && !Character.isWhitespace(name.charAt(0));
    }

    private GradeBook validateGradeBookId(Long gradeBookId) {
        return validateGradeBookId(gradeBookId, gradeBookList);
    }
    
    private GradeBook validateGradeBookId(Long gradeBookId, 
            GradeBookList gradeBookList) {
        GradeBook gradeBook = gradeBookList.getGradeBookById(gradeBookId);

        if (gradeBook == null) {
            throw new WebApplicationException("There is no GradeBook "
                    + "with the given id: " + gradeBookId,
                    Response.Status.NOT_FOUND);
        }
        return gradeBook;
    }

//    protected StreamingOutput getGradeBook(String name) {
//        final GradeBook gradebook = gradeBookDB.get(name);
//
//        if (gradebook == null) {
//            throw new WebApplicationException(Response.Status.NOT_FOUND);
//        }
//        return (OutputStream outputStream) -> {
//            outputGradeBook(outputStream, gradebook);
//        };
//    }
//    protected void outputGradeBook(OutputStream os, GradeBook gradebook)
//            throws IOException {
//        PrintStream writer = new PrintStream(os);
//        writer.println("<gradebook>");
//        writer.println("   <id>" + gradebook.getId()
//                + "</id>");
//        writer.println("   <name>" + gradebook.getName()
//                + "</name>");
//        writer.println("</gradebook>");
//    }
//    protected void outputGradeBooks(OutputStream os, GradeBookList gradebookList)
//            throws IOException {
//        PrintStream writer = new PrintStream(os);
//        writer.println("<gradebook-list>");
//
//        for (GradeBook gradebook : gradebookList.getGradeBookList()) {
//            writer.print("   ");
//            outputGradeBook(os, gradebook);
//        }
//
//        writer.println("</gradebook-list>");
//    }
    @Override
    public Response createGradeBook(String name) {

        if (!isValideGradeBookName(name)) {
            throw new WebApplicationException(name
                    + " is not a valid gradeBook tilte.",
                    Response.Status.BAD_REQUEST);
        }

        if (gradeBookList.containsTitle(name)) {
//            throw new WebApplicationException("The title "
//                    + name + " already exist.",
//                    Response.Status.BAD_REQUEST);
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("The title " + name + " already exist.").build();
        }

        //generate randon id
        Random random = new Random();
        Long id = random.nextLong() + 1;
        GradeBook gradeBook = new GradeBook();
        gradeBook.setName(name);
        gradeBook.setId(id);
        
        gradeBookList.add(gradeBook);

        System.out.println("Created gradeBook Id: " + gradeBook.getId());
        
        return Response.status(Response.Status.OK)
                .entity(gradeBook).build();
//        return gradeBook.getId();
    }

    @Override
    public Response updateGradeBook(String name) {
        return createGradeBook(name);
    }

    @Override
    public StreamingOutput getAllGradeBooks() {
        return (OutputStream outputStream) -> {
//            GradeBookList gradeBookList = new GradeBookList();
//
//            for (String id : gradeBookDB.keySet()) {
//                final GradeBook student = gradeBookDB.get(id);
//                gradeBookList.add(student);
//            }
//            outputGradeBooks(outputStream, gradeBookList);
            PrintStream writer = new PrintStream(outputStream);
            writer.print(gradeBookList.toString());
        };
    }

    @Override
    public Response deleteGradeBook(Long gradeBookId) {
        
        if (gradeBookList.removeGradeBookById(gradeBookId) == null) {
            throw new WebApplicationException("There is no GradeBook "
                    + "with the given id: " + gradeBookId,
                    Response.Status.NOT_FOUND);
        }
        
        if (secondaryGradeBookList.containsId(gradeBookId)) {
            // remove gradebook secondary copy
            secondaryGradeBookList.removeGradeBookById(gradeBookId);
        }
        
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response createSecondaryGradeBook(Long gradeBookId) {
        GradeBook gradeBook = validateGradeBookId(gradeBookId);

        if (secondaryGradeBookList.containsId(gradeBookId)) {
            throw new WebApplicationException("The server already has "
                    + "a secondary copy of the GradeBook",
                    Response.Status.NOT_FOUND);
        }   
        // create a secondary copy
        secondaryGradeBookList.add(gradeBook);
        
        return Response.status(Status.OK).build();
    }

    @Override
    public Response updateSecondaryGradeBook(Long gradebookId) {
        return createSecondaryGradeBook(gradebookId);
    }

    @Override
    public Response deleteSecondaryGradeBook(Long gradeBookId) {
        validateGradeBookId(gradeBookId);
        
        if (secondaryGradeBookList.removeGradeBookById(gradeBookId) == null) {
            throw new WebApplicationException("The server does not have a "
                    + "secondary copy of the GradeBook",
                    Response.Status.NOT_FOUND);
        }
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response createStudent(Long gradeBookId, String name, String grade) {

        GradeBook gradeBook = validateGradeBookId(gradeBookId);

        if (!isValideGrade(grade)) {
            throw new WebApplicationException(grade + " is not a valid grade.",
                    Response.Status.BAD_REQUEST);
        }

        Student current = new Student();
        current.setName(name);
        current.setGrade(grade);

        gradeBook.add(current);
        System.out.println("Created student: " + current.getName());
        System.out.println("encode name: " + encodeValue(current.getName()));
        System.out.println("encode grade: " + encodeValue(current.getGrade()));

        gradeBookList.add(gradeBook);
        
        if (secondaryGradeBookList.containsId(gradeBookId)) {
            // update the secondary copy
            secondaryGradeBookList.add(gradeBook);
        }

        return Response.status(Response.Status.OK).entity(current).build();
//        return Response.created(URI.create("/gradebook/" + gradeBookId
//                + "/student/" + encodeValue(current.getName()) + "/grade/"
//                + encodeValue(current.getGrade()))).build();
    }

    @Override
    public StreamingOutput getStudent(Long gradeBookId, String name) {
        return (OutputStream outputStream) -> {
            GradeBook gradeBook = validateGradeBookId(gradeBookId);

            Student student = gradeBook.getStudent(name);
            PrintStream writer = new PrintStream(outputStream);
            writer.print(student.toString());
        };
    }

    @Override
    public Response updateStudent(Long gradeBookId, String name, String grade) {

        GradeBook gradeBook = validateGradeBookId(gradeBookId);

        if (!isValideGrade(grade)) {
            throw new WebApplicationException(grade + " is not a valid grade.",
                    Response.Status.BAD_REQUEST);
        }

        Student current = gradeBook.getStudent(name);

        if (current == null) {
            throw new WebApplicationException("There is no student in "
                    + "the given GradeBook with the given student name: " + name,
                    Response.Status.BAD_REQUEST);
        }

        current.setName(name);
        current.setGrade(grade);

        gradeBook.add(current);
        System.out.println("Updated student: " + current.getName());
        System.out.println("encode name: " + encodeValue(current.getName()));
        System.out.println("encode grade: " + encodeValue(current.getGrade()));

        gradeBookList.add(gradeBook); //update 
        
        if (secondaryGradeBookList.containsId(gradeBookId)) {
            // update the secondary copy
            secondaryGradeBookList.add(gradeBook);
        }

        return Response.status(Response.Status.OK).entity(current).build();
    }

    @Override
    public StreamingOutput getAllStudents(Long gradeBookId) {
        return (OutputStream outputStream) -> {
            GradeBook gradeBook = validateGradeBookId(gradeBookId);
            
            StudentList studentList = gradeBook.fetchStudentList();
            PrintStream writer = new PrintStream(outputStream);
            writer.print(studentList.toString());
        };
    }

    @Override
    public Response deleteStudent(Long gradeBookId, String name) {

        GradeBook gradeBook = validateGradeBookId(gradeBookId);
        
        if (gradeBook.removeStudentByName(name) == null) {
            throw new WebApplicationException("There is no student in "
                    + "the given GradeBook with the given student name: " + name,
                    Response.Status.BAD_REQUEST);
        }
        
        gradeBookList.add(gradeBook); // update gradeBook

        if (secondaryGradeBookList.containsId(gradeBookId)) {
            gradeBook = secondaryGradeBookList.getGradeBookById(gradeBookId);
            gradeBook.removeStudentByName(name);
            // update the secondary copy
            secondaryGradeBookList.add(gradeBook);
        }
        
        return Response.status(Response.Status.OK).build();
    }

}

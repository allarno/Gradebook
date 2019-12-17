/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.franklin.comp655.group5.gradebook.resources;

import edu.franklin.comp655.group5.gradebook.model.GradeBook;
import edu.franklin.comp655.group5.gradebook.model.GradeBookListServer;
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
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

/**
 *
 * @author Alcir David
 */
public class GradeBookResourceService implements GradeBookResource {

    private Map<String, GradeBook> gradeBookDB = new ConcurrentHashMap<>();
    GradeBookListServer gradeBookList;
    
    public GradeBookResourceService (){
        gradeBookList = new GradeBookListServer();
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
    
    private GradeBook validateGradeBookId(Long gradeBookId){
        GradeBook gradeBook = gradeBookList.getGradeBookById(gradeBookId);
            
            if (gradeBook == null) {
                throw new WebApplicationException("There is no GradeBook "
                        + "with the given id: " + gradeBookId ,
                    Response.Status.BAD_REQUEST);
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

//    protected void outputGradeBooks(OutputStream os, GradeBookListServer gradebookList)
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
    public Long createGradeBook(String name) {
        
        if (!isValideGradeBookName(name)){
            throw new WebApplicationException(name +
                    " is not a valid gradeBook tilte.",
                    Response.Status.BAD_REQUEST);
        }
        
        if (gradeBookList.containsTitle(name)) {
            throw new WebApplicationException("The title " 
                    + name + " already exist.",
                    Response.Status.BAD_REQUEST);
        }
        
        GradeBook gradeBook = new GradeBook();
        gradeBook.setName(name);
        gradeBookList.add(gradeBook);
        
        System.out.println("Created gradeBook Id: " + gradeBook.getId());
        return gradeBook.getId();
    }

    @Override
    public Long updateGradeBook(String name) {
        return createGradeBook(name);
    }

    @Override
    public StreamingOutput getAllGradeBooks() {
        return (OutputStream outputStream) -> {
//            GradeBookListServer gradeBookList = new GradeBookListServer();
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
    public Response deleteGradeBook(Long gradebookId) {
        if (gradeBookList.removeGradeBookById(gradebookId) == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response createSecondaryGradeBook(Long gradebookId) {
//        if (!isValideGrade(grade)) {
//            throw new WebApplicationException(grade + " is not a valid grade.",
//                    Response.Status.BAD_REQUEST);
//        } 
//        
//        Student current = gradeBookDB.get(name);
//        
//        if (current == null)
//            current = new Student();
//        
//        current.setName(name);
//        current.setGrade(grade);
//        
//        gradeBookDB.put(name, current);
//        System.out.println("Created student: " + current.getName());
//        System.out.println("encode name: " + encodeValue(current.getName()));
//        System.out.println("encode grade: " + encodeValue(current.getGrade()));
//        
//        return Response.created(URI.create("/student/"
//                + encodeValue(current.getName())+"/grade/" 
//                + encodeValue(current.getGrade()))).build();

        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Response updateSecondaryGradeBook(Long gradebookId) {
        return createSecondaryGradeBook(gradebookId);
    }

    @Override
    public Response deleteSecondaryGradeBook(Long gradebookId) {
        if (gradeBookList.removeGradeBookById(gradebookId) == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
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
        
        return Response.created(URI.create("/gradebook/" + gradeBookId
                + "/student/" + encodeValue(current.getName()) + "/grade/"
                + encodeValue(current.getGrade()))).build();
    }

    @Override
    public StreamingOutput getStudent(Long gradebookId, String name) {
        return (OutputStream outputStream) -> {
            GradeBook gradeBook = gradeBookList.getGradeBookById(gradebookId);
            
            if (gradeBook == null) {
                throw new WebApplicationException("There is no GradeBook "
                        + "with the given id: " + gradebookId ,
                    Response.Status.BAD_REQUEST);
            }
            
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
                    + "the given GradeBOok with the given student name: " + name,
                    Response.Status.BAD_REQUEST);
        }

        current.setName(name);
        current.setGrade(grade);
        
        gradeBook.add(current);
        System.out.println("Updated student: " + current.getName());
        System.out.println("encode name: " + encodeValue(current.getName()));
        System.out.println("encode grade: " + encodeValue(current.getGrade()));

        gradeBookList.add(gradeBook); //update 
        
        return Response.created(URI.create("/gradebook/" + gradeBookId
                + "/student/" + encodeValue(current.getName()) + "/grade/"
                + encodeValue(current.getGrade()))).build();
    }

    @Override
    public StreamingOutput getAllStudents(Long gradebookId) {
        return (OutputStream outputStream) -> {
            GradeBook gradeBook = gradeBookList.getGradeBookById(gradebookId);
            
            if (gradeBook == null) {
                throw new WebApplicationException("There is no GradeBook "
                        + "with the given id: " + gradebookId ,
                    Response.Status.BAD_REQUEST);
            }
            
            StudentList studentList = gradeBook.getStudentList();
            PrintStream writer = new PrintStream(outputStream);
            writer.print(studentList.toString());
        };
    }

    @Override
    public Response deleteStudent(Long gradebookId, String name) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}

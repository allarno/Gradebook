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
    
    protected StreamingOutput getGradeBook(String name) { 
        final GradeBook gradebook = gradeBookDB.get(name);
                
        if (gradebook == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return (OutputStream outputStream) -> {
            outputGradeBook(outputStream, gradebook);
        };
    }
  
    protected void outputGradeBook(OutputStream os, GradeBook gradebook)
                                                     throws IOException {
      PrintStream writer = new PrintStream(os);
      writer.println("<gradebook>");
      writer.println("   <id>" + gradebook.getId()
                      + "</id>");
      writer.println("   <name>" + gradebook.getName()
                      + "</name>");
      writer.println("</gradebook>");
   }
    
    protected void outputGradeBooks(OutputStream os, GradeBookList gradebookList)
                                                     throws IOException {
      PrintStream writer = new PrintStream(os);
      writer.println("<gradebook-list>");
  
      for (GradeBook gradebook : gradebookList.getGradeBooks()) {
          writer.print("   ");
          outputGradeBook(os, gradebook);
      }
 
      writer.println("</gradebook-list>");
   }

    @Override
    public Response createGradeBook(String name) {
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
    public Response updateGradeBook(String name) {
        return createGradeBook(name);
    }

    @Override
    public StreamingOutput getAllGradeBooks() {
        return (OutputStream outputStream) -> {
            GradeBookList gradeBookList = new GradeBookList();
            
            for (String id : gradeBookDB.keySet()) {
                final GradeBook student = gradeBookDB.get(id);
                gradeBookList.add(student);
            }  
            outputGradeBooks(outputStream, gradeBookList);
        };  
    }

    @Override
    public Response deleteGradeBook(Long gradebookId) {
//        if (gradeBookDB.remove(name) == null) {
//            throw new WebApplicationException(Response.Status.NOT_FOUND);
//        }
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
//        if (gradeBookDB.remove(name) == null) {
//            throw new WebApplicationException(Response.Status.NOT_FOUND);
//        }
         return Response.status(Response.Status.OK).build();
    }
    
}

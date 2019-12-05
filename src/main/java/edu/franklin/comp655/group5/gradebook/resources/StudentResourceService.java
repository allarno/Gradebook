/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.franklin.comp655.group5.gradebook.resources;

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
public class StudentResourceService implements StudentResource {

   private Map<String, Student> studentDB = new ConcurrentHashMap<>();
   
   // Method to encode a string value using `UTF-8` encoding scheme
    private String encodeValue(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.toString());
        } catch (UnsupportedEncodingException ex) {
            throw new RuntimeException(ex.getCause());
        }
    }
    
    // Method to decode a string value using `UTF-8` decoding scheme
//    private String decodeValue(String value) {
//        try {
//            return URLDecoder.decode(value, StandardCharsets.UTF_8.toString());
//        } catch (UnsupportedEncodingException ex) {
//            throw new RuntimeException(ex.getCause());
//        }
//    }
    
    // Method to decode a string value
//    public String getPathMethod(String url) {
//       try {
//           return new URI(url).getPath();
//       } catch (URISyntaxException ex) {
//            throw new RuntimeException(ex.getCause());
//       }
//    }
   
   @Override
   public Response createStudent(String name, String grade) {
       
        if (!isValideGrade(grade)) {
            throw new WebApplicationException(grade + " is not a valid grade.",
                    Response.Status.BAD_REQUEST);
        } 
        
        Student current = studentDB.get(name);
        
        if (current == null)
            current = new Student();
        
        current.setName(name);
        current.setGrade(grade);
        
        studentDB.put(name, current);
        System.out.println("Created student: " + current.getName());
        System.out.println("encode name: " + encodeValue(current.getName()));
        System.out.println("encode grade: " + encodeValue(current.getGrade()));
        
        return Response.created(URI.create("/student/"
                + encodeValue(current.getName())+"/grade/" 
                + encodeValue(current.getGrade()))).build();
    }

    @Override
    public StreamingOutput getStudent(String name) { 
        final Student student = studentDB.get(name);
                
        if (student == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
        return (OutputStream outputStream) -> {
            outputStudent(outputStream, student);
        };
    }
    
    @Override
    public StreamingOutput getAllStudents() {
        
        return (OutputStream outputStream) -> {
            StudentList studentList = new StudentList();
            
            for (String id : studentDB.keySet()) {
                final Student student = studentDB.get(id);
                studentList.add(student);
            }  
            outputStudent(outputStream, studentList);
        };  
    }

    @Override
    public Response updateStudent(String name, String grade) {
        return createStudent(name, grade);
    }
    
    @Override
    public Response deleteStudent(String name) {
        if (studentDB.remove(name) == null) {
            throw new WebApplicationException(Response.Status.NOT_FOUND);
        }
         return Response.status(Response.Status.OK).build();
    }
    
//    String[] validGrades = {"A", "B", "C", "D", "E", "F", "I", "W", "Z"
//    , "a", "b", "c", "d", "e", "f", "i", "w", "z", "A+", "B+"};
//    ArrayList<String> grades = new ArrayList(Arrays.asList(validGrades));
    
    String regex = "([A-D]|[a-d])[+-]?|E|F|I|W|Z|e|f|i|w|z";
    
    private boolean isValideGrade(String grade) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(grade);
        return matcher.matches();
    }
  
    protected void outputStudent(OutputStream os, Student student)
                                                     throws IOException {
      PrintStream writer = new PrintStream(os);
      writer.println("<student>");
      writer.println("   <name>" + student.getName()
                      + "</name>");
      writer.println("   <grade>" + student.getGrade()
                      + "</grade>");
      writer.println("</student>");
   }
    
    protected void outputStudent(OutputStream os, StudentList studentList)
                                                     throws IOException {
      PrintStream writer = new PrintStream(os);
      writer.println("<student-list>");
  
      for (Student student : studentList.getAllStudents()) {
          writer.print("   ");
          outputStudent(os, student);
      }
 
      writer.println("</student-list>");
   }
    
}

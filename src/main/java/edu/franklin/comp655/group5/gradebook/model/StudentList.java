/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.franklin.comp655.group5.gradebook.model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.constraints.NotNull;

/**
 * Data object that represents a list of students 
 * in the DGB (Distributed GradeBooks).
 * @author Alcir David
 */
public class StudentList {
    private Map<String, Student> studentDB = new ConcurrentHashMap<>();

//    ArrayList<Student> studentList = new ArrayList();
//    
//    public ArrayList<Student> getAllStudents() {
//        return studentList;
//    }
//    
//    public void setStudentList(ArrayList<Student> studentList) {
//        this.studentList = studentList;
//    }
//    
//    public void add(Student student) {
//        studentList.add(student);
//    }
//    
    public ArrayList<Student> getStudentList() {
//        return new ArrayList(Arrays.asList(studentDB.values()));
        return new ArrayList(studentDB.values());
    }
    
    public Student getStudentByName(String name) {
        return studentDB.get(name);
    }
    
    public void add(@NotNull Student current){
        studentDB.put(current.getName(), current);
    }
            
    public void add(String studentName, String studentGrade) {
        Student current = studentDB.get(studentName);
        
        if (current == null)
            current = new Student();
        
        current.setName(studentName);
        current.setGrade(studentGrade);
        studentDB.put(studentName, current);
    }
    
    @Override
    public String toString() {
        StringBuilder writer = new StringBuilder();

        writer.append("<student-list>\n");

        for (Student student : getStudentList()) {
            writer.append("   ");
            writer.append(student.toString());
            writer.append("\n");
        }
        writer.append("</student-list>");

        return writer.toString();
//        return "GradeBookList{" + "gradeBookDB=" + gradeBookDB + '}';
    }
}

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.franklin.comp655.group5.gradebook.model;

import java.util.ArrayList;

/**
 * Data object that represents a list of students 
 * in the DGB (Distributed GradeBooks).
 * @author Alcir David
 */
public class StudentList {
    ArrayList<Student> studentList = new ArrayList();
    
    public ArrayList<Student> getAllStudents() {
        return studentList;
    }
    
    public void setStudentList(ArrayList<Student> studentList) {
        this.studentList = studentList;
    }
    
    public void add(Student student) {
        studentList.add(student);
    }
}

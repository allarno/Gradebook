/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.franklin.comp655.group5.gradebook.model;

/**
 * Data object that represent a Student in the gradeBook.
 * @author Alcir David
 */
public class Student {

    private String name;
    private String grade;

   public String getName() { 
       return name; 
   }
   
   public void setName(String name) {
       this.name = name; 
   }

   public String getGrade() { 
       return grade; 
   }
   
   public void setGrade(String grade) {
       this.grade = grade; 
   }
}

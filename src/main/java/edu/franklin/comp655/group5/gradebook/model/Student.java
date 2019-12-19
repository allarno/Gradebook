/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.franklin.comp655.group5.gradebook.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Data object that represents a Student in the DGB (Distributed GradeBooks).
 * @author Alcir David
 */
@XmlRootElement(name = "student")
public class Student {

    private String name;
    private String grade;

   @XmlElement
   public String getName() { 
       return name;
   }
   
   public void setName(String name) {
       this.name = name;
   }

   @XmlElement
   public String getGrade() { 
       return grade;
   }
   
   public void setGrade(String grade) {
       this.grade = grade;
   }
   
   @Override
    public String toString() {
        StringBuilder writer = new StringBuilder();
        writer.append("<student>\n");
        writer.append("   <name>");
        writer.append(name);
        writer.append("</name>\n");
        writer.append("   <grade>");
        writer.append(grade);
        writer.append("</grade>\n");
        writer.append("</student>");
        
        return writer.toString();
    }
}

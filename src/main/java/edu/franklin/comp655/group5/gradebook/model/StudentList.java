/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.franklin.comp655.group5.gradebook.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Data object that represents a list of students in the DGB (Distributed
 * GradeBooks).
 *
 * @author Alcir David
 * @author Allan Akhonya
 * @author Anirudha Samudrala
 */
@XmlRootElement(name = "student-list")
public class StudentList {

    final private Map<String, Student> studentDB = new ConcurrentHashMap<>();

    public void setStudentList(ArrayList<Student> studentList) {
        studentList.forEach(
                student -> studentDB.put(student.getName(), student));
    }

    @XmlElement(name = "student", type = Student.class)
    public ArrayList<Student> getStudentList() {
        return new ArrayList(studentDB.values());
    }

    public Student getStudentByName(String name) {
        return studentDB.get(name);
    }

    public void add(@NotNull Student current) {
        studentDB.put(current.getName(), current);
    }

    public void add(String studentName, String studentGrade) {
        Student current = studentDB.get(studentName);

        if (current == null) {
            current = new Student();
        }

        current.setName(studentName);
        current.setGrade(studentGrade);
        studentDB.put(studentName, current);
    }

    public Student remove(String name) {
        return studentDB.remove(name);
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
    }
}

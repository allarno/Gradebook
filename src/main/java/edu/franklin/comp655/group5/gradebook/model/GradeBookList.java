/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.franklin.comp655.group5.gradebook.model;

import java.util.ArrayList;

/**
 * Data object that represents a list of GradeBook 
 * in the DGB (Distributed GradeBooks).
 * @author Alcir David
 */
public class GradeBookList {
    ArrayList<GradeBook> GradeBookList = new ArrayList();
    
    public ArrayList<GradeBook> getGradeBooks() {
        return GradeBookList;
    }
    
    public void setGradeBookList(ArrayList<GradeBook> gradeBook) {
        this.GradeBookList = gradeBook;
    }
    
    public void add(GradeBook gradeBook) {
        GradeBookList.add(gradeBook);
    }
}

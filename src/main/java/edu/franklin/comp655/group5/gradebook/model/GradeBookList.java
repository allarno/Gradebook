/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package edu.franklin.comp655.group5.gradebook.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import javax.validation.constraints.NotNull;

/**
 * Data object that represents a list of GradeBooks in the DGB (Distributed
 * GradeBooks).
 *
 * @author Alcir David
 * @author Allan Akhonya
 * @author Anirudha Samudrala
 */
public class GradeBookList {

    final private Map<Long, GradeBook> gradeBookDB = new ConcurrentHashMap<>();
    final private Set<String> gradeBookNames = new HashSet();

    public ArrayList<GradeBook> getGradeBookList() {
        return new ArrayList(gradeBookDB.values());
    }

    public void setGradeBookList(ArrayList<GradeBook> gradeBooks) {
        gradeBooks.forEach(gradeBook -> add(gradeBook));
    }

    public void add(@NotNull GradeBook gradeBook) {
        add(gradeBook.getId(), gradeBook);
    }

    public void add(Long gradeBookId, @NotNull GradeBook gradeBook) {
        gradeBookNames.add(gradeBook.getName());
        gradeBookDB.put(gradeBookId, gradeBook);
    }

    public GradeBook getGradeBookById(Long gradeBookId) {
        return gradeBookDB.get(gradeBookId);
    }
    
    public GradeBook removeGradeBookById(Long gradeBookId) {
        GradeBook gradeBook = gradeBookDB.remove(gradeBookId);
        if (gradeBook != null) {
            gradeBookNames.remove(gradeBook.getName());
        }
                    
        return gradeBook;
    }
    
    public boolean containsTitle(String title){
        return gradeBookNames.contains(title);
    }
    
    public boolean containsId(Long gradeBookId){
        return gradeBookDB.containsKey(gradeBookId);
    }

    @Override
    public String toString() {
        StringBuilder writer = new StringBuilder();

        writer.append("<gradebook-list>\n");

        for (GradeBook gradebook : getGradeBookList()) {
            writer.append("   ");
            writer.append(gradebook.toString());
            writer.append("\n");
        }
        writer.append("</gradebook-list>");

        return writer.toString();
    }
}

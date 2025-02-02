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
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

/**
 *
 * @author Alcir David
 * @author Allan Akhonya
 * @author Anirudha Samudrala
 */
public class GradeBookResourceService implements GradeBookResource {

    GradeBookList gradeBookList;
    GradeBookList secondaryGradeBookList;

    public GradeBookResourceService() {
        gradeBookList = new GradeBookList();
        secondaryGradeBookList = new GradeBookList();
    }

    private final String VALID_GRADE_REGEX
            = "([A-D]|[a-d])[+-]?|E|F|I|W|Z|e|f|i|w|z";

    private boolean isValidGrade(String grade) {
        Pattern pattern = Pattern.compile(VALID_GRADE_REGEX);
        Matcher matcher = pattern.matcher(grade);
        return matcher.matches();
    }

    private boolean isValidGradeBookName(String name) {
        // GradeBoook title which must be a character string 
        //that begins with a non-whitespace character.
        return !name.isEmpty() && !Character.isWhitespace(name.charAt(0));
    }

    @Override
    public Response createGradeBook(String name) {

        if (!isValidGradeBookName(name)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(name + " is not a valid gradeBook tilte.").build();
        }

        if (gradeBookList.containsTitle(name)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("The title " + name + " already exist.").build();
        }
        //generate randon id
        Random random = new Random();
        Long id = random.nextLong() + 1;
        GradeBook gradeBook = new GradeBook();
        gradeBook.setName(name);
        gradeBook.setId(id);

        gradeBookList.add(gradeBook);
        System.out.println("Created gradeBook Id: " + gradeBook.getId());
        return Response.status(Response.Status.OK).entity(gradeBook).build();
    }

    @Override
    public Response updateGradeBook(String name) {
        return createGradeBook(name);
    }

    @Override
    public Response getAllGradeBooks() {
        return Response.status(Response.Status.OK)
                .entity(gradeBookList).build();
    }

    @Override
    public Response deleteGradeBook(Long gradeBookId) {

        if (gradeBookList.removeGradeBookById(gradeBookId) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("There is no GradeBook with the given id: "
                            + gradeBookId).build();
        }

        if (secondaryGradeBookList.containsId(gradeBookId)) {
            // remove gradebook secondary copy
            secondaryGradeBookList.removeGradeBookById(gradeBookId);
        }

        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response createSecondaryGradeBook(Long gradeBookId) {
        GradeBook gradeBook = gradeBookList.getGradeBookById(gradeBookId);

        if (gradeBook == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("There is no GradeBook "
                            + "with the given id: " + gradeBookId).build();
        }

        if (secondaryGradeBookList.containsId(gradeBookId)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("The server already has a secondary copy "
                            + "of the GradeBook").build();
        }
        // create a secondary copy
        secondaryGradeBookList.add(gradeBook);

        return Response.status(Status.OK).build();
    }

    @Override
    public Response updateSecondaryGradeBook(Long gradebookId) {
        return createSecondaryGradeBook(gradebookId);
    }

    @Override
    public Response deleteSecondaryGradeBook(Long gradeBookId) {

        if (!gradeBookList.containsId(gradeBookId)) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("There is no GradeBook "
                            + "with the given id: " + gradeBookId).build();
        }

        if (secondaryGradeBookList.removeGradeBookById(gradeBookId) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("The server does not have a "
                            + "secondary copy of the GradeBook").build();
        }

        return Response.status(Response.Status.OK).build();
    }

    @Override
    public Response createStudent(Long gradeBookId, String name, String grade) {
        GradeBook gradeBook = gradeBookList.getGradeBookById(gradeBookId);

        if (gradeBook == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("There is no GradeBook "
                            + "with the given id: " + gradeBookId).build();
        }

        Student current = gradeBook.getStudent(name);

        if (current != null) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("A grade already exists in the given GradeBook "
                            + "with the given student name: " + name).build();
        }

        if (!isValidGrade(grade)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(grade + " is not a valid grade.").build();
        }

        current = new Student();
        current.setName(name);
        current.setGrade(grade);

        gradeBook.add(current);
        System.out.println("Created student: " + current.getName()
                + " grade: " + current.getGrade());

        gradeBookList.add(gradeBook);

        if (secondaryGradeBookList.containsId(gradeBookId)) {
            // update the secondary copy
            secondaryGradeBookList.add(gradeBook);
        }

        return Response.status(Response.Status.OK).entity(current).build();
    }

    @Override
    public Response getStudent(Long gradeBookId, String name) {
        GradeBook gradeBook = gradeBookList.getGradeBookById(gradeBookId);

        if (gradeBook == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("There is no GradeBook "
                            + "with the given id: " + gradeBookId).build();
        }

        Student current = gradeBook.getStudent(name);

        if (current == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("There is no student in the given GradeBook "
                            + "with the given student name: " + name).build();
        }
        return Response.status(Response.Status.OK).entity(current).build();
    }

    @Override
    public Response updateStudent(Long gradeBookId, String name, String grade) {
        GradeBook gradeBook = gradeBookList.getGradeBookById(gradeBookId);

        if (gradeBook == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("There is no GradeBook "
                            + "with the given id: " + gradeBookId).build();
        }

        if (!isValidGrade(grade)) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity(grade + " is not a valid grade.").build();
        }

        Student current = gradeBook.getStudent(name);

        if (current == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("There is no student in the given GradeBook "
                            + "with the given student name: " + name).build();
        }

        current.setName(name);
        current.setGrade(grade);

        gradeBook.add(current);
        System.out.println("Updated student: " + current.getName()
                + " grade: " + current.getGrade());

        gradeBookList.add(gradeBook); //update 

        if (secondaryGradeBookList.containsId(gradeBookId)) {
            // update the secondary copy
            secondaryGradeBookList.add(gradeBook);
        }

        return Response.status(Response.Status.OK).entity(current).build();
    }

    @Override
    public Response getAllStudents(Long gradeBookId) {
        GradeBook gradeBook = gradeBookList.getGradeBookById(gradeBookId);

        if (gradeBook == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("There is no GradeBook "
                            + "with the given id: " + gradeBookId).build();
        }
        StudentList studentList = gradeBook.fetchStudentList();

        return Response.status(Response.Status.OK).entity(studentList).build();
    }

    @Override
    public Response deleteStudent(Long gradeBookId, String name) {
        GradeBook gradeBook = gradeBookList.getGradeBookById(gradeBookId);

        if (gradeBook == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("There is no GradeBook "
                            + "with the given id: " + gradeBookId).build();
        }

        if (gradeBook.removeStudentByName(name) == null) {
            return Response.status(Response.Status.NOT_FOUND)
                    .entity("There is no student in the given GradeBook "
                            + "with the given student name: " + name).build();
        }

        gradeBookList.add(gradeBook); // update gradeBook

        if (secondaryGradeBookList.containsId(gradeBookId)) {
            gradeBook = secondaryGradeBookList.getGradeBookById(gradeBookId);
            gradeBook.removeStudentByName(name);
            // update the secondary copy
            secondaryGradeBookList.add(gradeBook);
        }

        return Response.status(Response.Status.OK).build();
    }
}

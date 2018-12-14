package com.student.Controllers;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import com.mysql.jdbc.CommunicationsException;
import com.student.DAOs.StudentDAO;
import com.student.Models.Course;
import com.student.Models.StudentDetails;

@ManagedBean
@SessionScoped
public class CourseController {
	
	private ArrayList<Course> courses;
	private ArrayList<StudentDetails> studentDetails;
	StudentDAO dao;
	
	/**
	 * New studentDAO, courses ArrayList & studentDetails ArrayList
	 * @throws Exception
	 */
	public CourseController() throws Exception {
		super();
		dao = new StudentDAO();
		courses = new ArrayList<>();
		studentDetails = new ArrayList<>();
	}
	
	public void loadCourses() {
		try {
			courses = dao.loadCourses();
		} catch (Exception e) {
			FacesMessage message = new FacesMessage("Error: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
		}
	}
	
	public String addCourse(Course c) {
		try {
			dao.addCourse(c);
			return "index.xhtml";
		} catch (CommunicationsException e) {
			FacesMessage message = new FacesMessage("Error: Cannot connect to Database");
			FacesContext.getCurrentInstance().addMessage(null, message);
			System.out.println("Database Offline");
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			FacesMessage message = new FacesMessage("Error: Course already exists");
			FacesContext.getCurrentInstance().addMessage(null, message);
			System.out.println("Course already exists");
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			FacesMessage message = new FacesMessage("Error: Unknown Exception");
			FacesContext.getCurrentInstance().addMessage(null, message);
			System.out.println("Unknown Exception: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
	}

	public String deleteCourse(Course c){	
		try {
			dao.deleteCourse(c);
		} catch (SQLException e) {
			FacesMessage message = 	new FacesMessage("Error: Cannot Delete Course: " + c.getcID() + " as there are associated Students");
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return "list_courses.xhtml";
	}
	
	public String loadCourseStudentDetails(Course c) {
		try {
			studentDetails = dao.loadCourseStudentDetails(c);
			System.out.println(studentDetails);
		} catch (Exception e) {
			FacesMessage message = new FacesMessage("Error: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
		}
		return "course_student_details.xhtml";
	}
	
	/**
	 * Getters & Setters
	 */
	
	public ArrayList<Course> getCourses() {
		return courses;
	}
	
	public ArrayList<StudentDetails> getStudentDetails() {
		return studentDetails;
	}
	
	
}

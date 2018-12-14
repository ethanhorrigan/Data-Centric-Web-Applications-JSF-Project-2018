package com.student.Controllers;

import java.sql.SQLException;
import java.util.ArrayList;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;

import org.neo4j.driver.v1.exceptions.ClientException;
import org.neo4j.driver.v1.exceptions.ServiceUnavailableException;

import com.mysql.jdbc.CommunicationsException;
import com.student.DAOs.Neo4JDAO;
import com.student.DAOs.StudentDAO;
import com.student.Models.Student;
import com.student.Models.StudentDetails;

@ManagedBean
@SessionScoped
public class StudentController {

	private ArrayList<Student> students;
	private ArrayList<StudentDetails> studentDetails;
	Neo4JDAO neo4jDAO;
	StudentDAO dao;
	
	/**
	 * New StudentDAO, Students ArrayList & StudentDetails ArrayList
	 * @throws Exception
	 */
	public StudentController() throws Exception {
		super();
		dao = new StudentDAO();
		neo4jDAO = new Neo4JDAO();
		students = new ArrayList<>();
		studentDetails = new ArrayList<>();
	}
	
	public void loadStudents() {
		try {
			students = dao.loadStudents();
		} catch (Exception e) {
			FacesMessage message = new FacesMessage("Error: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
		}
	}
	
	public String loadStudentDetails(Student s) {
		try {
			studentDetails = dao.loadStudentDetails(s);
		} catch (Exception e) {
			FacesMessage message = new FacesMessage("Error: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
		}
		return "student_details";
	}
	
	public String deleteStudent(Student s){	
		try {
			getNeo4JDAO().deleteStudent(s);
		} catch (ClientException e) {
			FacesMessage message = new FacesMessage("Student: " + s.getName() + " has not been deleted from any database as he/she has relationships in Neo4j");
			FacesContext.getCurrentInstance().addMessage(null, message);
			e.printStackTrace();
			
			return "";
		} catch (ServiceUnavailableException e){
            FacesMessage message = new FacesMessage("Student " + s.getName() + " has not been deleted from Neo4j DB, as it offline." );
            FacesContext.getCurrentInstance().addMessage(null, message);
            e.printStackTrace();
        }
		try {
			dao.deleteStudent(s);
		} catch (SQLException e) {
			FacesMessage message = 	new FacesMessage("Error: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
		}
		return "list_students";
	}
	
	public String addStudent(Student s) {
		try {
			dao.addStudent(s);
		} catch (CommunicationsException e) {
			FacesMessage message = new FacesMessage("Error: Cannot connect to Database");
			FacesContext.getCurrentInstance().addMessage(null, message);
			System.out.println("Database Offline");
			e.printStackTrace();
			return null;
		} catch (SQLException e) {
			FacesMessage message = new FacesMessage("Error: " + e.getMessage());
			FacesContext.getCurrentInstance().addMessage(null, message);
			System.out.println("Student already exists");
			e.printStackTrace();
			return null;
		} catch (Exception e) {
			FacesMessage message = new FacesMessage("Error: Unknown Exception");
			FacesContext.getCurrentInstance().addMessage(null, message);
			System.out.println("Unknown Exception: " + e.getMessage());
			e.printStackTrace();
			return null;
		}
		try {
			getNeo4JDAO().addStudent(s);
		} catch (ServiceUnavailableException e){
            FacesMessage message = new FacesMessage("Warning: Student "+ s.getName() +" has not been deleted from Neo4j DB, as it offline." );
            FacesContext.getCurrentInstance().addMessage(null, message);
        }
		return "index.xhtml";
	}

	/**
	 * Getters & Setters
	 */
	public ArrayList<Student> getStudents() {
		return students;
	}

	public void setStudents(ArrayList<Student> students) {
		this.students = students;
	}

	public ArrayList<StudentDetails> getStudentDetails() {
		return studentDetails;
	}

	public void setStudentDetails(ArrayList<StudentDetails> studentDetails) {
		this.studentDetails = studentDetails;
	}
	
	public Neo4JDAO getNeo4JDAO() {
		return neo4jDAO;
	}
	
	
}

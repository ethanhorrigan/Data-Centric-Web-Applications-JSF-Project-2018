package com.student.DAOs;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.student.Models.Course;
import com.student.Models.Student;
import com.student.Models.StudentDetails;

public class StudentDAO {
	private DataSource mysqlDS;
	private Connection conn; 
	
	/**
	 * Constructor
	 * @throws Exception
	 */
	public StudentDAO() throws Exception {
		Context context = new InitialContext();
		String jndiName = "java:comp/env/jdbc/studentDB";
		mysqlDS = (DataSource) context.lookup(jndiName);
	}
	
/**
 * Students DAO
 */
	
	/**
	 * Methods to load all students.
	 * @return Students ArrayList
	 * @throws Exception
	 */
	public ArrayList<Student> loadStudents() throws Exception {
		 ArrayList<Student> students = new ArrayList<Student>();
		 conn = mysqlDS.getConnection();
		 
		 Statement stmt = conn.createStatement();		
		 ResultSet rs = stmt.executeQuery("select * from student");
		 
		 while (rs.next()) {
			 Student student = new Student();
			 student.setSid(rs.getString("sid"));
			 student.setcID(rs.getString("cID"));
			 student.setName(rs.getString("name"));
			 student.setAddress(rs.getString("address"));
			 students.add(student);
		 }
		 return students;
	}
	
	/**
	 * Method to add a Student into the Student Table.
	 * @param s = Student Object.
	 * @throws Exception
	 */
	public void addStudent(Student s) throws Exception{
		
		conn = mysqlDS.getConnection();
		Statement stmt = conn.createStatement();
		
		String query = "insert into student (sid ,cID, name, address) values ('" + s.getSid() + "', '" + s.getcID() + "', '" + s.getName() + "', '" + s.getAddress() + "');";
		stmt.executeUpdate(query);
	}
	
	/**
	 * Deletes a student from the student table
	 * @param s Student Object
	 * @throws SQLException
	 */
	public void deleteStudent(Student s) throws SQLException {		
		conn = mysqlDS.getConnection();
		
		Statement stmt = conn.createStatement();
		String query = "delete from student where sid in('" + s.getSid() +"');";
		
		stmt.executeUpdate(query);
	}
	
	/**
	 * Joins Student and Course.
	 * @param s Student Object
	 * @return Student details and the course details
	 * @throws Exception
	 */
	public ArrayList<StudentDetails> loadStudentDetails(Student s) throws Exception {

		 ArrayList<StudentDetails> studentDetails = new ArrayList<StudentDetails>();
		 conn = mysqlDS.getConnection();
		 
		 Statement stmt = conn.createStatement();		
		 ResultSet rs = stmt.executeQuery("select s.sid, s.name, s.cid, c.cname, c.duration from student s join course c on s.cid = c.cid and s.sid='"+ s.getSid()+ "';");
		 
		 while (rs.next()) {
			 StudentDetails student = new StudentDetails();
			 student.setSid(rs.getString("sid"));
			 student.setName(rs.getString("name"));
			 student.setcID(rs.getString("cID"));
			 student.setcName(rs.getString("cName"));
			 student.setDuration(rs.getInt("duration"));
			 studentDetails.add(student);
		 }
		 return studentDetails;
	}
	
	/**
	 * Course DAO
	 */
	
	/**
	 * Loads course table.
	 * @return All courses
	 * @throws Exception
	 */
	public ArrayList<Course> loadCourses() throws Exception {
		 ArrayList<Course> courses = new ArrayList<Course>();
		 conn = mysqlDS.getConnection();
		 
		 Statement stmt = conn.createStatement();		
		 ResultSet rs = stmt.executeQuery("select * from course");
		 
		 while (rs.next()) {
			 Course course = new Course();
			 course.setcID(rs.getString("cID"));
			 course.setcName(rs.getString("cName"));
			 course.setDuration(rs.getInt("duration"));
			 courses.add(course);
		 }
		 return courses;
	}
	
	/**
	 * Adds course into course table
	 * @param c Course Object
	 * @throws Exception
	 */
	public void addCourse(Course c) throws Exception{

		conn = mysqlDS.getConnection();
		Statement stmt = conn.createStatement();
		
		String query = "insert into course (cID, cNAme, duration) values ('" + c.getcID() + "', '" + c.getcName() + "', '" + c.getDuration() + "');";
		stmt.executeUpdate(query);

	}
	
	/**
	 * Removes a course from the course table
	 * @param c Course Object
	 * @throws SQLException
	 */
	public void deleteCourse(Course c) throws SQLException {	
		conn = mysqlDS.getConnection();
		
		Statement stmt = conn.createStatement();
		String query = "delete from course where cid in('" + c.getcID() +"');";
		
		stmt.executeUpdate(query);

	}
	
	/**
	 * Joins Student Details with Course to return all students doing a particular course
	 * @param c Course Object
	 * @return Student details doing a particular course
	 * @throws Exception
	 */
	public ArrayList<StudentDetails> loadCourseStudentDetails(Course c) throws Exception {

		 ArrayList<StudentDetails> students = new ArrayList<StudentDetails>();
		 conn = mysqlDS.getConnection();
		 
		 Statement stmt = conn.createStatement();		
		 ResultSet rs = stmt.executeQuery("select c.cid, c.cname, c.duration, s.name, s.address from course c join student s on c.cid = s.cid and c.cid='"+ c.getcID()+"';");
		 while (rs.next()) {
			 StudentDetails studentDetails = new StudentDetails();
			 studentDetails.setcID(rs.getString("cID"));
			 studentDetails.setcName(rs.getString("cName"));
			 studentDetails.setDuration(rs.getInt("duration"));
			 studentDetails.setName(rs.getString("name"));
			 studentDetails.setAddress(rs.getString("address"));
			 students.add(studentDetails);
		 }
		 return students;
	}
	
}

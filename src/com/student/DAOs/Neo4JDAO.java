package com.student.DAOs;

import static org.neo4j.driver.v1.Values.parameters;

import org.neo4j.driver.v1.AuthTokens;
import org.neo4j.driver.v1.Driver;
import org.neo4j.driver.v1.GraphDatabase;
import org.neo4j.driver.v1.Session;
import com.student.Models.Student;

public class Neo4JDAO {

	public Neo4JDAO() {
		//DEFAULT CONSTRUCTOR
	}

	public void addStudent(Student student) {
		Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "neo4jdb"));
		Session session = driver.session();

		session.writeTransaction(tx -> tx.run("CREATE(:STUDENT{name: $name, address: $address})",
				parameters("name", student.getName(), "address", student.getAddress())));

		session.close();
		driver.close();
	}

	public void deleteStudent(Student student) {
		Driver driver = GraphDatabase.driver("bolt://localhost:7687", AuthTokens.basic("neo4j", "neo4jdb"));
		Session session = driver.session();

		session.run("MATCH(s:STUDENT{name: $name}) delete s;", parameters("name", student.getName()));

		session.close();
		driver.close();
	}
}

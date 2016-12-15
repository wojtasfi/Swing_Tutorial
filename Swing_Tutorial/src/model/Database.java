package model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

// This is the class which we are going to connect to database and exchange data (this is the root data)
//conntec(), disconnect(), save(), load()
public class Database {

	private Connection con;
	private List<Person> people;
	
	private int port;
	private String user;
	private String password;
	
	public void load() throws SQLException{
		
		people.clear();
		
		String sql = "select id, name, age, employment_status, tax_id, us_citizen, gender, occupation from people order by name";
		
		Statement selectStatement = con.createStatement();
		ResultSet results =selectStatement.executeQuery(sql);
		
		while(results.next()){
			
			
			int id =results.getInt("id");
			String name = results.getString("name");
			String age = results.getString("age");
			String emp = results.getString("employment_status");
			String tax = results.getString("tax_id");
			boolean us = results.getBoolean("us_citizen");
			String gender = results.getString("gender");
			String occ = results.getString("occupation");
			
			Person person = new Person(id,name,occ,AgeCategory.valueOf(age),EmploymentCategory.valueOf(emp), tax, us, Gender.valueOf(gender));
			people.add(person);
			
			
			
		}
		
		
		selectStatement.close();
		results.close();
		
		
	}

	public void connect() throws Exception {

		// return pozwala na wyjście z metody
		if (con != null)
			return;

		try {
			Class.forName("com.mysql.jdbc.Driver");
		} catch (ClassNotFoundException e) {
			throw new Exception("Driver not found");
		}

		String Url = "jdbc:mysql://localhost:3306/swingtest";
		con = DriverManager.getConnection(Url, "root", "pollop123");


	}

	public void disconnect() {

		if (con != null) {
			try {
				con.close();
			} catch (SQLException e) {
				System.out.println("Can`t close connection");
			}
		}
	}

	public void save() throws SQLException {

		// First we check if exists, if true then UPDATE, else INSERT
		// Do not use real values, use ? instead
		String checkSql = "Select count(*) as count from people where id=?";
		PreparedStatement checkStmt = con.prepareStatement(checkSql);

		String insertSql = "insert into people (id,name,age, employment_status, tax_id, us_citizen, gender, occupation) "
				+ "values (?,?,?,?,?,?,?,?)";
		PreparedStatement insertStmt = con.prepareStatement(insertSql);
		
		String updateSql = "update people set name=?,age=?, employment_status=?, tax_id=?, us_citizen=?, gender=?, occupation=? where id =?";
		PreparedStatement updateStmt = con.prepareStatement(updateSql);

		for (Person person : people) {

			int id = person.getId();
			String name = person.getName();
			String occupation = person.getOccupation();
			AgeCategory age = person.getAgeCategory();
			EmploymentCategory emp = person.getEmpCat();
			String tax = person.getTaxId();
			boolean isUs = person.isUsCitizen();
			Gender gender = person.getGender();

			checkStmt.setInt(1, id); // position of the ?, and value to insert
			ResultSet checkResult = checkStmt.executeQuery(); // it is going to
																// bring back
																// results

			checkResult.next();
			int count = checkResult.getInt(1);

			if (count == 0) {
				int col = 1;

				insertStmt.setInt(col++, id);
				insertStmt.setString(col++, name);
				insertStmt.setString(col++, age.name());
				insertStmt.setString(col++, emp.name());
				insertStmt.setString(col++, tax);
				insertStmt.setBoolean(col++, isUs);
				insertStmt.setString(col++, gender.name());
				insertStmt.setString(col++, occupation);

				insertStmt.executeUpdate();
			} else {
				int col = 1;
				
				updateStmt.setString(col++, name);
				updateStmt.setString(col++, age.name());
				updateStmt.setString(col++, emp.name());
				updateStmt.setString(col++, tax);
				updateStmt.setBoolean(col++, isUs);
				updateStmt.setString(col++, gender.name());
				updateStmt.setString(col++, occupation);
				updateStmt.setInt(col++, id);
				
				updateStmt.executeUpdate();

			}

		}

		insertStmt.close();
		checkStmt.close();

	}


	public Database() {
		people = new LinkedList<Person>();

	}

	public void addPerson(Person person) {
		people.add(person);
	}

	public List<Person> getPeople() {
		return Collections.unmodifiableList(people);
	}

	public void saveToFile(File file) throws IOException {
		FileOutputStream fos = new FileOutputStream(file);
		ObjectOutputStream oos = new ObjectOutputStream(fos);

		Person[] persons = people.toArray(new Person[people.size()]);

		oos.writeObject(persons);

		oos.close();
	}

	public void loadFromFile(File file) throws IOException {
		FileInputStream fis = new FileInputStream(file);
		ObjectInputStream ois = new ObjectInputStream(fis);

		try {
			Person[] persons = (Person[]) ois.readObject();

			people.clear();
			people.addAll(Arrays.asList(persons));

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		ois.close();

	}

	public void removePerson(int index) {
		people.remove(index);

	}

	public void configure(int port, String user, String password) throws Exception {
		// TODO Auto-generated method stub
		
		this.port = port;
		this.user = user;
		this.password = password;
		
		if(con == null){
			//disconnect();
			connect();
		}
		
	}
}

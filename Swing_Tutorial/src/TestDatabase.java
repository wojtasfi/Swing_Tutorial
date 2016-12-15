import java.sql.SQLException;

import model.AgeCategory;
import model.Database;
import model.EmploymentCategory;
import model.Gender;
import model.Person;

public class TestDatabase {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		System.out.println("Running database test");
		
		Database db = new Database();
		
		try {
			db.connect();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		db.addPerson(new Person("Wojtek", "builder", AgeCategory.adult, EmploymentCategory.employed, "777", true, Gender.male));
		db.addPerson(new Person("Sue", "artist", AgeCategory.senior, EmploymentCategory.selfEmployed, "888", true, Gender.female));
		try {
			db.save();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		try {
			db.load();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		db.disconnect();
	}

}

package gui;

import java.util.List;

import javax.swing.table.AbstractTableModel;

import model.EmploymentCategory;
import model.Person;

//A way to represent data in table
public class PersonTableModel extends AbstractTableModel {

	private List<Person> db;

	private String[] colNames = { "ID", "Name", "Occupation", "Age Category", "Employment Category", "US Citizen",
			"Tax ID" };

	public void setData(List<Person> db) {
		this.db = db;
	}

	@Override
	public boolean isCellEditable(int row, int col) {

		switch (col) {
		case 1:
			return true;
		case 4:
			return true;

		case 5:
			return true;
		default:
			return false;
		}
	}

	@Override //Po wybraniu wartości na tabeli ona tam zostanie dzięki tej metodzie
	public void setValueAt(Object value, int row, int col) {

		if (db == null) {
			return;
		}
		Person person = db.get(row);

		switch (col) {
		case 1:

			person.setName((String) value);
			break;

		case 4:

			person.setEmpCat((EmploymentCategory) value);
			break;

		case 5:

			person.setUsCitizen((boolean) value);
			break;
		default:
			return;
		}
	}

	@Override
	public String getColumnName(int column) {
		// TODO Auto-generated method stub
		return colNames[column];
	}

	@Override
	public int getColumnCount() {
		// TODO Auto-generated method stub
		return 7;
	}

	@Override
	public int getRowCount() {
		// TODO Auto-generated method stub
		return db.size();
	}

	@Override
	public Object getValueAt(int row, int col) {
		Person person = db.get(row);

		switch (col) {
		case 0:
			return person.getId();
		case 1:
			return person.getName();
		case 2:
			return person.getOccupation();
		case 3:
			return person.getAgeCategory();
		case 4:
			return person.getEmpCat();
		case 5:
			return person.isUsCitizen();
		case 6:
			return person.getTaxId();
		}

		return null;
	}

	@Override
	public Class<?> getColumnClass(int col) {

		switch (col) {
		case 0:
			return Integer.class;
		case 1:
			return String.class;
		case 2:
			return String.class;
		case 3:
			return String.class;
		case 4:
			return EmploymentCategory.class;
		case 5:
			return Boolean.class;
		case 6:
			return String.class;
		default:
			return null;
		}

	}

}

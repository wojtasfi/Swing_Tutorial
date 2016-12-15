package gui;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JComboBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

import model.EmploymentCategory;


//AbstractCellEditor implementuje większość metod w tym przpadku
public class EmploymentCategoryEditor extends AbstractCellEditor implements TableCellEditor {

	private JComboBox combo;
	
	public EmploymentCategoryEditor(){
		combo = new JComboBox(EmploymentCategory.values());
	}
	
	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		return combo.getSelectedItem();
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		// TODO Auto-generated method stub
		combo.setSelectedItem(value);
		
		combo.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				fireEditingStopped();
			}
			
		});
		
		return combo;
	}

	@Override
	public boolean isCellEditable(EventObject e) {
		// TODO Auto-generated method stub
		return true;
	}

	

}

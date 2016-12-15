package gui;

import java.awt.Component;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.util.EventObject;

import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeCellEditor;
import javax.swing.tree.TreePath;

public class ServerTreeCellEditor extends AbstractCellEditor implements TreeCellEditor {

	
	private static final long serialVersionUID = 1L;
	
	
	private ServerTreeCellRenderer renderer;
	private JCheckBox checkBox;
	private ServerInfo info;
	
	public ServerTreeCellEditor(){
		
		renderer = new ServerTreeCellRenderer();
	}

	@Override
	public Component getTreeCellEditorComponent(JTree tree, Object value, boolean selected, boolean expanded, boolean leaf,
			int row) {
		Component component = (JCheckBox) renderer.getTreeCellRendererComponent(tree, value, selected, expanded, leaf, row, true);
		
		if(leaf){
			
			DefaultMutableTreeNode treeNode = (DefaultMutableTreeNode) value;
			
			info = (ServerInfo) treeNode.getUserObject();
			
			 checkBox = (JCheckBox) component;
			
			ItemListener itemListener = new ItemListener(){

				@Override
				public void itemStateChanged(ItemEvent arg0) {
					// TODO Auto-generated method stub
					fireEditingStopped();
					checkBox.removeItemListener(this); //this refers to the anonymous class now
					
					
				}
				
			};
			checkBox.addItemListener(itemListener);
		}
		
		return component;
	}

	@Override
	public Object getCellEditorValue() {
		// TODO Auto-generated method stub
		
		info.setChecked(checkBox.isSelected());
		return info;
	}

	@Override
	//I need to ask if cell is editable (is leaf) before editing it
	// If True then it gets the TreeCellEditorComponenet
	public boolean isCellEditable(EventObject event) {

		if(!(event instanceof MouseEvent)) return false;
		
		MouseEvent mouseEvent = (MouseEvent) event;
		
		JTree tree = (JTree) event.getSource();
		
		TreePath path = tree.getPathForLocation(mouseEvent.getX(), mouseEvent.getY());
		
		if(path ==null) return false;
		
		Object lastComponent = path.getLastPathComponent();
		
		if(lastComponent == null) return false;
		
		DefaultMutableTreeNode  treeNode = (DefaultMutableTreeNode) lastComponent;
		
		return treeNode.isLeaf();
		
	}


	
}

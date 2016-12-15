package gui;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.URL;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JToolBar;

public class Toolbar extends JToolBar implements ActionListener {

	private JButton saveButton;
	private JButton refreshButton;
	private ToolbarListener toolbarListener;

	public Toolbar()  {
		
		//setBorder(BorderFactory.createEtchedBorder());

		saveButton = new JButton("Save");
		//saveButton.setIcon(Utils.createIcon("/images/save.png"));
		saveButton.setToolTipText("Save");
		
		refreshButton = new JButton("Refresh");
		//refreshButton.setIcon(Utils.createIcon("/images/refresh-button.jpg"));
		refreshButton.setToolTipText("Refresh");

		saveButton.addActionListener(this);
		refreshButton.addActionListener(this);

		add(saveButton);
		
		add(refreshButton);

	}
	
	//Loading images
	private ImageIcon createIcon(String path){
		
		URL url =getClass().getResource(path);
		
		if(url == null){
			JOptionPane.showMessageDialog(Toolbar.this, "Unable to load resuroce: " + path,"Icon problem", 
					JOptionPane.ERROR_MESSAGE);
		}
		ImageIcon icon = new ImageIcon(url);
		return icon;
		
	}

	public void setToolbarListener(ToolbarListener listener) {
		this.toolbarListener = listener;
	}

	@Override
	public void actionPerformed(ActionEvent e) {

		JButton clicked = (JButton) e.getSource();

		if (clicked == saveButton) {
			if (toolbarListener != null) {
				toolbarListener.saveEventOccured();
			}
		} else {
			if (toolbarListener != null) {
				toolbarListener.refreshEventOccured();
			}
		}

	}
}

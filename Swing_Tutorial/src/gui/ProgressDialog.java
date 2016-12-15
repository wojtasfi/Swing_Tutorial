package gui;

import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Window;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JProgressBar;
import javax.swing.SwingUtilities;

public class ProgressDialog extends JDialog {
	
	private JButton cancelButton;
	private JProgressBar progressBar;
	private ProgressDialogListener listener;
	
	
	public void setProgDialListener(ProgressDialogListener listener){
		this.listener = listener;
	}
	
	public ProgressDialog(Window parent, String title){
		super(parent, title, ModalityType.APPLICATION_MODAL);
		
		cancelButton = new JButton("Cancel");
		progressBar = new JProgressBar();
		
		setLayout(new FlowLayout());
		setLocationRelativeTo(parent);
		
		Dimension size = cancelButton.getPreferredSize();
		size.width = 400;
		progressBar.setPreferredSize(size);
		
		
		progressBar.setStringPainted(true);
		progressBar.setString("Retreiveing masseges...");
		add(progressBar);
		add(cancelButton);
		
		
		cancelButton.addActionListener(new ActionListener(){

			@Override
			public void actionPerformed(ActionEvent e) {
				if(listener != null){
					listener.progressDialogCancelled();
				}
				
			}
			
		});
		//min size for these type of controlls
		
		setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
		
		addWindowListener(new WindowAdapter(){

			@Override
			public void windowClosing(WindowEvent e) {
				if(listener != null){
					listener.progressDialogCancelled();
				}
			}
			
		});
		pack();
		
	}
	
	public void setMaximum(int count){
		progressBar.setMaximum(count);
	}
	
	//current value
	public void setValue(int value){
		int progress = 100*value/progressBar.getMaximum() ;
		progressBar.setString(String.format("%d%% complete", progress));
		progressBar.setValue(value);
		
	}

	@Override
	public void setVisible(final boolean visible) {
		
		
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				
				if (visible == false){
					try {
						Thread.sleep(1000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				else {
					progressBar.setValue(0);
				}
				
				if(visible){
					setCursor(Cursor.getPredefinedCursor(Cursor.WAIT_CURSOR));
				}else {
					setCursor(Cursor.getDefaultCursor());
				}
					
				//accessing super class 
				ProgressDialog.super.setVisible(visible);
				
			}
			
			
			
		});
	}
	
	
	

}

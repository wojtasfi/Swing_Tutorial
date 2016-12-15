package gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutionException;

import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTree;
import javax.swing.SwingWorker;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreeSelectionModel;

import controller.MessageServer;

public class MessagePanel extends JPanel implements ProgressDialogListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3846058234088966557L;
	private JTree serverTree;
	private ServerTreeCellRenderer treeCellRenderer;
	private ServerTreeCellEditor treeCellEditor;

	private Set<Integer> selectedServers;
	private MessageServer messageServer;

	private ProgressDialog progressDialog;
	private SwingWorker<List<Message>, Integer>  worker;
	
	private TextPanel textPanel;
	private JList messageList;
	private JSplitPane upperPane;
	private JSplitPane lowerPane;
	private DefaultListModel messageListModel;
	
	
	

	public MessagePanel(JFrame parent) {
		setLayout(new BorderLayout());
		
		
		messageListModel = new DefaultListModel();

		progressDialog = new ProgressDialog(parent, "Messages Downloading...");
		messageServer = new MessageServer();

		selectedServers = new TreeSet<Integer>();
		selectedServers.add(0);
		selectedServers.add(1);
		selectedServers.add(2);
		
		progressDialog.setProgDialListener(this);

		treeCellRenderer = new ServerTreeCellRenderer();
		treeCellEditor = new ServerTreeCellEditor();

		/*
		 * I need to download the icons package
		 * treeCellRenderer.setLeafIcon(Utils.createIcon("/images/Server16.gif")
		 * ); treeCellRenderer.setOpenIcon(Utils.createIcon(
		 * "/images/WebComponent16.gif"));
		 * treeCellRenderer.setClosedIcon(Utils.createIcon(
		 * "/images/WebComponentAdd16.gif"));
		 */

		serverTree = new JTree(createTree());
		serverTree.setCellRenderer(treeCellRenderer);
		serverTree.setEditable(true);
		serverTree.setCellEditor(treeCellEditor);
		serverTree.getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);

		messageServer.setSelectedServers(selectedServers);
		
		treeCellEditor.addCellEditorListener(new CellEditorListener() {

			@Override
			public void editingStopped(ChangeEvent e) {
				ServerInfo info = (ServerInfo) treeCellEditor.getCellEditorValue();
				System.out.println(info.toString() + " " + info.getId() + " " + info.isChecked());

				int serverId = info.getId();

				if (info.isChecked()) {
					selectedServers.add(serverId);
				} else {
					selectedServers.remove(serverId);
				}

				messageServer.setSelectedServers(selectedServers);

				retriveMessages();

			}

			

			@Override
			public void editingCanceled(ChangeEvent e) {
				// TODO Auto-generated method stub

			}

		});

		serverTree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) serverTree.getLastSelectedPathComponent();
				Object userObject = node.getUserObject();

				if (userObject instanceof ServerInfo) {

					if (userObject != null) {

						int id = ((ServerInfo) userObject).getId();
					}
				}
			}

		});

		textPanel = new TextPanel();
		messageList = new JList(messageListModel);
		
		messageList.setCellRenderer(new MessageListRenderer());
		
		
		messageList.addListSelectionListener(new ListSelectionListener(){

			@Override
			public void valueChanged(ListSelectionEvent arg0) {
				// TODO Auto-generated method stub
				Message message = (Message) messageList.getSelectedValue();
				
				try{
					textPanel.setText(message.getContents());
				}catch(NullPointerException e){
					
				}
				
			}
			
		});
		lowerPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,  new JScrollPane(messageList), textPanel);
		upperPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, new JScrollPane(serverTree), lowerPane);
		
		textPanel.setMinimumSize(new Dimension(10,100));
		messageList.setMinimumSize(new Dimension(10,100));
		//lowerPane.setDividerLocation(location);
		
		//Który ma zabrać wolne miejsce jak robię resize:
		//upperPane.setResizeWeight(1.0);
		//upperPane.setResizeWeight(0);
		
		
		upperPane.setResizeWeight(0.5);
		lowerPane.setResizeWeight(0.5);
		add(upperPane, BorderLayout.CENTER);

		
	}
	
	public void refresh(){
		retriveMessages();
	}


	private void retriveMessages() {
		// TODO Auto-generated method stub

		progressDialog.setMaximum(messageServer.getMessageCount());

		progressDialog.setVisible(true);

		worker = new SwingWorker<List<Message>, Integer>() {

			@Override
			protected List<Message> doInBackground() throws Exception {

				int count = 0;

				List<Message> retrievedMessages = new ArrayList<Message>();

				
				for (Message message : messageServer) {
					
					if(isCancelled()) break;

					System.out.println(message.getTitle());

					retrievedMessages.add(message);

					count++;

					publish(count); // process will receive whatever you
									// publish
				}
				return retrievedMessages;
			}

			@Override
			protected void done() {
				progressDialog.setVisible(false);
				
				if(isCancelled()) return;
				 
				try {
					List<Message> retrievedMessages = get();
					
					messageListModel.removeAllElements();
					
					for(Message message: retrievedMessages){
						messageListModel.addElement(message);
					}
					
					
					messageList.setSelectedIndex(0);
				} catch (InterruptedException | ExecutionException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

				
			}

			@Override
			protected void process(List<Integer> counts) {

				int retrieved = counts.get(counts.size() - 1);

				progressDialog.setValue(retrieved);
			}

		};

		worker.execute();
	}
	private DefaultMutableTreeNode createTree() {

		DefaultMutableTreeNode top = new DefaultMutableTreeNode("Server");

		DefaultMutableTreeNode branch1 = new DefaultMutableTreeNode("USA");
		DefaultMutableTreeNode server1 = new DefaultMutableTreeNode(
				new ServerInfo("New York", 0, selectedServers.contains(0)));
		DefaultMutableTreeNode server2 = new DefaultMutableTreeNode(
				new ServerInfo("Boston", 1, selectedServers.contains(1)));
		DefaultMutableTreeNode server3 = new DefaultMutableTreeNode(
				new ServerInfo("Los Angeles", 2, selectedServers.contains(2)));

		branch1.add(server1);
		branch1.add(server2);
		branch1.add(server3);

		DefaultMutableTreeNode branch2 = new DefaultMutableTreeNode("UK");
		DefaultMutableTreeNode server4 = new DefaultMutableTreeNode(
				new ServerInfo("London", 3, selectedServers.contains(3)));
		DefaultMutableTreeNode server5 = new DefaultMutableTreeNode(
				new ServerInfo("Edinburgh", 4, selectedServers.contains(4)));

		branch2.add(server4);
		branch2.add(server5);

		top.add(branch1);
		top.add(branch2);

		return top;

	}

	@Override
	public void progressDialogCancelled() {
		if(worker != null){
			worker.cancel(true);
		}
	}
}

// This objects stores values when in the tree node (the name of that node)

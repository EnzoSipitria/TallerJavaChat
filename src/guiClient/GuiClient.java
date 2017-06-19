package guiClient;

import java.awt.EventQueue;
import java.awt.ScrollPane;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;

import conections.Channel;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;

public class GuiClient {

	protected static final String SEPARATOR = "::";
	private JFrame frame;
	private JTextArea chatTextArea;
	private static Client client;
	String name="";
	private static JTabbedPane tabbedPaneChats = new JTabbedPane(JTabbedPane.TOP);;
	private static JTextArea txtAreaConversation;
//	private static JTextArea txtAreaUserList;
	private static JList<String> channelList = new JList<>();
//	private static DefaultListModel<String> userListModel;
//	private static DefaultListModel<String> channelListModel;
	private static JList<String> userList = new JList<>();
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GuiClient window = new GuiClient("");
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public GuiClient(String name) {
		this.name=name;
		
		name = JOptionPane.showInputDialog("ingrese su usuario");
		client = new Client(name);
		initialize();
		frame.setTitle(name);
	}


	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		
		frame = new JFrame(name);
//		frame.setTitle(name);
		frame.setBounds(100, 100, 800, 600);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(360, 459, 402, 2);
		frame.getContentPane().add(separator);
		
		chatTextArea = new JTextArea();
		chatTextArea.setBounds(360, 468, 298, 36);
		frame.getContentPane().add(chatTextArea);
		
		JButton btnSend = new JButton("Enviar");
		btnSend.setBounds(668, 472, 89, 23);
		frame.getContentPane().add(btnSend);
		btnSend.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				int chatSelecionado = tabbedPaneChats.getSelectedIndex();
				System.out.println(tabbedPaneChats.getTitleAt(chatSelecionado)+"sjladjasdadasdas");
				String channelName = tabbedPaneChats.getTitleAt(chatSelecionado);
				client.sendMessage("chat"+SEPARATOR+client.getName()+SEPARATOR+channelName+SEPARATOR+chatTextArea.getText());
				
//				setConversation(chatTextArea.getText(),name);
				chatTextArea.setText("");
				}
		});
		
		JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(10, 54, 334, 391);
		frame.getContentPane().add(tabbedPane);
		
//		userListModel = new DefaultListModel<String>();
//		userList = new JList<String>();
		
		tabbedPane.addTab("usuarios", null, userList, null);
		
//		channelListModel = new DefaultListModel<String>();
//		channelList = new JList<String>();
		tabbedPane.addTab("canales", null, channelList, null);
		
		tabbedPaneChats.setBounds(360, 54, 414, 394);
		frame.getContentPane().add(tabbedPaneChats);
		
		JScrollPane scrollPane = new JScrollPane();
//		tabbedPaneChats.addTab("New tab", null, scrollPane, null);
		
		txtAreaConversation = new JTextArea();
		scrollPane.setViewportView(txtAreaConversation);
		
		JButton btnPrivateChat = new JButton("private");
		btnPrivateChat.setBounds(10, 459, 89, 23);
		frame.getContentPane().add(btnPrivateChat);
		btnPrivateChat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				String selectedUser = userList.getSelectedValue();
				
				
				client.privateChat(selectedUser);
				
//				setConversation(chatTextArea.getText(),name);
				chatTextArea.setText("");
				}
		});
		
		JButton btnCreateChannel = new JButton("nuevo canal");
		btnCreateChannel.setBounds(109, 459, 104, 23);
		frame.getContentPane().add(btnCreateChannel);
		
		btnCreateChannel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String channelName = JOptionPane.showInputDialog("ingrese el nombre del canal");
				client.createChannel(channelName);
				
//				setConversation(chatTextArea.getText(),name);
				chatTextArea.setText("");
				}
		});
		
		
		JButton btnJoinChat = new JButton("Ingresar");
		btnJoinChat.setBounds(223, 459, 89, 23);
		frame.getContentPane().add(btnJoinChat);
		btnJoinChat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				String selectedChannel = channelList.getSelectedValue();
				client.joinChannel(selectedChannel);
//				nuevoChatTab(selectedChannel);
//				setConversation(chatTextArea.getText(),name);
				chatTextArea.setText("");
				}
		});
		
		JButton btnUsersConectedChannel = new JButton("Mostrar conectados al canal");
		btnUsersConectedChannel.setBounds(80, 493, 183, 23);
		frame.getContentPane().add(btnUsersConectedChannel);
		btnUsersConectedChannel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String selectedChannel = channelList.getSelectedValue();
				client.usersConected(selectedChannel,client.getName());
				
//				setConversation(chatTextArea.getText(),name);
				chatTextArea.setText("");
				}
		});
	}
	
	
	public static void nuevoChatTab (String chatName){
//		JTabbedPane tabbedPaneChats = new JTabbedPane(JTabbedPane.TOP);
//		tabbedPaneChats.setBounds(360, 54, 414, 394);
//		frame.getContentPane().add(tabbedPaneChats);
		
		JTextArea newConversation = new JTextArea();
//		JScrollPane newScrollPane = new JScrollPane();
		tabbedPaneChats.addTab(chatName, null, newConversation, null);
		
//		newScrollPane.setViewportView(newConversation);
		
		
		
	}
	
	public static void mostrarConectados(ArrayList<String> usuarios){
		String lista="";
		for (String string : usuarios) {
			lista+= string+"\n";
		}
		JOptionPane.showMessageDialog(null,lista );
		
		
	}
	
	public static void interfazCanales(){
		DefaultListModel<String> channelListModel = new DefaultListModel<>();
		for (String canal : client.getCanales()) 
			if ( canal.contains("[priv]") )
			channelListModel.addElement(canal);
		channelList.setModel(channelListModel);
	
	}
	

	public static void interfazUsuarios(){
		
		DefaultListModel<String> userListModel = new DefaultListModel<>();
		for (String user : client.getUsuarios()) {
			System.out.println("user : "+user);
			if ( !user.equals(client.getName()) )
				userListModel.addElement(user);
			System.out.println("agregado usuario:"+user);
			
		}
		userList.setModel(userListModel);
	}
	
	public static void setConversation(String texto, String nombre, String chatName){
		for(int i=0; i < tabbedPaneChats.getTabCount();i++){
			if(tabbedPaneChats.getTitleAt(i).equals(chatName))
				((JTextArea) tabbedPaneChats.getComponentAt(i)).append("["+nombre+"] "+texto+"\n");
				
//				txtAreaConversation.append("["+nombre+"] "+texto+"\n");
					
		}
		//txtAreaConversation.append("["+nombre+"] "+texto+"\n");
		
	}
}

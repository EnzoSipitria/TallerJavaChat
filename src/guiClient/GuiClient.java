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
import javax.swing.LayoutStyle.ComponentPlacement;
import javax.swing.ListModel;
import javax.swing.table.DefaultTableModel;

import conections.Channel;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.GroupLayout;
import javax.swing.GroupLayout.Alignment;

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
	private JButton btnCreateChannel;
	private JButton btnJoinChat;
	private JButton btnPrivateChat;
	private JButton btnUsersConectedChannel;
	private JButton btnDejarSala;
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

		JSeparator separator = new JSeparator();

		chatTextArea = new JTextArea();
		chatTextArea.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				enterPressed(e);
			}
		});

		JButton btnSend = new JButton("Enviar");
		
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
		tabbedPane.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int index = tabbedPane.getSelectedIndex();
				String selectedTab = tabbedPane.getTitleAt(index);
				if ( selectedTab.equals("usuarios") ) {
					
					btnJoinChat.setVisible(false);
					btnPrivateChat.setVisible(true);
					btnUsersConectedChannel.setVisible(false);
				}
				if ( selectedTab.equals("canales")){
					btnJoinChat.setVisible(true);
					btnPrivateChat.setVisible(false);
					btnUsersConectedChannel.setVisible(true);
				}
					
			}
		});

		//		userListModel = new DefaultListModel<String>();
		//		userList = new JList<String>();

		tabbedPane.addTab("usuarios", null, userList, null);

		//		channelListModel = new DefaultListModel<String>();
		//		channelList = new JList<String>();
		tabbedPane.addTab("canales", null, channelList, null);

		JScrollPane scrollPane = new JScrollPane();
		//		tabbedPaneChats.addTab("New tab", null, scrollPane, null);

		txtAreaConversation = new JTextArea();
		scrollPane.setViewportView(txtAreaConversation);

		btnPrivateChat = new JButton("private");
		btnPrivateChat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				String selectedUser = userList.getSelectedValue();


				client.privateChat(selectedUser);

				//				setConversation(chatTextArea.getText(),name);
				chatTextArea.setText("");
				btnDejarSala.setVisible(true);
			}
		});

		btnCreateChannel = new JButton("nuevo canal");

		btnCreateChannel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String channelName = JOptionPane.showInputDialog("ingrese el nombre del canal");
				if ( channelName!=null )
					client.createChannel(channelName);

				//				setConversation(chatTextArea.getText(),name);
				chatTextArea.setText("");
				btnDejarSala.setVisible(true);
			}
		});


		btnJoinChat = new JButton("Ingresar");
		btnJoinChat.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {

				String selectedChannel = channelList.getSelectedValue();
				client.joinChannel(selectedChannel);
				//				nuevoChatTab(selectedChannel);
				//				setConversation(chatTextArea.getText(),name);
				chatTextArea.setText("");
				btnDejarSala.setVisible(true);
			}
		});

		btnUsersConectedChannel = new JButton("Mostrar conectados al canal");

		btnUsersConectedChannel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				String selectedChannel = channelList.getSelectedValue();
				client.usersConected(selectedChannel,client.getName());

				//				setConversation(chatTextArea.getText(),name);
				chatTextArea.setText("");
			}
		});

		btnDejarSala = new JButton("Dejar Sala");
		btnDejarSala.setVisible(false);
		btnDejarSala.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent arg0) {
				
				int index = tabbedPaneChats.getSelectedIndex();
				//				tabbedPane.getTitleAt(index);
				client.leaveChat(tabbedPaneChats.getTitleAt(index));
			}
		});
		btnJoinChat.setVisible(false);
		btnPrivateChat.setVisible(true);
		btnCreateChannel.setVisible(true);
		btnUsersConectedChannel.setVisible(false);
		GroupLayout groupLayout = new GroupLayout(frame.getContentPane());
		groupLayout.setHorizontalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(668)
					.addComponent(btnDejarSala, GroupLayout.PREFERRED_SIZE, 106, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 334, GroupLayout.PREFERRED_SIZE)
					.addGap(16)
					.addComponent(tabbedPaneChats, GroupLayout.PREFERRED_SIZE, 414, GroupLayout.PREFERRED_SIZE))
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(10)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnJoinChat, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE)
						.addComponent(btnPrivateChat, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))
					.addGap(39)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addGroup(groupLayout.createSequentialGroup()
							.addGap(46)
							.addComponent(btnCreateChannel, GroupLayout.PREFERRED_SIZE, 104, GroupLayout.PREFERRED_SIZE))
						.addComponent(btnUsersConectedChannel, GroupLayout.PREFERRED_SIZE, 190, GroupLayout.PREFERRED_SIZE))
					.addGap(32)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(separator, GroupLayout.PREFERRED_SIZE, 402, GroupLayout.PREFERRED_SIZE)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(chatTextArea, GroupLayout.PREFERRED_SIZE, 298, GroupLayout.PREFERRED_SIZE)
							.addPreferredGap(ComponentPlacement.RELATED)
							.addGap(10)
							.addComponent(btnSend, GroupLayout.PREFERRED_SIZE, 89, GroupLayout.PREFERRED_SIZE))))
				.addGap(2)
		);
		groupLayout.setVerticalGroup(
			groupLayout.createParallelGroup(Alignment.LEADING)
				.addGroup(groupLayout.createSequentialGroup()
					.addGap(11)
					.addComponent(btnDejarSala)
					.addGap(20)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(tabbedPane, GroupLayout.PREFERRED_SIZE, 391, GroupLayout.PREFERRED_SIZE)
						.addComponent(tabbedPaneChats, GroupLayout.PREFERRED_SIZE, 394, GroupLayout.PREFERRED_SIZE))
					.addGap(11)
					.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
						.addComponent(btnJoinChat)
						.addComponent(btnPrivateChat)
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(btnCreateChannel)
							.addGap(11)
							.addComponent(btnUsersConectedChannel))
						.addGroup(groupLayout.createSequentialGroup()
							.addComponent(separator, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
							.addGap(7)
							.addGroup(groupLayout.createParallelGroup(Alignment.LEADING)
								.addComponent(chatTextArea, GroupLayout.PREFERRED_SIZE, 36, GroupLayout.PREFERRED_SIZE)
								.addGroup(groupLayout.createSequentialGroup()
									.addGap(4)
									.addPreferredGap(ComponentPlacement.RELATED)
									.addComponent(btnSend))))))
				.addGap(2)
		);
		frame.getContentPane().setLayout(groupLayout);
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

	private void enterPressed (KeyEvent event){
		if (event.getKeyCode()==KeyEvent.VK_ENTER){
			int chatSelecionado = tabbedPaneChats.getSelectedIndex();
			System.out.println(tabbedPaneChats.getTitleAt(chatSelecionado)+"sjladjasdadasdas");
			String channelName = tabbedPaneChats.getTitleAt(chatSelecionado);
			client.sendMessage("chat"+SEPARATOR+client.getName()+SEPARATOR+channelName+SEPARATOR+chatTextArea.getText());

			//				setConversation(chatTextArea.getText(),name);
			
			chatTextArea.setText("");
		}
		
			
		
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
			if ( !canal.contains("[priv]") )
				channelListModel.addElement(canal);
		channelList.setModel(channelListModel);

	}


	public static void interfazUsuarios(){

		DefaultListModel<String> userListModel = new DefaultListModel<>();
		for (String user : client.getUsuarios()) {
			System.out.println("user : "+user);
			if ( !user.equals(client.getName()) )
				userListModel.addElement(user);
//			System.out.println("agregado usuario:"+user);

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

	public static void deleteChannelTab(String channelName) {
		// TODO Auto-generated method stub
		for(int i=0; i < tabbedPaneChats.getTabCount();i++){
			if( tabbedPaneChats.getTitleAt(i).equals(channelName) ){
				tabbedPaneChats.getComponentAt(i).setVisible(false);
				tabbedPaneChats.remove(i);
				}
			tabbedPaneChats.remove(tabbedPaneChats.getSelectedIndex());
				
		}

	}
}

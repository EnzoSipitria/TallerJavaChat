package guiServer;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JTextArea;

import guiClient.Client;
import guiClient.GuiClient;

import javax.swing.JButton;
import javax.swing.JTextField;

public class MainWindowServer {

	private JFrame frame;

	/**
	 * Launch the application.
	 */
	
	
	private Server server;
	private Client client;
	private JTextField textFieldCliente;
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					MainWindowServer window = new MainWindowServer();
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
	public MainWindowServer() {
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();
		frame.setBounds(100, 100, 1920, 1080);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(10, 57, 334, 515);
		frame.getContentPane().add(textArea);
		
		JButton btnClient = new JButton("New Client");
		btnClient.setBounds(462, 81, 186, 23);
		frame.getContentPane().add(btnClient);
		
		textFieldCliente = new JTextField();
		textFieldCliente.setBounds(658, 82, 197, 20);
		frame.getContentPane().add(textFieldCliente);
		textFieldCliente.setColumns(10);
		btnClient.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				new GuiClient(textFieldCliente.getText());				
				
			}
		});
	}

}

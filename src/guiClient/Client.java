package guiClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.ArrayList;

import conections.Channel;

public class Client {
	
	private Socket socketConnection;
	private int port=2317;
	private String host = "localhost";
	private DataOutputStream dataOut;
	private DataInputStream dataIn;
	private InputStream is;
	private OutputStream os;
	
	private ArrayList<String> usuarios= new ArrayList<>();
	private ArrayList<String> canales = new ArrayList<>();
	
	
	
	public DataOutputStream getDataOut() {
		return dataOut;
	}



	public void setDataOut(DataOutputStream dataOut) {
		this.dataOut = dataOut;
	}



	public DataInputStream getDataIn() {
		return dataIn;
	}



	public void setDataIn(DataInputStream dataIn) {
		this.dataIn = dataIn;
	}



	public InputStream getIs() {
		return is;
	}



	public void setIs(InputStream is) {
		this.is = is;
	}



	public OutputStream getOs() {
		return os;
	}



	public void setOs(OutputStream os) {
		this.os = os;
	}



	public String getName() {
		return name;
	}



	public void setName(String name) {
		this.name = name;
	}


	private String name;
	
	public Client (String name, Socket socket){
        try {
        	this.name=name;
        	this.socketConnection=socket;
        	is = socketConnection.getInputStream();
			os = socketConnection.getOutputStream();
			dataIn = new DataInputStream(is);
			dataOut = new DataOutputStream(os);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public Client(String name) {
		this.name=name;
		try{
            System.out.println("cliente inicio...");
            socketConnection = new Socket(host, port);
            is = socketConnection.getInputStream();
            os = socketConnection.getOutputStream();
            dataIn = new DataInputStream(is);
            dataOut = new DataOutputStream(os);
            
            dataOut.writeUTF(name);
            ClientThread hilo = new ClientThread(socketConnection, this);
            hilo.start();    
            
            
            System.out.println("hilo iniciado"+name);
        } catch (Exception e){
        	
        }
		
		// TODO Auto-generated constructor stub
	}
	
	
	
	
	
	
	
	public ArrayList<String> getUsuarios() {
		return usuarios;
	}




	public ArrayList<String> getCanales() {
		return canales;
	}

	
	public void createChatTab(String chatName){
		GuiClient.nuevoChatTab(chatName);
	}


	public void updateUsers(String user){
		usuarios.add(user);
		GuiClient.interfazUsuarios();
	}
	
	public void updateChannels(String channelName){
		
		canales.add(channelName);
		GuiClient.interfazCanales();
		System.out.println(canales.toString());
		
	}
	
	public void sendMessage(String mensajeEnviado){
		
		try{
			/**
			 * se lee de la entrada o del campo de texto del chat
			 */
            dataOut = new DataOutputStream(os);
            dataOut.writeUTF(mensajeEnviado);
            System.out.println("se envio un mensaje");
        } catch (Exception e){
            
        }
		
	}
	
	public void usersConected(String canal,String name){
		
		try {
			dataOut = new DataOutputStream(socketConnection.getOutputStream());
			dataOut.writeUTF("getUsuarios"+GuiClient.SEPARATOR+name+GuiClient.SEPARATOR+canal);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void channelsAvailable(){
		
		
		/*mostrando la lista de canales ebn la interfaz*/
		
		
	}
	
	public void createChannel(String channelName){
		try {
			dataOut = new DataOutputStream(socketConnection.getOutputStream());
			dataOut.writeUTF("createChannel"+GuiClient.SEPARATOR+channelName+GuiClient.SEPARATOR+name);//se le puede mandar un nombre para el canal? 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void privateChat(String receptor){
		try {
			dataOut = new DataOutputStream(socketConnection.getOutputStream());
			dataOut.writeUTF("private"+GuiClient.SEPARATOR+name+GuiClient.SEPARATOR+receptor);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	public void joinChannel (String canal){
		try {
			dataOut = new DataOutputStream(socketConnection.getOutputStream());
			dataOut.writeUTF("joinChannel"+GuiClient.SEPARATOR+canal+GuiClient.SEPARATOR+name);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}

	public void showConectedUsers(ArrayList<String> usuarios){
		GuiClient.mostrarConectados(usuarios);
		
	}

	public void actualizarVista(String texto,String nombre, String chatName) {
		
		GuiClient.setConversation(texto,nombre, chatName);
		// TODO Auto-generated method stub
		
	}
	
	
	public static void main(String[] args) {
		Client cliente = new Client("cliente");
		cliente.channelsAvailable();
	}



	public void updateChannels(ArrayList<String> channels) {
		canales.clear();
		canales.addAll(channels);
		GuiClient.interfazCanales();
		// TODO Auto-generated method stub
		
	}



	public void updateUsers(ArrayList<String> users) {
		usuarios.clear();
		usuarios.addAll(users);
		GuiClient.interfazUsuarios();
		// TODO Auto-generated method stub
		
	}
	
	
	
	
	

}

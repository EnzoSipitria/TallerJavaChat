package guiServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.TimerTask;

import org.omg.CORBA.PUBLIC_MEMBER;

import conections.Channel;
import guiClient.Client;

public class Server implements Runnable{

	
	public static final String SEPARATOR = "::";
	private ServerSocket serverSocket;
	private int port = 2317;
    private ArrayList<Channel> canales= new ArrayList<>(); 
    private ArrayList<Client> usuarios = new ArrayList<>();
//    private Channel channel = new Channel();

	public Server(int port) {
		try {
			this.serverSocket = new ServerSocket(this.port);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	@Override
	public void run() {
//		canales.add(channel);
		while (true){
			Socket socketClient = null;
			try {
				System.out.println("servidor niciado esperando conexiones");
				socketClient = serverSocket.accept();
//				usuarios.add(socketClient);
				System.out.println("usuario aceptado");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			DataInputStream dataIn = null;
			try {
				dataIn = new DataInputStream(socketClient.getInputStream());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			ServerThread hilo = null;
			String userName= "";
			try {
				userName = dataIn.readUTF();
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			usuarios.add(new Client(userName, socketClient));
			enviarMensajes("userConected"+SEPARATOR+userName);
			enviarMensajes("availableUsers"+SEPARATOR+availableUsers());
			enviarMensajes("availableChannels"+SEPARATOR+availableChannels());
 				hilo = new ServerThread(socketClient,userName, this, null);
			
			hilo.start();
		}
		
	}
	

	
	/**
	 * emvia un mensaje a todos los usuarios(directivas del servidor para el funcionamiento del system chat)
	 * @param mensaje mensaje con la directiva para actualizar las vistas del cliente
	 * @param emisor quien envia el mensaje
	 */
	public void enviarMensajes(String mensaje) {
		for (Client user : usuarios) {
			DataOutputStream dataOut = new DataOutputStream(user.getOs());
			try {
				dataOut.writeUTF(mensaje);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		// TODO Auto-generated method stub
		
	}

	public String availableUsers(){
		String listaUsuarios="";
		for (Client client : usuarios) {
			listaUsuarios += client.getName()+SEPARATOR;
		}
		return listaUsuarios;
	}
	
	public String availableChannels(){
		String listaCanales="";
		for (Channel channel : canales) {
			listaCanales+= channel.getNombre()+SEPARATOR;
		}
		return listaCanales;
	}
	/**
	 * mensaje encviado por un emisor en alguna conversacion(canal)
	 * 
	 * @param mensaje
	 * @param emisor
	 * @param canal
	 */
	public void enviarMensajes(String mensaje,String emisor,String canal){
		Channel channel=getChannel(canal);
		channel.sendMessage(emisor,mensaje);
		
		
		
//			channel.sendMessage(name , mensaje);
		
		
	}
	
	private void deleteEmptyChannels(){
		for (int i = 0; i < canales.size(); i++) {
			if ( canales.get(i).isEmpty() )
				canales.remove(i);
			
		}
		enviarMensajes("availableChannels"+SEPARATOR+availableChannels());

	}
	
	public void deleteUserOnChannel(String emisor, String channelName){
//		int i = 0;
		for (Channel channel : canales) {
				if ( channel.getNombre().equals(channelName) ){
					channel.deleteUser(emisor);
					System.out.println(emisor+" eliminado del canal "+channelName);
				}
//				if ( channel.isEmpty() ){
//					canales.remove(i);
//				}
//				i++;
		}
//		enviarMensajes(mensaje);
		enviarMensajes("userDesconected"+SEPARATOR+emisor+SEPARATOR+channelName, emisor, channelName);
		deleteEmptyChannels();
	}
	
	public void sendUsersConected(String channelName, String emisor){
		String usersList = "";
		for (Channel channel : canales) {
			if ( channel.getNombre().equals(channelName) ){				
				for (String usuario : channel.getUsuarios()) {
					usersList += usuario+SEPARATOR;
				}
			}
			
		}
		enviarMensajes("usersListToChannel"+SEPARATOR+emisor+SEPARATOR+channelName+SEPARATOR+usersList);
		
	}
	
	public Client  getUsuario(String client) {
		for (Client cliente : usuarios) {
			if ( cliente.getName().equals(client) )
				return cliente;
		}
		
		return null;
	}


	private Channel getChannel (String channelName){
		System.out.println("buscando canal");
		for (Channel channel : canales) {
			if ( channel.getNombre().equals(channelName))
				System.out.println("canal encontrado");
				return channel;
		}
		
		return null;
		
	}
	public void setUsuarios(ArrayList<Client> usuarios) {
		this.usuarios = usuarios;
	}


	public ArrayList<Channel> canalesDisponibles() {
		return canales;
	}
	
	public void addCanal(String name,String channelName) {
		Channel channel = new Channel(channelName,this);
		channel.addUser(name);
		canales.add(channel);
		
	}
	
	

	//crea un canal con los dos usuarios recibidos como parametro
	
	public void privateChat(String emisor,String receptor){
		addCanal(emisor, "[priv]"+emisor+"-"+receptor);
		joinChannel("[priv]"+emisor+"-"+receptor, receptor);
		
	}
	
	public void joinChannel(String channelName, String userName){
		for (Channel channel : canales) {
			if ( channel.getNombre().equals(channelName) )
				channel.addUser(userName);
				break;
		}
		
	}
	
	

	
	
	
	
	
	public static void main(String[] args) {
		Server server=new Server(6545);
		server.run();
				
	}


	








}

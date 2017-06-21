package guiClient;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.JTextArea;

import com.sun.corba.se.impl.protocol.giopmsgheaders.Message;


public class ClientThread extends Thread {

	private Socket SocketCliente;
    private DataInputStream entrada;
    private Client cliente;
    
    public ClientThread(Socket sc, Client c){
        SocketCliente=sc;
        cliente=c;
//        GuiClient.interfazCanales();
//        GuiClient.interfazUsuarios();
    }
    
    public void run(){
        String rawMessage= "";
        String action="";
		String name;
		String receptor;
		String emisor;
		String mensaje;
		String[] decoded;
//    	Scanner sc = new Scanner(System.in);
    	entrada = new DataInputStream(cliente.getIs());
    	boolean conectado=true;
        while(conectado){
    		
				try {
					rawMessage = entrada.readUTF();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.out.println("mensaje recibido: "+rawMessage);
			
    		action = rawMessage.substring(0, rawMessage.indexOf(GuiClient.SEPARATOR));
    		decoded = rawMessage.split(GuiClient.SEPARATOR); 
    		System.out.println(action+":    :"+rawMessage);
    		switch (action) {
			case "createdChannel":{
				name = decoded[2];
				emisor = decoded[1];
				cliente.updateChannels(name);
				if ( emisor.equals(cliente.getName()) ) {
					cliente.createChatTab(name);
				}
				System.out.println("sala creadaa mostrar interfaz para canal nuevo"+name);
				
				
			}break;
			
			case "createdPrivateChannel":{
				emisor = decoded[1];
				name = decoded[2];
				cliente.updateChannels(name);
				if ( emisor.equals(cliente.getName()) ) {
					cliente.createChatTab(name);
				}
				
			}break; 
			case "userDesconected" :{
				name = decoded[2];
				emisor=decoded[1];
				System.out.println("emisor: "+emisor);
				System.out.println(cliente.getName()+" canal nombre: "+ name);
				if ( emisor.equals(cliente.getName()) ){
					System.out.println("estoy en el if si el emisor debe eliminar el tab");
					cliente.deleteChannel(name,emisor);
					}
				else cliente.actualizarVista(emisor+" no quiere hablar mas con ustedes ;)", "serverAdmin",name);
				
			}break;
			case "availableChannels":{
				ArrayList<String> channels= new ArrayList<>();
				for (int i = 1; i < decoded.length; i++) {
					channels.add(decoded[i]);
				}
				cliente.updateChannels(channels);
				System.out.println("sala creadaa mostrar interfaz para canal nuevo");
				
			}break;
			
			case "usersListToChannel":{
				emisor =decoded[1];
				name = decoded[2];
				if ( emisor.equals(cliente.getName()) ){
					ArrayList<String> usuarios=new ArrayList<>();
					for (int i = 3; i < decoded.length; i++) {
						usuarios.add(decoded[i]);
					}
					cliente.showConectedUsers(usuarios);
					
				}
				
			}break;
			case "availableUsers":{
				ArrayList<String> users= new ArrayList<>();
				for (int i = 1; i < decoded.length; i++) {
					users.add(decoded[i]);
				}
				cliente.updateUsers(users);
				
			}break;
			case "userJoined": {
				emisor = decoded[1];
				name = decoded [2];
				if ( emisor.equals(cliente.getName()) ) {
					cliente.createChatTab(name);
					
				}
			}break;
			case "userConected":{
				emisor = decoded[1];
				cliente.updateUsers(emisor);
				System.out.println("usuario conectado");
				
				
			}break;
			case "chat":{
				emisor = decoded[1];
				mensaje = decoded[3];
				name = decoded[2];
				cliente.actualizarVista(mensaje, emisor,name);
				System.out.println("chat recibido actualizar interfaz");
			}
				
				break;

			default:
				System.out.println("**** says: "+rawMessage);
				break;
			}
	
        
        }
        
        
        
    }
}


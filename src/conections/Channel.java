package conections;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;

import guiClient.Client;
import guiClient.GuiClient;
import guiServer.Server;


public class Channel {

	//	protected static final String SEPARATOR = "::";
	private String nombre;
	private ArrayList<String> usuarios = new ArrayList<>();
	private Server server;

	public Channel(String channelName, Server sv) {
		this.server=sv;
		this.nombre=channelName;
	}

	public void deleteUser(String userName){
		for (int i = 0; i < usuarios.size(); i++) {
			if ( usuarios.get(i).equals(userName) ){
				usuarios.remove(i);
				break;
			}
		}
	}

	public boolean isEmpty(){
		return usuarios.size()==0;
	}

	public ArrayList<String> getUsuarios() {
		return this.usuarios;
	}

	public String getNombre() {
		// TODO Auto-generated method stub
		return this.nombre;
	}

	public void sendMessage (String nombre, String mensaje){

		Client cliente;
		for (String usuario : usuarios) {
			try {
				cliente=server.getUsuario(usuario);
				DataOutputStream dataOut = new DataOutputStream(cliente.getOs());
				dataOut.writeUTF(mensaje);
				System.out.println(usuarios.size()+"  chaneel mensaje enviado"+mensaje);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}

	public void addUser(String name) {
		// TODO Auto-generated method stub
		usuarios.add(name);

	}



}

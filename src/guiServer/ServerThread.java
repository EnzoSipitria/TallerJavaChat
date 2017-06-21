package guiServer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

import conections.Channel;
import guiClient.GuiClient;

public class ServerThread extends Thread {

	
	private DataInputStream dataIn;
	private String rawMessage;
	private DataOutputStream dataOut;
	private Server server;
	private Socket socketConnection;
	private Channel channel;
	
	public ServerThread(Socket socket, String message, Server server, Channel channel) {
		// TODO Auto-generated constructor stub
		this.socketConnection=socket;
		this.server=server;
		this.rawMessage = message;
		this.channel  = channel;
	}
	

	@Override
	public void run() {
		// TODO Auto-generated method stub
		String action ="";
		String name;
		String receptor;
		String emisor;
		String mensaje;
		String[] decoded;
		boolean conectado=true;
		while (conectado){
			try {
				dataIn = new DataInputStream(socketConnection.getInputStream());
				rawMessage = dataIn.readUTF();
				//action = rawMessage.substring(0, rawMessage.indexOf(Server.SEPARATOR));
				decoded = rawMessage.split(server.SEPARATOR);
				action= decoded[0];
				System.out.println(rawMessage+":     :"+action);
				switch (action) {
				case "createChannel":{
					name=decoded[1];
					emisor=decoded[2];
					server.addCanal(emisor, name);
					server.enviarMensajes("createdChannel"+server.SEPARATOR+emisor+server.SEPARATOR+name);
					System.out.println("canal creado");
				}break;
				case "getUsuarios":{
					emisor = decoded[1];
					name = decoded[2];
					server.sendUsersConected(name, emisor);
				}break;

				case "exitChannel": {
					name = decoded[2];
					emisor = decoded[1];
					server.deleteUserOnChannel(emisor, name);
					
				}break;
				case "joinChannel":{

					name = decoded[1];
					emisor = decoded[2];
					server.joinChannel(name, emisor);
					server.enviarMensajes("userJoined"+server.SEPARATOR+emisor+server.SEPARATOR+name);
					System.out.println("usuario agregado al canal");
					
				}break;
				case "private":{
					emisor = decoded[1];
					receptor = decoded[2];
					
					server.privateChat(emisor, receptor);
					server.enviarMensajes("createdPrivateChannel"+server.SEPARATOR+emisor+server.SEPARATOR+"[priv]"+emisor+"-"+receptor);
					server.enviarMensajes("createdPrivateChannel"+server.SEPARATOR+receptor+server.SEPARATOR+"[priv]"+emisor+"-"+receptor);
					System.out.println("crear chat entre 2 personas"+rawMessage);
				}break;
				case "chat":
				{
					name = decoded[2];
					emisor = decoded[1];
					mensaje = decoded[3];
					
					System.out.println("se recibio un mensaje para alguien");
					server.enviarMensajes("chat"+server.SEPARATOR+emisor+server.SEPARATOR+name+server.SEPARATOR+mensaje,emisor,name);
				};
				break;

				default:
					break;
				}
				
				
			} catch (IOException e) {
				
				// TODO Auto-generated catch block
				conectado=false;
				e.printStackTrace();
				break;
			}
			server.enviarMensajes("availableUsers"+server.SEPARATOR+server.availableUsers());
			server.enviarMensajes("availableChannels"+server.SEPARATOR+server.availableChannels());
		}
		
		/**
		 * remuve usuario de la lista
		 * y loggea que un usuario se a desconectado
		 * 
		 */
		
		
		try {
			
			socketConnection.close();
			dataIn.close();
			dataOut.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
	}



/**
 *  envio de mensajes
 *  
 * toma objectOutputStream = new dataOutput(socketConection.getDataOutputStream)
 * 
 * 
 */
	
	
	
	

}

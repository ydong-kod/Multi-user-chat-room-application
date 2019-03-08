package MultiUserChatRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Server {
	
	public static ArrayList<Socket> Sockets =new ArrayList<Socket>();
	
	public static class relayThread extends Thread {
		
		Socket client;
		ArrayList<Socket> clients;

		public relayThread(Socket client,ArrayList<Socket> clients)throws Exception {
			this.client = client;
			this.clients = clients;
		}
		@Override
		public void run() {
			String relayMessage;
			try {
				BufferedReader inFromSourceClient = new BufferedReader(new InputStreamReader(this.client.getInputStream()));
				while(true){
					//read messages from source client
					if((relayMessage = inFromSourceClient.readLine()) != null) {
						for(Socket socket:clients){
							if(client!=socket){
								//relay messages to all other clients
								PrintWriter outToClient = new PrintWriter(socket.getOutputStream(), true);
								outToClient.println(relayMessage);
							}
						}
					}	
				}
			}catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}
	
	public static void main(String[] args) throws Exception {

		ServerSocket serverSocket = new ServerSocket(9999);
		System.out.println("Waiting For Clients......");
		boolean flag = true;
		int counter = 0;
		while (flag) {
			//Socket established everytime a new client connected
			Socket socket = serverSocket.accept();
			if(counter < 10) {
				//Add new client into client list
				Sockets.add(socket);
				System.out.println("User No." + ++counter + " connected to the server.");
				//Start a new Thread
				new relayThread(socket,Sockets).start();
			}else {
				PrintWriter errorMessage = new PrintWriter(socket.getOutputStream(), true);
				errorMessage.println("Limit reached, please wait...");
				flag = false;
			}
		}
		
	}
}
 

package MultiUserChatRoom;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Date;

public class Client {
	
	public static class sendThread extends Thread {
		private Socket clientSocket;
		//constructor
		sendThread(Socket clientSocket){
			this.clientSocket = clientSocket;
		}
		@Override
		public void run() {
			try {
				BufferedReader inFromUser = new BufferedReader(new InputStreamReader(System.in));
				
				PrintWriter outToServer = new PrintWriter(clientSocket.getOutputStream(),true);
				boolean flag = true;
				while(flag) {
					String userName = sendThread.getLocalHostIP();
					Date d = new Date();
					//receive input from user's keyboard and transfer messages through socket to server
					String str = "[" + userName + "] : " + inFromUser.readLine() + "	|" + d.toString();
					if(str.contains("BYE_BYE")) {
						flag = false;
						System.out.println("***********Logged Out. Hope To See You Again.************");
					}else {
						outToServer.println(str);
					}
				}
			} catch(IOException e){
					e.printStackTrace();
			}
		}
		public static String getLocalHostIP() { 
	          String ip; 
	          try { 
	               InetAddress addr = InetAddress.getLocalHost(); 

	               ip = addr.getHostAddress();  
	          } catch(Exception ex) { 
	              ip = ""; 
	          } 
	           
	          return ip; 
	     } 
	}
	
public static class receiveThread extends Thread {
		private Socket clientSocket;
		//constructor
		receiveThread(Socket clientSocket){
			this.clientSocket = clientSocket;
		}
		@Override
		public void run() {
			try {
				BufferedReader inFromServer = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				//receive messages from server and print on console
				while(true) {
					System.out.println(inFromServer.readLine());
				}
			} catch(IOException e){
					e.printStackTrace();
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		System.out.println("Welcome to the Multi User Chat Room.");
		System.out.println("Connection established.");
		System.out.println("Start chatting rightnow. Type in BYE_BYE to exit. Enjoy! \n \n \n");
		Socket clientSocket = new Socket("localhost", 9999);
		//Threads start.
		new receiveThread(clientSocket).start();
		new sendThread(clientSocket).start();
		
		
	
	}

}

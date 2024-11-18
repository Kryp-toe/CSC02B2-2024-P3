package csc02a2.server.https;

import java.io.IOException;
import java.net.ServerSocket;

public class Server
{
	private ServerSocket serverSocket= null;
	private Boolean running = false;
	
	public Server(int port)
	{
		try
		{
			//create server connection
			serverSocket = new ServerSocket(port);
			running = true;
		} catch (IOException e) {
			running = false;
			e.printStackTrace();
		}
	}
	
	public void startServer()
	{
		System.out.println("Ready for client to connect to server - localhost:" + serverSocket.getLocalPort());
		
		while(running)
		{
			Thread thread = null;
			try
			{
				//create thread of clients (accept the clients)
				thread = new Thread(new ClientHandler(serverSocket.accept()));
				
				//allow clients to request pages
				thread.start();
				
				System.out.println("Server running on port: " + serverSocket.getLocalPort());
			} catch (IOException e)
			{
				e.printStackTrace();
			}
		}
	}
}
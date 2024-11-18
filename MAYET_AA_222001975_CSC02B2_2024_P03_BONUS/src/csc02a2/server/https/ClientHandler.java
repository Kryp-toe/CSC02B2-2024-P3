package csc02a2.server.https;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

public class ClientHandler implements Runnable
{
	private Socket clientSocket;
	
	public ClientHandler(Socket clientSocket)
	{
		//accepted a client from server
		this.clientSocket = clientSocket;
	}

	@Override
	public void run()
	{
		BufferedReader inBufferedReader = null;
		DataOutputStream dataOutputStream = null;
		
		try
		{
			//create input/output streams
			inBufferedReader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
			dataOutputStream = new DataOutputStream(new BufferedOutputStream(clientSocket.getOutputStream()));
			
			//read the users request
			String requestString = inBufferedReader.readLine();
			
			//display the request on console
			System.out.println(requestString);
			
			//separate request by space char
			StringTokenizer stringTokenizer = new StringTokenizer(requestString);
			
			//count number of tokens
			System.out.println("Tokens: " + stringTokenizer.countTokens());
			
			//the type of request from client
			String requestType = stringTokenizer.nextToken();
			
			//the file the client wants
			String fileName = stringTokenizer.nextToken().substring(1);
			
			//location where file is found
			String filePath = "data/"+ fileName;
			
			//determain if it is a jpeg/html file
			switch (fileName)
			{
			case "Joburg":
			case "Durban":
				filePath += ".html";
				break;
			
			case "Cape":
				filePath += "WithImage.html";
				break;
			
			case "Africa":
				filePath += ".jpg";
			
			case "Sample":
				filePath += ".html";
			
			default:
				break;
			}
			
			//create the file
			File file = new File(filePath);
			
			if(file.exists())
			{
				//if the user is requesting the file
				if(requestType.equals("GET"))
				{
					System.out.println("200: Request can be fulfilled");
					
					//the protocol to be written to client (in bytes)
					dataOutputStream.writeBytes("HTTPS/1.1 200 OK \r\n"
											+ "connection: close \r\n"
											+ "Content-Type: text/html\r\n"
											+ "Content-Length:" + file.length() + "\r\n"
											+ "\r\n");
					
					//input stream of the file being read, to be output afterwards
					BufferedInputStream fileBufferedInputStream = new BufferedInputStream(new FileInputStream(file));
					
					//general capability to read a file (1024 bytes is 1 KB)
					byte[] buffer = new byte[2048];
					int n = 0;
					
					//while their are bytes to be read from the file the user requested
					while((n = fileBufferedInputStream.read(buffer))>0)
					{
						//display how many bytes were read
						System.out.println(n);
						
						//write the bytes to the client (give the client the bytes(file))
						dataOutputStream.write(buffer, 0, n);
					}
					
					//close the file input stream
					fileBufferedInputStream.close();
					
					//new line
					dataOutputStream.writeBytes("\r\n");
					
					//"flush" all of the outputs to the client
					dataOutputStream.flush();					
				}
			}else
			{				
				if(fileName.equals("favicon.ico"))
				{
					System.err.println("ERROR 404: favicon FILE NOT FOUND");
				}else {
					System.err.println("ERROR 404: FILE DOES NOT EXIST");
				}
			}
			
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}
}
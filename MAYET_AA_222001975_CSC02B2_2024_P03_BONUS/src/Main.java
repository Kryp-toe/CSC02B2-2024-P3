import csc02a2.server.https.Server;

public class Main
{
	public static void main(String[] args)
	{
		System.out.println("Please go to localhost:4321/Sample for the video");
		Server server = new Server(4321);
		server.startServer();
	}
}
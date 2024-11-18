import csc02a2.server.https.Server;

public class Main
{
	public static void main(String[] args)
	{
		Server server = new Server(4321);
		server.startServer();
	}
}
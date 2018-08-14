public class Main
{
	public static void main(String[] args)
	{
		RelativityClient client = new RelativityClient();
		client.get("https://www.example.com", 5000);
	}
}
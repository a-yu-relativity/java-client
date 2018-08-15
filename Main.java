public class Main
{
    public static void main(String[] args)
    {
        // RelativityClient client = new RelativityClient();
        // client.get("https://www.example.com", 5000);

        int expectedNumOfArgs = 3;
        if (args.length != expectedNumOfArgs)
        {
            System.out.println("Need three arguments: instance URL, username, password");
            return;
        }

        String url = args[0];
        String username = args[1];
        String password = args[2];

        RelativityClient relClient = new RelativityClient(url, username, password);
        int workspaceId = 6578147;
        Samples.basicQuery(relClient, workspaceId);
    }
}
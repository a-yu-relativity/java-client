public class Main
{
    public static void main(String[] args)
    {
        // RelativityClient client = new RelativityClient();
        // client.get("https://www.example.com", 5000);

        int expectedNumOfArgs = 4;
        if (args.length != expectedNumOfArgs)
        {
            System.out.println(
                "Need four arguments: instance URL, username, password, workspace ID");
            return;
        }

        String url = args[0];
        String username = args[1];
        String password = args[2];
        int workspaceId = Integer.parseInt(args[3]);

        RelativityClient relClient = new RelativityClient(url, username, password);
        Samples.basicQuery(relClient, workspaceId);
    }
}
public class Samples
{
    /*
     * Retrieves the first 25 documents from a workspace
     */
    public static void basicQuery(RelativityClient relClient, int workspaceId)
    {
        String url = String.format("/Relativity.REST/Workspace/%d/Document", workspaceId);
        String result = relClient.get(url, 5000);
    }
}
import java.nio.file.*;

public class Samples
{
    /**
     * Basic GET on documents in a workspace
     */    
    public static void basicQuery(RelativityClient relClient, int workspaceId)
    {
        String url = String.format("/Relativity.REST/Workspace/%d/Document", workspaceId);
        String result = relClient.get(url, 5000);
    }


    /**
     * Retrieves the first 25 documents from a workspace
     * @param relClient the Relativity HTTP client
     * @param workspaceId the artifact ID of the workspace we want to query
     */
    public static void testObjMgr(RelativityClient relClient, int workspaceId)
    {
        
        String url = String.format(
            "/Relativity.REST/api/Relativity.Objects/workspace/%d/object/query", 
            workspaceId);
        // in practice, one would use a JSON library for Java,
        // but we shall read the JSON from a file
        String json = "";
        String fileName = "payload.json";
        Path jsonPath = Paths.get(fileName);
        try
        {
            json = new String(Files.readAllBytes(jsonPath));
            // System.out.println(json);
        }
        catch (Exception e)
        {
            System.out.println("Error occured reading json:");
            System.out.println(e.getMessage());
            return;
        }

        // query with object manager
        int timeout= 5000;
        relClient.post(url, json, timeout);
    }


    public static void createDocument(RelativityClient relClient, int workspaceId)
    {
        String url = String.format(
            "/Relativity.REST/Workspace/%d/Document", workspaceId);

        // in practice, one would use a JSON library for Java,
        // but we shall read the JSON from a file
        String json = "";
        String fileName = "upload.json";
        Path jsonPath = Paths.get(fileName);
        try
        {
            String templateJson = new String(Files.readAllBytes(jsonPath));
            json = String.format(templateJson, workspaceId);
        }
        catch (Exception e)
        {
            System.out.println("Error occured reading json:");
            System.out.println(e.getMessage());
            return;
        }

        // query with object manager
        int timeout= 5000;
        relClient.post(url, json, timeout);

    }
}
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
     *
     * @param relClient the Relativity HTTP client
     * @param workspaceId the artifact ID of the workspace we want to query
     * @param payloadFile the path to the text file containing the JSON
     */
    public static void genericObjMgrQuery(
        RelativityClient relClient, int workspaceId, String payloadFile)
    {
        String url = String.format(
            "/Relativity.REST/api/Relativity.Objects/workspace/%d/object/query", 
            workspaceId);
        // in practice, one would use a JSON library for Java,
        // but we shall read the JSON from a file
        String json = "";
        String fileName = payloadFile;
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

    
    /**
     * Query for the unique identifier field (Control Number, Doc ID Beg, etc.)
     * on the Document object
     * @param relClient the Relativity HTTP client
     * @param workspaceId the artifact ID of the workspace we want to query
     */
    public static void queryIdentifier(RelativityClient relClient, int workspaceId)
    {
        genericObjMgrQuery(relClient, workspaceId, "queryIdentifier.json");
    }


    /*
     * Query all fields on the document object
     */
    public static void queryAllFieldsOnDoc(RelativityClient relClient, int workspaceId)
    {
        String jsonFile = "queryAllDocFields.json";
        genericObjMgrQuery(relClient, workspaceId, jsonFile);
    }


    /*
     * Create a field on the Document object
     */
    public static void createField(RelativityClient relClient, int workspaceId)
    {
        String url = String.format("/Relativity.REST/Workspace/%d/Field", workspaceId);

    }
}
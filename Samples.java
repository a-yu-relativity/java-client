import java.nio.file.*;


public class Samples
{
    private static String getJsonFromFile(String jsonFile)
    {
        String json = "";
        Path jsonPath = Paths.get(jsonFile);
        try
        {
            json = new String(Files.readAllBytes(jsonPath));
            // System.out.println(json);
        }
        catch (Exception e)
        {
            System.out.println("Error occured reading json:");
            System.out.println(e.getMessage());
            return "";
        }
        return json;
    }


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
        String json = getJsonFromFile(payloadFile);

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
     * Create a field on the Document object.
     * For documentation on the required fields for each field type,
     * look at https://platform.relativity.com/9.6/Content/RSAPI/DTO_reference/Field/Field_properties.htm#Required
     * Also, the mapping from Field Name to Field Type ID can be found in FieldType.java
     */
    public static void createField(RelativityClient relClient, int workspaceId)
    {
        String url = String.format("/Relativity.REST/Workspace/%d/Field", workspaceId);
        String jsonFile = "createField.json";
        String json = getJsonFromFile(jsonFile);

        // insert Workspace ID into payload
        String formattedJson = String.format(json, workspaceId);
        System.out.println(formattedJson);
        int timeout = 10000;
        relClient.post(url, formattedJson, timeout);
    }
}

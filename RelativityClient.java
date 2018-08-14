import java.util.ArrayList;
import java.util.List;
import java.net.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class RelativityClient
{
    private String instanceUrl;
    private String username;
    private String password;


    public RelativityClient(String instanceUrl, String username, String password)
    {
        this.instanceUrl = cleanUrl(instanceUrl);
        this.username = username;
        this.password = password;   
    }


    public RelativityClient()
    {
        String empty = "";
        this.instanceUrl = empty;
        this.username = empty;
        this.password = empty;
    }


    /*
     * Get rid of any trailing slashes
     */
    private static String cleanUrl(String instanceUrl)
    {
        if (instanceUrl.endsWith("/"))
        {
            // get rid of any trailing slashes
            int len = instanceUrl.length();
            return cleanUrl(instanceUrl.substring(0, len - 1));
        }   
        return instanceUrl;
    }


    /*
     * Checks if the specified string begins with a slash
     */
    private static boolean startsWithSlash(String url)
    {
        return url.startsWith("/");
    }


    public void log(String message)
    {
        System.out.println(message);
    }


    /*
     * HTTP GET
     */
    public String get(String url, int timeout)
    {
        String retVal = "";

        if (!startsWithSlash(url) && !url.isEmpty() && !url.startsWith("http"))
        {
            url = "/" + url;
        }
        URL requestUrl;
        String fullUrl = this.instanceUrl + url;
        try
        {
            requestUrl = new URL(fullUrl);
        }
        catch (MalformedURLException e)
        {
            log("URL is not of a proper format: " + fullUrl);
            return retVal;
        }
        HttpURLConnection conn = null;
        try
        {
            conn = (HttpURLConnection) requestUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
        }
        catch (ProtocolException pe)
        {
            log("Failed to set request method as GET");
        }
        
        catch (IOException ioe)
        {
            log("Error opening connection");
            return retVal;
        }

        int statusCode = 0;

        try
        {
            if (conn == null)
                return retVal;
            statusCode = conn.getResponseCode();
        }
        catch (IOException ioe)
        {
            log("Request failed.");
            return retVal;
        }
        
        if (statusCode == 200)
        {
            BufferedReader in;
            try
            {
                in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            }
            catch (IOException ioe)
            {
                log("Error creating Buffered Reader");
                return retVal;
            }

            String inputLine;
            StringBuffer content = new StringBuffer();
            try
            {
                if (in == null)
                    return retVal;
                while ((inputLine = in.readLine()) != null) 
                {
                    content.append(inputLine);
                }
            }

            catch (IOException ioe)
            {
                log("Failed to read line");
                return retVal;
            }
            
            try
            {
                in.close();
            }
            catch (IOException ioe)
            {
                log("Failed to close stream");
                return retVal;
            }

            log(content.toString());
            retVal = content.toString();
        }
        return retVal;
    }
}
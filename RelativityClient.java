import java.util.ArrayList;
import java.util.List;
import java.net.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;
import java.util.Base64;

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


    /**
     * Does this client use a base URL? 
     */
    private boolean doesUseBaseUrl()
    {
        return !(this.instanceUrl == null || this.instanceUrl.equals(""));
    }


    private String getFullUrl(String url)
    {
        // check to make sure there is a slash 
        // separating the request URL and the path
        // unless we are just feeding in the entire URL
        if (!startsWithSlash(url) && !url.isEmpty() && !url.startsWith("http"))
        {
            url = "/" + url;
        }

        // concatenate with base URL if needed
        String fullUrl = url;
        if (this.doesUseBaseUrl())
        {
            fullUrl = this.instanceUrl + url;
        }

        return fullUrl;
    } 


    private void handleFailedRequest(HttpURLConnection conn)
    {
        if (conn == null)
        {
            log("Error: connection is null.");
            return;
        }

        // print out error message
    }


    private void setRequestHeaders(HttpURLConnection conn)
    {
        conn.setRequestProperty("Content-Type", "application/json;charset=utf-8");
        conn.setRequestProperty("X-CSRF-Header", "-");

        // encode username and password for Basic auth
        String toEncode = String.format("%s:%s", this.username, this.password);
        byte[] b64Encoded = Base64.getEncoder().encode(toEncode.getBytes());
        String authHeader = "Basic " + new String(b64Encoded);

        conn.setRequestProperty("Authorization", authHeader);
    }


    /**
     * Prints out and returns the response content
     * @param conn The connection object that has already sent the request
     * @return the response content
     */
    private String getResponseContent(HttpURLConnection conn)
    {
        String retVal = "";

        BufferedReader in;
        try
        {
            in = new BufferedReader(
                new InputStreamReader(conn.getInputStream()));
        }
        catch (IOException ioe)
        {
            log("Error creating Buffered Reader");
            log(ioe.getMessage());
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
            log(ioe.getMessage());
            return retVal;
        }
        
        try
        {
            in.close();
        }
        catch (IOException ioe)
        {
            log("Failed to close stream");
            log(ioe.getMessage());
            return retVal;
        }

        log(content.toString());
        retVal = content.toString();
        return retVal;
    }


    /**
     * Perform the inital setup for a URL common to
     * all methods
     * @param requestUrl the resource location
     * @param httpMethod which HTTP method we are using
     *                   (GET, POST, PUT, etc.)
     * @param timeout timeout in milliseconds
     * @return the resulting connection
     */
    private HttpURLConnection getInitConnection(
        URL requestUrl, String httpMethod, int timeout)
    {
        HttpURLConnection conn = null;
        try
        {
            conn = (HttpURLConnection) requestUrl.openConnection();
            conn.setRequestMethod(httpMethod);
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            this.setRequestHeaders(conn);
        }
        catch (ProtocolException pe)
        {
            log("Failed to set request method");
            log(pe.getMessage());
        }       
        catch (IOException ioe)
        {
            log("Error opening connection");
            log(ioe.getMessage());
        }
        return conn; 
    }


    /**
     * Overridable for logging
     * @param message the message to print out
     */
    public void log(String message)
    {
        System.out.println(message);
    }


    // -------------------
    // Getters and Setters
    // -------------------

    public String getUrl()
    {
        return this.instanceUrl;
    }

    public void setUrl(String newUrl)
    {
        this.instanceUrl = newUrl;
    }


    public String getUsername()
    {
        return this.username;
    }

    public void setUsername(String newUsername)
    {
        this.username = newUsername;
    }


    public void setPassword(String newPassword)
    {
        this.password = newPassword;
    }


    // ------------
    // HTTP methods
    // ------------

    /**
     * HTTP GET method
     * @param url the path of the resource we want to GET
     * @param timeout timeout in milliseconds for both
     *                connecting and reading
     */
    public String get(String url, int timeout)
    {
        String retVal = "";

        String fullUrl = this.getFullUrl(url);

        // TODO: add any query string params

        URL requestUrl = null;
        try
        {
            requestUrl = new URL(fullUrl);
        }
        catch (MalformedURLException e)
        {
            log("URL is not of a proper format: " + fullUrl);
            return retVal;
        }
        HttpURLConnection conn = getInitConnection(requestUrl, "GET", timeout);
        if (conn == null)
        {
            log("Error: connection is null.");
            return retVal;
        }
            
        int statusCode = 0;
        try
        {
            statusCode = conn.getResponseCode();
        }
        catch (IOException ioe)
        {
            log("Request failed.");
            return retVal;
        }
        
        if (statusCode == HttpURLConnection.HTTP_OK)
        {
            retVal = this.getResponseContent(conn);
        }

        else
        {
            // this means we did not receive a 200
            log("Error: status code " + Integer.toString(statusCode));
        }

        return retVal;
    }


    // public String post(String url, String payload, int timeout)
    // {

    // }
}
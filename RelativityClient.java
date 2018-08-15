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


    /*
     * Does this client use a base URL? 
     */
    private boolean doesUseBaseUrl()
    {
        return (this.instanceUrl == null || this.instanceUrl.equals(""));
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
            fullUrl = this.instanceUrl + url;

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


    /*
     * Overridable for logging
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

    /*
     * HTTP GET
     */
    public String get(String url, int timeout)
    {
        String retVal = "";

        String fullUrl = this.getFullUrl(url);
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
        HttpURLConnection conn = null;
        try
        {
            conn = (HttpURLConnection) requestUrl.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(timeout);
            conn.setReadTimeout(timeout);
            setRequestHeaders(conn);
        }
        catch (ProtocolException pe)
        {
            log("Failed to set request method");
            log(pe.getMessage());
            return retVal;
        }       
        catch (IOException ioe)
        {
            log("Error opening connection");
            log(ioe.getMessage());
            return retVal;
        }

        int statusCode = 0;

        try
        {
            if (conn == null)
            {
                log("Error: connection is null.");
                return retVal;
            }
                
            statusCode = conn.getResponseCode();
        }
        catch (IOException ioe)
        {
            log("Request failed.");
            return retVal;
        }
        
        if (statusCode == HttpURLConnection.HTTP_OK)
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
        }

        else
        {
            // this means we did not receive a 200
            log("Error: status code " + Integer.toString(statusCode));
        }

        return retVal;
    }


    // public String post()
}
package CoinpaymentsClient;

import com.google.gson.*;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 @author Hosfad
  **/
public class CoinPaymentsAPI
{
    private String				private_key;
    private String				public_key;
    private Map<String, String>	params;
    private static Logger		logger	= LoggerFactory.getLogger(CoinPaymentsAPI.class);

    /**
     * ctor
     *
     * @param public_key from the api
     * @param private_key from the api
     */
    public CoinPaymentsAPI(String public_key, String private_key)
    {
        this.private_key = private_key;
        this.public_key = public_key;
        this.params = new HashMap<String, String>();
    }

    /**
     * Sets a specific key to a specific value
     * @param key key that should be set
     * @param value value for this key. If the value is null then the key is deleted
     * @return
     */
    public CoinPaymentsAPI set(String key, Object value)
    {

        if (key == null && value == null) {
            this.params.clear();
        }
        else
        if (value == null) {
            this.params.remove(key);
        }
        else {
            this.params.put(key, value.toString());
        }
        return this;
    }

    /**
     * Encodes a single String to UTF8
     * @return
     */
    private String urlEncodeUTF8(String s)
    {
        try {
            return URLEncoder.encode(s, "UTF-8");
        }
        catch (UnsupportedEncodingException e) {
            throw new UnsupportedOperationException(e);
        }
    }

    /**
     * Returns a URL from Key-Value sets
     * @param map
     */
    private String urlEncodeUTF8(Map<?, ?> map)
    {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<?, ?> entry : map.entrySet()) {
            if (sb.length() > 0) {
                sb.append("&");
            }
            sb.append(String.format("%s=%s", urlEncodeUTF8(entry.getKey().toString()),
                    urlEncodeUTF8(entry.getValue().toString())));
        }
        return sb.toString();
    }

    /**
     * Calls the API
     * @param cmd
     */
    public JsonObject call(String cmd)
    {

        // Copy the current map to
        Map<String, String> req = new HashMap<>();
        req.putAll(params);
        params.clear();

        // Set the API command and required fields
        req.put("version", "1");
        req.put("cmd", cmd);
        req.put("key", this.public_key);
        req.put("format", "json");

        try {
            // Generate the query string
            String post_data = urlEncodeUTF8(req);

            // Calculate the HMAC
            HmacUtils utils = new HmacUtils(HmacAlgorithms.HMAC_SHA_512,this.private_key);
            String hmac = utils.hmacHex(post_data);
            URL obj = new URL("https://www.coinpayments.net/api.php");
            HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

            // Set the request headers
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0");
            con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");
            con.setRequestProperty("HMAC", hmac);

            // Send post request
            con.setDoOutput(true);
            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(post_data);
            wr.flush();
            wr.close();

            // Wait for the response code
            int responseCode = con.getResponseCode();
            logger.debug("Sending 'POST' request to URL : https://www.coinpayments.net/api.php");
            logger.debug("Post parameters : " + post_data);
            logger.debug("Response Code : " + responseCode);

            // Read the full response
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Parse the JSON response with GSON

            JsonElement jsonTree = JsonParser.parseString(response.toString());
            JsonObject jsonObject = jsonTree.getAsJsonObject();

            // If there is an error - throw an Exception
            if (jsonObject.get("error").getAsString().equals("ok") == false) {
                throw new IllegalStateException(jsonObject.get("error").getAsString());
            }

            // Otherwise return the result object
            Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            String prettyJson = gson.toJson(jsonObject);
            logger.debug(prettyJson);

            return jsonObject.getAsJsonObject("result");
        }
        catch (Exception e) {
            logger.error("Exception occured: " + e.getMessage());
            throw new CoinpaymentsApiCallException(e.getMessage());
        }
    }

    /**
     * Exception which is thrown if the API call fails
     */
    public class CoinpaymentsApiCallException extends RuntimeException
    {

        private static final long serialVersionUID = -703701416098191297L;

        public CoinpaymentsApiCallException(String message)
        {
            super(message);
        }

    }


}
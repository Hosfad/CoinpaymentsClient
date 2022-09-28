package Hosfad.CoinpaymentsClient;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.codec.digest.HmacAlgorithms;
import org.apache.commons.codec.digest.HmacUtils;

import javax.net.ssl.HttpsURLConnection;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Hosfad
 **/
public class CoinPaymentsAPI
{
    private static final String API_URL = "https://www.coinpayments.net/api.php";

    private final String public_key, private_key;
    private final Map<String, String> params;

    /**
     * @param public_key  from the api
     * @param private_key from the api
     */
    public CoinPaymentsAPI(String public_key, String private_key)
    {
        this.public_key = public_key;
        this.private_key = private_key;
        this.params = new HashMap<>();
    }

    /**
     * Sets request params
     */
    public CoinPaymentsAPI set(String key, Object value)
    {
        if (value == null)
        {
            if (key == null) this.params.clear();
            else this.params.remove(key);
        }
        else
            this.params.put(key, value.toString());

        return this;
    }

    /**
     * Encode Url
     */
    private String urlEncodeUTF8(Object s)
    {
        return URLEncoder.encode(s.toString(), StandardCharsets.UTF_8);
    }

    /**
     * Encode query URL
     */
    private String urlEncodeUTF8(Map<String, String> map)
    {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : map.entrySet())
        {
            String format = sb.length() > 0 ? "%s=%s" : "&%s=%s";
            sb.append(String.format(format, urlEncodeUTF8(entry.getKey()), urlEncodeUTF8(entry.getValue())));
        }
        return sb.toString();
    }

    /**
     * Calls the API
     */
    public JsonObject call(String cmd)
    {
        // Put all params
        Map<String, String> req = new HashMap<>(params);
        params.clear();

        // Set command and required fields
        String[] params = { "version", "1", "cmd", cmd, "key", this.public_key, "format", "json" };
        for (int i = 0; i < params.length - 1; i++) req.put(params[i], params[i + 1]);

        try
        {
            // Generate query string
            String post_data = urlEncodeUTF8(req);

            // Calculate the HMAC
            String hmac = new HmacUtils(HmacAlgorithms.HMAC_SHA_512, this.private_key).hmacHex(post_data);
            HttpsURLConnection con = (HttpsURLConnection) new URL(API_URL).openConnection();

            // Set the request headers and post request
            con.setRequestMethod("POST");
            con.setRequestProperty("User-Agent", "Mozilla/5.0"); con.setRequestProperty("Accept-Language", "en-US,en;q=0.5"); con.setRequestProperty("HMAC", hmac);
            con.setDoOutput(true);

            try (DataOutputStream wr = new DataOutputStream(con.getOutputStream())) { wr.writeBytes(post_data); }

            // Read response
            StringBuilder response;

            try (BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream())))
            {
                response = new StringBuilder();
                String inputLine;
                while ((inputLine = in.readLine()) != null) response.append(inputLine);
            }

            JsonObject jsonObject = (JsonObject) JsonParser.parseString(response.toString());

            // If API returns a bad request
            String error = jsonObject.get("error").getAsString();
            if (!error.equals("ok")) throw new IllegalStateException(error);

            return jsonObject.getAsJsonObject("result");
        }
        catch (Exception e)
        {
            throw new RuntimeException(e.getMessage());
        }
    }

    private interface PrintJson
    {
        void print(JsonObject json, String field);
    }

    /**
     * Example Code
     */
    public static void main(String[] args)
    {
        if (args.length != 2)
        {
            System.out.println("Usage: <PUBLIC_KEY> <PRIVATE_KEY>");
            return;
        }

        CoinPaymentsAPI api = new CoinPaymentsAPI(args[0], args[1]);

        PrintJson pj = (json, field) -> System.out.println(json.get(field).getAsString());

        // Getting basic account information
        JsonObject accountInfo = api.call("get_basic_info");
        pj.print(accountInfo, "username");
        pj.print(accountInfo, "email");
        // ...

        // Getting rates
        JsonObject ratesInfo = api.call("rates");
        pj.print(ratesInfo, "is_fiat");
        pj.print(ratesInfo, "rate_btc");
        // ...

        // Creating a transaction
        // Amount in dollars
        JsonObject transactionInfo = api
                .set("amount", 10)
                .set("currency1", "USD")
                .set("currency2", "BTC")
                .set("buyer_name", "John doe")
                .set("buyer_email", "Reeeeeeeeeee@dev.com")
                .call("create_transaction");
        pj.print(transactionInfo, "txn_id");
        pj.print(transactionInfo, "status_url");
        pj.print(transactionInfo, "qrcode_url");
        // ...
    }
}

package CoinpaymentsClient;

import CoinpaymentsClient.Responses.AccountInformationResponse;
import CoinpaymentsClient.Responses.ExchangeRatesResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class CoinpaymentsClient {
   public static Gson gson = new GsonBuilder().setPrettyPrinting().create();

    /**
     * ctor
     *
     * @param publicAPIKey from the api
     * @param privateAPIKey from the api
     */
    public String publicAPIKey;
    public String privateAPIKey;

    CoinPaymentsAPI api ;
    public CoinpaymentsClient(String publicAPIKey, String privateAPIKey) {
        this.publicAPIKey = publicAPIKey;
        this.privateAPIKey = privateAPIKey;
        this.api = new CoinPaymentsAPI(publicAPIKey, privateAPIKey);
    }

    public String getCallBackAddress(String currency){
        JsonObject callbackAddress = api.set("currency",currency).call("get_callback_address");
        return callbackAddress.get("address").getAsString();
    }

    public AccountInformationResponse getBasicAccountInformation(){

        JsonObject accountInfo = api.call("get_basic_info");

        AccountInformationResponse response = new AccountInformationResponse();
        response.username = accountInfo.get("username").getAsString();
        response.merchant_id = accountInfo.get("merchant_id").getAsString();
        response.email = accountInfo.get("email").getAsString();
        response.public_name = accountInfo.get("public_name").getAsString();
        response.time_joined = accountInfo.get("time_joined").getAsLong();
        response.kyc_status = accountInfo.get("time_joined").getAsBoolean();
        response.kyc_volume_limit = accountInfo.get("kyc_volume_limit").getAsLong();
        response.kyc_volume_used = accountInfo.get("kyc_volume_used").getAsLong();
        response.swych_tos_accepted = accountInfo.get("swych_tos_accepted").getAsBoolean();
        return response;
    }

    public ExchangeRatesResponse getExchangeRates(){
        ExchangeRatesResponse response = new ExchangeRatesResponse();
        JsonObject accountInfo = api.call("rates");
        response.result = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> acceptedCoins = accountInfo.entrySet();
        for (Map.Entry<String, JsonElement> coin : acceptedCoins){
            ExchangeRatesResponse.Result result = new ExchangeRatesResponse.Result();

            result.is_fiat = coin.getValue().getAsJsonObject().get("is_fiat").getAsInt();
            result.rate_btc = coin.getValue().getAsJsonObject().get("rate_btc").getAsFloat();
            result.last_update = coin.getValue().getAsJsonObject().get("last_update").getAsLong();
            result.tx_fee = coin.getValue().getAsJsonObject().get("tx_fee").getAsFloat();
            result.status = coin.getValue().getAsJsonObject().get("status").getAsString();
            result.image = coin.getValue().getAsJsonObject().get("image").getAsString();
            result.name = coin.getValue().getAsJsonObject().get("name").getAsString();
            result.confirms = coin.getValue().getAsJsonObject().get("confirms").getAsInt();
            result.can_convert = coin.getValue().getAsJsonObject().get("can_convert").getAsInt();
            response.result.put(coin.getKey() ,result );
        }

        return response;
    }

    public ExchangeRatesResponse getExchangeRatesShort(){
        ExchangeRatesResponse response = new ExchangeRatesResponse();
        JsonObject accountInfo = api.set("short",1).call("rates");
        response.result = new HashMap<>();
        Set<Map.Entry<String, JsonElement>> acceptedCoins = accountInfo.entrySet();
        for (Map.Entry<String, JsonElement> coin : acceptedCoins){
            ExchangeRatesResponse.Result result = new ExchangeRatesResponse.Result();

            result.is_fiat = coin.getValue().getAsJsonObject().get("is_fiat").getAsInt();
            result.rate_btc = coin.getValue().getAsJsonObject().get("rate_btc").getAsFloat();
            result.last_update = coin.getValue().getAsJsonObject().get("last_update").getAsLong();
            result.tx_fee = coin.getValue().getAsJsonObject().get("tx_fee").getAsFloat();
            result.status = coin.getValue().getAsJsonObject().get("status").getAsString();
            result.image = coin.getValue().getAsJsonObject().get("image").getAsString();
            result.name = coin.getValue().getAsJsonObject().get("name").getAsString();
            result.confirms = coin.getValue().getAsJsonObject().get("confirms").getAsInt();
            result.can_convert = coin.getValue().getAsJsonObject().get("can_convert").getAsInt();
            response.result.put(coin.getKey() ,result );
        }

        return response;
    }




    public static void main(String[] args) {
        CoinpaymentsClient client = new CoinpaymentsClient("b00925c33907b245e68a0757425ab8c63016ac7f63b44fb9e0ac90b2693a0dcb" ,"0E18fBA9e23ff0f709bD7446c0B98eD3CD4864F3b21cF92e09dEce40d15065Ed");
     client.getCallBackAddress("BTC");

    }

}

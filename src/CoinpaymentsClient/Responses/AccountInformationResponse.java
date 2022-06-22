package CoinpaymentsClient.Responses;


import CoinpaymentsClient.CoinpaymentsClient;

public class AccountInformationResponse {
    public String username;
    public String merchant_id;
    public String email;
    public String public_name;

    public long time_joined;
    public boolean kyc_status;
    public long kyc_volume_limit;
    public long kyc_volume_used;
    public boolean swych_tos_accepted;

 @Override
    public String toString(){
     String jsonString = CoinpaymentsClient.gson.toJson(this);

     return jsonString;
 }
}

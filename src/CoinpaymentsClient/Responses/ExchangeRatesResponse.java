package CoinpaymentsClient.Responses;

import CoinpaymentsClient.CoinpaymentsClient;

import java.util.Map;

public class ExchangeRatesResponse {
    public Map<String, Result> result;

    public static class Result {
        public int is_fiat;
        public float rate_btc;
        public long last_update;
        public float tx_fee;

        public String status;
        public String image;
        public String name;
        public int confirms;
        public int can_convert;
        public String[] capabilities;
        public String explorer;

        @Override
        public String toString() {
            String jsonString = CoinpaymentsClient.gson.toJson(this);

            return jsonString;
        }
    }

    @Override
    public String toString() {
        String jsonString = CoinpaymentsClient.gson.toJson(this);

        return jsonString;
    }

}
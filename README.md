# Coinpayments Client
Coinpayments java wrapper for easy interactions with the Coinpayments gateway 



**REQUIREMENTS** :

GSON 

Apache commons-codec-1.15



Usage : 

```
    static CoinPaymentsAPI api = new CoinPaymentsAPI("PUBLIC_KEY", "PRIVATE_KEY");

    public static void main(String[] args) {
        // Getting basic account information
        JsonObject accountInfo = api.call("get_basic_info");
        System.out.println(accountInfo.get("username").getAsString());
        System.out.println(accountInfo.get("email").getAsString());
        // ...

        // Getting rates
        JsonObject ratesInfo = api.call("rates");
        System.out.println(ratesInfo.get("is_fiat").getAsString());
        System.out.println(ratesInfo.get("rate_btc").getAsString());
        // ...


        // Creating a transaction
                                      // ammount in dollar
        JsonObject transactionInfo = api.set("amount", 10)
                .set("currency1", "USD")
                .set("currency2", "BTC")
                .set("buyer_name", "John doe")
                .set("buyer_email", "Reeeeeeeeeee@dev.com")
                .call("create_transaction");
        System.out.println(transactionInfo.get("txn_id").getAsString());
        System.out.println(transactionInfo.get("status_url").getAsString());
        System.out.println(transactionInfo.get("qrcode_url").getAsString());
        // ...

    }
    ```

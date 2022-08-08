# Coinpayments Client
Coinpayments java wrapper for easy interactions with the Coinpayments gateway 

Gonna be adding more features as i get the time to do so , Will eventually have support for all Coinpayments requests


**REQUIREMENTS**
```
GSON 
Apache commons-codec-1.15```



Usage : 

Initialize client :
```java
     CoinpaymentsClient client = new CoinpaymentsClient("PUBLIC_API_KEY" ,"PRIVATE_API_KEY");
```

Get basic account information :
```java
     AccountInformationResponse accountInfo = client.getBasicAccountInformation();
        System.out.println(accountInfo.username);
        System.out.println(accountInfo.email);
        System.out.println(accountInfo.merchant_id);
        System.out.println(accountInfo.public_name);
```
Get exchange rates and supported coins : 
```java
     ExchangeRatesResponse exchangeRates = client.getExchangeRates();
     ExchangeRatesResponse.Result result = exchangeRates.result.get("BTC");
        System.out.println(result.is_fiat);
        System.out.println(result.name);
        System.out.println(result.rate_btc);
        System.out.println(result.image);
        System.out.println(result.confirms);
```
Get callback address :
```java
        String address = client.getCallBackAddress("BTC");
        System.out.println(address);
```

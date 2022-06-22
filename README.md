# Coinpayments Client
Coinpayments java wrapper for easy interactions with the Coinpayments gateway 

Gonna be adding more features as i get the time to do so , Will eventually have support for all Coinpayments requests

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

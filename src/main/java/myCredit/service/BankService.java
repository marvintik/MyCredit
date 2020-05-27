package myCredit.service;

import com.google.gson.Gson;
import lombok.SneakyThrows;
import myCredit.domain.BankAccount;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class BankService {

    @SneakyThrows
    public BankAccount.Example getClientMono(String token) {

        var gson = new Gson();

        var httpClient = HttpClient.newBuilder()
                .build();

        var request = HttpRequest.newBuilder()
                .GET()
                .header("X-Token", token)
                .uri(URI.create("https://api.monobank.ua/personal/client-info"))
                .build();

        var response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        var example = gson.fromJson(response.body(), BankAccount.Example.class);
        return getMyExample(example);
    }

    private BankAccount.Example getMyExample(BankAccount.Example example) {
        for (BankAccount.Account account:example.getAccounts()
             ) {
            account.setBalance(account.getBalance()/100);
            account.setCreditLimit(account.getCreditLimit()/100);
            switch (account.getCurrencyCode()){
               case 978: account.setCurrencyName("EUR"); break;
                case 980: account.setCurrencyName("UAH"); break;
                case 840: account.setCurrencyName("USD"); break;
            }
        }
        return example;
    }
}


package myCredit.domain;

import lombok.Data;

import java.util.List;

@Data
public class BankAccount {

    @Data
    public class Account {
        private double balance;
        private double creditLimit;
        private Integer currencyCode;
        private String currencyName;
    }

    @Data
    public class Example {
        private String name;
        private List<Account> accounts = null;
    }
}

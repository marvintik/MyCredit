package myCredit.utils;

import myCredit.domain.Credit;
import myCredit.domain.Person;
import myCredit.domain.User;

import java.util.ArrayList;
import java.util.List;

public class PrintCredit {

    public static List<Credit> printCredits(User user) {
        List<Person> persons = user.getPersons();
        List<Credit> credits = new ArrayList();
        for (Person person : persons) {
            credits.addAll(person.getCredits());
        }
        double sumCost = 0;
        double sumMonthPay = 0;
        for (Credit credit : credits) {
            if (credit.getCost() != null) {
                sumCost += credit.getCost();
            }
            if (credit.getMonthPay() != null) {
                sumMonthPay += credit.getMonthPay();
            }
        }
        return credits;
    }

    public static double printSum(List<Credit> credits) {
        double sumCost = 0;
        for (Credit credit : credits) {
            if (credit.getCost() != null) {
                sumCost += credit.getCost();
            }
        }
        return sumCost;
    }

    public static double printMonthPay(List<Credit> credits) {
        double sumMonthPay = 0;
        for (Credit credit : credits) {
            if (credit.getMonthPay() != null) {
                sumMonthPay += credit.getMonthPay();
            }
        }
        return sumMonthPay;
    }
}

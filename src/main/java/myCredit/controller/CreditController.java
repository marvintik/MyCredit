package myCredit.controller;

import lombok.SneakyThrows;
import myCredit.domain.Credit;
import myCredit.domain.Person;
import myCredit.domain.User;
import myCredit.service.CreditService;
import myCredit.service.PersonService;
import myCredit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("")
public class CreditController {

    @Autowired
    CreditService creditService;

    @Autowired
    PersonService personService;
    @Autowired
    UserService userService;

    @RequestMapping(value ="/mycredit/credit/{id}", method = RequestMethod.GET)
    public Credit getCredit(@PathVariable Integer id) {
        return creditService.getCredit(id);
    }


    @GetMapping(value = "/mycredit/person/{id}/credit/add")
    public String createCreditByPersonForm (@PathVariable Integer id, Model model) {
        Credit credit = new Credit();
        Person person =personService.getPerson(id);
        model.addAttribute("credit", credit);
        model.addAttribute("person", person);
        return "/newCredit";
    }

    @PostMapping(value = "/mycredit/person/{id}/credit/add")
    public String createCreditByPersonForm(@ModelAttribute Credit credit, @PathVariable Integer id, Model model){
        Person person = personService.getPerson(id);
        credit.setPerson(person);
        creditService.createCredit(credit);
        String str = "redirect:/mycredit/person/" +id;
        return str;
    }

    @GetMapping(value = "/mycredit/person/{id}/credit/{creditId}/edit")
    public String editCreditByPersonForm (@PathVariable Integer id, @PathVariable Integer creditId,Model model) {
        Credit credit = creditService.getCredit(creditId);
        Person person = credit.getPerson();
        model.addAttribute("credit", credit);
        model.addAttribute("person", person);
        return "/editCredit";
    }

    @PostMapping(value = "/mycredit/person/{id}/credit/{creditId}/edit")
    public String editCreditByPersonForm(@ModelAttribute Credit credit, @PathVariable Integer id,  @PathVariable Integer creditId, Model model){
        credit.setId(creditId);
        credit.setPerson(personService.getPerson(id));
        creditService.saveCredit(credit);
        String str = "redirect:/mycredit/person/" +id;
        return str;
    }

    @GetMapping(value = "/mycredit/person/{id}/credit/{creditId}/delete")
    public String deleteCreditByPerson(@PathVariable Integer id, @PathVariable Integer creditId) {
        creditService.deleteCredit(creditId);
        String str  = "redirect:/mycredit/person/" +id;
        return str;
    }

    @GetMapping(value = "/mycredit/credit/{id}/delete")
    public String deleteCredit(@PathVariable Integer id){ creditService.deleteCredit(id);
    return "redirect:/mycredit/credit/all";
    }

    @ResponseBody
    @PostMapping(value = "/mycredit/credit/{id}/edit")
    public Credit editCredit(@RequestBody Credit credit, @PathVariable Integer id) {
        creditService.saveCredit(credit);
        Integer i = credit.getId();
      Credit editCredit = creditService.getCredit(i);
    return editCredit;}

    @GetMapping(value = "/mycredit/credit/search/bank/{bank}")
    public String searchCreditByBank(@PathVariable String bank, Model model){
        List<Credit> credits = creditService.listCreditBank(bank);
        double sumCost = 0;
        double sumMonthPay = 0;
        for (Credit credit:credits) {
            if (credit.getCost() != null) {sumCost += credit.getCost();}
            if (credit.getMonthPay() != null) {sumMonthPay += credit.getMonthPay();}
        }
        model.addAttribute("bank", bank);
        model.addAttribute("sumCost", sumCost);
        model.addAttribute("sumMonthPay", sumMonthPay);
        model.addAttribute("credits", credits);
        return "/credit";
    }


    @GetMapping(value = "/mycredit/credit/search/title/{title}")
    public String searchCreditByTitle(@PathVariable String title, Model model){
        List<Credit> credits =   creditService.listCreditTitle(title);
        double sumCost = 0;
        double sumMonthPay = 0;
        for (Credit credit:credits) {
            if (credit.getCost() != null) {sumCost += credit.getCost();}
            if (credit.getMonthPay() != null) {sumMonthPay += credit.getMonthPay();}
        }
        String bank = title;
        model.addAttribute("bank", bank);
        model.addAttribute("sumCost", sumCost);
        model.addAttribute("sumMonthPay", sumMonthPay);
        model.addAttribute("credits", credits);
        return "/credit";
    }


    @GetMapping(value = "/mycredit/credit/search/datestart/{dateStart}")
    public List<Credit> searchCreditByDateStart(@PathVariable LocalDate dateStart){
        return creditService.listCreditByDateStart(dateStart);
    }


    @GetMapping(value = "/mycredit/credit/search/dateend/{dateEnd}")
    public List<Credit> searchCreditByDateEnd(@PathVariable LocalDate dateEnd){
        return creditService.listCreditByDateEnd(dateEnd);
    }

    @GetMapping(value="/mycredit/credit/all")
    public String home(Model model) {
        List<Credit> credits = creditService.listAll();
        double sumCost = 0;
        double sumMonthPay = 0;
        for (Credit credit:credits) {
            if (credit.getCost() != null) {sumCost += credit.getCost();}
            if (credit.getMonthPay() != null) {sumMonthPay += credit.getMonthPay();}
        }
        var bank = "Все банки";
        model.addAttribute("bank", bank);
        model.addAttribute("sumCost", sumCost);
        model.addAttribute("sumMonthPay", sumMonthPay);
        model.addAttribute("credits", credits);
        return "/credit";
    }

    @RequestMapping(value = "/mycredit/credits/search", method = RequestMethod.GET)
    public ModelAndView filter() {
        ModelAndView mav = new ModelAndView("search"/*, "command", new Filter()*/);
        //System.out.println(filter());
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/mycredit/credits",  method = RequestMethod.GET)
    public List<Credit> getCreditList() {
        return creditService.listAllCredits();
    }

    @PostMapping("/mycredit/credits/save")
    public void saveCredit(@RequestBody Credit credit) {
        creditService.saveCredit(credit);
    }

    @GetMapping(value="/user/{id}/credit/all")
    public String allCreditsByUser(Model model, @PathVariable Integer id) {
       //List<Credit> credits = creditService.listAll();
        User user = userService.getUser(id);
        List<Person> persons = user.getPersons();
        List<Credit> credits = new ArrayList();
        for(Person person:persons){
            credits.addAll(person.getCredits());
        }
        double sumCost = 0;
        double sumMonthPay = 0;
        for (Credit credit:credits) {
            if (credit.getCost() != null) {sumCost += credit.getCost();}
            if (credit.getMonthPay() != null) {sumMonthPay += credit.getMonthPay();}
        }
        var bank = "Все банки";
        model.addAttribute("bank", bank);
        model.addAttribute("sumCost", sumCost);
        model.addAttribute("sumMonthPay", sumMonthPay);
        model.addAttribute("credits", credits);
        return "/credit";
    }
}

package myCredit.controller;

import myCredit.domain.BankAccount;
import myCredit.domain.Credit;
import myCredit.domain.Person;
import myCredit.domain.User;
import myCredit.service.BankService;
import myCredit.service.PersonService;
import myCredit.service.UserService;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.Comparator;
import java.util.List;

@Controller
@RequestMapping("")
public class PersonController {

    @Autowired
    PersonService personService;

    @Autowired
    BankService bankService;

    @Autowired
    UserService userService;

    /*VieWController*/
    /*Person*/

    @GetMapping("/mycredit/person/add")
    public String newPersonForm(Model model) {
        Person person = new Person();
        String personAdd = "mycredit/person";
        model.addAttribute("personAdd", personAdd);
        model.addAttribute("person", person);
        return "/newPerson";
    }

    @PostMapping("/mycredit/person/add")
    public String newPersonAdd(@ModelAttribute Person person, Model model){
        if (person.getImage()=="" || person.getImage()==null){
            person.setImage("https://goo.su/16eh");
        }
        personService.createPerson(person);
        return "redirect:/mycredit/person/all";
    }

    @GetMapping(value = "/mycredit/person/{id}/edit")
    public String editPersonForm (@PathVariable Integer id,Model model) {
        Person person = personService.getPerson(id);
        model.addAttribute("person", person);
        return "/editPerson";
    }

    @PostMapping(value = "/mycredit/person/{id}/edit")
    public String editPersonForm(@ModelAttribute Person person, @PathVariable Integer id, Model model){
        person.setId(id);
        personService.savePerson(person);
        return "redirect:/mycredit/person/all";
    }

    @GetMapping(value = "/mycredit/person/{id}/delete")
    public String deleteCreditByPerson(@PathVariable Integer id) {personService.deletePerson(id);
        return "redirect:/mycredit/person/all";}

    @GetMapping(value="/mycredit/person/all")
    public String home(Model model) {
        List<Person> persons = personService.listAll();
        String personAdd = "mycredit/person";
        model.addAttribute("persons", persons);
        model.addAttribute("personAdd", personAdd);
        return "/person";
    }

    @RequestMapping(value = "/mycredit/person/persons/searchView", method = RequestMethod.GET)
    public String filter() {
        return "";
    }

    @GetMapping(value = "/mycredit/person/{id}/bank/mono")
    public String getAccountMono(@PathVariable Integer id,Model model) {
        var person = personService.getPerson(id);
        var token = person.getTokenMono();
        if (token != null) {
        BankAccount.Example example = bankService.getClientMono(token);
        model.addAttribute("example", example);
        return "/personIdMono";}
        else {
            return "redirect:/mycredit/person/"+id;
        }
    }

    @GetMapping("/mycredit/person/{id}")
    public String getPerson(@PathVariable Integer id, Model model) {
        Person person = personService.getPerson(id);
        List<Credit> credits = person.getCredits();
        double sumCost = 0;
        double sumMonthPay = 0;
        for (Credit credit:credits) {
            if (credit.getCost() != null) {sumCost += credit.getCost();}
            if (credit.getMonthPay() != null) {sumMonthPay += credit.getMonthPay();}
        }
        model.addAttribute("person", person);
        model.addAttribute("sumCost", sumCost);
        model.addAttribute("sumMonthPay", sumMonthPay);
        return "/personId";
    }

    /*User*/

    @GetMapping("/user/{id}/person/all")
    public String getPerson(Model model, @PathVariable Integer id) {
        User user = userService.getUser(id);
        List<Person> persons = user.getPersons();
        String personAdd = "user/" + id + "/person";
        model.addAttribute("persons", persons);
        model.addAttribute("personAdd", personAdd);
        return "person";
    }

    @GetMapping("/user/{id}/person/add")
    public String newPersonForm(Model model, @PathVariable Integer id) {
        Person person = new Person();
        String personAdd = "user/" + id + "/person";
        model.addAttribute("personAdd", personAdd);
        model.addAttribute("person", person);
        return "newPerson";
    }

    @PostMapping("/user/{id}/person/add")
    public String newPersonAdd(@ModelAttribute Person person, Model model, @PathVariable Integer id) {
        if (person.getImage() == "" || person.getImage() == null) {
            person.setImage("https://goo.su/16eh");
        }
        person.setUser(userService.getUser(id));
        personService.createPerson(person);
        String redirect = "redirect:/user/" + id + "/person/all";
        return redirect;
    }

}
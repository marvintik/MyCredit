package myCredit.controller;

import myCredit.domain.Credit;
import myCredit.domain.Person;
import myCredit.service.CreditService;
import myCredit.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequestMapping("/mycredit/")
public class CreditController {

    @Autowired
    CreditService creditService;

    @Autowired
    PersonService personService;

    @RequestMapping(value ="/credit/{id}", method = RequestMethod.GET)
    public Credit getCredit(@PathVariable Integer id) {
        return creditService.getCredit(id);
    }


    @GetMapping(value = "/person/{id}/credit/add")
    public String createCreditByPersonForm (@PathVariable Integer id, Model model) {
        Credit credit = new Credit();
        Person person =personService.getPerson(id);
        model.addAttribute("credit", credit);
        model.addAttribute("person", person);
        return "/newCredit";
    }

    @PostMapping(value = "/person/{id}/credit/add")
    public String createCreditByPersonForm(@ModelAttribute Credit credit, @PathVariable Integer id, Model model){
        Person person = personService.getPerson(id);
        credit.setPerson(person);
        creditService.createCredit(credit);
        String str = "redirect:/mycredit/person/" +id;
        return str;
    }

    @GetMapping(value = "/person/{id}/credit/{creditId}/edit")
    public String editCreditByPersonForm (@PathVariable Integer id, @PathVariable Integer creditId,Model model) {
        Credit credit = creditService.getCredit(creditId);
        Person person = credit.getPerson();
        model.addAttribute("credit", credit);
        model.addAttribute("person", person);
        return "/editCredit";
    }

    @PostMapping(value = "/person/{id}/credit/{creditId}/edit")
    public String editCreditByPersonForm(@ModelAttribute Credit credit, @PathVariable Integer id,  @PathVariable Integer creditId, Model model){
        credit.setId(creditId);
        credit.setPerson(personService.getPerson(id));
        creditService.saveCredit(credit);
        String str = "redirect:/mycredit/person/" +id;
        return str;
    }

    @GetMapping(value = "/person/{id}/credit/{creditId}/delete")
    public String deleteCreditByPerson(@PathVariable Integer id, @PathVariable Integer creditId) {creditService.deleteCredit(creditId);
            String str = "redirect:/mycredit/person/" +id;
            return str;
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/credit/{id}/delete")
    public void deleteCredit(@PathVariable Integer id){ creditService.deleteCredit(id); }

    @ResponseBody
    @PostMapping(value = "/credit/{id}/edit")
    public Credit editCredit(@RequestBody Credit credit, @PathVariable Integer id) {
        creditService.saveCredit(credit);
        Integer i = credit.getId();
      Credit editCredit = creditService.getCredit(i);
    return editCredit;}

    @ResponseBody
    @GetMapping(value = "/credit/search/{bank}")
    public List<Credit> searchCreditByBank(@PathVariable String bank){
        return creditService.listCreditBank(bank);
    }

    @ResponseBody
    @GetMapping(value = "/credit/search/{title}")
    public List<Credit> searchCreditByTitle(@PathVariable String title){
        return creditService.listCreditTitle(title);
    }

    @ResponseBody
    @GetMapping(value = "/credit/search/{dateStart}")
    public List<Credit> searchCreditByDateStart(@PathVariable LocalDate dateStart){
        return creditService.listCreditByDateStart(dateStart);
    }

    @ResponseBody
    @GetMapping(value = "/credit/search/{dateEnd}")
    public List<Credit> searchCreditByDateEnd(@PathVariable LocalDate dateEnd){
        return creditService.listCreditByDateEnd(dateEnd);
    }

    @GetMapping(value="/credit/all")
    public String home(Model model) {
        List<Credit> credits = creditService.listAll();
        double sumCost = 0;
        double sumMonthPay = 0;
        for (Credit credit:credits) {
            if (credit.getCost() != null) {sumCost += credit.getCost();}
            if (credit.getMonthPay() != null) {sumMonthPay += credit.getMonthPay();}
        }
        model.addAttribute("sumCost", sumCost);
        model.addAttribute("sumMonthPay", sumMonthPay);
        model.addAttribute("credits", credits);
        return "/credit";
    }

    @RequestMapping(value = "/credits/search", method = RequestMethod.GET)
    public ModelAndView filter() {
        ModelAndView mav = new ModelAndView("search"/*, "command", new Filter()*/);
        //System.out.println(filter());
        return mav;
    }

    @ResponseBody
    @RequestMapping(value = "/credits",  method = RequestMethod.GET)
    public List<Credit> getCreditList() {
        return creditService.listAllCredits();
    }

    @PostMapping("/credits/save")
    public void saveCredit(@RequestBody Credit credit) {
        creditService.saveCredit(credit);
    }
}

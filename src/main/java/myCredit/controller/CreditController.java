package myCredit.controller;

import myCredit.domain.Credit;
import myCredit.domain.Person;
import myCredit.service.CreditService;
import myCredit.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.time.LocalDate;
import java.util.List;

@Controller
@RestController
@RequestMapping("/api/v1")
public class CreditController {

    @Autowired
    CreditService creditService;

    @Autowired
    PersonService personService;

    @RequestMapping(value ="/credit/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Credit getCredit(@PathVariable Integer id) {
        return creditService.getCredit(id);
    }


    @ResponseBody
    @PostMapping(value = "/person/{id}/credit/add")
    public Credit createCreditByPerson(@RequestBody Credit credit, @PathVariable Integer id) {
        credit.setPerson(personService.getPerson(id));
        Credit newCredit =  creditService.createCredit(credit);
        return newCredit;
    }

    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping(value = "/person/{id}/credit/{creditId}/delete")
    public void deleteCreditByPerson(@PathVariable Integer id, @PathVariable Integer creditId) {creditService.deleteCredit(creditId); }

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


    @RequestMapping(value="/credits/viewAll")
    public ModelAndView home() {
        List<Credit> listCredit = creditService.listAll();
        ModelAndView mav = new ModelAndView("credit");
        mav.addObject("listCredit", listCredit);
        return mav;
    }

    @RequestMapping(value = "/credits//search", method = RequestMethod.GET)
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

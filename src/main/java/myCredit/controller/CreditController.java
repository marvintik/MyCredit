package myCredit.controller;

import lombok.SneakyThrows;
import myCredit.domain.Credit;
import myCredit.domain.Person;
import myCredit.domain.User;
import myCredit.service.CreditService;
import myCredit.service.PersonService;
import myCredit.service.UserService;
import myCredit.utils.PdfCreator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static myCredit.utils.PrintCredit.*;

@Controller
@RequestMapping("")
public class CreditController {

    @Autowired
    CreditService creditService;

    @Autowired
    PersonService personService;
    @Autowired
    UserService userService;
    @Autowired
    PdfCreator pdfCreator;

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
    public String editCreditByPersonForm(@ModelAttribute Credit credit, @PathVariable Integer id,  @PathVariable Integer creditId, Model model) throws myCredit.exceptions.EntityNotFoundException {
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
    public Credit editCredit(@RequestBody Credit credit, @PathVariable Integer id) throws myCredit.exceptions.EntityNotFoundException {
        creditService.saveCredit(credit);
        Integer i = credit.getId();
      Credit editCredit = creditService.getCredit(i);
    return editCredit;}

    @GetMapping(value = "/mycredit/credit/search/bank/{bank}")
    public String searchCreditByBank(@PathVariable String bank, Model model){
        List<Credit> credits = creditService.listCreditBank(bank);
        model.addAttribute("bank", bank);
        model.addAttribute("sumCost", printSum(credits));
        model.addAttribute("sumMonthPay", printMonthPay(credits));
        model.addAttribute("credits", credits);
        return "/credit";
    }


    @GetMapping(value = "/mycredit/credit/search/title/{title}")
    public String searchCreditByTitle(@PathVariable String title, Model model){
        List<Credit> credits =   creditService.listCreditTitle(title);
        String bank = title;
        model.addAttribute("bank", bank);
        model.addAttribute("sumCost", printSum(credits));
        model.addAttribute("sumMonthPay", printMonthPay(credits));
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
        var bank = "Все банки";
        model.addAttribute("bank", bank);
        model.addAttribute("sumCost", printSum(credits));
        model.addAttribute("sumMonthPay", printMonthPay(credits));
        model.addAttribute("credits", credits);
        model.addAttribute("id", 0);
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

    @SneakyThrows
    @PostMapping("/mycredit/credits/save")
    public void saveCredit(@RequestBody Credit credit) {
        creditService.saveCredit(credit);
    }

    @GetMapping(value="/user/{id}/credit/all")
    public String allCreditsByUser(Model model, @PathVariable Integer id) {
        User user = userService.getUser(id);
        List<Credit> credits = printCredits(user);
        var bank = "Все банки";
        model.addAttribute("bank", bank);
        model.addAttribute("sumCost", printSum(credits));
        model.addAttribute("sumMonthPay", printMonthPay(credits));
        model.addAttribute("credits", credits);
        model.addAttribute("id", user.getId());
        return "/credit";
    }

    @RequestMapping(value = "/user/{id}/credit/all/pdf/", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPdf(HttpServletResponse response, @PathVariable("id") int id) throws EntityNotFoundException, IOException {
        var user = userService.getUser(id);

        ByteArrayInputStream bis = pdfCreator.createUserPdf(user);

        HttpHeaders headers = new HttpHeaders();
        String name = user.getUsername() +"_credits.pdf";
        headers.add("Content-Disposition", "inline; " +name);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
    }
}

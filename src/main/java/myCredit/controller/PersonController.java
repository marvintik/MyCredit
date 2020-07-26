package myCredit.controller;

import com.itextpdf.text.Document;
import lombok.SneakyThrows;
import myCredit.domain.BankAccount;
import myCredit.domain.Credit;
import myCredit.domain.Person;
import myCredit.domain.User;
import myCredit.service.BankService;
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
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.persistence.EntityNotFoundException;
import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
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

    @Autowired
    PdfCreator pdfCreator;

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
    public String newPersonAdd(@ModelAttribute Person person, Model model) {
        if (person.getImage() == "" || person.getImage() == null) {
            person.setImage("https://goo.su/16eh");
        }
        personService.createPerson(person);
        return "redirect:/mycredit/person/all";
    }

    @GetMapping(value = "/mycredit/person/{id}/edit")
    public String editPersonForm(@PathVariable Integer id, Model model) {
        Person person = personService.getPerson(id);
        model.addAttribute("person", person);
        return "/editPerson";
    }

    @PostMapping(value = "/mycredit/person/{id}/edit")
    public String editPersonForm(@ModelAttribute Person person, @PathVariable Integer id, Model model) {
        person.setId(id);
        personService.savePerson(person);
        return "redirect:/mycredit/person/all";
    }

    @GetMapping(value = "/mycredit/person/{id}/delete")
    public String deleteCreditByPerson(@PathVariable Integer id) {
        personService.deletePerson(id);
        return "redirect:/mycredit/person/all";
    }

    @GetMapping(value = "/mycredit/person/all")
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
    public String getAccountMono(@PathVariable Integer id, Model model) {
        var person = personService.getPerson(id);
        var token = person.getTokenMono();
        if (token != null) {
            BankAccount.Example example = bankService.getClientMono(token);
            model.addAttribute("example", example);
            return "/personIdMono";
        } else {
            return "redirect:/mycredit/person/" + id;
        }
    }

    @GetMapping("/mycredit/person/{id}")
    public String getPerson(@PathVariable Integer id, Model model) {
        Person person = personService.getPerson(id);
        List<Credit> credits = person.getCredits();
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
      //  model.addAttribute("image", image);
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

    @SneakyThrows
    @PostMapping("/user/{id}/person/add")
    public String newPersonAdd(@ModelAttribute Person person, @RequestParam("file") MultipartFile file, Model model, @PathVariable Integer id) {
        if (person.getImage() == "" || person.getImage() == null && file == null) {
            person.setImage("https://goo.su/16eh");
        } else {
            file.getContentType();
            file.getOriginalFilename();
            person.setImageFile(file.getBytes());
        }
        person.setUser(userService.getUser(id));
        personService.createPerson(person);
        String redirect = "redirect:/user/" + id + "/person/all";
        return redirect;
    }

    @RequestMapping(value = "/image/{id}", method = RequestMethod.GET)
    public void getImage(HttpServletResponse response, @PathVariable("id") int id) throws EntityNotFoundException, IOException {
        var person = personService.getPerson(id);
        response.setContentType(MediaType.IMAGE_PNG_VALUE);
        response.getOutputStream().write(person.getImageFile());
        response.setContentLength(person.getImageFile().length);
    }


    @RequestMapping(value = "/person/pdf/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_PDF_VALUE)
    public ResponseEntity<InputStreamResource> getPdf(HttpServletResponse response, @PathVariable("id") int id) throws EntityNotFoundException, IOException {
        var person = personService.getPerson(id);
      //  Document document = pdfCreator.createPdf(person);

        ByteArrayInputStream bis = pdfCreator.createPdf(person);

        HttpHeaders headers = new HttpHeaders();
        String name = person.getName() +"_credits.pdf";
        headers.add("Content-Disposition", "inline; " +name);

        return ResponseEntity
                .ok()
                .headers(headers)
                .contentType(MediaType.APPLICATION_PDF)
                .body(new InputStreamResource(bis));
//        HttpHeaders headers = new HttpHeaders();
//        response.setContentType(MediaType.APPLICATION_PDF_VALUE);
//        response.getOutputStream().write(person.getImageFile());
//        response.setContentLength(person.getImageFile().length);
    }


    }
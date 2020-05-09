package myCredit.controller;

import myCredit.domain.Credit;
import myCredit.domain.Person;
import myCredit.service.PersonService;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/mycredit/person")
public class PersonController {

    @Autowired
    PersonService personService;

    /*VieWController*/

    @RequestMapping("/persons/newView")
    public ModelAndView newPersonForm() {
        Person person = new Person();
        ModelAndView model = new ModelAndView("new_person");
        model.addObject("person", person);
        return model;
    }

    @RequestMapping(value = "/persons/saveView", method = RequestMethod.POST)
    public String createPerson(@RequestParam(value="name") String name, @RequestParam (value="image") String image)  {
        Person person = new Person();
        person.setName(name);
        person.setImage(image);
        personService.savePerson(person);
        return "redirect:/";

    }

    @GetMapping(value="/all")
    public String home(Model model) {
        List<Person> persons = personService.listAll();
        model.addAttribute("persons", persons);
        return "/person";
    }

    @RequestMapping(value = "/persons/searchView", method = RequestMethod.GET)
    public ModelAndView filter() {
        ModelAndView mav = new ModelAndView("search"/*, "command", new Filter()*/);
        //System.out.println(filter());
        return mav;
    }

    @GetMapping("/{id}/view")
    public ModelAndView getPerson(@PathVariable Integer id) {
        Person person = personService.getPerson(id);
        ModelAndView mav = new ModelAndView("personId");
        mav.addObject("person", person);
        return mav;
    }

}
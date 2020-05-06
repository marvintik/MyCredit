package myCredit.controller;

import com.google.gson.Gson;
import myCredit.domain.Person;
import myCredit.service.PersonService;
import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("/api/v1/persons")
public class PersonController {

    @Autowired
    PersonService personService;

 /*   @GetMapping("/{id}")
    public ModelAndView getPerson(@PathVariable Integer id) {
        Person person = personService.getPerson(id);
        ModelAndView mav = new ModelAndView("personId");
        mav.addObject("person", person);
        return mav;
    }*/


    @ResponseBody
    @RequestMapping(value ="/{id}", method = RequestMethod.GET)
    public Person gPersonId (@PathVariable Integer id){
        return personService.getPerson(id);
    }

    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public void deletePerson(@PathVariable Integer id){
        personService.deletePerson(id);
    }

    @PostMapping(value = "/new")
    public void createPerson(@RequestBody Person person){
        personService.createPerson(person);
    }

    @PostMapping(value = "/edit")
    public void editPerson(@RequestBody Person person){
        personService.savePerson(person);
    }


    @RequestMapping("/newView")
    public ModelAndView newPersonForm() {
        Person person = new Person();
        ModelAndView model = new ModelAndView("new_person");
        model.addObject("person", person);
        return model;
    }

    @RequestMapping(value = "/saveView", method = RequestMethod.POST)
    public String createPerson(@RequestParam(value="name") String name, @RequestParam (value="image") String image)  {
        Person person = new Person();
        person.setName(name);
        person.setImage(image);
        personService.savePerson(person);
        return "redirect:/";

    }

    @RequestMapping(value="/viewAll")
    public ModelAndView home() {
        List<Person> listPerson = personService.listAll();
        ModelAndView mav = new ModelAndView("person");
        mav.addObject("listPerson", listPerson);
        return mav;
    }

    @RequestMapping(value = "/search", method = RequestMethod.GET)
    public ModelAndView filter() {
        ModelAndView mav = new ModelAndView("search"/*, "command", new Filter()*/);
        //System.out.println(filter());
        return mav;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    public List<Person> getPersonList() {
        return personService.listAllPersons();
    }

    @PostMapping(value = "/save")
    public void savePerson(@RequestBody Person person) {
        personService.savePerson(person);
    }
}
package myCredit.controller;

import com.google.gson.Gson;
import myCredit.domain.Credit;
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
@RequestMapping("/api/v1")
public class PersonController {

    @Autowired
    PersonService personService;

   @GetMapping("/{id}/view")
    public ModelAndView getPerson(@PathVariable Integer id) {
        Person person = personService.getPerson(id);
        ModelAndView mav = new ModelAndView("personId");
        mav.addObject("person", person);
        return mav;
    }


    @ResponseBody
    @RequestMapping(value ="/person/{id}", method = RequestMethod.GET)
    public Person gPersonId (@PathVariable Integer id){
        return personService.getPerson(id);
    }

    @ResponseStatus(HttpStatus.OK)
    @RequestMapping(value = "/person/{id}/delete", method = RequestMethod.DELETE)
    public void deletePerson(@PathVariable Integer id){
        personService.deletePerson(id);
    }

    @ResponseBody
    @PostMapping(value = "/person/add")
    public Person createPerson(@RequestBody Person person){
        Person newPerson = personService.createPerson(person);
        return newPerson;
    }

    @ResponseBody
    @PostMapping(value = "/person/{id}/edit")
    public Person editPerson(@RequestBody Person person, @PathVariable Integer id){
        personService.savePerson(person);
        Person editPerson = personService.getPerson(id);
        return editPerson;
    }

    @ResponseBody
    @GetMapping(value = "/person/{id}/credits")
    public List<Credit> getCreditPerson(@PathVariable Integer id){
       Person person = personService.getPerson(id);
       List<Credit> list = person.getCredits();
       return list;
    }

    @ResponseBody
    @RequestMapping(value = "/persons", method = RequestMethod.GET)
    public List<Person> getPersonList() {
        return personService.listAllPersons();
    }

    @PostMapping(value = "/persons/save")
    public void savePerson(@RequestBody Person person) {
        personService.savePerson(person);
    }

    @ResponseBody
    @GetMapping(value = "/person/search/{name}")
    public List<Person> searchByName(@PathVariable String name){
       return personService.listPersonName(name);
    }

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

    @RequestMapping(value="/persons/viewAll")
    public ModelAndView home() {
        List<Person> listPerson = personService.listAll();
        ModelAndView mav = new ModelAndView("person");
        mav.addObject("listPerson", listPerson);
        return mav;
    }

    @RequestMapping(value = "/persons/searchView", method = RequestMethod.GET)
    public ModelAndView filter() {
        ModelAndView mav = new ModelAndView("search"/*, "command", new Filter()*/);
        //System.out.println(filter());
        return mav;
    }

}
package myCredit.controller;

import myCredit.domain.Credit;
import myCredit.domain.Person;
import myCredit.service.PersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1")
public class PersonRestController {
    @Autowired
    PersonService personService;


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

}

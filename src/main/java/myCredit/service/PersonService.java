package myCredit.service;

import myCredit.domain.Person;
import myCredit.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class PersonService {

    @Autowired
    PersonRepository personRepository;


    public Person getPerson(Integer id) {
        return personRepository.findById(id).get();
    }


    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    public void savePerson(Person person) {
        personRepository.save(person);
    }

    public List<Person> listAll() {
        List<Person> list = new ArrayList<>();
        personRepository.findAll().forEach(list::add);
        list.sort(Comparator.comparing(Person::getId));
        return list;
    }

    public void deletePerson(Integer id){
        personRepository.deleteById(id);
    }

    public List<Person> listAllPersons() {
        List<Person> list = new ArrayList<>();
        personRepository.findAll().forEach(list::add);
       list.sort(Comparator.comparing(Person::getId));
        return list;
    }

    public List<Person> listPersonName(String name){
        return personRepository.findByName(name);
    }
}

package myCredit.service;

import myCredit.domain.Person;
import myCredit.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class PersonService {

    @Autowired
    PersonRepository personRepository;


    public Person getPerson(Integer id) {
        return personRepository.findById(id).get();
    }

    @Transactional
    public Person createPerson(Person person) {
        return personRepository.save(person);
    }

    @Transactional
    public void savePerson(Person person) {
        personRepository.save(person);
    }

    public List<Person> listAll() {
        List<Person> list = new ArrayList<>();
        personRepository.findAll().forEach(list::add);
        return list;
    }

    @Transactional
    public void deletePerson(Integer id){
        personRepository.deleteById(id);
    }


    @Transactional
    public List<Person> listAllPersons() {
        List<Person> list = new ArrayList<>();
        personRepository.findAll().forEach(list::add);
        return list;
    }
}

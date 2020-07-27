package myCredit.service;

import myCredit.domain.Credit;
import myCredit.domain.Person;
import myCredit.exceptions.EntityNotFoundException;
import myCredit.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
@Transactional
public class CreditService {

    @Autowired
    CreditRepository creditRepository;


    public Credit getCredit(Integer id) {
        return creditRepository.findById(id).get();
    }
    
    public Credit createCredit(Credit create) {
        if (creditRepository.existsById(create.getId())) {
            create.setId((int) creditRepository.count()+1);
        };
        return creditRepository.save(create);
    }



    public void saveCredit(Credit credit) throws EntityNotFoundException {
        if(creditRepository.existsById(credit.getId())){
            creditRepository.save(credit);}
        else  throw new EntityNotFoundException(Credit.class, credit.getId());
    }

    public List<Credit> listAll() {
        List<Credit> list = new ArrayList<>();
        creditRepository.findAll().forEach(list::add);
        list.sort(Comparator.comparing(Credit::getId));
        return list;
    }

    public void deleteCredit(Integer id){
        creditRepository.deleteById(id);
    }

    public List<Credit> listAllCredits() {
        List<Credit> list = new ArrayList<>();
        creditRepository.findAll().forEach(list::add);
        list.sort(Comparator.comparing(Credit::getId));
        return list;
    }

    public List<Credit> listCreditBank(String bank){
        return creditRepository.findByBank(bank);
    }

    public List<Credit> listCreditTitle(String title){
       return creditRepository.findByTitle(title);
    }

    public List<Credit> listCreditByDateStart(LocalDate date){
        return creditRepository.findByDateStart(date);
    }

    public List<Credit> listCreditByDateEnd(LocalDate date){
        return creditRepository.findByDateEnd(date);
    }

}

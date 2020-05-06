package myCredit.service;

import myCredit.domain.Credit;
import myCredit.repository.CreditRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
public class CreditService {

    @Autowired
    CreditRepository creditRepository;


    public Credit getCredit(Integer id) {
        return creditRepository.findById(id).get();
    }
    
    public Credit createCredit(Credit create) {
        return creditRepository.save(create);
    }

    public void saveCredit(Credit credit) {
        creditRepository.save(credit);
    }

    public List<Credit> listAll() {
        List<Credit> list = new ArrayList<>();
        creditRepository.findAll().forEach(list::add);
        return list;
    }

    public void deleteCredit(Integer id){
        creditRepository.deleteById(id);
    }


    @Transactional
    public List<Credit> listAllCredits() {
        List<Credit> list = new ArrayList<>();
        creditRepository.findAll().forEach(list::add);
        return list;
    }
}

package myCredit.controller;

import myCredit.domain.Credit;
import myCredit.service.CreditService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RestController
@RequestMapping("/api/v1/credits")
public class CreditController {

    @Autowired
    CreditService creditService;

    @RequestMapping(value ="/{id}", method = RequestMethod.GET)
    @ResponseBody
    public Credit getCredit(@PathVariable Integer id) {
        return creditService.getCredit(id);
    }


    @PostMapping(value = "/new")
    public void createCredit(@RequestBody Credit credit) {
        creditService.createCredit(credit);
    }

    @DeleteMapping(value = "/delete")
    public void deleteCredit(@RequestBody Integer id){ creditService.deleteCredit(id);}

    @PostMapping(value = "/edit")
    public void editCredit(@RequestBody Credit credit) { creditService.saveCredit(credit);}

    @RequestMapping(value="/viewAll")
    public ModelAndView home() {
        List<Credit> listCredit = creditService.listAll();
        ModelAndView mav = new ModelAndView("credit");
        mav.addObject("listCredit", listCredit);
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
    public List<Credit> getCreditList() {
        return creditService.listAllCredits();
    }

    @PostMapping("/save")
    public void saveCredit(@RequestBody Credit credit) {
        creditService.saveCredit(credit);
    }
}

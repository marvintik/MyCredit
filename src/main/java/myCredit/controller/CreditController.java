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


    @ResponseBody
    @PostMapping(value = "/new")
    public Credit createCredit(@RequestBody Credit credit) {
        Credit credit1 = creditService.createCredit(credit);
        return credit1;
    }

    @ResponseBody
    @DeleteMapping(value = "/delete")
    public String deleteCredit(@RequestBody Integer id){ creditService.deleteCredit(id);
        return "Status.OK";
    }

    @ResponseBody
    @PostMapping(value = "/edit")
    public Credit editCredit(@RequestBody Credit credit) {
        creditService.saveCredit(credit);
        Integer i = credit.getId();
      Credit editCredit = creditService.getCredit(i);
    return editCredit;}

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

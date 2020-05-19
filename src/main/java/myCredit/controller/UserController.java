package myCredit.controller;

import myCredit.domain.User;
import myCredit.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/mycredit/user")
public class UserController {

    @Autowired
    UserService userService;


    @GetMapping("/add")
    public String newUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "/newUser";
    }

    @PostMapping("/add")
    public String newUserAdd(@ModelAttribute User user, Model model){
        userService.saveUser(user);
        return "redirect:/";
    }
}
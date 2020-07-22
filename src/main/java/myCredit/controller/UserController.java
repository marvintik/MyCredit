package myCredit.controller;

import myCredit.domain.Person;
import myCredit.domain.User;
import myCredit.domain.UserFacebook;
import myCredit.domain.UserGoogle;
import myCredit.service.PersonService;
import myCredit.service.UserService;
import net.minidev.json.JSONUtil;
import org.hibernate.tool.schema.internal.exec.ScriptTargetOutputToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class UserController {

    @Autowired
    UserService userService;
    @Autowired
    PersonService personService;

    @GetMapping("/user")
    public String userPage(Model model, @AuthenticationPrincipal Authentication authentication) {
        Object principal = authentication.getPrincipal();
        if (principal instanceof User) {
            User user = (User) principal;
            model.addAttribute("user", user);
        }  else {
            OAuth2User user = (OAuth2User) principal;
            User userOauth = userService.getUserByUsername(user.getAttribute("email"));
            System.out.println(user.getName());
            model.addAttribute("user", userOauth);}
        return "/user";
    }

    @GetMapping("/account/add")
    public String newUserForm(Model model) {
        User user = new User();
        model.addAttribute("user", user);
        return "/newUser";
    }

    @PostMapping("/account/add")
    public String newUserAdd(@ModelAttribute User user, Model model) {
        userService.saveUser(user);
        return "redirect:/";
    }

    @RequestMapping("/oauth2LoginSuccess")
    public String getOauth2LoginInfo(Model model, @AuthenticationPrincipal OAuth2AuthenticationToken authenticationToken) {
        OAuth2User user = authenticationToken.getPrincipal();
        String clientId = authenticationToken.getAuthorizedClientRegistrationId();
        if (clientId.equals("facebook")) {
            UserFacebook userFacebook = userService.getUserFacebook(authenticationToken);
            model.addAttribute("user", userFacebook);
        }
        if (clientId.equals("google")) {
            UserGoogle userGoogle = userService.getUserGoogle(authenticationToken);
            model.addAttribute("user", userGoogle);
        }
        System.out.println("userId: " + user.getName());
        return "userOauth";
    }




}
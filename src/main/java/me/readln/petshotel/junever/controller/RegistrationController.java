package me.readln.petshotel.junever.controller;

import me.readln.petshotel.junever.model.User;
import me.readln.petshotel.junever.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;

@Controller
public class RegistrationController {

    @Autowired
    private UserService userService;

    @GetMapping("/registration")
    public String registration(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(@Valid User userForm, BindingResult bindingResult, Model model) {

        if (bindingResult.hasErrors()) {
            model.addAttribute("user", userForm);
            return "registration";
        }

        if (!userForm.getPassword().equals(userForm.getPasswordConfirm())) {
            model.addAttribute("user", userForm);
            model.addAttribute("passwordError", "Passwords don't match");
            return "registration";
        }

        if (!userService.saveUser(userForm)) {
            model.addAttribute("user", userForm);
            model.addAttribute("usernameError", "Name '" + userForm.getUsername() + "' already exist");
            return "registration";
        }

        model.addAttribute("user", userForm);
        model.addAttribute("signUpSuccess", "Thanks for registration!");

        return "login";
    }
}

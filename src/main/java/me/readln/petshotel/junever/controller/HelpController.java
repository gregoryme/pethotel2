package me.readln.petshotel.junever.controller;

import me.readln.petshotel.junever.etc.Constant;
import me.readln.petshotel.junever.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Date;

@Controller
public class HelpController {

    @Autowired
    private UserService userService;

    @GetMapping("/help")
    public String userList(Model model) {
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("adminName", Constant.ADMIN_USER_NAME);
        model.addAttribute("today", new Date());
        return "help";
    }

}

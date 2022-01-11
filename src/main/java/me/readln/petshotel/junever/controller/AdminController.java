package me.readln.petshotel.junever.controller;

import me.readln.petshotel.junever.model.User;
import me.readln.petshotel.junever.model.Visit;
import me.readln.petshotel.junever.service.EventService;
import me.readln.petshotel.junever.service.UserService;
import me.readln.petshotel.junever.service.VisitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.Date;

@Controller
public class AdminController {

    @Autowired
    private VisitService visitService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    @GetMapping("/admin")
    public String userList(Model model) {
        model.addAttribute("listOfAllUsers", userService.allUsers());
        model.addAttribute("listAllVisits", visitService.getAllVisits());
        model.addAttribute("today", new Date());
        return "admin";
    }

    // delete event
    @GetMapping("/admin/{idVisit}")
    public String deleteEventAdmin(@PathVariable(value = "idVisit") long idVisit) {
        Visit visit = visitService.getVisitById(idVisit);
        eventService.deleteEventsByVisit(visit);
        visitService.deleteVisitById(idVisit);
        String s = "redirect:/admin";

        return s;
    }

    // restore magic
    @GetMapping("/admin/magic/{userName}")
    public String restoreMagicForUserAdmin(@PathVariable(value = "userName") String userName) {
        User user = userService.getUserByUsername(userName);
        user.setMagic(0);
        userService.saveUserForUpdate(user);
        String s = "redirect:/admin";

        return s;
    }

}

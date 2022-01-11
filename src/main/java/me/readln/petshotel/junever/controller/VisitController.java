package me.readln.petshotel.junever.controller;

import me.readln.petshotel.junever.etc.Constant;
import me.readln.petshotel.junever.generate.Magic;
import me.readln.petshotel.junever.model.Event;
import me.readln.petshotel.junever.model.EventStatus;
import me.readln.petshotel.junever.model.User;
import me.readln.petshotel.junever.model.Visit;
import me.readln.petshotel.junever.service.EventService;
import me.readln.petshotel.junever.service.UserService;
import me.readln.petshotel.junever.service.VisitService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.Date;

@Controller
public class VisitController {

    @Autowired
    private VisitService visitService;

    @Autowired
    private EventService eventService;

    @Autowired
    private UserService userService;

    // home page
    @GetMapping("/")
    public String viewHomePage(Model model) {
        if (userService.getCurrentUser().getUsername() == null) return "login";

        User currentUser = userService.getCurrentUser();
        model.addAttribute("listAllVisits", visitService.getActiveVisitsInOrderByCheckInDateAndByUser(currentUser));
        model.addAttribute("today", new Date());

        // magic button matter
        if (currentUser.getMagic() < Constant.MAX_ATTEMPTS_GENERATE_BUTTON_PER_USER) {
            model.addAttribute("magic", "OK");
        } else {
            model.addAttribute("magic", "NOT");
        }

        return "index";
    }

    // show all visits
    @GetMapping("/showAllVisits")
    public String showAllVisits(Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("listAllVisits", visitService.getAllVisitsInOrderByCheckInDateAndByUser(currentUser));
        model.addAttribute("today", new Date());

        return "list_all_visits";
    }

    // visit info
    @GetMapping("/showVisitPage/{id}")
    public String showVisitPage(@PathVariable(value = "id") long id, Model model) {
        Visit visit = visitService.getVisitById(id);
        model.addAttribute("visit", visit);
        model.addAttribute("today", new Date());

        return "visit_page";
    }

    // check-out
    @GetMapping("/setCheckOutStatus/{id}")
    public String setCheckOutStatus(@PathVariable(value = "id") long id, Model model) {
        Visit visit = visitService.getVisitById(id);
        visit.setActiveStatus(false);
        visitService.saveVisit(visit);

        //CHANGE STATUS FOR CHECKED OUT VISIT
        eventService.changeEventStatus(EventStatus.MISSED, EventStatus.AWAIT, visit);
        model.addAttribute("visit", visit);
        model.addAttribute("today", new Date());

        return "visit_page";
    }

    // restore visit
    @GetMapping("/restoreVisitActiveStatus/{id}")
    public String restoreVisitActiveStatus(@PathVariable(value = "id") long id, Model model) {

        Visit visit = visitService.getVisitById(id);
        visit.setActiveStatus(true);
        visitService.saveVisit(visit);

        //RESTORE STATUS "AWAIT" FOR EVENTS WITH "MISSED"
        eventService.changeEventStatus(EventStatus.AWAIT, EventStatus.MISSED, visit);
        model.addAttribute("visit", visit);
        model.addAttribute("today", new Date());

        return "visit_page";
    }

    // new visit
    @GetMapping("/showNewVisitForm")
    public String showNewVisitForm(Model model) {
        User currentUser = userService.getCurrentUser();
        Visit visit = new Visit();
        visit.setActiveStatus(true);
        visit.setUser(currentUser);
        model.addAttribute("visit", visit);
        model.addAttribute("today", new Date());

        return "new_visit";
    }

    // save visit
    @PostMapping("/saveVisit")
    public String saveVisit(@ModelAttribute("visit") Visit visit) {
        visitService.saveVisit(visit);
        return "redirect:/";
    }

    // delete visit
    @GetMapping("/deleteVisit/{idVisit}")
    public String deleteVisit(@PathVariable(value = "idVisit") long idVisit) {
        Visit visit = visitService.getVisitById(idVisit);
        eventService.deleteEventsByVisit(visit);
        visitService.deleteVisitById(idVisit);

        return "redirect:/";
    }

    // delete visit
    @GetMapping("/generate")
    public String generateVisit() {

        // if = checking how many attempts this user already used
        User currentUser = userService.getCurrentUser();
        if (currentUser.getMagic() < Constant.MAX_ATTEMPTS_GENERATE_BUTTON_PER_USER) {

            Magic magic = new Magic(); // "Magic" is a source of random data for the DB

            // generate one visit

            Visit visit = new Visit();
            visit.setActiveStatus(true);
            visit.setUser(userService.getCurrentUser());
            visit.setPetName(magic.getRandomPetName());
            visit.setOwnerName(magic.getRandomOwnerName());
            visit.setOwnerContact(magic.getRandomOwnerContact());
            visit.setBoxNumber(magic.getRandomBoxNumber());
            visit.setVisitNotes(magic.getRandomNote());
            visit.setCheckInDate(magic.getRandomCheckInDate());
            visit.setCheckOutDate(magic.getRandomCheckOutDate());

            // save generated visit to the DB
            visitService.saveVisit(visit);

            // generate three events for generated visit
            Event event;
            final int generatedEventsQuantity = 3;
            for (int i = 0; i < generatedEventsQuantity; i++) {
                event = new Event();
                event.setUser(userService.getCurrentUser());
                event.setVisit(visit);
                event.setEventStatus(EventStatus.AWAIT);
                event.setEventDescription(magic.getRandomEventDescription());
                //@ToDo generate time
                // save generated event to the DB
                eventService.saveEvent(event);
            }

            // increase and save quantity of attempts
            currentUser.setMagic(currentUser.getMagic() + 1);
            userService.saveUserForUpdate(currentUser);
        }

        return "redirect:/";
    }
}

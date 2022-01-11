package me.readln.petshotel.junever.controller;

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

import java.sql.Time;
import java.util.Date;

@Controller
public class EventController {

    @Autowired
    private EventService eventService;

    @Autowired
    private VisitService visitService;

    @Autowired
    private UserService userService;

    // all AWAITING events page - user version
    @GetMapping("/showCurrentEventsForAllVisits")
    public String showAllAwaitingEvents(Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("listCurrentEventsForAllVisits",
                eventService.getAwaitingEventsInOrderAndByUser(currentUser));
        model.addAttribute("today", new Date());

        return "current_events_for_all_visits";
    }

    // all (absolutely) events page
    @GetMapping("/showAllEventsForAllVisits")
    public String showAllEvents(Model model) {
        User currentUser = userService.getCurrentUser();
        model.addAttribute("listAllEventsForAllVisits", eventService.getAllEventsByUserInOrder(currentUser));
        model.addAttribute("today", new Date());

        return "all_events_for_all_visits";
    }

    @GetMapping("/showEventPage/{id}")
    public String showEventPage(@PathVariable(value = "id") long id, Model model) {
        Event event = eventService.getEventById(id);
        event.setGoTime(event.getStartTime().toString());
        model.addAttribute("event", event);
        model.addAttribute("today", new Date());

        return "event_page";
    }

    // save event for specific visit
    @PostMapping("/saveEventForVisit/{idVisit}")
    public String saveEventForVisit(
            @RequestParam("goTime") String goTime,
            @PathVariable(value = "idVisit") long id,
            @ModelAttribute("event") Event event) {

        Visit visit = visitService.getVisitById(id);
        String timeString = "" + goTime;
        if (timeString.length() <= 5 ) timeString = timeString + ":00";
        event.setStartTime(Time.valueOf(timeString));
        event.setVisit(visit);
        eventService.saveEvent(event);

        String s = "redirect:/showSelectiveScript/" + id;
        return s;
    }

    // show script (list of events) for specific visit
    @GetMapping("/showSelectiveScript/{id}")
    public String showSelectiveScriptPage(@PathVariable(value = "id") long id, Model model) {

        Event event = new Event();
        event.setUser(userService.getCurrentUser());
        event.setGoTime("00:00");
        Visit visit = visitService.getVisitById(id);
        model.addAttribute("listSelectiveEvents", eventService.getEventsByVisitAndOrderByTime(visit));
        model.addAttribute("event", event);
        model.addAttribute("visit", visit);

        // ALL = mode with all events, not only "AWAITS"
        String weSee = "ALL";

        model.addAttribute("weSee", weSee);
        model.addAttribute("today", new Date());

        return "events_management";
    }

    // show ACTIVE script (list of AWAITING events) for specific visit
    @GetMapping("/showSelectiveScriptAwaitingEvents/{id}")
    public String showSelectiveScriptPageWithAwaitingEvents(@PathVariable(value = "id") long id, Model model) {

        Event event = new Event();
        event.setUser(userService.getCurrentUser());
        Visit visit = visitService.getVisitById(id);
        model.addAttribute("listSelectiveEvents", eventService.getAwaitingEventsByVisitInOrder(visit));
        model.addAttribute("event", event);
        model.addAttribute("visit", visit);

        // SELECTIVE = mode with "AWAITS" events
        String weSee = "SELECTIVE";

        model.addAttribute("weSee", weSee);
        model.addAttribute("today", new Date());

        return "events_management";
    }

    @GetMapping("/makeEventDone/{id}")
    public String makeEventDone(@PathVariable(value = "id") long id, Model model) {
        Event event = eventService.getEventById(id);
        event.setEventStatus(EventStatus.DONE);
        eventService.saveEvent(event);
        model.addAttribute("event", event);
        model.addAttribute("today", new Date());
        return "event_page";
    }

    @GetMapping("/makeEventAwait/{id}")
    public String makeEventAwait(@PathVariable(value = "id") long id, Model model) {
        Event event = eventService.getEventById(id);
        event.setEventStatus(EventStatus.AWAIT);
        eventService.saveEvent(event);
        model.addAttribute("event", event);
        model.addAttribute("today", new Date());

        return "event_page";
    }

    @GetMapping("/cloneEvent/{idEvent}")
    public String cloneEvent(@PathVariable(value = "idEvent") long idEvent, Model model) {
        Event event = eventService.getEventById(idEvent);
        Event newEvent = new Event();
        newEvent.setUser(event.getUser());
        newEvent.setEventStatus(event.getEventStatus());
        newEvent.setEventDate(event.getEventDate());
        newEvent.setStartTime(event.getStartTime());
        newEvent.setEventDescription(event.getEventDescription());
        newEvent.setEventNotes(event.getEventNotes());
        newEvent.setVisit(event.getVisit());
        eventService.saveEvent(newEvent);
        model.addAttribute("listSelectiveEvents", eventService.getEventsByVisitAndOrderByTime(event.getVisit()));
        model.addAttribute("event", event);
        model.addAttribute("visit", event.getVisit());
        String weSee = "ALL";
        model.addAttribute("weSee", weSee);
        model.addAttribute("today", new Date());

        return "events_management";
    }

    // delete event
    @GetMapping("/deleteEventOfVisit/{idEvent}/{idVisit}")
    public String deleteEventOfVisit(@PathVariable(value = "idEvent") long idEvent,
                                     @PathVariable(value = "idVisit") long idVisit) {

        eventService.deleteEventById(idEvent);

        String s = "redirect:/showSelectiveScript/" + idVisit;

        return s;
    }

}

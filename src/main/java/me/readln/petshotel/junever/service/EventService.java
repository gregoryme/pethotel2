package me.readln.petshotel.junever.service;

import me.readln.petshotel.junever.model.Event;
import me.readln.petshotel.junever.model.EventStatus;
import me.readln.petshotel.junever.model.User;
import me.readln.petshotel.junever.model.Visit;
import me.readln.petshotel.junever.repo.EventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventService {

    @Autowired
    private EventRepository eventRepository;

    public List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    public List<Event> getAllEventsByUser(User user) { return eventRepository.findAllByUser(user); }

    public List<Event> getAllEventsInOrder() {
        return eventRepository.findByOrderByEventDateAscStartTimeAsc();
    }

    public List<Event> getAllEventsByUserInOrder(User user) {
        return eventRepository.findByUserByOrderByEventDateStartTime(user);
    }

    public List<Event> getEventsByVisit(Visit visit) {
        return this.eventRepository.findByVisit(visit);
    }

    public List<Event> getEventsByVisitAndOrderByTime(Visit visit) {
        return this.eventRepository.findByVisitOrderByEventDateAscStartTimeAsc(visit);
    }

    public List<Event> getAwaitingEventsByVisit (Visit visit) {
        return this.eventRepository.findByVisitAndEventStatus(visit, EventStatus.AWAIT);
    }

    public List<Event> getAwaitingEventsByVisitInOrder(Visit visit) {
        return this.eventRepository.findByVisitAndEventStatusOrderByEventDateAscStartTimeAsc(visit, EventStatus.AWAIT);
    }

    public List<Event> getAwaitingEvents() {
        return this.eventRepository.findByEventStatus(EventStatus.AWAIT);
    }

    public List<Event> getAwaitingEventsAndByUser(User user) {
        return this.eventRepository.findByUserAndEventStatus(user, EventStatus.AWAIT);
    }

    public List<Event> getAwaitingEventsInOrder() {
        return this.eventRepository.findByEventStatusOrderByEventDateAscStartTimeAsc(EventStatus.AWAIT);
    }

    public List<Event> getAwaitingEventsInOrderAndByUser(User user) {
        return this.eventRepository.findAndByUserAndByEventStatusOrderByEventDateStartTime(EventStatus.AWAIT, user);
    }

    public void saveEvent(Event event) {
        this.eventRepository.save(event);
    }

    public Event getEventById(long id) {
        Optional<Event> optional = eventRepository.findById(id);
        Event event = null;
        if (optional.isPresent()) {
            event = optional.get();
        } else {
            throw new RuntimeException("Event not found, visit's id = " + id);
        }
        return event;
    }

    public void deleteEventById(long id) {
        this.eventRepository.deleteById(id);
    }

    public void changeEventStatus(EventStatus newEventStatus, EventStatus oldEventStatus, Visit visit) {
        eventRepository.setEventStatusFor(newEventStatus, oldEventStatus, visit);
    }

    public void deleteEventsByVisit(Visit visit) {
        eventRepository.deleteEventsByVisit(visit);
    }

}

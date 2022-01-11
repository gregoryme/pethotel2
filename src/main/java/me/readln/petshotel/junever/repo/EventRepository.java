package me.readln.petshotel.junever.repo;

import me.readln.petshotel.junever.model.Event;
import me.readln.petshotel.junever.model.EventStatus;
import me.readln.petshotel.junever.model.User;
import me.readln.petshotel.junever.model.Visit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

    List<Event> findByVisit(Visit visit);
    List<Event> findByVisitAndEventStatus(Visit visit, EventStatus eventStatus);
    List<Event> findByEventStatus(EventStatus eventStatus);
    List<Event> findByUserAndEventStatus (User user, EventStatus eventStatus);

    @Transactional
    @Query("SELECT e FROM Event e WHERE e.user = ?1")
    List<Event> findAllByUser(User user);

    List<Event> findByEventStatusOrderByEventDateAscStartTimeAsc(EventStatus eventStatus);

    @Transactional
    @Query("SELECT e FROM Event e WHERE e.user = ?2 AND e.eventStatus = ?1 ORDER BY e.eventDate, e.startTime")
    List<Event> findAndByUserAndByEventStatusOrderByEventDateStartTime(EventStatus eventStatus, User user);

    List<Event> findByVisitAndEventStatusOrderByEventDateAscStartTimeAsc(Visit visit, EventStatus eventStatus);

    @Transactional
    @Query("SELECT e FROM Event e WHERE e.user = ?3 AND e.visit = ?1  AND e.eventStatus = ?2 ORDER BY e.eventDate, e.startTime")
    List<Event> findByVisitAndEventStatusAndByUserOrderByEventDateStartTime(Visit visit, EventStatus eventStatus, User user);

    @Modifying
    @Transactional
    @Query("UPDATE Event u SET u.eventStatus = ?1 WHERE u.visit=?3 AND u.eventStatus = ?2")
    void setEventStatusFor(EventStatus newEventStatus, EventStatus oldEventStatus, Visit visit);

    @Modifying
    @Transactional
    @Query("DELETE FROM Event u WHERE u.visit = ?1")
    void deleteEventsByVisit(Visit visit);

    List<Event> findByOrderByEventDateAscStartTimeAsc();

    @Transactional
    @Query("SELECT e FROM Event e WHERE e.user = ?1 ORDER BY e.eventDate, e.startTime")
    List<Event> findByUserByOrderByEventDateStartTime(User user);

    List<Event> findByVisitOrderByEventDateAscStartTimeAsc(Visit visit);

}

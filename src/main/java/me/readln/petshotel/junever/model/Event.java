package me.readln.petshotel.junever.model;

import javax.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.sql.Time;

@Data
@Entity
@Table(name = "events")
public class Event {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column (name = "description")
    private String eventDescription;

    @Column (name = "event_date")
    private java.sql.Date eventDate = new Date(System.currentTimeMillis());

    @Column (name = "start_time")
    private Time startTime = new Time(System.currentTimeMillis());

    @Transient
    private String goTime;

    @Column (name = "notes")
    private String eventNotes;

    @Column (name = "status")
    private EventStatus eventStatus = EventStatus.AWAIT;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_visits")
    private Visit visit;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_users")
    private User user;

}

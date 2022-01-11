package me.readln.petshotel.junever.model;

import javax.persistence.*;
import lombok.Data;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Collection;

@Data
@Entity
@Table (name = "visits")
public class Visit {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private long id;

    @Column (name = "pet_name")
    private String petName;

    @Column (name = "box_number")
    private String boxNumber;

    @Column (name = "checkInDate")
    private Date checkInDate = new Date(System.currentTimeMillis());

    //TODO: check_in time

    @Column (name = "checkOutDate")
    private Date checkOutDate = new Date(System.currentTimeMillis());

    //TODO: check_out time

    @Column (name = "activeStatus")
    private boolean activeStatus;

    @Column (name = "owner_name")
    private String ownerName;

    @Column (name = "owner_contact")
    private String ownerContact;

    @Column (name = "notes")
    private String visitNotes;

    @OneToMany
    private Collection<Event> events = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_users")
    private User user;

}

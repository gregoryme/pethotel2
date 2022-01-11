package me.readln.petshotel.junever.repo;

import me.readln.petshotel.junever.model.User;
import me.readln.petshotel.junever.model.Visit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface VisitRepository extends JpaRepository<Visit, Long> {

    List<Visit> findByActiveStatusTrue();

    List<Visit> findByOrderByCheckInDateAsc(); // all in order

    @Transactional
    @Query("SELECT v FROM Visit v WHERE v.user = ?1 ORDER BY v.checkInDate")
    List<Visit> findByOrderByCheckInDateAndByUser(User user);

    @Transactional
    @Query("SELECT v FROM Visit v WHERE v.activeStatus = true ORDER BY v.checkInDate")
    List<Visit> findByOrderAndActiveStatus();

    @Transactional
    @Query("SELECT v FROM Visit v WHERE v.user = ?1 AND v.activeStatus = true ORDER BY v.checkInDate")
    List<Visit> findByOrderAndActiveStatusAndByUser(User user);

}

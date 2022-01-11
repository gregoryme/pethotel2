package me.readln.petshotel.junever.service;

import me.readln.petshotel.junever.model.User;
import me.readln.petshotel.junever.model.Visit;
import me.readln.petshotel.junever.repo.VisitRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class VisitService {

    @Autowired
    private VisitRepository visitRepository;

    public List<Visit> getAllVisits() {
        return visitRepository.findAll();
    }

    public List<Visit> getActiveVisits() {
        return visitRepository.findByActiveStatusTrue();
    }

    public void saveVisit(Visit visit) {
        this.visitRepository.save(visit);
    }

    public Visit getVisitById(long id) {
        Optional<Visit> optional = visitRepository.findById(id);
        Visit visit = null;
        if (optional.isPresent()) {
            visit = optional.get();
        } else {
            throw new RuntimeException("Visit not found, visit's id = " + id);
        }
        return visit;
    }

    public void deleteVisitById(long id) {
        this.visitRepository.deleteById(id);
    }

    public List<Visit> getActiveVisitsInOrderByCheckInDate() {
        return visitRepository.findByOrderAndActiveStatus();
    }

    public List<Visit> getActiveVisitsInOrderByCheckInDateAndByUser(User user) {
        return visitRepository.findByOrderAndActiveStatusAndByUser(user);
    }

    public List<Visit> getAllVisitsInOrderByCheckInDate() {
        return visitRepository.findByOrderByCheckInDateAsc();
    }

    public List<Visit> getAllVisitsInOrderByCheckInDateAndByUser(User user) {
        return visitRepository.findByOrderByCheckInDateAndByUser(user);
    }

}

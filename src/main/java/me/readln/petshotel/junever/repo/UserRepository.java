package me.readln.petshotel.junever.repo;

import me.readln.petshotel.junever.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
    User findByUsername(String username);
}

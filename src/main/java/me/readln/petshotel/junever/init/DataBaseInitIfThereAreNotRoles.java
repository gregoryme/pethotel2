package me.readln.petshotel.junever.init;

import me.readln.petshotel.junever.etc.Constant;
import me.readln.petshotel.junever.model.Role;
import me.readln.petshotel.junever.model.User;
import me.readln.petshotel.junever.repo.RoleRepository;
import me.readln.petshotel.junever.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.List;

@Component
public class DataBaseInitIfThereAreNotRoles {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserService userService;

    @PostConstruct
    private void postConstruct() {
        List<Role> roleList = roleRepository.findAll();
        if (roleList.isEmpty()) {

            // probably, db is empty

            Role role_user = new Role();
            role_user.setName("ROLE_USER");
            Role role_admin = new Role();
            role_admin.setName("ROLE_ADMIN");
            roleRepository.save(role_user);
            roleRepository.save(role_admin);
            System.out.println("PETS HOTEL @PostConstruct: Role's Database is initialized");

            User user = new User();
            user.setUsername(Constant.ADMIN_USER_NAME);
            user.setPassword(Constant.ADMIN_PASSWORD);
            user.setMagic(0);

            if (userService.saveAdminUser(user)) {
                System.out.println("PETS HOTEL @PostConstruct: Admin user is initialized");
            } else {
                System.out.println("*** ERROR! PETS HOTEL @PostConstruct: Admin is not initialized");
            }
        }
    }
}

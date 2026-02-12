package ru.suleymanov.controller.secured;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.suleymanov.entity.User;
import ru.suleymanov.entity.UserRole;
import ru.suleymanov.service.UserService;

import java.util.Optional;

@Controller
@Transactional
@RequestMapping("/super-admin")

public class PrivateSuperAdminController {

    private final UserService userService;

    @Autowired
    public PrivateSuperAdminController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/make-user-admin")
    public String makeUserAdmin(@RequestParam long id) {
        Optional<User> userToBeUpgradeOptional = userService.findById(id);
        if (userToBeUpgradeOptional.isEmpty()) {
            return "redirect:/admin";
        }

        User userToBeUpgraded = userToBeUpgradeOptional.get();
        if (userToBeUpgraded.isSimpleSuperAdminRole()) {
            return "redirect:/admin";
        }
        userService.updateUserRole(id, UserRole.ADMIN);
        return "redirect:/admin";
    }

}

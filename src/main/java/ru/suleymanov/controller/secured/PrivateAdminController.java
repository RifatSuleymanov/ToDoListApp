package ru.suleymanov.controller.secured;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.suleymanov.entity.User;
import ru.suleymanov.entity.UserRole;
import ru.suleymanov.service.UserService;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/admin")
public class PrivateAdminController {

    private final UserService userService;

    @Autowired
    public PrivateAdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String getManagementPage(Model model) {
        User user = userService.getCurrentUser();

        model.addAttribute("userName", user.getName());
        if (user.isSimpleSuperAdminRole()) {
            List<User> candidatesToDelete = userService.findAllByUserRoleIn(Arrays.asList(UserRole.USER, UserRole.ADMIN));
            List<User> candidatesToUpgrade = candidatesToDelete.stream()
                    .filter(User::isSimpleUserRole)
                    .toList();
            model.addAttribute("candidatesToDelete", candidatesToDelete);
            model.addAttribute("candidatesToUpgrade", candidatesToUpgrade);
        } else {
            List<User> candidatesToDelete = userService.findAllByUserRoleIn(Collections.singleton(UserRole.USER));
            model.addAttribute("candidatesToDelete", candidatesToDelete);
        }

        return "private/admin/management-page";
    }

    @PostMapping("/delete-user")
    public String deleteUser(@RequestParam Long id) {
        User currentUser = userService.getCurrentUser();
        Optional<User> userToBeDeletedOptional = userService.findById(id);

        if (userToBeDeletedOptional.isEmpty()) {
            return "redirect:/admin";
        } 
        
        User userToBeDeleted = userToBeDeletedOptional.get();
        
        if (userToBeDeleted.isSimpleAdminRole()) {
            return "redirect:/admin";
        } else if (userToBeDeleted.isSimpleAdminRole() && !currentUser.isSimpleSuperAdminRole()) {
            return "redirect:/admin";
        }
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}

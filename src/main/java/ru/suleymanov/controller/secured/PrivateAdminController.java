package ru.suleymanov.controller.secured;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class PrivateAdminController {

    @GetMapping
    public String getManagementPage() {
        return "private/admin/management-page";
    }
}

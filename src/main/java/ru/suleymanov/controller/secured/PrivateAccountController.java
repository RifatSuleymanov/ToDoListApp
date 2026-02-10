package ru.suleymanov.controller.secured;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.suleymanov.entity.RecordStatus;
import ru.suleymanov.entity.dto.RecordsContainerDto;
import ru.suleymanov.service.RecordService;


@Controller()
@RequestMapping("/account")
public class PrivateAccountController {

    private final RecordService recordService;


    @Autowired
    public PrivateAccountController(RecordService recordService) {
        this.recordService = recordService;
    }

    @GetMapping("")
    public String getMainPage(Model model, @RequestParam(name = "filter", required = false) String filterMode) {
        RecordsContainerDto container = recordService.findAllRecords(filterMode);
        model.addAttribute("records", container.getRecords());
        model.addAttribute("numberOfDoneRecords", container.getNumberOfDoneRecords());
        model.addAttribute("numberOfActiveRecords", container.getNumberOfActiveRecords());
        return "private/account-page";
    }

    @PostMapping("/add-record")
    public String addRecord(@RequestParam("title") String title) {
        recordService.saveRecord(title);
        return "redirect:/account";
    }

    @PostMapping( "/make-record-done")
    public String makeRecordDone(@RequestParam int id,
                                 @RequestParam(name = "filter", required = false) String filterMode) {
        recordService.updateRecordStatus(id, RecordStatus.DONE);
        return "redirect:/account" + (filterMode != null && !filterMode.isBlank() ? "?filter=" + filterMode : "");
    }

    @PostMapping( "/delete-record")
    public String deleteRecord(@RequestParam int id,
                               @RequestParam(name = "filter", required = false) String filterMode) {
        recordService.deleteRecord(id);
        return "redirect:/account" + (filterMode != null && !filterMode.isBlank() ? "?filter=" + filterMode : "");
    }

}

package ru.suleymanov.controller;

import ru.suleymanov.entity.RecordStatus;
import ru.suleymanov.entity.dto.QueryParameters;
import ru.suleymanov.entity.dto.RecordsContainerDto;
import ru.suleymanov.service.RecordService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class CommandController {

    private final RecordService recordService;

    @Autowired
    public CommandController(RecordService recordService) {
        this.recordService = recordService;
        System.out.println("CommandController initialized");
    }

    @RequestMapping({"", "/"})
    public String redirectToHomePage(HttpServletRequest request) {
        System.out.println("redirectToHomePage log");
        return "redirect:" + "/home";
    }

    @RequestMapping("/home")
    public String getMainPage(Model model, @RequestParam(name = "filter", required = false) String filterMode) {
        System.out.println("getMainPage log");
        System.out.println("getMainPageHome log");
        RecordsContainerDto container = recordService.findAllRecords(filterMode);
        model.addAttribute("records", container.getRecords());
        model.addAttribute("numberOfDoneRecords", container.getNumberOfDoneRecords());
        model.addAttribute("numberOfActiveRecords", container.getNumberOfActiveRecords());
        return "main-page";
    }

    @RequestMapping(value = "/add-record", method = RequestMethod.POST)
    public String addRecord(@RequestParam("title") String title) {
        System.out.println("addRecord log");
        recordService.saveRecord(title);
        return "redirect:/home";
    }

    @RequestMapping(value = "/make-record-done", method = RequestMethod.POST)
    public String makeRecordDone(QueryParameters parameters) {
        System.out.println("makeRecordDone log");
        recordService.updateRecordStatus(parameters.getId(), RecordStatus.DONE);
        return "redirect:/home" + (parameters.getFilter() != null && !parameters.getFilter().isBlank() ? "?filter=" + parameters.getFilter() : "");
    }

    @RequestMapping(value = "/delete-record", method = RequestMethod.POST)
    public String deleteRecord(QueryParameters parameters) {
        System.out.println("deleteRecord log");
        recordService.deleteRecord(parameters.getId());
        return "redirect:/home" + (parameters.getFilter() != null && !parameters.getFilter().isBlank() ? "?filter=" + parameters.getFilter() : "");
    }
}

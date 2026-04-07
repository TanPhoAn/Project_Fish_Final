package com.phuoctan.controller;


import com.phuoctan.entity.customerRequest;
import com.phuoctan.service.customerRequestService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class ContactController {
    private final customerRequestService customerRequestService;

    public ContactController(customerRequestService customerRequestService) {
        this.customerRequestService = customerRequestService;
    }

    @GetMapping("/contact")
    public String contactPage(Model model) {
        customerRequest request  = new customerRequest();
        model.addAttribute("requestForm",request);
        return "page/contact";
    }

    @PostMapping("/contact/request-sending")
    public String sendRequest(@ModelAttribute("requestForm") customerRequest request, Model model) {
    model.addAttribute("requestForm",request);

    customerRequestService.saveRequest(request);

    return "redirect:/page/request-confirmation";
    }

    @GetMapping("/page/request-confirmation")
    public String requestConfirmationPage() {

        return "/page/request-confirmation";
    }


}

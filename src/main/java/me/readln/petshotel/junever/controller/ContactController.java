package me.readln.petshotel.junever.controller;

import me.readln.petshotel.junever.etc.Constant;
import me.readln.petshotel.junever.etc.ContactMessage;
import me.readln.petshotel.junever.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import java.util.Date;

@Controller
public class ContactController {

    @Autowired
    private UserService userService;

    @Autowired
    public JavaMailSender emailSender;

    // contact page
    @GetMapping("/contact")
    public String userList(Model model) {
        ContactMessage contactMessage = new ContactMessage();
        contactMessage.setUserName(userService.getCurrentUser().getUsername());
        model.addAttribute("message", contactMessage);
        model.addAttribute("today", new Date());
        model.addAttribute("welcome", "");
        return "contact";
    }

    // message (in form of email) sender controller
    @PostMapping("/sendMessage")
    public String sendMessage(@ModelAttribute("message") ContactMessage contactMessage, Model model) {

        if (isEmailAddressValid(contactMessage.getEmail())) {

            SimpleMailMessage message = new SimpleMailMessage();

            message.setTo(Constant.EMAIL_FOR_MESSAGES_FROM_SITE);
            message.setFrom(contactMessage.getEmail());
            message.setSubject("Message from web-site of the Pet Friends Hotel");

            String text = "Message body:\n\n\n" +
                    contactMessage.getText() +
                    "\n\n=======================================\n" +
                    "Sent from: \n" +
                    "Name: " + contactMessage.getName() + "\n" +
                    "Username on site: " + contactMessage.getUserName() + "\n" +
                    "Email: " + contactMessage.getEmail() + "\n" +
                    "=======================================";

            message.setText(text);
            this.emailSender.send(message);
        }

        // clear "form" before "Thanks"
        contactMessage.setEmail("");
        contactMessage.setText("");
        contactMessage.setName("");
        model.addAttribute("message", contactMessage);
        // "Thanks"
        model.addAttribute("welcome", "Thank you!");

        return "contact";
    }

    private boolean isEmailAddressValid(String email) {
        boolean result = true;
        try {
            InternetAddress emailAddress = new InternetAddress(email);
            emailAddress.validate();
        } catch (AddressException ex) {
            result = false;
        }
        return result;
    }
}

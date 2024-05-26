package com.example.RailingShop.Controllers;

import com.example.RailingShop.Entities.User;
import com.example.RailingShop.Services.UserService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
//---- ИМА address_up.html обаче не сработи (препраща ме в /shop/all (Проблема е че след като се логна нищо не проверява дали съм логнат), вписано е в SecurityConfig

@Controller
@RequestMapping("/shop")
public class AccountController {

    @Autowired
    private UserService userService;

    @GetMapping("/accountDetails")
    public String accountDetails(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            return "address_up";
        } else {
            redirectAttributes.addFlashAttribute("message", "You are not logged in");
            return "redirect:/shop/all";
        }
    }

    @PostMapping("/updateAddress")
    public String saveUserForm(@Valid User user, BindingResult bindingResult, Model model, HttpSession session) {

        if (bindingResult.hasErrors()) {
            return "address_up";
        }
        userService.updateUserInformation(user);
        User updatedUser = userService.findById(user.getId());
        session.setAttribute("user", updatedUser);
        model.addAttribute("message", "Your information has been updated successfully!!!");
        return "address_up";
    }
}



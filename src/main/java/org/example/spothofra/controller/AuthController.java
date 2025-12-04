package org.example.spothofra.controller;

import lombok.RequiredArgsConstructor;
import org.example.spothofra.entity.Utilisateur;
import org.example.spothofra.service.UtilisateurService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UtilisateurService utilisateurService;

    @GetMapping("/login")
    public String loginPage() {
        return "auth/login"; // templates/auth/login.html
    }

    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("utilisateur", new Utilisateur());
        return "auth/register"; // templates/auth/register.html
    }

    @PostMapping("/register")
    public String handleRegister(
            @ModelAttribute("utilisateur") Utilisateur utilisateur,
            BindingResult bindingResult,
            RedirectAttributes redirectAttributes
    ) {
        if (bindingResult.hasErrors()) {
            return "auth/register";
        }

        try {
            // register citoyen by default
            utilisateurService.registerCitizen(utilisateur);
            redirectAttributes.addFlashAttribute("successMessage", "Compte créé avec succès. Vous pouvez vous connecter.");
            return "redirect:/login";
        } catch (IllegalArgumentException ex) {
            bindingResult.rejectValue("email", "email.exists", ex.getMessage());
            return "auth/register";
        }
    }
}

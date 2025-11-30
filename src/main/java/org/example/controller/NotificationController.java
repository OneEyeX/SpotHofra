package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Notification;
import org.example.entity.Utilisateur;
import org.example.service.NotificationService;
import org.example.service.UtilisateurService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@RequestMapping("/notifications")
public class NotificationController {

    private final NotificationService notificationService;
    private final UtilisateurService utilisateurService;

    private Utilisateur getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return utilisateurService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Utilisateur courant introuvable pour email " + email));
    }

    @GetMapping
    public String listNotifications(
            Model model,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Utilisateur current = getCurrentUser();
        Page<Notification> page = notificationService.getNotificationsUtilisateur(current.getId(), pageable);

        model.addAttribute("page", page);
        model.addAttribute("notifications", page.getContent());
        return "notification/list"; // templates/notification/list.html
    }

    @PostMapping("/{id}/lu")
    public String markAsRead(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        notificationService.marquerCommeLue(id);
        redirectAttributes.addFlashAttribute("successMessage", "Notification marquée comme lue.");
        return "redirect:/notifications";
    }
}

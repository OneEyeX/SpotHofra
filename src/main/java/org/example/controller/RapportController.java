package org.example.controller;

import lombok.RequiredArgsConstructor;
import org.example.entity.Rapport;
import org.example.entity.Utilisateur;
import org.example.service.RapportService;
import org.example.service.UtilisateurService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;
import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin/rapports")
public class RapportController {

    private final RapportService rapportService;
    private final UtilisateurService utilisateurService;

    private Utilisateur getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return utilisateurService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Utilisateur courant introuvable pour email " + email));
    }

    @GetMapping
    public String listRapports(Model model) {
        Utilisateur admin = getCurrentUser();
        List<Rapport> rapports = rapportService.findRapportsByAuteur(admin.getId());
        model.addAttribute("rapports", rapports);
        return "rapport/list"; // templates/rapport/list.html
    }

    @GetMapping("/nouveau")
    public String showRapportForm() {
        return "rapport/form"; // templates/rapport/form.html
    }

    @PostMapping
    public String generateRapport(
            @RequestParam("debut") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate debut,
            @RequestParam("fin") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate fin,
            RedirectAttributes redirectAttributes
    ) {
        Utilisateur admin = getCurrentUser();
        rapportService.genererRapport(debut, fin, admin.getId());

        redirectAttributes.addFlashAttribute("successMessage", "Rapport généré avec succès.");
        return "redirect:/admin/rapports";
    }

    @GetMapping("/{id}")
    public String viewRapport(@PathVariable Long id, Model model) {
        Rapport rapport = rapportService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Rapport introuvable avec id " + id));
        model.addAttribute("rapport", rapport);
        return "rapport/detail"; // templates/rapport/detail.html
    }
}

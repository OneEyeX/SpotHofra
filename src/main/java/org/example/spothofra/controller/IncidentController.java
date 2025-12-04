package org.example.spothofra.controller;

import lombok.RequiredArgsConstructor;
import org.example.spothofra.entity.CategorieIncident;
import org.example.spothofra.entity.Incident;
import org.example.spothofra.entity.Quartier;
import org.example.spothofra.entity.Utilisateur;
import org.example.spothofra.enums.StatutIncident;
import org.example.spothofra.service.IncidentService;
import org.example.spothofra.service.UtilisateurService;
import org.example.spothofra.repository.CategorieIncidentRepository;
import org.example.spothofra.repository.QuartierRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PageableDefault;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequiredArgsConstructor
@RequestMapping("/incidents")
public class IncidentController {

    private final IncidentService incidentService;
    private final UtilisateurService utilisateurService;
    private final CategorieIncidentRepository categorieIncidentRepository;
    private final QuartierRepository quartierRepository;

    // Helper: get current logged Utilisateur
    private Utilisateur getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String email = auth.getName();
        return utilisateurService.findByEmail(email)
                .orElseThrow(() -> new IllegalStateException("Utilisateur courant introuvable pour email " + email));
    }

    /* ================= CITOYEN ================= */

    @GetMapping("/citoyen")
    public String listIncidentsCitoyen(
            Model model,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Utilisateur current = getCurrentUser();
        Page<Incident> page = incidentService.listerIncidentsCitoyen(current.getId(), pageable);

        model.addAttribute("page", page);
        model.addAttribute("incidents", page.getContent());
        return "incident/citoyen-list"; // templates/incident/citoyen-list.html
    }

    @GetMapping("/citoyen/nouveau")
    public String showNewIncidentForm(Model model) {
        List<CategorieIncident> categories = categorieIncidentRepository.findAll();
        List<Quartier> quartiers = quartierRepository.findAll();

        model.addAttribute("incident", new Incident());
        model.addAttribute("categories", categories);
        model.addAttribute("quartiers", quartiers);
        return "incident/citoyen-form"; // templates/incident/citoyen-form.html
    }

    @PostMapping("/citoyen")
    public String createIncident(
            @ModelAttribute("incident") Incident incident,
            @RequestParam("categorieId") Long categorieId,
            @RequestParam("quartierId") Long quartierId,
            RedirectAttributes redirectAttributes
    ) {
        Utilisateur current = getCurrentUser();

        incidentService.signalerIncident(
                incident,
                current.getId(),
                categorieId,
                quartierId
        );

        redirectAttributes.addFlashAttribute("successMessage", "Incident signalé avec succès.");
        return "redirect:/incidents/citoyen";
    }

    /* ================= DETAIL INCIDENT ================= */

    @GetMapping("/{id}")
    public String viewIncident(@PathVariable Long id, Model model) {
        Incident incident = incidentService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Incident introuvable avec id " + id));

        model.addAttribute("incident", incident);
        model.addAttribute("historique", incident.getHistorique());
        model.addAttribute("photos", incident.getPhotos());
        return "incident/detail"; // templates/incident/detail.html
    }

    /* ================= AGENT ================= */

    @GetMapping("/agent")
    public String listIncidentsAgent(
            Model model,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        Utilisateur agent = getCurrentUser();
        Page<Incident> page = incidentService.listerIncidentsAgent(agent.getId(), pageable);

        model.addAttribute("page", page);
        model.addAttribute("incidents", page.getContent());
        return "incident/agent-list"; // templates/incident/agent-list.html
    }

    @PostMapping("/{id}/assigner")
    public String assignToCurrentAgent(
            @PathVariable Long id,
            RedirectAttributes redirectAttributes
    ) {
        Utilisateur agent = getCurrentUser();

        incidentService.assignerAgent(id, agent.getId());
        redirectAttributes.addFlashAttribute("successMessage", "Incident assigné à vous avec succès.");

        return "redirect:/incidents/agent";
    }

    @PostMapping("/{id}/statut")
    public String changeStatut(
            @PathVariable Long id,
            @RequestParam("nouveauStatut") StatutIncident nouveauStatut,
            @RequestParam(name = "commentaire", required = false) String commentaire,
            RedirectAttributes redirectAttributes
    ) {
        Utilisateur acteur = getCurrentUser();

        incidentService.changerStatut(id, nouveauStatut, acteur.getId(), commentaire);
        redirectAttributes.addFlashAttribute("successMessage", "Statut de l'incident mis à jour.");

        return "redirect:/incidents/" + id;
    }

    /* ================= FILTRE SIMPLE PAR STATUT (ADMIN OU AGENT) ================= */

    @GetMapping("/filtre")
    public String filterByStatut(
            @RequestParam(name = "statut", required = false) StatutIncident statut,
            Model model,
            @PageableDefault(size = 10) Pageable pageable
    ) {
        if (statut == null) {
            statut = StatutIncident.SIGNALE;
        }
        Page<Incident> page = incidentService.rechercherParStatut(statut, pageable);

        model.addAttribute("page", page);
        model.addAttribute("incidents", page.getContent());
        model.addAttribute("statutSelectionne", statut);
        model.addAttribute("statuts", StatutIncident.values());

        return "incident/liste-filtre"; // templates/incident/liste-filtre.html
    }
}

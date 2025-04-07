package com.example.reclamationweb.Controller;

import com.example.reclamationweb.Entities.Reclamation;
import com.example.reclamationweb.Service.ReclamationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/reclamations")
public class ReclamationController {

    @Autowired
    private ReclamationService reclamationService;

    @PostMapping
    public Reclamation addReclamation(@RequestBody Reclamation reclamation) {
        return reclamationService.addReclamation(reclamation);
    }

    @GetMapping
    public List<Reclamation> getAllReclamations() {
        return reclamationService.getAllReclamations();
    }

    @GetMapping("/{id}")
    public Optional<Reclamation> getReclamationById(@PathVariable Long id) {
        return reclamationService.getReclamationById(id);
    }

    @PutMapping("/{id}")
    public Reclamation updateReclamation(@PathVariable Long id, @RequestBody Reclamation updatedReclamation) {
        return reclamationService.updateReclamation(id, updatedReclamation);
    }

    @DeleteMapping("/{id}")
    public void deleteReclamation(@PathVariable Long id) {
        reclamationService.deleteReclamation(id);
    }
}



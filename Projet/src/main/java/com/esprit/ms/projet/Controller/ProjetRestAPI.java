package com.esprit.ms.projet.Controller;

import com.esprit.ms.projet.Entity.Projet;
import com.esprit.ms.projet.Repository.ProjetRepository;
import com.esprit.ms.projet.Service.ProjetService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/projets")
@RequiredArgsConstructor
public class ProjetRestAPI {
    @Autowired
    private ProjetRepository projetRepository;
    @Autowired
    private ProjetService projetService;


    @GetMapping
    public List<Projet> getAllProjects() {
        return projetService.getAllProjects();
    }


    @GetMapping("/{id}")
    public Projet getProjectById(@PathVariable Long id) {
        return projetService.getProjectById(id);
    }


    @PostMapping
    public Projet createProject(@RequestBody Projet project) {
        return projetService.createProject(project);
    }


    @PutMapping("/{id}")
    public Projet updateProject(@PathVariable Long id, @RequestBody Projet updatedProject) {
        return projetService.updateProject(id, updatedProject);
    }


    @DeleteMapping("/{id}")
    public void deleteProject(@PathVariable Long id) {
        projetService.deleteProject(id);
    }

    @PostMapping("/{id}/upload")
    public ResponseEntity<String> uploadFile(@PathVariable Long id, @RequestParam("file") MultipartFile file) {
        try {
            projetService.storeFile(id, file);
            return ResponseEntity.ok("File uploaded successfully for project ID: " + id);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
    }

    @Value("${welcome.message}")
    private String welcomeMessage;
    @GetMapping("/welcome")
    public String welcome() {
        return welcomeMessage;
    }

}

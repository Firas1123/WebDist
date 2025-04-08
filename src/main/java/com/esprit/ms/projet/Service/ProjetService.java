package com.esprit.ms.projet.Service;

import com.esprit.ms.projet.Controller.ProjetRestAPI;
import com.esprit.ms.projet.Entity.Projet;
import com.esprit.ms.projet.Repository.ProjetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
@Service
@RequiredArgsConstructor
public class ProjetService {
    @Autowired
    private ProjetRepository projetRepository;
    @Autowired
    private EmailService emailService;


    public List<Projet> getAllProjects() {
        return projetRepository.findAll();
    }


    public Projet getProjectById(Long id) {
        return projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));
    }


    public Projet createProject(Projet projet) {
        Projet saved = projetRepository.save(projet);
        emailService.sendProjectCreationEmail("regmahdi2002@gmail.com", "Mahdi", saved.getTitle());
        return saved;
    }


    public Projet updateProject(Long id, Projet updatedProject) {
        Projet project = projetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + id));

        project.setTitle(updatedProject.getTitle());
        project.setDescription(updatedProject.getDescription());
        project.setStartDate(updatedProject.getStartDate());
        project.setEndDate(updatedProject.getEndDate());
        project.setUserId(updatedProject.getUserId());

        return projetRepository.save(project);
    }


    public void deleteProject(Long id) {
        projetRepository.deleteById(id);
    }

    public void storeFile(Long projetId, MultipartFile file) throws IOException {
        Projet projet = projetRepository.findById(projetId)
                .orElseThrow(() -> new RuntimeException("Project not found with id: " + projetId));

        String uploadDir = "uploads/" + projetId;
        File dir = new File(uploadDir);
        if (!dir.exists()) dir.mkdirs();

        Path filePath = Paths.get(uploadDir, file.getOriginalFilename());
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        System.out.println(" File saved to: " + filePath.toAbsolutePath());
    }

}

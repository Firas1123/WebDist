package com.example.reclamationweb.Repository;

import com.example.reclamationweb.Entities.Reclamation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ReclamationRepository extends JpaRepository<Reclamation, Long> {
    List<Reclamation> findByStatus(String status);
}

package com.ms.candidat.msmeeting.Repository;

import com.ms.candidat.msmeeting.Entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}

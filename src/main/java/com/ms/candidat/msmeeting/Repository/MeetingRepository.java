package com.ms.candidat.msmeeting.Repository;

import com.ms.candidat.msmeeting.Entities.Meeting;
import com.ms.candidat.msmeeting.Entities.MeetingCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MeetingRepository extends JpaRepository<Meeting, Long> {
    @Query("SELECT m FROM Meeting m WHERE m.category = :category")
    List<Meeting> findByCategory(@Param("category") MeetingCategory category);
    List<Meeting> findAllByOrderByStartDateTimeAsc();
    List<Meeting> findAllByOrderByStartDateTimeDesc();


}

package com.ms.candidat.msmeeting.Services;

import com.ms.candidat.msmeeting.Entities.Meeting;
import com.ms.candidat.msmeeting.Entities.MeetingCategory;
import com.ms.candidat.msmeeting.Repository.MeetingRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@EnableAsync
public class MeetingService implements IMeetingService {
    @Autowired
    private MeetingRepository meetingRepository;

    @Override
    public List<Meeting> getAllMeetings() {
        return meetingRepository.findAll();
    }

    @Override
    public Meeting getMeetingById(long id) {
        return meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found with ID: " + id));
    }

    @Override
    public Meeting addMeeting(Meeting meeting) {
        // Setting createdAt when a meeting is created
        meeting.setCreatedAt(LocalDateTime.now());
        // Save meeting with supervisorId and internIds already included in the object
        return meetingRepository.save(meeting);
    }

    @Override
    public Meeting updateMeeting(Meeting meeting, long id) {
        Meeting existingMeeting = meetingRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Meeting not found with id: " + id));

        // Update fields with provided values
        if (meeting.getTitle() != null) {
            existingMeeting.setTitle(meeting.getTitle());
        }
        if (meeting.getLocation() != null) {
            existingMeeting.setLocation(meeting.getLocation());
        }
        if (meeting.getDescription() != null) {
            existingMeeting.setDescription(meeting.getDescription());
        }
        if (meeting.getType() != null) {
            existingMeeting.setType(meeting.getType());
        }
        if (meeting.getCategory() != null) {
            existingMeeting.setCategory(meeting.getCategory());
        }
        if (meeting.getStartDateTime() != null) {
            existingMeeting.setStartDateTime(meeting.getStartDateTime());
        }
        if (meeting.getEndDateTime() != null) {
            existingMeeting.setEndDateTime(meeting.getEndDateTime());
        }

        // No need to pass supervisorId or internIds separately, these are part of the meeting object
        if (meeting.getSupervisorId() != null) {
            existingMeeting.setSupervisorId(meeting.getSupervisorId());
        }
        if (meeting.getInternIds() != null && !meeting.getInternIds().isEmpty()) {
            existingMeeting.setInternIds(meeting.getInternIds());
        }

        return meetingRepository.save(existingMeeting);
    }

    @Override
    public void deleteMeeting(long id) {
        meetingRepository.findById(id).ifPresentOrElse(
                meetingRepository::delete,
                () -> { throw new EntityNotFoundException("Meeting not found with ID: " + id); }
        );
    }

    @Override
    public List<Meeting> getMeetingsByCategory(MeetingCategory category) {
        return meetingRepository.findByCategory(category);
    }

    @Override
    public List<Meeting> getMeetingsSortedByStartDateTime(boolean ascending) {
        return ascending
                ? meetingRepository.findAllByOrderByStartDateTimeAsc()
                : meetingRepository.findAllByOrderByStartDateTimeDesc();
    }
}

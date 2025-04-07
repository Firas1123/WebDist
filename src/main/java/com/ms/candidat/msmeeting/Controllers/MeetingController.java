package com.ms.candidat.msmeeting.Controllers;

import com.ms.candidat.msmeeting.Entities.Meeting;
import com.ms.candidat.msmeeting.Entities.MeetingCategory;
import com.ms.candidat.msmeeting.Services.IMeetingService;
import jakarta.persistence.EntityNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("web/Meeting")
public class MeetingController {
    @Autowired
    private IMeetingService meetingService;

    // Get All Meetings
    @GetMapping
    public ResponseEntity<List<Meeting>> getAllMeetings() {
        List<Meeting> Meetings = meetingService.getAllMeetings();
        return ResponseEntity.ok(Meetings);
    }
    //  GET Meeting by ID
    @GetMapping("/{id}")
    public ResponseEntity<Meeting> getMeetingById(@PathVariable long id) {
        try {
            Meeting meeting = meetingService.getMeetingById(id);
            return ResponseEntity.ok(meeting);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
    // POST (Create) a new meeting
    @PostMapping
    public ResponseEntity<Meeting> addMeeting(
            @RequestBody Meeting meeting,
            @RequestParam Long supervisorId,
            @RequestParam List<Long> internIds) {

        Meeting createdMeeting = meetingService.addMeeting(meeting, supervisorId, internIds);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMeeting);
    }

    @PutMapping("update/{id}")
    public ResponseEntity<Meeting> updateMeeting(
            @RequestBody Meeting meeting,
            @PathVariable long id,
            @RequestParam Long supervisorId,
            @RequestParam List<Long> internIds) {

        try {
            Meeting updatedMeeting = meetingService.updateMeeting(meeting, id, supervisorId, internIds);
            return ResponseEntity.ok(updatedMeeting);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // DELETE an Meeting
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable long id) {
        try {
            meetingService.deleteMeeting(id);
            return ResponseEntity.noContent().build();  // 204 No Content (successful deletion)
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }
    @GetMapping("/category/{category}")
    public List<Meeting> getMeetingsByCategory(@PathVariable MeetingCategory category) {
        return meetingService.getMeetingsByCategory(category);
    }
    @GetMapping("/sorted-by-start")
    public ResponseEntity<List<Meeting>> getMeetingsSorted(
            @RequestParam(defaultValue = "true") boolean ascending) {
        List<Meeting> sortedMeetings = meetingService.getMeetingsSortedByStartDateTime(ascending);
        return ResponseEntity.ok(sortedMeetings);
    }










}

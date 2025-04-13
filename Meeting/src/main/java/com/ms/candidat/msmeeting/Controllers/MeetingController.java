package com.ms.candidat.msmeeting.Controllers;

import com.ms.candidat.msmeeting.Entities.Meeting;
import com.ms.candidat.msmeeting.Entities.MeetingCategory;
import com.ms.candidat.msmeeting.Services.IMeetingService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("web/Meeting")
public class MeetingController {

    private final IMeetingService meetingService;

    // ✅ Explicit constructor injection (no Lombok needed)
    public MeetingController(IMeetingService meetingService) {
        this.meetingService = meetingService;
    }

    // GET all meetings
    @GetMapping
    public ResponseEntity<List<Meeting>> getAllMeetings() {
        return ResponseEntity.ok(meetingService.getAllMeetings());
    }

    // GET meeting by ID
    @GetMapping("/{id}")
    public ResponseEntity<Meeting> getMeetingById(@PathVariable long id) {
        try {
            return ResponseEntity.ok(meetingService.getMeetingById(id));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // POST (create) a new meeting (supervisorId and internIds are now part of the meeting object)
    @PostMapping
    public ResponseEntity<Meeting> addMeeting(@RequestBody Meeting meeting) {
        // Meeting already contains supervisorId and internIds
        Meeting createdMeeting = meetingService.addMeeting(meeting);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdMeeting);
    }

    // PUT (update) an existing meeting (supervisorId and internIds are now part of the meeting object)
    @PutMapping("update/{id}")
    public ResponseEntity<Meeting> updateMeeting(@RequestBody Meeting meeting, @PathVariable long id) {
        try {
            Meeting updatedMeeting = meetingService.updateMeeting(meeting, id);
            return ResponseEntity.ok(updatedMeeting);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // DELETE a meeting
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMeeting(@PathVariable long id) {
        try {
            meetingService.deleteMeeting(id);
            return ResponseEntity.noContent().build();
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
    }

    // GET meetings by category
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Meeting>> getMeetingsByCategory(@PathVariable MeetingCategory category) {
        return ResponseEntity.ok(meetingService.getMeetingsByCategory(category));
    }

    // GET sorted meetings by start date/time
    @GetMapping("/sorted-by-start")
    public ResponseEntity<List<Meeting>> getMeetingsSorted(@RequestParam(defaultValue = "true") boolean ascending) {
        return ResponseEntity.ok(meetingService.getMeetingsSortedByStartDateTime(ascending));
    }

    // Welcome message
    @GetMapping("/")
    public String home() {
        return "Welcome to the API!";
    }
}

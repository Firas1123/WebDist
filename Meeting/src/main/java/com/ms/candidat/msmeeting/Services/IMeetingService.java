package com.ms.candidat.msmeeting.Services;

import com.ms.candidat.msmeeting.Entities.Meeting;
import com.ms.candidat.msmeeting.Entities.MeetingCategory;

import java.util.List;

public interface IMeetingService {

    List<Meeting> getAllMeetings();

    Meeting getMeetingById(long id);

    // Updated method to accept Meeting object instead of supervisorId and internIds separately
    Meeting addMeeting(Meeting meeting);

    // Updated method to accept Meeting object instead of supervisorId and internIds separately
    Meeting updateMeeting(Meeting meeting, long id);

    void deleteMeeting(long id);

    List<Meeting> getMeetingsByCategory(MeetingCategory category);

    List<Meeting> getMeetingsSortedByStartDateTime(boolean ascending);
}

package com.ms.candidat.msmeeting.Services;

import com.ms.candidat.msmeeting.Entities.Meeting;
import com.ms.candidat.msmeeting.Entities.MeetingCategory;
import org.springdoc.core.converters.models.Sort;

import java.util.List;

public interface IMeetingService {

    public List<Meeting> getAllMeetings();
    public Meeting getMeetingById(long id);
    public Meeting addMeeting(Meeting meeting,Long supervisorId, List<Long> internId);
    public Meeting updateMeeting(Meeting meeting, long id, Long supervisorId, List<Long> internIds);
    public void deleteMeeting(long id);
    public List<Meeting> getMeetingsByCategory(MeetingCategory category);
    List<Meeting> getMeetingsSortedByStartDateTime(boolean ascending);




}

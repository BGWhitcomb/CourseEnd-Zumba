package com.courseend.zumba.dao;

import java.util.List;

import com.courseend.zumba.model.Participant;

public interface IParticipantDAO {

	void addParticipant(Participant participant);

	void updateParticipant(Participant participant);

	void deleteParticipant(int participantId);

	Participant getParticipant(int participantId);

	List<Participant> getAllParticipants();

}

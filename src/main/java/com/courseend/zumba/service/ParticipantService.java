package com.courseend.zumba.service;

import java.util.List;

import com.courseend.zumba.dao.ParticipantDAO;
import com.courseend.zumba.model.Participant;

public class ParticipantService implements IParticipantService {

	private ParticipantDAO participantDAO;

	public ParticipantService(ParticipantDAO participantDAO) {
		this.setParticipantDAO(participantDAO);
	}

	@Override
	public void addParticipant(Participant participant) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateParticipant(Participant participant) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteParticipant(int participantId) {
		// TODO Auto-generated method stub

	}

	@Override
	public Participant getParticipant(int participantId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Participant> getAllParticipants() {
		// TODO Auto-generated method stub
		return null;
	}

	public ParticipantDAO getParticipantDAO() {
		return participantDAO;
	}

	public void setParticipantDAO(ParticipantDAO participantDAO) {
		this.participantDAO = participantDAO;
	}

}
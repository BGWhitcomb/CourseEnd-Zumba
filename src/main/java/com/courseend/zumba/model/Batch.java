package com.courseend.zumba.model;

import java.time.LocalDate;
import java.util.List;

public class Batch {

	private int bid;
	private String name;
	private LocalDate scheduledOn;
	private String startTime;
	private List<Participant> participants;

	public Batch(int bid, String name, LocalDate scheduledOn, String startTime, List<Participant> participants) {
		super();
		this.bid = bid;
		this.name = name;
		this.scheduledOn = scheduledOn;
		this.startTime = startTime;
		this.participants = participants;
	}

	public Batch() {
		super();
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public LocalDate getScheduledOn() {
		return scheduledOn;
	}

	public void setScheduledOn(LocalDate date) {
		this.scheduledOn = date;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public List<Participant> getParticipants() {
		return participants;
	}

	public void setParticipants(List<Participant> participants) {
		this.participants = participants;
	}

	@Override
	public String toString() {
		return "Batch [bid=" + bid + ", name=" + name + ", scheduledOn=" + scheduledOn + ", startTime=" + startTime
				+ ", participants=" + participants + "]";
	}

}

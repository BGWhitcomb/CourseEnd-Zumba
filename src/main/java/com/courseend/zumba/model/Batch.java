package com.courseend.zumba.model;

import java.util.Date;
import java.util.List;

public class Batch {

	private int batchId;
	private String name;
	private Date scheduledOn;
	private String startTime;
	private List<Participant> participants;

	public Batch(int batchId, String name, Date scheduledOn, String startTime, List<Participant> participants) {
		super();
		this.batchId = batchId;
		this.name = name;
		this.scheduledOn = scheduledOn;
		this.startTime = startTime;
		this.participants = participants;
	}

	public Batch() {
		super();
	}

	public int getBatchId() {
		return batchId;
	}

	public void setBatchId(int batchId) {
		this.batchId = batchId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getScheduledOn() {
		return scheduledOn;
	}

	public void setScheduledOn(Date scheduledOn) {
		this.scheduledOn = scheduledOn;
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
		return "Batch [batchId=" + batchId + ", name=" + name + ", scheduledOn=" + scheduledOn + ", startTime="
				+ startTime + ", participants=" + participants + "]";
	}

}

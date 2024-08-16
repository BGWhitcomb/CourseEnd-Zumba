package com.courseend.zumba.model;

import java.time.LocalDate;

public class Participant {

	private int pid;
	private String name;
	private String phone;
	private String email;
	private int bid;

	// Inner join sql
	private String batchName;
	private LocalDate scheduledOn;
	private String startTime;

	public Participant(int pid, String name, String phone, String email, int bid) {
		super();
		this.pid = pid;
		this.name = name;
		this.phone = phone;
		this.email = email;
		this.bid = bid;
	}

	public Participant() {
		super();
	}

	public int getPid() {
		return pid;
	}

	public void setPid(int pid) {
		this.pid = pid;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public int getBid() {
		return bid;
	}

	public void setBid(int bid) {
		this.bid = bid;
	}

	public String getBatchName() {
		return batchName;
	}

	public void setBatchName(String batchName) {
		this.batchName = batchName;
	}

	public LocalDate getScheduledOn() {
		return scheduledOn;
	}

	public void setScheduledOn(LocalDate scheduledOn) {
		this.scheduledOn = scheduledOn;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@Override
	public String toString() {
		return "Participant [pid=" + pid + ", name=" + name + ", phone=" + phone + ", email=" + email + ", bid=" + bid
				+ ", batchName=" + batchName + ", scheduledOn=" + scheduledOn + ", startTime=" + startTime + "]";
	}

}

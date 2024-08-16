package com.courseend.zumba.model;

public class Participant {

	private int participantId;
	private String name;
	private String phone;
	private String email;

	public Participant(int participantId, String name, String phone, String email, int bid) {
		super();
		this.participantId = participantId;
		this.name = name;
		this.phone = phone;
		this.email = email;
	}

	public Participant() {
		super();
	}

	public int getParticipantId() {
		return participantId;
	}

	public void setParticipantId(int participantId) {
		this.participantId = participantId;
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

	@Override
	public String toString() {
		return "Participant [participantId=" + participantId + ", name=" + name + ", phone=" + phone + ", email="
				+ email + "]";
	}

}

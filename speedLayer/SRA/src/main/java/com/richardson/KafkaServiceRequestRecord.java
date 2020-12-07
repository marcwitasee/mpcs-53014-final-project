package com.richardson;

import org.apache.kafka.common.protocol.types.Field;

import java.time.LocalDateTime;

public class KafkaServiceRequestRecord {
	public KafkaServiceRequestRecord(String ownerDepartment, String createdDate,
									 String closedDate, String status, String srNumber,
									 String communityArea, String srType) {
		super();
		this.ownerDepartment = ownerDepartment;
		this.createdDate = createdDate;
		this.closedDate = closedDate;
		this.status = status;
		this.srNumber = srNumber;
		this.communityArea = communityArea;
		this.srType = srType;
	}
	public String getOwnerDepartment() {
		return ownerDepartment;
	}
	public void setOwnerDepartment(String ownerDepartment) {
		this.ownerDepartment = ownerDepartment;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String createdDate) {
		this.createdDate = createdDate;
	}
	public String getClosedDate() {
		return closedDate;
	}
	public void setClosedDate(String closedDate) {
		this.closedDate = closedDate;
	}
	public String getStatus() { return status; }
	public void setStatus(String status) { this.status = status; }
	public String getSrNumber() {
		return srNumber;
	}
	public void setSrNumber(String srNumber) {
		this.srNumber = srNumber;
	}
	public String getCommunityArea() {
		return communityArea;
	}
	public void setCommunityArea(String communityArea) {
		this.communityArea = communityArea;
	}
	public String getSrType() {
		return srType;
	}
	public void setSrType(String srType) {
		this.srType = srType;
	}
	String ownerDepartment;
	String createdDate;
	String closedDate;
	String status;
	String srNumber;
	String communityArea;
	String srType;
	@Override
	public String toString() {
		return "RV [ownerDepartment: " + ownerDepartment + " createdDate: " + createdDate
				+ " closedDate: " + closedDate + " Status: " + status + " SR Number: " + srNumber
				+ " communityArea: " + communityArea;
	}
}

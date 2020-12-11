package com.richardson;

import org.apache.kafka.common.protocol.types.Field;

import java.time.LocalDateTime;

public class KafkaServiceRequestRecord {
	public KafkaServiceRequestRecord(String ownerDepartment, String createdDate,
									 String closedDate, String status, String srNumber,
									 String communityArea, String srType, String latitude, String longitude) {
		super();
		this.ownerDepartment = ownerDepartment;
		this.createdDate = createdDate;
		this.closedDate = closedDate;
		this.status = status;
		this.srNumber = srNumber;
		this.communityArea = communityArea;
		this.srType = srType;
		this.latitude = latitude;
		this.longitude = longitude;
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
	public String getLatitude() {
		return latitude;
	}
	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
	public String getLongitude() { return longitude; }
	public void setLongitude(String longitude) { this.longitude = longitude; }
	String ownerDepartment;
	String createdDate;
	String closedDate;
	String status;
	String srNumber;
	String communityArea;
	String srType;
	String latitude;
	String longitude;
	@Override
	public String toString() {
		return "RV [ownerDepartment: " + ownerDepartment + " createdDate: " + createdDate
				+ " closedDate: " + closedDate + " Status: " + status + " SR Number: " + srNumber
				+ " communityArea: " + communityArea + " srType: " + srType + " latitude: " + latitude
				+ " longitude: " + longitude;
	}
}

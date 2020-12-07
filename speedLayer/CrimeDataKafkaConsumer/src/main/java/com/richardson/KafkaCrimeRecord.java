package com.richardson;

import com.sun.org.apache.xpath.internal.operations.Bool;
import org.apache.kafka.common.protocol.types.Field;

public class KafkaCrimeRecord {
		public KafkaCrimeRecord(String id, String date, String primaryType, //String arrest,
							String communityArea, String updatedOn) {
		super();
		this.id = id;
		this.date = date;
		this.primaryType = primaryType;
		this.communityArea = communityArea;
		this.updatedOn = updatedOn;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getPrimaryType() {
		return primaryType;
	}
	public void setPrimaryType(String primaryType) {
		this.primaryType = primaryType;
	}
	public String getCommunityArea() { return communityArea; }
	public void setCommunityArea(String communityArea) { this.communityArea = communityArea; }
	public String getUpdatedOn() { return updatedOn; }
	public void setUpdatedOn(String updatedOn) { this.updatedOn = updatedOn; }
	String id;
	String date;
	String primaryType;
	String communityArea;
	String updatedOn;
	@Override
	public String toString() {
		return "RV [id: " + id + " date: " + date + " primaryType: " + primaryType
				+ " communityArea: " + communityArea + " Updated On : " + updatedOn +"]";
	}
}

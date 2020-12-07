
package com.richardson;

import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "sr_number",
    "sr_type",
    "sr_short_code",
    "owner_department",
    "status",
    "created_date",
    "last_modified_date",
    "closed_date",
    "street_address",
    "city",
    "state",
    "zip_code",
    "street_number",
    "street_direction",
    "street_name",
    "street_type",
    "duplicate",
    "legacy_record",
    "community_area",
    "ward",
    "electricity_grid",
    "police_sector",
    "police_district",
    "police_beat",
    "precinct",
    "created_hour",
    "created_day_of_week",
    "created_month",
    "x_coordinate",
    "y_coordinate",
    "latitude",
    "longitude",
    "location"
})
public class ServiceRequest {

    @JsonProperty("sr_number")
    private String srNumber;
    @JsonProperty("sr_type")
    private String srType;
    @JsonProperty("sr_short_code")
    private String srShortCode;
    @JsonProperty("owner_department")
    private String ownerDepartment;
    @JsonProperty("status")
    private String status;
    @JsonProperty("created_date")
    private String createdDate;
    @JsonProperty("last_modified_date")
    private String lastModifiedDate;
    @JsonProperty("closed_date")
    private String closedDate;
    @JsonProperty("street_address")
    private String streetAddress;
    @JsonProperty("city")
    private String city;
    @JsonProperty("state")
    private String state;
    @JsonProperty("zip_code")
    private String zipCode;
    @JsonProperty("street_number")
    private String streetNumber;
    @JsonProperty("street_direction")
    private String streetDirection;
    @JsonProperty("street_name")
    private String streetName;
    @JsonProperty("street_type")
    private String streetType;
    @JsonProperty("duplicate")
    private Boolean duplicate;
    @JsonProperty("legacy_record")
    private Boolean legacyRecord;
    @JsonProperty("community_area")
    private String communityArea;
    @JsonProperty("ward")
    private String ward;
    @JsonProperty("electricity_grid")
    private String electricityGrid;
    @JsonProperty("police_sector")
    private String policeSector;
    @JsonProperty("police_district")
    private String policeDistrict;
    @JsonProperty("police_beat")
    private String policeBeat;
    @JsonProperty("precinct")
    private String precinct;
    @JsonProperty("created_hour")
    private String createdHour;
    @JsonProperty("created_day_of_week")
    private String createdDayOfWeek;
    @JsonProperty("created_month")
    private String createdMonth;
    @JsonProperty("x_coordinate")
    private String xCoordinate;
    @JsonProperty("y_coordinate")
    private String yCoordinate;
    @JsonProperty("latitude")
    private String latitude;
    @JsonProperty("longitude")
    private String longitude;
    @JsonProperty("location")
    private Location location;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("sr_number")
    public String getSrNumber() {
        return srNumber;
    }

    @JsonProperty("sr_number")
    public void setSrNumber(String srNumber) {
        this.srNumber = srNumber;
    }

    @JsonProperty("sr_type")
    public String getSrType() {
        return srType;
    }

    @JsonProperty("sr_type")
    public void setSrType(String srType) {
        this.srType = srType;
    }

    @JsonProperty("sr_short_code")
    public String getSrShortCode() {
        return srShortCode;
    }

    @JsonProperty("sr_short_code")
    public void setSrShortCode(String srShortCode) {
        this.srShortCode = srShortCode;
    }

    @JsonProperty("owner_department")
    public String getOwnerDepartment() {
        return ownerDepartment;
    }

    @JsonProperty("owner_department")
    public void setOwnerDepartment(String ownerDepartment) {
        this.ownerDepartment = ownerDepartment;
    }

    @JsonProperty("status")
    public String getStatus() {
        return status;
    }

    @JsonProperty("status")
    public void setStatus(String status) {
        this.status = status;
    }

    @JsonProperty("created_date")
    public String getCreatedDate() {
        return createdDate;
    }

    @JsonProperty("created_date")
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    @JsonProperty("last_modified_date")
    public String getLastModifiedDate() {
        return lastModifiedDate;
    }

    @JsonProperty("last_modified_date")
    public void setLastModifiedDate(String lastModifiedDate) {
        this.lastModifiedDate = lastModifiedDate;
    }

    @JsonProperty("closed_date")
    public String getClosedDate() {
        return closedDate;
    }

    @JsonProperty("closed_date")
    public void setClosedDate(String closedDate) {
        this.closedDate = closedDate;
    }

    @JsonProperty("street_address")
    public String getStreetAddress() {
        return streetAddress;
    }

    @JsonProperty("street_address")
    public void setStreetAddress(String streetAddress) {
        this.streetAddress = streetAddress;
    }

    @JsonProperty("city")
    public String getCity() {
        return city;
    }

    @JsonProperty("city")
    public void setCity(String city) {
        this.city = city;
    }

    @JsonProperty("state")
    public String getState() {
        return state;
    }

    @JsonProperty("state")
    public void setState(String state) {
        this.state = state;
    }

    @JsonProperty("zip_code")
    public String getZipCode() {
        return zipCode;
    }

    @JsonProperty("zip_code")
    public void setZipCode(String zipCode) {
        this.zipCode = zipCode;
    }

    @JsonProperty("street_number")
    public String getStreetNumber() {
        return streetNumber;
    }

    @JsonProperty("street_number")
    public void setStreetNumber(String streetNumber) {
        this.streetNumber = streetNumber;
    }

    @JsonProperty("street_direction")
    public String getStreetDirection() {
        return streetDirection;
    }

    @JsonProperty("street_direction")
    public void setStreetDirection(String streetDirection) {
        this.streetDirection = streetDirection;
    }

    @JsonProperty("street_name")
    public String getStreetName() {
        return streetName;
    }

    @JsonProperty("street_name")
    public void setStreetName(String streetName) {
        this.streetName = streetName;
    }

    @JsonProperty("street_type")
    public String getStreetType() {
        return streetType;
    }

    @JsonProperty("street_type")
    public void setStreetType(String streetType) {
        this.streetType = streetType;
    }

    @JsonProperty("duplicate")
    public Boolean getDuplicate() {
        return duplicate;
    }

    @JsonProperty("duplicate")
    public void setDuplicate(Boolean duplicate) {
        this.duplicate = duplicate;
    }

    @JsonProperty("legacy_record")
    public Boolean getLegacyRecord() {
        return legacyRecord;
    }

    @JsonProperty("legacy_record")
    public void setLegacyRecord(Boolean legacyRecord) {
        this.legacyRecord = legacyRecord;
    }

    @JsonProperty("community_area")
    public String getCommunityArea() {
        return communityArea;
    }

    @JsonProperty("community_area")
    public void setCommunityArea(String communityArea) {
        this.communityArea = communityArea;
    }

    @JsonProperty("ward")
    public String getWard() {
        return ward;
    }

    @JsonProperty("ward")
    public void setWard(String ward) {
        this.ward = ward;
    }

    @JsonProperty("electricity_grid")
    public String getElectricityGrid() {
        return electricityGrid;
    }

    @JsonProperty("electricity_grid")
    public void setElectricityGrid(String electricityGrid) {
        this.electricityGrid = electricityGrid;
    }

    @JsonProperty("police_sector")
    public String getPoliceSector() {
        return policeSector;
    }

    @JsonProperty("police_sector")
    public void setPoliceSector(String policeSector) {
        this.policeSector = policeSector;
    }

    @JsonProperty("police_district")
    public String getPoliceDistrict() {
        return policeDistrict;
    }

    @JsonProperty("police_district")
    public void setPoliceDistrict(String policeDistrict) {
        this.policeDistrict = policeDistrict;
    }

    @JsonProperty("police_beat")
    public String getPoliceBeat() {
        return policeBeat;
    }

    @JsonProperty("police_beat")
    public void setPoliceBeat(String policeBeat) {
        this.policeBeat = policeBeat;
    }

    @JsonProperty("precinct")
    public String getPrecinct() {
        return precinct;
    }

    @JsonProperty("precinct")
    public void setPrecinct(String precinct) {
        this.precinct = precinct;
    }

    @JsonProperty("created_hour")
    public String getCreatedHour() {
        return createdHour;
    }

    @JsonProperty("created_hour")
    public void setCreatedHour(String createdHour) {
        this.createdHour = createdHour;
    }

    @JsonProperty("created_day_of_week")
    public String getCreatedDayOfWeek() {
        return createdDayOfWeek;
    }

    @JsonProperty("created_day_of_week")
    public void setCreatedDayOfWeek(String createdDayOfWeek) {
        this.createdDayOfWeek = createdDayOfWeek;
    }

    @JsonProperty("created_month")
    public String getCreatedMonth() {
        return createdMonth;
    }

    @JsonProperty("created_month")
    public void setCreatedMonth(String createdMonth) {
        this.createdMonth = createdMonth;
    }

    @JsonProperty("x_coordinate")
    public String getXCoordinate() {
        return xCoordinate;
    }

    @JsonProperty("x_coordinate")
    public void setXCoordinate(String xCoordinate) {
        this.xCoordinate = xCoordinate;
    }

    @JsonProperty("y_coordinate")
    public String getYCoordinate() {
        return yCoordinate;
    }

    @JsonProperty("y_coordinate")
    public void setYCoordinate(String yCoordinate) {
        this.yCoordinate = yCoordinate;
    }

    @JsonProperty("latitude")
    public String getLatitude() {
        return latitude;
    }

    @JsonProperty("latitude")
    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    @JsonProperty("longitude")
    public String getLongitude() {
        return longitude;
    }

    @JsonProperty("longitude")
    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    @JsonProperty("location")
    public Location getLocation() {
        return location;
    }

    @JsonProperty("location")
    public void setLocation(Location location) {
        this.location = location;
    }

    @JsonAnyGetter
    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    @JsonAnySetter
    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}

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
    "id",
    "case_number",
    "date",
    "block",
    "iucr",
    "primary_type",
    "description",
    "location_description",
    "arrest",
    "domestic",
    "beat",
    "district",
    "ward",
    "community_area",
    "fbi_code",
    "year",
    "updated_on"
})
public class CrimeResponse {

    @JsonProperty("id")
    private String id;
    @JsonProperty("case_number")
    private String caseNumber;
    @JsonProperty("date")
    private String date;
    @JsonProperty("block")
    private String block;
    @JsonProperty("iucr")
    private String iucr;
    @JsonProperty("primary_type")
    private String primaryType;
    @JsonProperty("description")
    private String description;
    @JsonProperty("location_description")
    private String locationDescription;
    @JsonProperty("arrest")
    private Boolean arrest;
    @JsonProperty("domestic")
    private Boolean domestic;
    @JsonProperty("beat")
    private String beat;
    @JsonProperty("district")
    private String district;
    @JsonProperty("ward")
    private String ward;
    @JsonProperty("community_area")
    private String communityArea;
    @JsonProperty("fbi_code")
    private String fbiCode;
    @JsonProperty("year")
    private String year;
    @JsonProperty("updated_on")
    private String updatedOn;
    @JsonIgnore
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    @JsonProperty("id")
    public String getId() {
        return id;
    }

    @JsonProperty("id")
    public void setId(String id) {
        this.id = id;
    }

    @JsonProperty("case_number")
    public String getCaseNumber() {
        return caseNumber;
    }

    @JsonProperty("case_number")
    public void setCaseNumber(String caseNumber) {
        this.caseNumber = caseNumber;
    }

    @JsonProperty("date")
    public String getDate() {
        return date;
    }

    @JsonProperty("date")
    public void setDate(String date) {
        this.date = date;
    }

    @JsonProperty("block")
    public String getBlock() {
        return block;
    }

    @JsonProperty("block")
    public void setBlock(String block) {
        this.block = block;
    }

    @JsonProperty("iucr")
    public String getIucr() {
        return iucr;
    }

    @JsonProperty("iucr")
    public void setIucr(String iucr) {
        this.iucr = iucr;
    }

    @JsonProperty("primary_type")
    public String getPrimaryType() {
        return primaryType;
    }

    @JsonProperty("primary_type")
    public void setPrimaryType(String primaryType) {
        this.primaryType = primaryType;
    }

    @JsonProperty("description")
    public String getDescription() {
        return description;
    }

    @JsonProperty("description")
    public void setDescription(String description) {
        this.description = description;
    }

    @JsonProperty("location_description")
    public String getLocationDescription() {
        return locationDescription;
    }

    @JsonProperty("location_description")
    public void setLocationDescription(String locationDescription) {
        this.locationDescription = locationDescription;
    }

    @JsonProperty("arrest")
    public Boolean getArrest() {
        return arrest;
    }

    @JsonProperty("arrest")
    public void setArrest(Boolean arrest) {
        this.arrest = arrest;
    }

    @JsonProperty("domestic")
    public Boolean getDomestic() {
        return domestic;
    }

    @JsonProperty("domestic")
    public void setDomestic(Boolean domestic) {
        this.domestic = domestic;
    }

    @JsonProperty("beat")
    public String getBeat() {
        return beat;
    }

    @JsonProperty("beat")
    public void setBeat(String beat) {
        this.beat = beat;
    }

    @JsonProperty("district")
    public String getDistrict() {
        return district;
    }

    @JsonProperty("district")
    public void setDistrict(String district) {
        this.district = district;
    }

    @JsonProperty("ward")
    public String getWard() {
        return ward;
    }

    @JsonProperty("ward")
    public void setWard(String ward) {
        this.ward = ward;
    }

    @JsonProperty("community_area")
    public String getCommunityArea() {
        return communityArea;
    }

    @JsonProperty("community_area")
    public void setCommunityArea(String communityArea) {
        this.communityArea = communityArea;
    }

    @JsonProperty("fbi_code")
    public String getFbiCode() {
        return fbiCode;
    }

    @JsonProperty("fbi_code")
    public void setFbiCode(String fbiCode) {
        this.fbiCode = fbiCode;
    }

    @JsonProperty("year")
    public String getYear() {
        return year;
    }

    @JsonProperty("year")
    public void setYear(String year) {
        this.year = year;
    }

    @JsonProperty("updated_on")
    public String getUpdatedOn() {
        return updatedOn;
    }

    @JsonProperty("updated_on")
    public void setUpdatedOn(String updatedOn) {
        this.updatedOn = updatedOn;
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

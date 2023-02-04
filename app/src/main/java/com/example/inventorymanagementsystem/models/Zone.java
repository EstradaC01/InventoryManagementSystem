package com.example.inventorymanagementsystem.models;

public class Zone {
    private String zoneId, description, typeOfUnit;
    private Boolean canBeDeleted;

    public Zone() {

    }

    public Zone(String zoneId, String description, String typeOfUnit, Boolean canBeDeleted) {
        this.zoneId = zoneId;
        this.description = description;
        this.typeOfUnit = typeOfUnit;
        this.canBeDeleted = canBeDeleted;
    }

    public String getZoneId() {
        return zoneId;
    }

    public void setZoneId(String zoneId) {
        this.zoneId = zoneId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTypeOfUnit() {
        return typeOfUnit;
    }

    public void setTypeOfUnit(String typeOfUnit) {
        this.typeOfUnit = typeOfUnit;
    }

    public Boolean getCanBeDeleted() {
        return canBeDeleted;
    }

    public void setCanBeDeleted(Boolean canBeDeleted) {
        this.canBeDeleted = canBeDeleted;
    }
}

package com.example.inventorymanagementsystem.models;

public class UnitType {

    private String unitType;
    private Boolean enabled;
    private Boolean canBeDeleted;

    public UnitType() {

    }

    public UnitType(String unitType, Boolean enabled, Boolean canBeDeleted) {
        this.unitType = unitType;
        this.enabled = enabled;
        this.canBeDeleted = canBeDeleted;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public void setActive(Boolean enabled) {
        this.enabled= enabled;
    }

    public Boolean getCanBeDeleted() {
        return canBeDeleted;
    }

    public void setCanBeDeleted(Boolean canBeDeleted) {
        this.canBeDeleted = canBeDeleted;
    }

    public String getUnitType() {
        return unitType;
    }

    public void setUnitType(String unitType) {
        this.unitType = unitType;
    }
}

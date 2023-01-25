package com.example.inventorymanagementsystem;

public class Location {
    private String aisle, rack, level, zone, lpLimit, weight, height, width, length, status, name;
    private Boolean canBeDeleted, hasInventory;

    public Location() {

    }

    public Location(String aisle, String rack, String level, String zone, String lpLimit, String weight,
                    String height, String width, String length, String status, String name, Boolean canBeDeleted, Boolean hasInventory) {
        this.aisle = aisle;
        this.rack = rack;
        this.level = level;
        this.zone = zone;
        this.lpLimit = lpLimit;
        this.weight = weight;
        this.height = height;
        this.width = width;
        this.length = length;
        this.status = status;
        this.name = name;
        this.canBeDeleted = canBeDeleted;
        this.hasInventory = hasInventory;
    }

    public String getAisle() {
        return aisle;
    }

    public void setAisle(String aisle) {
        this.aisle = aisle;
    }

    public String getRack() {
        return rack;
    }

    public void setRack(String rack) {
        this.rack = rack;
    }

    public String getLevel() {
        return level;
    }

    public void setLevel(String level) {
        this.level = level;
    }

    public String getZone() {
        return zone;
    }

    public void setZone(String zone) {
        this.zone = zone;
    }

    public String getLpLimit() {
        return lpLimit;
    }

    public void setLpLimit(String lpLimit) {
        this.lpLimit = lpLimit;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getHeight() {
        return height;
    }

    public void setHeight(String height) {
        this.height = height;
    }

    public String getWidth() {
        return width;
    }

    public void setWidth(String width) {
        this.width = width;
    }

    public String getLength() {
        return length;
    }

    public void setLength(String length) {
        this.length = length;
    }

    public Boolean getCanBeDeleted() {
        return canBeDeleted;
    }

    public void setCanBeDeleted(Boolean canBeDeleted) {
        this.canBeDeleted = canBeDeleted;
    }

    public Boolean getHasInventory() {
        return hasInventory;
    }

    public void setHasInventory(Boolean hasInventory) {
        this.hasInventory = hasInventory;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

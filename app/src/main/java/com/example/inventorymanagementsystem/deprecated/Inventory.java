package com.example.inventorymanagementsystem.deprecated;

public class Inventory {
    private String unitId, productId,
            productDescription, piecesPerBox,
            numberOfBoxes, totalPieces,
            piecesMarked, availableUnits,
            onHandUnits, location,
            disposition, dateTimeCreated;

    public Inventory() {

    }
    public Inventory(String unitId, String productId,
                     String productDescription, String piecesPerBox,
                     String numberOfBoxes, String totalPieces,
                     String piecesMarked, String availableUnits,
                     String onHandUnits, String location,
                     String disposition, String dateTimeCreated) {
        this.unitId = unitId;
        this.productId = productId;
        this.productDescription = productDescription;
        this.piecesPerBox = piecesPerBox;
        this.numberOfBoxes = numberOfBoxes;
        this.totalPieces = totalPieces;
        this.piecesMarked = piecesMarked;
        this.availableUnits = availableUnits;
        this.onHandUnits = onHandUnits;
        this.location = location;
        this.disposition = disposition;
        this.dateTimeCreated = dateTimeCreated;
    }

    public String getUnitId() {
        return unitId;
    }

    public void setUnitId(String unitId) {
        this.unitId = unitId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getProductDescription() {
        return productDescription;
    }

    public void setProductDescription(String productDescription) {
        this.productDescription = productDescription;
    }

    public String getPiecesPerBox() {
        return piecesPerBox;
    }

    public void setPiecesPerBox(String piecesPerBox) {
        this.piecesPerBox = piecesPerBox;
    }

    public String getNumberOfBoxes() {
        return numberOfBoxes;
    }

    public void setNumberOfBoxes(String numberOfBoxes) {
        this.numberOfBoxes = numberOfBoxes;
    }

    public String getTotalPieces() {
        return totalPieces;
    }

    public void setTotalPieces(String totalPieces) {
        this.totalPieces = totalPieces;
    }

    public String getPiecesMarked() {
        return piecesMarked;
    }

    public void setPiecesMarked(String piecesMarked) {
        this.piecesMarked = piecesMarked;
    }

    public String getAvailableUnits() {
        return availableUnits;
    }

    public void setAvailableUnits(String availableUnits) {
        this.availableUnits = availableUnits;
    }

    public String getOnHandUnits() {
        return onHandUnits;
    }

    public void setOnHandUnits(String onHandUnits) {
        this.onHandUnits = onHandUnits;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(String dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }
}

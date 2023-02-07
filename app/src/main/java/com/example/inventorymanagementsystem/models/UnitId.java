package com.example.inventorymanagementsystem.models;

public class UnitId {
    private String unitId, productId, dateTimeCreated,
    piecesPerBox, numberOfBoxes, totalPieces, receiptId, location, disposition,
    piecesMarked, piecesAvailable;

    public UnitId() {

    }
    public UnitId(String unitId, String productId, String dateTimeCreated,
                  String piecesPerBox, String numberOfBoxes,
                  String totalPieces, String receiptId,
                  String location, String disposition,
                  String piecesMarked, String piecesAvailable) {
        this.unitId = unitId;
        this.productId = productId;
        this.dateTimeCreated = dateTimeCreated;
        this.piecesPerBox = piecesPerBox;
        this.numberOfBoxes = numberOfBoxes;
        this.totalPieces = totalPieces;
        this.receiptId = receiptId;
        this.location = location;
        this.disposition = disposition;
        this.piecesMarked = piecesMarked;
        this.piecesAvailable = piecesAvailable;
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


    public String getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(String dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
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

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
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

    public String getPiecesMarked() {
        return piecesMarked;
    }

    public void setPiecesMarked(String piecesMarked) {
        this.piecesMarked = piecesMarked;
    }

    public String getPiecesAvailable() {
        return piecesAvailable;
    }

    public void setPiecesAvailable(String piecesAvailable) {
        this.piecesAvailable = piecesAvailable;
    }
}

package com.example.inventorymanagementsystem.models;

public class UnitId {
    private String unitId, productId, productDescription, dateTimeCreated,
    piecesPerBox, numberOfBoxes, totalPieces, receiptId;

    public UnitId() {

    }
    public UnitId(String unitId, String productId,
                  String productDescription, String dateTimeCreated,
                  String piecesPerBox, String numberOfBoxes,
                  String totalPieces, String receiptId) {
        this.unitId = unitId;
        this.productId = productId;
        this.productDescription = productDescription;
        this.dateTimeCreated = dateTimeCreated;
        this.piecesPerBox = piecesPerBox;
        this.numberOfBoxes = numberOfBoxes;
        this.totalPieces = totalPieces;
        this.receiptId = receiptId;
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
}

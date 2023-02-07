package com.example.inventorymanagementsystem.models;

public class Receiving {
    String productId, numberOfUnits,
            piecesPerBox, totalBoxes,
            loosePieces, disposition,
            location, weight,
            PO, shipFrom,
            receiptId, dateTimeCreated;

    public Receiving() {

    }

    public Receiving(String productId, String numberOfUnits,
                     String piecesPerBox, String totalBoxes,
                     String loosePieces, String disposition,
                     String location, String weight,
                     String PO, String shipFrom,
                     String receiptId, String dateTimeCreated) {
        this.productId = productId;
        this.numberOfUnits = numberOfUnits;
        this.piecesPerBox = piecesPerBox;
        this.totalBoxes = totalBoxes;
        this.loosePieces = loosePieces;
        this.disposition = disposition;
        this.location = location;
        this.weight = weight;
        this.PO = PO;
        this.shipFrom = shipFrom;
        this.receiptId = receiptId;
        this.dateTimeCreated = dateTimeCreated;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getNumberOfUnits() {
        return numberOfUnits;
    }

    public void setNumberOfUnits(String numberOfUnits) {
        this.numberOfUnits = numberOfUnits;
    }

    public String getPiecesPerBox() {
        return piecesPerBox;
    }

    public void setPiecesPerBox(String piecesPerBox) {
        this.piecesPerBox = piecesPerBox;
    }

    public String getTotalBoxes() {
        return totalBoxes;
    }

    public void setTotalBoxes(String totalBoxes) {
        this.totalBoxes = totalBoxes;
    }

    public String getLoosePieces() {
        return loosePieces;
    }

    public void setLoosePieces(String loosePieces) {
        this.loosePieces = loosePieces;
    }

    public String getDisposition() {
        return disposition;
    }

    public void setDisposition(String disposition) {
        this.disposition = disposition;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getWeight() {
        return weight;
    }

    public void setWeight(String weight) {
        this.weight = weight;
    }

    public String getPO() {
        return PO;
    }

    public void setPO(String PO) {
        this.PO = PO;
    }

    public String getShipFrom() {
        return shipFrom;
    }

    public void setShipFrom(String shipFrom) {
        this.shipFrom = shipFrom;
    }

    public String getReceiptId() {
        return receiptId;
    }

    public void setReceiptId(String receiptId) {
        this.receiptId = receiptId;
    }

    public String getDateTimeCreated() {
        return dateTimeCreated;
    }

    public void setDateTimeCreated(String dateTimeCreated) {
        this.dateTimeCreated = dateTimeCreated;
    }
}

package com.example.awizom.dotapp.Models;

public class ReceivedModel
{
    public String CatalogName;
    public String SerialNo;
    public String Design;
    public int PageNo;
    public double Price;
    public String Unit;
    public double Price2;
    public double AQty;
    public String TelorName;
    public boolean ReceivedFromTalor;
    public String ReceivedBy;

    public String getCatalogName() {
        return CatalogName;
    }

    public void setCatalogName(String catalogName) {
        CatalogName = catalogName;
    }

    public String getSerialNo() {
        return SerialNo;
    }

    public void setSerialNo(String serialNo) {
        SerialNo = serialNo;
    }

    public String getDesign() {
        return Design;
    }

    public void setDesign(String design) {
        Design = design;
    }

    public int getPageNo() {
        return PageNo;
    }

    public void setPageNo(int pageNo) {
        PageNo = pageNo;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public double getPrice2() {
        return Price2;
    }

    public void setPrice2(double price2) {
        Price2 = price2;
    }

    public double getAQty() {
        return AQty;
    }

    public void setAQty(double AQty) {
        this.AQty = AQty;
    }

    public String getTelorName() {
        return TelorName;
    }

    public void setTelorName(String telorName) {
        TelorName = telorName;
    }

    public boolean isReceivedFromTalor() {
        return ReceivedFromTalor;
    }

    public void setReceivedFromTalor(boolean receivedFromTalor) {
        ReceivedFromTalor = receivedFromTalor;
    }

    public String getReceivedBy() {
        return ReceivedBy;
    }

    public void setReceivedBy(String receivedBy) {
        ReceivedBy = receivedBy;
    }
}

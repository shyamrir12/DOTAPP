package com.example.awizom.dotapp.Models;

import java.util.List;

public class Catelog {
    public List<String> OrderItems;
    public String CatalogName;

    public List<String> getOrderItems() {
        return OrderItems;
    }

    public void setOrderItems(List<String> orderItems) {
        OrderItems = orderItems;
    }

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

    public int getCatalogID() {
        return CatalogID;
    }

    public void setCatalogID(int catalogID) {
        CatalogID = catalogID;
    }

    public String SerialNo;
    public String Design;
    public int PageNo;
    public double Price;
    public String Unit;
    public int CatalogID;
}

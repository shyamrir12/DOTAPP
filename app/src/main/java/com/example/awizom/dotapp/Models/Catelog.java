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

    public String getMaterialType() {
        return MaterialType;
    }

    public void setMaterialType(String materialType) {
        MaterialType = materialType;
    }

    public String SerialNo;
    public String Design;
    public int PageNo;
    public double Price;
    public String Unit;
    public String MaterialType;
    public int CatalogID;

    public Double Elight;
    public Double Roman;
    public Double APlat;
    public Double ElightPrice;
    public Double RomanPrice;
    public Double APlatPrice;

    public Double getElight() {
        return Elight;
    }

    public void setElight(Double elight) {
        Elight = elight;
    }

    public Double getRoman() {
        return Roman;
    }

    public void setRoman(Double roman) {
        Roman = roman;
    }

    public Double getAPlat() {
        return APlat;
    }

    public void setAPlat(Double APlat) {
        this.APlat = APlat;
    }

    public Double getElightPrice() {
        return ElightPrice;
    }

    public void setElightPrice(Double elightPrice) {
        ElightPrice = elightPrice;
    }

    public Double getRomanPrice() {
        return RomanPrice;
    }

    public void setRomanPrice(Double romanPrice) {
        RomanPrice = romanPrice;
    }

    public Double getAPlatPrice() {
        return APlatPrice;
    }

    public void setAPlatPrice(Double APlatPrice) {
        this.APlatPrice = APlatPrice;
    }


}

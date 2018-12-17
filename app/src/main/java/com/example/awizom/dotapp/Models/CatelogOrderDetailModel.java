package com.example.awizom.dotapp.Models;

public class CatelogOrderDetailModel {

    public int OrderItemID;
    public String MaterialType;
    public double Price2;
    public double Qty;
    public double AQty;
    public String OrderUnit;
    public int OrderRoomID;

    public String CatalogName;
    public String SerialNo;
    public String Design;
    public int PageNo;
    public double Price;
    public String Unit;
    public int CatalogID;
    public String RoomName;
    public int OrderID;

    public int OrderStatusID;
    public boolean OrderPlaced;
    public boolean MaterialReceived;
    public String HandOverTo;
    public String TelorName;
    public boolean ReceivedFromTalor;
    public String ReceivedBy;
    public boolean Cancel;
    public boolean Dispatch;


    public Double Elight;
    public Double Roman;
    public Double APlat;
    public Double ElightPrice;
    public Double RomanPrice;
    public Double APlatPrice;

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public int getOrderItemID() {
        return OrderItemID;
    }

    public void setOrderItemID(int orderItemID) {
        OrderItemID = orderItemID;
    }

    public String getMaterialType() {
        return MaterialType;
    }

    public void setMaterialType(String materialType) {
        MaterialType = materialType;
    }

    public double getPrice2() {
        return Price2;
    }

    public void setPrice2(int price2) {
        Price2 = price2;
    }

    public double getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public double getAQty() {
        return AQty;
    }

    public void setAQty(int AQty) {
        this.AQty = AQty;
    }

    public String getOrderUnit() {
        return OrderUnit;
    }

    public void setOrderUnit(String orderUnit) {
        OrderUnit = orderUnit;
    }

    public int getOrderRoomID() {
        return OrderRoomID;
    }

    public void setOrderRoomID(int orderRoomID) {
        OrderRoomID = orderRoomID;
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

    public void setPrice(int price) {
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


    public boolean isOrderPlaced() {
        return OrderPlaced;
    }

    public void setOrderPlaced(boolean orderPlaced) {
        OrderPlaced = orderPlaced;
    }

    public boolean isMaterialReceived() {
        return MaterialReceived;
    }

    public void setMaterialReceived(boolean materialReceived) {
        MaterialReceived = materialReceived;
    }

    public String getHandOverTo() {
        return HandOverTo;
    }

    public void setHandOverTo(String handOverTo) {
        HandOverTo = handOverTo;
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

    public boolean isCancel() {
        return Cancel;
    }

    public void setCancel(boolean cancel) {
        Cancel = cancel;
    }

    public boolean isDispatch() {
        return Dispatch;
    }

    public void setDispatch(boolean dispatch) {
        Dispatch = dispatch;
    }

    public int getOrderStatusID() {
        return OrderStatusID;
    }

    public void setOrderStatusID(int orderStatusID) {
        OrderStatusID = orderStatusID;
    }


    public void setPrice2(double price2) {
        Price2 = price2;
    }

    public void setQty(double qty) {
        Qty = qty;
    }

    public void setAQty(double AQty) {
        this.AQty = AQty;
    }

    public void setPrice(double price) {
        Price = price;
    }

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

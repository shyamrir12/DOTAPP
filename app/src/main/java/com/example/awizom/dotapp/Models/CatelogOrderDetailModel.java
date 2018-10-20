package com.example.awizom.dotapp.Models;

public class CatelogOrderDetailModel {

    public int OrderItemID;
    public String MaterialType;
    public int Price2;
    public int Qty;
    public int AQty;
    public String OrderUnit;
    public int OrderRoomID;

    public String CatalogName;
    public String SerialNo;
    public String Design;
    public int PageNo;
    public int Price;
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

    public int getPrice2() {
        return Price2;
    }

    public void setPrice2(int price2) {
        Price2 = price2;
    }

    public int getQty() {
        return Qty;
    }

    public void setQty(int qty) {
        Qty = qty;
    }

    public int getAQty() {
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

    public int getPrice() {
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
}

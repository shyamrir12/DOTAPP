package com.example.awizom.dotapp.Models;

public class DataOrder {


    public int CustomerID ;
    public String CustomerName ;
    public String Address ;
    public String Mobile ;
    public String InteriorName ;
    public String InteriorMobile ;
    public int OrderStatusID ;
    public boolean OrderPlaced ;
    public boolean MaterialReceived ;
    public String HandOverTo ;
    public String TelorName ;
    public boolean ReceivedFromTalor ;
    public String ReceivedBy ;
    public boolean Cancel ;
    public boolean Dispatch ;
    public int OrderID ;
    public double Advance ;
    public String OrderDate ;
    public int SNO ;
    public String RoomList ;

    public int getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(int customerID) {
        CustomerID = customerID;
    }

    public String getCustomerName() {
        return CustomerName;
    }

    public void setCustomerName(String customerName) {
        CustomerName = customerName;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getInteriorName() {
        return InteriorName;
    }

    public void setInteriorName(String interiorName) {
        InteriorName = interiorName;
    }

    public String getInteriorMobile() {
        return InteriorMobile;
    }

    public void setInteriorMobile(String interiorMobile) {
        InteriorMobile = interiorMobile;
    }

    public int getOrderStatusID() {
        return OrderStatusID;
    }

    public void setOrderStatusID(int orderStatusID) {
        OrderStatusID = orderStatusID;
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

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public double getAdvance() {
        return Advance;
    }

    public void setAdvance(double advance) {
        Advance = advance;
    }

    public String getOrderDate() {
        return OrderDate;
    }

    public void setOrderDate(String orderDate) {
        OrderDate = orderDate;
    }

    public int getSNO() {
        return SNO;
    }

    public void setSNO(int SNO) {
        this.SNO = SNO;
    }

    public String getRoomList() {
        return RoomList;
    }

    public void setRoomList(String roomList) {
        RoomList = roomList;
    }
}

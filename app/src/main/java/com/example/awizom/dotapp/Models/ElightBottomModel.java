package com.example.awizom.dotapp.Models;

public class ElightBottomModel {

    public int OrderRoomId;
    public String RoomName;
    public String Elight;
    public String Roman;
    public String APlat;
    public int OrderID;
    public Double TotalAmount;
    public Double ATotalAmount;

    public Double getActualTotalAmount() {
        return ATotalAmount;
    }

    public void setActualTotalAmount(Double aTotalAmount) {
        ATotalAmount = aTotalAmount;
    }

    public int getOrderRoomId() {
        return OrderRoomId;
    }

    public void setOrderRoomId(int orderRoomId) {
        OrderRoomId = orderRoomId;
    }

    public String getRoomName() {
        return RoomName;
    }

    public void setRoomName(String roomName) {
        RoomName = roomName;
    }

    public String getElight() {
        return Elight;
    }

    public void setElight(String elight) {
        Elight = elight;
    }

    public String getRoman() {
        return Roman;
    }

    public void setRoman(String roman) {
        Roman = roman;
    }

    public String getAPlat() {
        return APlat;
    }

    public void setAPlat(String APlat) {
        this.APlat = APlat;
    }

    public int getOrderID() {
        return OrderID;
    }

    public void setOrderID(int orderID) {
        OrderID = orderID;
    }

    public Double getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(Double totalAmount) {
        TotalAmount = totalAmount;
    }

}

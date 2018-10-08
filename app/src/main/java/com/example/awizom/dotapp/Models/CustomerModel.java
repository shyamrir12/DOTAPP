package com.example.awizom.dotapp.Models;

import java.util.List;

public class CustomerModel {

    public String Order[];
    public int CustomerID;
    public String CustomerName;
    public String Address;
    public String Mobile;
    public String InteriorName;
    public String InteriorMobile;

    public String[] getOrder() {
        return Order;
    }

    public void setOrder(String[] order) {
        Order = order;
    }

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
}

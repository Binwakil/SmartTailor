package com.wakili.smarttailor.Model;

import java.util.Date;

public class OrderModel {

    private String OrderName, CustomerID, Style, ProductNo;
    private Date OrderTime, CollectionTime;
    private String  TotalPrice, AmountDue, Balance;


    public String getOrderName() {
        return OrderName;
    }

    public void setOrderName(String orderName) {
        OrderName = orderName;
    }

    public String getCustomerID() {
        return CustomerID;
    }

    public void setCustomerID(String customerID) {
        CustomerID = customerID;
    }

    public String getStyle() {
        return Style;
    }

    public void setStyle(String style) {
        Style = style;
    }

    public String getProductNo() {
        return ProductNo;
    }

    public void setProductNo(String productNo) {
        ProductNo = productNo;
    }

    public Date getOrderTime() {
        return OrderTime;
    }

    public void setOrderTime(Date orderTime) {
        OrderTime = orderTime;
    }

    public Date getCollectionTime() {
        return CollectionTime;
    }

    public void setCollectionTime(Date collectionTime) {
        CollectionTime = collectionTime;
    }

    public String getTotalPrice() {
        return TotalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        TotalPrice = totalPrice;
    }

    public String getAmountDue() {
        return AmountDue;
    }

    public void setAmountDue(String amountDue) {
        AmountDue = amountDue;
    }

    public String getBalance() {
        return Balance;
    }

    public void setBalance(String balance) {
        Balance = balance;
    }
}

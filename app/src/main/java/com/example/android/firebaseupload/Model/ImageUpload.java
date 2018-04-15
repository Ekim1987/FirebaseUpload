package com.example.android.firebaseupload.Model;


public class ImageUpload {

    public String pharmacyName;
    public String practiceNo;
    public String uri;
    public String customerName;
    public String message;
    public String startKm;
    public String endKm;
    public String invoiceCounter;

    public ImageUpload(String pharmacyName, String practiceNo) {
        this.pharmacyName = pharmacyName;
        this.practiceNo = practiceNo;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStartKm() {
        return startKm;
    }

    public void setStartKm(String startKm) {
        this.startKm = startKm;
    }

    public String getEndKm() {
        return endKm;
    }

    public void setEndKm(String endKm) {
        this.endKm = endKm;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public ImageUpload() {
    }

    public String getInvoiceCounter() {
        return invoiceCounter;
    }

    public void setInvoiceCounter(String invoiceCounter) {
        this.invoiceCounter = invoiceCounter;
    }

    public ImageUpload(String pharmacyName, String practiceNo, String uri, String customerName, String message, String startKm, String endKm, String invoiceCounter) {
        this.pharmacyName = pharmacyName;
        this.practiceNo = practiceNo;
        this.uri = uri;
        this.customerName = customerName;
        this.message = message;
        this.startKm = startKm;
        this.endKm = endKm;
        this.invoiceCounter=invoiceCounter;

    }

    public String getPharmacyName() {
        return pharmacyName;
    }

    public void setPharmacyName(String pharmacyName) {
        this.pharmacyName = pharmacyName;
    }

    public String getPracticeNo() {
        return practiceNo;
    }

    public void setPracticeNo(String practiceNo) {
        this.practiceNo = practiceNo;
    }
}

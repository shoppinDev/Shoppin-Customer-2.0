package com.dealwala.main.dealwala.model;

/**
 * Created by Jaimin Patel on 23-Jun-16.
 */
public class MyLoyaltyModel {

    String totalpin,reedemedpin,merchantid,merchantname;

    public MyLoyaltyModel() {
    }

    public MyLoyaltyModel(String totalpin, String reedemedpin, String merchantid, String merchantname) {

        this.totalpin = totalpin;
        this.reedemedpin = reedemedpin;
        this.merchantid = merchantid;
        this.merchantname = merchantname;
    }

    public String getTotalpin() {
        return totalpin;
    }

    public void setTotalpin(String totalpin) {
        this.totalpin = totalpin;
    }

    public String getReedemedpin() {
        return reedemedpin;
    }

    public void setReedemedpin(String reedemedpin) {
        this.reedemedpin = reedemedpin;
    }

    public String getMerchantid() {
        return merchantid;
    }

    public void setMerchantid(String merchantid) {
        this.merchantid = merchantid;
    }

    public String getMerchantname() {
        return merchantname;
    }

    public void setMerchantname(String merchantname) {
        this.merchantname = merchantname;
    }
}

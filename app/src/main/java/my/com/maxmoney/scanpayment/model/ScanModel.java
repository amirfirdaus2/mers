package my.com.maxmoney.scanpayment.model;

import java.util.Date;

/**
 * Created by root on 17/01/2018.
 */

public class ScanModel {

    private String name;
    private String transactionId;
    private Date timestamp;
    private int status;
    private String amount;

    public ScanModel(String name, String transactionId, Date timestamp, int status) {
        this.name = name;
        this.transactionId = transactionId;
        this.timestamp = timestamp;
        this.status = status;
    }

    public ScanModel() {

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public Date getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Date timestamp) {
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }
}

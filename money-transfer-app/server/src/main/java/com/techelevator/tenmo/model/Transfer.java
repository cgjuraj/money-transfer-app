package com.techelevator.tenmo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.math.BigDecimal;

public class Transfer {

    @JsonProperty("transfer_id")
    private int transferId;
    @JsonProperty("sender_id")
    private int senderId;
    @JsonProperty("receiver_id")
    private int receiverId;
    @JsonProperty("transfer_amount")
    @Positive
    private BigDecimal transferAmount;
    @JsonProperty("transfer_status")
    private String transferStatus;

    public Transfer() {}

    public Transfer(int transferId, int senderId, int receiverId, BigDecimal transferAmount, String transferStatus) {
        this.transferId = transferId;
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.transferAmount = transferAmount;
        this.transferStatus = transferStatus;
    }

    public int getTransferId() {
        return transferId;
    }

    public void setTransferId(int transferId) {
        this.transferId = transferId;
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public BigDecimal getTransferAmount() {
        return transferAmount;
    }

    public void setTransferAmount(BigDecimal transferAmount) {
        this.transferAmount = transferAmount;
    }

    public String getTransferStatus() {
        return transferStatus;
    }

    public void setTransferStatus(String transferStatus) {
        this.transferStatus = transferStatus;
    }

    @Override
    public String toString() {
        return "Transfer{" +
                "transferId=" + transferId +
                ", senderId=" + senderId +
                ", receiverId=" + receiverId +
                ", transferAmount=" + transferAmount +
                ", transferStatus='" + transferStatus + '\'' +
                '}';
    }
}

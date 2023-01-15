package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;

import java.util.List;

public interface TransferDao {

    public int sendTransfer(Transfer transfer);

    public List<Transfer> getListOfTransfersById(int id);

    public Transfer getTransferById(int id);

    public int requestTransfer(Transfer transfer);

    public List<Transfer> getListOfPendingTransfersById(int id);

    public void approveTransfer(int id);

    public void rejectTransfer(int id);


}

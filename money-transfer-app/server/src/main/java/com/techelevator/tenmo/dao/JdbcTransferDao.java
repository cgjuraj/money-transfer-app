package com.techelevator.tenmo.dao;

import com.techelevator.tenmo.model.Transfer;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class JdbcTransferDao implements TransferDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcTransferDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public int sendTransfer(Transfer transfer) {
        String transferSql = "INSERT INTO transfer (sender_id, receiver_id, transfer_amount, transfer_status) VALUES (?, ?, ?, ?) RETURNING transfer_id";
        String updateSenderSql = "UPDATE account SET balance = balance - ? WHERE user_id = ?";
        String updateReceiverSql = "UPDATE account SET balance = balance + ? WHERE user_id = ?";

        try {
            Integer newTransferId = jdbcTemplate.queryForObject(transferSql, Integer.class, transfer.getSenderId(), transfer.getReceiverId(), transfer.getTransferAmount(), "Approved");
            jdbcTemplate.update(updateSenderSql, transfer.getTransferAmount(), transfer.getSenderId());
            jdbcTemplate.update(updateReceiverSql, transfer.getTransferAmount(), transfer.getReceiverId());
            return newTransferId;
        } catch (DataAccessException e) {
            System.out.println(e);
            return 0;
        }
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public List<Transfer> getListOfTransfersById(int id) {
        List<Transfer> transferList = new ArrayList<>();
        String sql = "SELECT * FROM transfer WHERE sender_id = ? OR receiver_id = ?";
        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);
        while (results.next()) {
            Transfer currentTransfer = mapRowToTransfer(results);
            transferList.add(currentTransfer);
        }
        return transferList;
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public Transfer getTransferById(int id) {
        Transfer transfer;
        String sql = "SELECT * FROM transfer WHERE transfer_id = ?";
        SqlRowSet result = jdbcTemplate.queryForRowSet(sql, id);

        if(result.next()) {
            transfer = mapRowToTransfer(result);
            return transfer;
        } else {
            return null;
        }
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public int requestTransfer(Transfer transfer){
        String sql = "INSERT INTO transfer (sender_id, receiver_id, transfer_amount, transfer_status) VALUES (?, ?, ?, ?) RETURNING transfer_id";

        try {
            Integer newTransferId = jdbcTemplate.queryForObject(sql, Integer.class, transfer.getSenderId(), transfer.getReceiverId(), transfer.getTransferAmount(), "Pending");
            return newTransferId;
        } catch (DataAccessException e) {
            System.out.println(e);
            return 0;
        }
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public List<Transfer> getListOfPendingTransfersById(int id){
        String sql = "SELECT * FROM transfer WHERE (sender_id = ? OR receiver_id = ?) AND transfer_status = 'Pending'";

        List<Transfer> transferList = new ArrayList<>();

        SqlRowSet results = jdbcTemplate.queryForRowSet(sql, id, id);

        while(results.next()) {
            Transfer currentTransfer = mapRowToTransfer(results);
            transferList.add(currentTransfer);
        }
        return transferList;
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public void approveTransfer(int id) {
        String approveStatusSql = "UPDATE transfer SET transfer_status = 'Approved' WHERE transfer_id = ? AND transfer_status = 'Pending';";
        String updateSenderSql = "UPDATE account SET balance = balance - ? WHERE user_id = ?";
        String updateReceiverSql = "UPDATE account SET balance = balance + ? WHERE user_id = ?";

        Transfer transfer = this.getTransferById(id);

        try {
            jdbcTemplate.update(approveStatusSql, transfer.getTransferId());
            jdbcTemplate.update(updateSenderSql, transfer.getTransferAmount(), transfer.getSenderId());
            jdbcTemplate.update(updateReceiverSql, transfer.getTransferAmount(), transfer.getReceiverId());
        } catch (DataAccessException e) {
            System.out.println(e);
        }
    }

    @Override
    @PreAuthorize("isAuthenticated()")
    public void rejectTransfer(int id){
        String sql = "UPDATE transfer SET transfer_status = 'Rejected' WHERE transfer_id = ? AND transfer_status = 'Pending'";
        jdbcTemplate.update(sql, id);
    }


    public Transfer mapRowToTransfer(SqlRowSet result){
        Transfer transfer = new Transfer();

        transfer.setTransferId(result.getInt("transfer_id"));
        transfer.setSenderId(result.getInt("sender_id"));
        transfer.setReceiverId(result.getInt("receiver_id"));
        transfer.setTransferStatus(result.getString("transfer_status"));
        transfer.setTransferAmount(result.getBigDecimal("transfer_amount"));

        return transfer;
    }
}

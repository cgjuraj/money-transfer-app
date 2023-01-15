package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcTransferDao;
import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.Transfer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;
import java.util.List;

public class JdbcTransferDaoTests extends BaseDaoTests{

    private JdbcTransferDao sut;
    private Transfer testTransfer;
    private Transfer testTransfer2;
    private Transfer testSentTransfer;
    private Transfer testRequestTransfer;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcTransferDao(jdbcTemplate);
        testTransfer = new Transfer(3001, 1001, 1002, new BigDecimal("100.00"), "Approved");
        testSentTransfer = new Transfer(3003, 1001, 1002, new BigDecimal("100.00"), "Approved");
        testTransfer2 = new Transfer(3002, 1002, 1001, new BigDecimal("50.00"), "Approved");
        testRequestTransfer = new Transfer(3004, 1001, 1002, new BigDecimal("100.00"), "Pending");
    }

    @Test
    public void getList_of_transfers_by_id(){
        List<Transfer> transferList = sut.getListOfTransfersById(1001);
        Assert.assertEquals(2, transferList.size());
        assertTransfersMatch(testTransfer, transferList.get(0));
        assertTransfersMatch(testTransfer2, transferList.get(1));
    }

    @Test
    public void getTransfer_by_id(){
        Transfer transfer = sut.getTransferById(3001);
        assertTransfersMatch(testTransfer, transfer);

        transfer = sut.getTransferById(3002);
        assertTransfersMatch(testTransfer2, transfer);
    }

    @Test
    public void sent_transfer_has_expected_values_when_retrieved(){
        int sentTransferId = sut.sendTransfer(testSentTransfer);

        Assert.assertNotNull(sentTransferId);

        Transfer newTransfer = sut.getTransferById(sentTransferId);
        int newTransferId = newTransfer.getTransferId();
        Transfer retrievedTransfer = sut.getTransferById(newTransferId);

        assertTransfersMatch(testSentTransfer, retrievedTransfer);
    }

    @Test
    public void requestTransfer_inserts_pending_transfer() {
        int requestedTransferId = sut.requestTransfer(testRequestTransfer);
        Transfer requestedTransfer = sut.getTransferById(requestedTransferId);

        String status = requestedTransfer.getTransferStatus();

        Assert.assertEquals("Pending", status);
    }

    @Test
    public void getListOfPendingTransfersById_returns_correct_amount_of_transfers() {
        sut.requestTransfer(testRequestTransfer);
        sut.requestTransfer(testRequestTransfer);
        List<Transfer> transferList = sut.getListOfPendingTransfersById(1001);
        Assert.assertEquals(2, transferList.size());
    }

//    @Test
//    public void approveRequest_updates_status_and_balance() {
//        sut.approveTransfer(testRequestTransfer.getTransferId());
//        Assert.assertEquals("Approved", testRequestTransfer.getTransferStatus());
//    }

//    @Test
//    public void rejectTransfer_updates_status_to_rejected() {
//
//    }

    private void assertTransfersMatch(Transfer expected, Transfer actual){
        Assert.assertEquals(expected.getTransferId(), actual.getTransferId());
        Assert.assertEquals(expected.getSenderId(), actual.getSenderId());
        Assert.assertEquals(expected.getReceiverId(), actual.getReceiverId());
        Assert.assertEquals(expected.getTransferAmount(), actual.getTransferAmount());
        Assert.assertEquals(expected.getTransferStatus(), actual.getTransferStatus());
    }


}

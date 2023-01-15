package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.TransferDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.Transfer;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.security.Principal;
import java.util.List;

@RestController
public class TransferController {

    private TransferDao transferDao;
    private AccountDao accountDao;
    private UserDao userDao;

    public TransferController(TransferDao transferDao, AccountDao accountDao, UserDao userDao) {
        this.transferDao = transferDao;
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path="/transfer/send", method=RequestMethod.POST)
    public int transfer(@RequestBody Transfer transfer, Principal principal) {
        String username = principal.getName();
        int userId = userDao.findIdByUsername(username);

        BigDecimal transferredMoney;
        BigDecimal currentBalance;

        transferredMoney = transfer.getTransferAmount();
        currentBalance = accountDao.findBalanceByUser(userId);

        int balanceHigherThanTransfer = transferredMoney.compareTo(currentBalance);
        int balanceHigherThanZero = transferredMoney.compareTo(BigDecimal.ZERO);

        if (balanceHigherThanTransfer <= 0 && balanceHigherThanZero == 1) {
            return transferDao.sendTransfer(transfer);
        } else if (!(balanceHigherThanTransfer <= 0)) {
            System.out.println("Insufficient balance - can't send more than you have.");
            return 0;
        } else {
            System.out.println("Insufficient transfer amount - transfer amount is zero or below.");
            return 0;
        }
    }

    @RequestMapping(path="/transfer/user", method=RequestMethod.GET)
    public List<Transfer> getTransfers(Principal principal) {
        String username = principal.getName();
        int userId = userDao.findIdByUsername(username);

        return transferDao.getListOfTransfersById(userId);
    }

    @RequestMapping(path = "/transfer", method = RequestMethod.POST)
    public Transfer getTransferById(@RequestBody Transfer transfer) {
        int transferId = transfer.getTransferId();
        return transferDao.getTransferById(transferId);
    }

    @RequestMapping(path = "/transfer/request", method = RequestMethod.POST)
    public int requestTransfer(@RequestBody Transfer transfer, Principal principal) {
        String username = principal.getName();
        int userId = userDao.findIdByUsername(username);

        BigDecimal transferredMoney;
        BigDecimal currentBalance;

        transferredMoney = transfer.getTransferAmount();
        currentBalance = accountDao.findBalanceByUser(userId);

        int balanceHigherThanTransfer = transferredMoney.compareTo(currentBalance);
        int balanceHigherThanZero = transferredMoney.compareTo(BigDecimal.ZERO);

        if (balanceHigherThanTransfer <= 0 && balanceHigherThanZero == 1) {
            return transferDao.requestTransfer(transfer);
        } else if (!(balanceHigherThanTransfer <= 0)) {
            System.out.println("Invalid - can't request more than you have.");
            return 0;
        } else {
            System.out.println("Insufficient transfer amount - transfer amount is zero or below.");
            return 0;
        }
    }

    @RequestMapping(path = "/transfer/list", method = RequestMethod.GET)
    public List<Transfer> getListOfPendingTransfersById(Principal principal){
        String username = principal.getName();
        int userId = userDao.findIdByUsername(username);
        return transferDao.getListOfPendingTransfersById(userId);
    }

    @RequestMapping(path = "/transfer/reject/{id}", method = RequestMethod.PUT)
    public void rejectTransfer(@PathVariable int id){
        transferDao.rejectTransfer(id);
    }

    @RequestMapping(path = "/transfer/approve/{id}", method = RequestMethod.PUT)
    public void approveTransfer(@PathVariable int id){
        transferDao.approveTransfer(id);
    }

}

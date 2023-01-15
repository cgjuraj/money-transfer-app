package com.techelevator.tenmo.controller;

import com.techelevator.tenmo.dao.AccountDao;
import com.techelevator.tenmo.dao.UserDao;
import com.techelevator.tenmo.model.User;
import org.springframework.web.bind.annotation.*;

import java.awt.print.PrinterIOException;
import java.math.BigDecimal;
import java.security.Principal;

@RestController
public class AccountController {

    private AccountDao accountDao;
    private UserDao userDao;

    public AccountController(AccountDao accountDao, UserDao userDao){
        this.accountDao = accountDao;
        this.userDao = userDao;
    }

    @RequestMapping(path = "/balance", method = RequestMethod.GET)
    public BigDecimal getBalanceByUserId(Principal principal){
        String username = principal.getName();
        int userId = userDao.findIdByUsername(username);
        return accountDao.findBalanceByUser(userId);
    }
}

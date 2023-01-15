package com.techelevator.tenmo.dao;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class JdbcAccountDao implements AccountDao{

    private JdbcTemplate jdbcTemplate;

    public JdbcAccountDao(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    // username vs userId
    // how to test
    @PreAuthorize("isAuthenticated()")
    public BigDecimal findBalanceByUser(int userId){
        String sql = "SELECT balance FROM account WHERE user_id = ?";
        BigDecimal userBalance = jdbcTemplate.queryForObject(sql, BigDecimal.class, userId);
        return userBalance;
    }



}

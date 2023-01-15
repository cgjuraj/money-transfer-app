package com.techelevator.dao;

import com.techelevator.tenmo.dao.JdbcAccountDao;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import java.math.BigDecimal;

public class JdbcAccountDaoTests extends BaseDaoTests{

    private JdbcAccountDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcAccountDao(jdbcTemplate);
    }

    @Test
    public void findBalanceByUser_returns_correct_balance() {
        BigDecimal expectedBalance = new BigDecimal("1000.00");
        BigDecimal result = sut.findBalanceByUser(1002);
        Assert.assertEquals(expectedBalance, result);
    }

}

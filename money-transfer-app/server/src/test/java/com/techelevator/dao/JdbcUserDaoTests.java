package com.techelevator.dao;


import com.techelevator.tenmo.dao.JdbcUserDao;
import com.techelevator.tenmo.model.User;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.util.List;

public class JdbcUserDaoTests extends BaseDaoTests{

    private JdbcUserDao sut;

    @Before
    public void setup() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        sut = new JdbcUserDao(jdbcTemplate);
    }

    @Test
    public void createNewUser() {
        boolean userCreated = sut.create("TEST_USER","test_password");
        Assert.assertTrue(userCreated);
        User user = sut.findByUsername("TEST_USER");
        Assert.assertEquals("TEST_USER", user.getUsername());
    }

    @Test
    public void findIdByUsername_returns_1001_when_passed_bob() {
        int userId = sut.findIdByUsername("bob");
        Assert.assertEquals(1001, userId);
    }

    @Test
    public void findAll_returns_all_users() {
        List<User> resultList = sut.findAll();
        int expectedSize = 2;
        int resultListSize = resultList.size();
        Assert.assertEquals(expectedSize, resultListSize);
    }

    @Test
    public void findByUsername_returns_correct_User() {
        int expectedId = 1002;
        String expectedPasswordHash = "$2a$10$Ud8gSvRS4G1MijNgxXWzcexeXlVs4kWDOkjE7JFIkNLKEuE57JAEy";
        User user = sut.findByUsername("user");
        Assert.assertEquals(expectedId, user.getId());
        Assert.assertEquals(expectedPasswordHash, user.getPassword());
    }

}

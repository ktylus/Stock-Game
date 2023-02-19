package org.stock_game;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

import static org.stock_game.TestUtilities.TEST_DATABASE;
import static org.stock_game.TestUtilities.TEST_USER;
import static org.stock_game.TestUtilities.NONEXISTENT_USER;

class DAOPasswordTest {

    private DAOPassword daoPassword;

    @BeforeEach
    void setUp() {
        daoPassword = new DAOPassword(TEST_DATABASE);
    }

    @Test
    void getPasswordOfExistingUser() {
        String password = daoPassword.getPassword(TEST_USER);
        String expectedPassword = "d5ac1917e29aa21f93005bec480965c00072a91bf77ba077ab415bb210c73341";
        assertEquals(expectedPassword, password);
    }

    @Test
    void getPasswordOfNonexistentUser() {
        String password = daoPassword.getPassword(NONEXISTENT_USER);
        assertNull(password);
    }

    @Test
    void getPasswordSaltOfExistingUser() {
        String passwordSalt = daoPassword.getPasswordSalt(TEST_USER);
        String expectedSalt = "ae47e4c670f3769891038edc17223af5a449c9c5fdeed8a3599b43a8d4e2609a";
        assertEquals(expectedSalt, passwordSalt);
    }

    @Test
    void getPasswordSaltOfNonexistentUser() {
        String passwordSalt = daoPassword.getPasswordSalt(NONEXISTENT_USER);
        assertNull(passwordSalt);
    }
}
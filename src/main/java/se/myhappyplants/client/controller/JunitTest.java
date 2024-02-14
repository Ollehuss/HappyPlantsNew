package se.myhappyplants.client.controller;

import org.junit.After;
import org.junit.jupiter.api.*;
import static org.mockito.Mockito.*;

import static org.junit.Assert.assertNull;

import se.myhappyplants.client.model.LoggedInUser;
import se.myhappyplants.client.model.RootName;
import se.myhappyplants.client.controller.*;
import se.myhappyplants.client.service.ServerConnection;
import se.myhappyplants.shared.User;


import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JunitTest {
    private LoginPaneController lpc;
    private MainPaneController mpc;

    private StartClient startClient;


    public static void main(String[] args) throws IOException {
        JunitTest testFile = new JunitTest();
        testFile.setup();
        testFile.testLogOutButton();
    }
    public void testLogOutButton() throws IOException {
        setup();
        testWriteEmailToTextFile();
        testSetUserToNull();
        testSetRootToLoginPane();
       // testLogoutButtonPressed();
    }

    @BeforeEach
    public void setup() throws IOException {
        this.lpc = new LoginPaneController();
        this.mpc = new MainPaneController();
        this.startClient = new StartClient();
     //   startClient = mock(StartClient.class);
    }
    @Test
    public void testSetUserToNull() {
       String result = mpc.setUserToNull();
       assertEquals("User set to null", result);
    }
    @Test
    public void testSetRootToLoginPane() throws IOException{
        String result = mpc.setRootToLoginPane();
        if (loginPane.equals(RootName.loginPane.toString())) {
            assertEquals("Root set to loginPane", result);
        }
        assertEquals("Root set to loginPane", result);
        assertEquals(RootName.loginPane.toString(), StartClient.getRoot());    }
    @Test
    public void testWriteEmailToTextFile() throws IOException {
        String email = "test@test.com";
        LoggedInUser.getInstance().setUser(new User(email, "test", "test", true));
        String result = mpc.writeEmailToTextFile();
        assertEquals("Email written to file", result);
    }
   /* @Test
    public void testLogoutButtonPressed() {
    }

    */
}

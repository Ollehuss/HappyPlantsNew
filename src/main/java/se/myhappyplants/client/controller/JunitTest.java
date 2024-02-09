package se.myhappyplants.client.controller;

import org.junit.jupiter.api.*;

import static org.junit.Assert.assertNull;

import se.myhappyplants.client.model.LoggedInUser;
import se.myhappyplants.shared.User;




import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JunitTest {
    private LoginPaneController lpc;
    private MainPaneController mpc;

    private StartClient startClient;


    public static void main(String[] args) throws IOException {
        JunitTest testFile = new JunitTest();
        testFile.setup();
        testFile.testLoginButton();
    }
    public void testLoginButton() throws IOException {
        setup();
        testWriteEmailToTextFile();
        testSetUserToNull();
        testSetRootToLoginPane();
       // testLogoutButtonPressed();
    }

    @BeforeEach
    public void setup() {
        this.lpc = new LoginPaneController();
        this.mpc = new MainPaneController();
        this.startClient = new StartClient();
    }
    @Test
    public void testSetUserToNull() {
       String result = mpc.setUserToNull();
       assertEquals("User set to null", result);
    }
    @Test
    public void testSetRootToLoginPane(){
        LoggedInUser.getInstance().setUser(new LoggedInUser().getUser());
        String result = mpc.setRootToLoginPane();
        assertEquals("Root set to loginPane", result);

    }
    @Test
    public void testWriteEmailToTextFile() {
        String email = "test@test.com";
        LoggedInUser.getInstance().setUser(new User(email, "test", "test", true));
        String result = mpc.writeEmailToTextFile();
        assertEquals("Email written to file", result);
    }
 /*   @Test
    public void testLogoutButtonPressed() {
    }
  */
}

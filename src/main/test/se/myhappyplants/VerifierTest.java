package se.myhappyplants;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.Test;
import se.myhappyplants.client.controller.RegisterPaneController;
import se.myhappyplants.client.model.Verifier;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mockito.Mockito.*;

class VerifierTest {

    //KRAV-ID: ANV06F, ANV07F
    //TEST-ID: T11
    @Test
    void testGetErrorMessage() {
        Verifier verifier = mock(Verifier.class);

        when(verifier.getErrorMessage(Verifier.errorType.WRONG_EMAIL_FORMAT)).thenReturn("Please enter your email address in format: yourname@example.com");
        when(verifier.getErrorMessage(Verifier.errorType.NO_USERNAME)).thenReturn("Please enter a username");
        when(verifier.getErrorMessage(Verifier.errorType.NO_PASSWORD)).thenReturn("Please enter a password");
        when(verifier.getErrorMessage(Verifier.errorType.WRONG_EMAIL)).thenReturn("Please enter the same email twice");
        when(verifier.getErrorMessage(Verifier.errorType.WRONG_PASSWORD)).thenReturn("Please enter the same password twice");

        assertEquals("Please enter your email address in format: yourname@example.com", verifier.getErrorMessage(Verifier.errorType.WRONG_EMAIL_FORMAT));
        assertEquals("Please enter a username", verifier.getErrorMessage(Verifier.errorType.NO_USERNAME));
        assertEquals("Please enter a password", verifier.getErrorMessage(Verifier.errorType.NO_PASSWORD));
        assertEquals("Please enter the same email twice", verifier.getErrorMessage(Verifier.errorType.WRONG_EMAIL));
        assertEquals("Please enter the same password twice", verifier.getErrorMessage(Verifier.errorType.WRONG_PASSWORD));
    }

    //KRAV-ID: ANV06F, ANV07F
    //TEST-ID: T12
    @Test
    void testValidateRegistration() {
        RegisterPaneController registerPaneController = mock(RegisterPaneController.class);
        Verifier verifier = new Verifier();
        when(registerPaneController.getComponentsToVerify()).thenReturn(new String[]{"valid@example.com", "valid@example.com", "username", "password", "password"});
        assertTrue(verifier.validateRegistration(registerPaneController));
        when(registerPaneController.getComponentsToVerify()).thenReturn(new String[]{"invalid", "invalid", "username", "password", "password"});
        assertFalse(verifier.validateRegistration(registerPaneController));
        when(registerPaneController.getComponentsToVerify()).thenReturn(new String[]{"valid@example.com", "valid@example.com", "username", "password", "password2"});
        assertFalse(verifier.validateRegistration(registerPaneController));
        when(registerPaneController.getComponentsToVerify()).thenReturn(new String[]{"valid@example.com", "valid@example.com", "", "password", "password"});
        assertFalse(verifier.validateRegistration(registerPaneController));
    }

    //KRAV-ID: ANV03F
    //TEST-ID: T07
    @Test
    public void testInvalidEmail() {
        Verifier verified = new Verifier();
        assertFalse(verified.validateEmail("test.EmailWHENiswrong.com"));
    }

    //KRAV-ID: ANV03F
    //TEST-ID: T08
    @Test
    public void testvalidEmail() {
        Verifier verified = new Verifier();
        assertTrue(verified.validateEmail("test.EmailWHENisCorrect@correct.com"));
    }
}



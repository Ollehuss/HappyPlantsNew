package se.myhappyplants.Java;

import javafx.embed.swing.JFXPanel;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import se.myhappyplants.client.controller.RegisterPaneController;
import se.myhappyplants.client.model.Verifier;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

class VerifierTest2 {

    //ANV06F
    @BeforeAll
    static void initToolkit() {
        new JFXPanel();
    }

    //ANV06F
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
}

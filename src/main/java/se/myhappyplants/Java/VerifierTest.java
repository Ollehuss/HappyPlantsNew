package se.myhappyplants.Java;

import org.junit.jupiter.api.Test;
import se.myhappyplants.client.model.Verifier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class VerifierTest {

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
}



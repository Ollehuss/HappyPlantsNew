package se.myhappyplants;

import org.junit.jupiter.api.Test;
import se.myhappyplants.shared.User;

import java.io.File;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class UserTest {
    @Test
    public void testSetAvatar_Success() {
        // Arrange
        User user = new User(1, "test@email.com", "testUser", true, true);
        String pathToImg = "path/to/image.jpg";

        // Act
        user.setAvatar(pathToImg);

        // Assert
        String expected = new File(pathToImg).toURI().toString();
        String result = user.getAvatarURL();

        assertEquals(expected, result);
    }

    @Test
    public void testSetAvatar_Failure() {
        // Arrange
        User user = new User(1, "test@email.com", "testUser", true, true);
        String pathToImg = "path/to/image.jpg";

        // Act
        user.setAvatar(pathToImg);

        // Assert
        String expected = new File(pathToImg).toURI().toString();
        String result = user.getAvatarURL() + "xyz";

        assertNotEquals(expected, result);
    }
}

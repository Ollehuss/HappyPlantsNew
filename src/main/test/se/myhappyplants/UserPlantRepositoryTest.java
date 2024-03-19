package se.myhappyplants;

import org.junit.jupiter.api.Test;
import se.myhappyplants.server.services.UserPlantRepository;
import se.myhappyplants.shared.User;

import java.sql.SQLException;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserPlantRepositoryTest {

    //KRAV-ID: SK02F
    //TEST-ID: T21
    @Test
    public void testRunLastWateredQuery_Success() throws SQLException {
        UserPlantRepository userPlantRepository = mock(UserPlantRepository.class);
        User testUser = mock(User.class);
        LocalDate testDate = LocalDate.now();

        String testQuery = userPlantRepository.createLastWateredQuery(testUser, "Valentino's plant", testDate);
        when(userPlantRepository.runLastWateredQuery(testQuery)).thenReturn(true);

        assertTrue(userPlantRepository.runLastWateredQuery(testQuery));
    }

    //KRAV-ID: SK02F
    //TEST-ID: T22
    @Test
    public void testRunLastWateredQuery_Failure() throws SQLException {
        UserPlantRepository userPlantRepository = mock(UserPlantRepository.class);
        User testUser = mock(User.class);
        LocalDate testDate = LocalDate.now();

        String testQuery = userPlantRepository.createLastWateredQuery(testUser, "Valentino's plant", testDate);
        when(userPlantRepository.runLastWateredQuery(testQuery)).thenReturn(false);

        assertFalse(userPlantRepository.runLastWateredQuery(testQuery));
    }

    //KRAV-ID: SK02F
    //TEST-ID: T23
    @Test
    public void replaceSingleQuotesWithDoubleQuotes() {
        UserPlantRepository userPlantRepository = mock(UserPlantRepository.class);
        User testUser = new User(49, "email", "username", false);
        String nickname = "Valentino's plant";
        LocalDate testDate = LocalDate.of(2022, 2, 10);

        String expected = "UPDATE public.user_plant SET last_watered = '2022-02-10' WHERE user_id = 49 AND nickname = 'Valentino''s plant';";

        when(userPlantRepository.createLastWateredQuery(testUser, nickname, testDate)).thenReturn(expected);
        String actual = userPlantRepository.createLastWateredQuery(testUser, nickname, testDate);

        assertEquals(expected, actual);
    }
}

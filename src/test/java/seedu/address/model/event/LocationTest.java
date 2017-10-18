package seedu.address.model.event;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class LocationTest {

    @Test
    public void isValidTitle() {
        // invalid location
        assertFalse(Location.isValidLocation("")); // empty string
        assertFalse(Location.isValidLocation(" ")); // spaces only

        // valid location
        assertTrue(Location.isValidLocation("sentosa")); // alphabets only
        assertTrue(Location.isValidLocation("123456789")); // numbers only
        assertTrue(Location.isValidLocation("This is a testing event to test to accept long location")); // long description
        assertTrue(Location.isValidLocation("Hello World with Test")); // with capital letters
        assertTrue(Location.isValidLocation("5top Test")); // alphanumeric characters
    }
}

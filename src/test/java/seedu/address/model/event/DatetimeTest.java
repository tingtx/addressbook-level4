package seedu.address.model.event;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

//@author kaiyu92
public class DatetimeTest {

    @Test
    public void isValidDate() {
        // invalid datetime
        assertFalse(Datetime.isValidDatetime("")); // empty string
        assertFalse(Datetime.isValidDatetime(" ")); // spaces only

        // valid datetime
        assertTrue(Datetime.isValidDatetime("123456789")); // numbers only
        assertTrue(Datetime
                .isValidDatetime("This is a testing event to test to accept long location")); // long description
        assertTrue(Datetime.isValidDatetime("Hello World with Test")); // with capital letters
        assertTrue(Datetime.isValidDatetime("5top Test")); // alphanumeric characters
    }
}

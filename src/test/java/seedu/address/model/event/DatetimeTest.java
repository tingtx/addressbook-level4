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
        assertTrue(Datetime.isValidDatetime("02-09-2017 2015"));
    }
}

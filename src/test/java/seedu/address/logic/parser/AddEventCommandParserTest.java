package seedu.address.logic.parser;

import org.junit.Test;
import seedu.address.logic.commands.AddEventCommand;

import static seedu.address.commons.core.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.commands.CommandTestUtil.DATETIME_DESC_DEEPAVALI;
import static seedu.address.logic.commands.CommandTestUtil.DESCRIPTION_DESC_DEEPAVALI;
import static seedu.address.logic.commands.CommandTestUtil.LOCATION_DESC_DEEPAVALI;
import static seedu.address.logic.commands.CommandTestUtil.TITLE_DESC_DEEPAVALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DATETIME_DEEPAVALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_DESCRIPTION_DEEPAVALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_LOCATION_DEEPAVALI;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TITLE_DEEPAVALI;
import static seedu.address.logic.parser.CommandParserTestUtil.assertParseFailure;

public class AddEventCommandParserTest {
    private AddEventCommandParser parser = new AddEventCommandParser();

    /*@Test
    public void parse_allFieldsPresent_success() {
        Event expectedEvent = new EventBuilder().withTitle(VALID_TITLE_DEEPAVALI)
                .withDescription(VALID_DESCRIPTION_DEEPAVALI)
                .withLocation(VALID_LOCATION_DEEPAVALI).withDatetime(VALID_DATETIME_DEEPAVALI).build();

        // multiple titles - last title accepted
        assertParseSuccess(parser, AddEventCommand.COMMAND_WORD + TITLE_DESC_SPECTRA + TITLE_DESC_DEEPAVALI
                + DESCRIPTION_DESC_DEEPAVALI
                + LOCATION_DESC_DEEPAVALI + DATETIME_DESC_DEEPAVALI, new AddEventCommand(expectedEvent));

        // [alias] multiple titles - last title accepted
        assertParseSuccess(parser, AddEventCommand.COMMAND_ALIAS + TITLE_DESC_SPECTRA + TITLE_DESC_DEEPAVALI
                + DESCRIPTION_DESC_DEEPAVALI
                + LOCATION_DESC_DEEPAVALI + DATETIME_DESC_DEEPAVALI, new AddEventCommand(expectedEvent));

        // multiple description - last description accepted
        assertParseSuccess(parser, AddEventCommand.COMMAND_WORD + TITLE_DESC_DEEPAVALI + DESCRIPTION_DESC_SPECTRA
                + DESCRIPTION_DESC_DEEPAVALI
                + LOCATION_DESC_DEEPAVALI + DATETIME_DESC_DEEPAVALI, new AddEventCommand(expectedEvent));

        // [alias] multiple description - last description accepted
        assertParseSuccess(parser, AddEventCommand.COMMAND_ALIAS + TITLE_DESC_DEEPAVALI + DESCRIPTION_DESC_SPECTRA
                + DESCRIPTION_DESC_DEEPAVALI
                + LOCATION_DESC_DEEPAVALI + DATETIME_DESC_DEEPAVALI, new AddEventCommand(expectedEvent));

        // multiple locations - last location accepted
        assertParseSuccess(parser, AddEventCommand.COMMAND_WORD + TITLE_DESC_DEEPAVALI + DESCRIPTION_DESC_DEEPAVALI
                + LOCATION_DESC_SPECTRA
                + LOCATION_DESC_DEEPAVALI + DATETIME_DESC_DEEPAVALI, new AddEventCommand(expectedEvent));

        // [alias] multiple locations - last location accepted
        assertParseSuccess(parser, AddEventCommand.COMMAND_ALIAS + TITLE_DESC_DEEPAVALI + DESCRIPTION_DESC_DEEPAVALI
                + LOCATION_DESC_SPECTRA
                + LOCATION_DESC_DEEPAVALI + DATETIME_DESC_DEEPAVALI, new AddEventCommand(expectedEvent));

        // multiple datetime - last datetime accepted
        assertParseSuccess(parser, AddEventCommand.COMMAND_WORD + TITLE_DESC_DEEPAVALI + DESCRIPTION_DESC_DEEPAVALI
                + LOCATION_DESC_DEEPAVALI
                + DATETIME_DESC_SPECTRA + DATETIME_DESC_DEEPAVALI, new AddEventCommand(expectedEvent));

        // [alias] multiple datetime - last datetime accepted
        assertParseSuccess(parser, AddEventCommand.COMMAND_ALIAS + TITLE_DESC_DEEPAVALI + DESCRIPTION_DESC_DEEPAVALI
                + LOCATION_DESC_DEEPAVALI
                + DATETIME_DESC_SPECTRA + DATETIME_DESC_DEEPAVALI, new AddEventCommand(expectedEvent));
    }*/

    /*@Test
    public void parse_optionalFieldsMissing_success() {
        // zero tags
        Event expectedEvent = new EventBuilder().withTitle(VALID_TITLE_SPECTRA)
                .withDescription(VALID_DESCRIPTION_SPECTRA)
                .withLocation(VALID_LOCATION_SPECTRA).withDatetime(VALID_DATETIME_SPECTRA).build();
        // command word
        assertParseSuccess(parser, AddEventCommand.COMMAND_WORD + TITLE_DESC_SPECTRA + DESCRIPTION_DESC_SPECTRA
                + LOCATION_DESC_SPECTRA + DATETIME_DESC_SPECTRA, new AddEventCommand(expectedEvent));

    }*/

    @Test
    public void parse_compulsoryFieldMissing_failure() {
        String expectedMessage = String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE);

        // missing title prefix
        assertParseFailure(parser, AddEventCommand.COMMAND_WORD + VALID_TITLE_DEEPAVALI + DESCRIPTION_DESC_DEEPAVALI
                + LOCATION_DESC_DEEPAVALI + DATETIME_DESC_DEEPAVALI, expectedMessage);

        // missing description prefix
        assertParseFailure(parser, AddEventCommand.COMMAND_WORD + TITLE_DESC_DEEPAVALI + VALID_DESCRIPTION_DEEPAVALI
                + LOCATION_DESC_DEEPAVALI + DATETIME_DESC_DEEPAVALI, expectedMessage);

        // missing location prefix
        assertParseFailure(parser, AddEventCommand.COMMAND_WORD + TITLE_DESC_DEEPAVALI + DESCRIPTION_DESC_DEEPAVALI
                + VALID_LOCATION_DEEPAVALI + DATETIME_DESC_DEEPAVALI, expectedMessage);

        // missing datetime prefix
        assertParseFailure(parser, AddEventCommand.COMMAND_WORD + TITLE_DESC_DEEPAVALI + DESCRIPTION_DESC_DEEPAVALI
                + LOCATION_DESC_DEEPAVALI + VALID_DATETIME_DEEPAVALI, expectedMessage);
    }

    /*@Test
    public void parse_invalidValue_failure() {
        // invalid title
        assertParseFailure(parser, AddEventCommand.COMMAND_WORD + INVALID_TITLE_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_NAME_CONSTRAINTS);

        // [alias] invalid name
        assertParseFailure(parser, AddCommand.COMMAND_ALIAS + INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Name.MESSAGE_NAME_CONSTRAINTS);

        // invalid phone
        assertParseFailure(parser, AddCommand.COMMAND_WORD + NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // [alias] invalid phone
        assertParseFailure(parser, AddCommand.COMMAND_ALIAS + NAME_DESC_BOB + INVALID_PHONE_DESC + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Phone.MESSAGE_PHONE_CONSTRAINTS);

        // invalid email
        assertParseFailure(parser, AddCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC
                + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_EMAIL_CONSTRAINTS);

        // [alias] invalid email
        assertParseFailure(parser, AddCommand.COMMAND_ALIAS + NAME_DESC_BOB + PHONE_DESC_BOB + INVALID_EMAIL_DESC
                + ADDRESS_DESC_BOB + TAG_DESC_HUSBAND + TAG_DESC_FRIEND, Email.MESSAGE_EMAIL_CONSTRAINTS);

        // invalid address
        assertParseFailure(parser, AddCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + INVALID_ADDRESS_DESC + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Address.MESSAGE_ADDRESS_CONSTRAINTS);

        // [alias] invalid address
        assertParseFailure(parser, AddCommand.COMMAND_ALIAS + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                        + INVALID_ADDRESS_DESC + TAG_DESC_HUSBAND + TAG_DESC_FRIEND,
                Address.MESSAGE_ADDRESS_CONSTRAINTS);

        // invalid tag
        assertParseFailure(parser, AddCommand.COMMAND_WORD + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_TAG_CONSTRAINTS);

        // [alias] invalid tag
        assertParseFailure(parser, AddCommand.COMMAND_ALIAS + NAME_DESC_BOB + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + ADDRESS_DESC_BOB + INVALID_TAG_DESC + VALID_TAG_FRIEND, Tag.MESSAGE_TAG_CONSTRAINTS);

        // two invalid values, only first invalid value reported
        assertParseFailure(parser, AddCommand.COMMAND_WORD + INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + INVALID_ADDRESS_DESC, Name.MESSAGE_NAME_CONSTRAINTS);

        // [alias] two invalid values, only first invalid value reported
        assertParseFailure(parser, AddCommand.COMMAND_ALIAS + INVALID_NAME_DESC + PHONE_DESC_BOB + EMAIL_DESC_BOB
                + INVALID_ADDRESS_DESC, Name.MESSAGE_NAME_CONSTRAINTS);
    }*/
}

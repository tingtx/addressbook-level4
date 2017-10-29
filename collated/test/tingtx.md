# tingtx
###### /java/seedu/address/logic/parser/OrderCommandParserTest.java
``` java
public class OrderCommandParserTest {

    private OrderCommandParser parser = new OrderCommandParser();

    @Test
    public void parse_emptyArg_throwsParseException() {
        assertParseFailure(parser, "     ",
                String.format(MESSAGE_INVALID_COMMAND_FORMAT, OrderCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_validArgs_returnsOrderCommand() {

        OrderCommand expectedOrderCommand;

        //one parameter
        expectedOrderCommand = new OrderCommand("BIRTHDAY");

        assertParseSuccess(parser, "BIRTHDAY", expectedOrderCommand); //same parameter

        assertParseSuccess(parser, "birtHDaY", expectedOrderCommand); //case insensitive

        //two parameters
        expectedOrderCommand = new OrderCommand("TAG NAME");

        assertParseSuccess(parser, "TAG NAME", expectedOrderCommand); //same parameter

        assertParseSuccess(parser, "tAG namE", expectedOrderCommand); //case insenstive
    }
}
```
###### /java/seedu/address/logic/commands/OrderCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for {@code OrderCommand}.
 */
public class OrderCommandTest {

    private Model model;
    private Model expectedModel;

    private String firstParameter;
    private String secondParameter;
    private String thirdParameter;
    private String fourthParameter;

    @Before
    public void setUp() {
        firstParameter = "NAME";
        secondParameter = "ADDRESS";
        thirdParameter = "BIRTHDAY";
        fourthParameter = "TAG";

        model = new ModelManager(getTypicalAddressBook(), getTypicalEventBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), model.getEventBook(), new UserPrefs());

    }

    //Interaction with model
    @Test
    public void execute_listIsOrdered_showsEverything() {
        OrderCommand command = prepareCommand(firstParameter);
        assertCommandSuccess(command, model, OrderCommand.MESSAGE_ORDER_SUCCESS
                + firstParameter, expectedModel);
    }

    @Test
    public void execute_invalidParameter_throwsCommandException() {
        OrderCommand command = prepareCommand(" ");
        assertCommandFailure(command, model, OrderCommand.MESSAGE_ORDER_WRONG_PARAMETER);

        command = prepareCommand("EMAIL");
        assertCommandFailure(command, model, OrderCommand.MESSAGE_ORDER_WRONG_PARAMETER);

    }

    @Test
    public void execute_nameParameter_orderSuccess() throws CommandException {
        OrderCommand command = prepareCommand(firstParameter);
        assertOrderSuccess(command, OrderCommand.MESSAGE_ORDER_SUCCESS + firstParameter,
                Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }


    @Test
    public void execute_addressParameter_orderSuccess() throws CommandException {
        OrderCommand command = prepareCommand(secondParameter);
        assertOrderSuccess(command, OrderCommand.MESSAGE_ORDER_SUCCESS + secondParameter,
                Arrays.asList(DANIEL, ALICE, BENSON, GEORGE, FIONA, ELLE, CARL));
    }

    @Test
    public void execute_birthdayParameter_orderSuccess() throws CommandException {
        OrderCommand command = prepareCommand(thirdParameter);
        assertOrderSuccess(command, OrderCommand.MESSAGE_ORDER_SUCCESS + thirdParameter,
                Arrays.asList(GEORGE, ALICE, CARL, DANIEL, BENSON, FIONA, ELLE));
    }


    @Test
    public void execute_tagParameter_orderSuccess() throws CommandException {
        OrderCommand command = prepareCommand(fourthParameter);
        assertOrderSuccess(command, OrderCommand.MESSAGE_ORDER_SUCCESS + fourthParameter,
                Arrays.asList(ALICE, CARL, DANIEL, ELLE, FIONA, GEORGE, BENSON));
    }

    @Test
    public void execute_birthdayTagParameter_orderSuccess() throws CommandException {
        OrderCommand command = prepareCommand(thirdParameter.concat(" " + fourthParameter));
        assertOrderSuccess(command, OrderCommand.MESSAGE_ORDER_SUCCESS + thirdParameter + " "
                        + fourthParameter,
                Arrays.asList(GEORGE, ALICE, CARL, DANIEL, FIONA, BENSON, ELLE));
    }

    @Test
    public void equals() {
        final OrderCommand standardCommand = new OrderCommand(thirdParameter + " " + firstParameter);

        // save values -> returns true
        OrderCommand commandWithSameValues = new OrderCommand("BIRTHDAY NAME");
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        //null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> return false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different parameter -> returns false
        assertFalse(standardCommand.equals(new OrderCommand(fourthParameter + " " + thirdParameter)));

        //different order of parameter -> return false
        assertFalse(standardCommand.equals(new OrderCommand(firstParameter + " " + thirdParameter)));

    }

    private OrderCommand prepareCommand(String parameter) {
        OrderCommand orderCommand = new OrderCommand(parameter);
        orderCommand.setData(model, new CommandHistory(), new UndoRedoStack(), new Config());
        return orderCommand;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     * * - the command feedback is equal to {@code expectedMessage}<br>
     * - the {@code FilteredList<ReadOnlyPerson>} is equal to {@code expectedList}<br>
     */
    private void assertOrderSuccess(OrderCommand command, String expectedMessage, List<ReadOnlyPerson> expectedList)
            throws CommandException {
        CommandResult commandResult = command.executeUndoableCommand();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
    }
}
```
###### /java/seedu/address/model/person/BirthdayTest.java
``` java
public class BirthdayTest {
    @Test
    public void isValidBirthday() {
        // invalid Birthday
        assertFalse(Birthday.isValidBirthday(" ")); // spaces only
        assertFalse(Birthday.isValidBirthday("bi-rt-hday")); // non-numeric
        assertFalse(Birthday.isValidBirthday("bi-10-1994")); // non-numeric
        assertFalse(Birthday.isValidBirthday("12-09-")); // no year
        assertFalse(Birthday.isValidBirthday("12--1996")); // no month
        assertFalse(Birthday.isValidBirthday("-12-1996")); // no date
        assertFalse(Birthday.isValidBirthday("1-12-1996")); // date less than 2 digit
        assertFalse(Birthday.isValidBirthday("01-2-1996")); // month less than 2 digit
        assertFalse(Birthday.isValidBirthday("01-12-199")); // year less than 4 digit
        assertFalse(Birthday.isValidBirthday("0112-1996")); // missing '-'
        assertFalse(Birthday.isValidBirthday("12/12/1996")); // '/' invalid


        // valid Birthday
        assertTrue(Birthday.isValidBirthday("12-12-1994")); // exact dd-mm-yyyy format
        assertTrue(Birthday.isValidBirthday(""));
    }
}
```

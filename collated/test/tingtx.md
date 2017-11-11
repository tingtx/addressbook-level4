# tingtx
###### /java/seedu/address/logic/parser/GeneralBookParserTest.java
``` java
    @Test
    public void parseCommand_group() throws Exception {
        final String group = "TEST";
        final List<Index> indexes = new ArrayList<>();
        indexes.add(INDEX_FIRST_PERSON);
        GroupCommand command = (GroupCommand) parser.parseCommand(GroupCommand.COMMAND_WORD + " "
                + INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_GROUP + group);
        Assert.assertEquals(new GroupCommand(indexes, group), command);

        //alias
        command = (GroupCommand) parser.parseCommand(aliasSettings.getGroupCommand().getAlias()
                + " " + INDEX_FIRST_PERSON.getOneBased() + " " + PREFIX_GROUP + group);
        Assert.assertEquals(new GroupCommand(indexes, group), command);
    }
```
###### /java/seedu/address/logic/parser/GeneralBookParserTest.java
``` java
    @Test
    public void parseCommand_order() throws Exception {
        final String parameter = "NAME";
        OrderCommand command = (OrderCommand) parser.parseCommand(OrderCommand.COMMAND_WORD + " "
                + parameter);
        Assert.assertEquals(new OrderCommand(parameter), command);

        //alias
        command = (OrderCommand) parser.parseCommand(aliasSettings.getOrderCommand().getAlias()
                + " " + parameter);
        Assert.assertEquals(new OrderCommand(parameter), command);
    }
```
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
###### /java/seedu/address/logic/parser/ListCommandParserTest.java
``` java
public class ListCommandParserTest {

    private ListCommandParser parser = new ListCommandParser();

    @Test
    public void parse_noPrefix_throwsParseException() {
        assertParseFailure(parser, "TEST", String.format(MESSAGE_INVALID_COMMAND_FORMAT,
                ListCommand.MESSAGE_USAGE));
    }

    @Test
    public void parse_emptyArg_returnListCommand() {
        ListCommand expectedListCommand = new ListCommand("");
        assertParseSuccess(parser, "     ", expectedListCommand);
    }

    @Test
    public void parse_validArgs_returnsListCommand() {
        ListCommand expectedListCommand = new ListCommand("g/TEST");
        assertParseSuccess(parser, " g/TEST", expectedListCommand);
    }


}
```
###### /java/seedu/address/logic/parser/GroupCommandParserTest.java
``` java
public class GroupCommandParserTest {

    private static final String MESSAGE_INVALID_FORMAT =
            String.format(MESSAGE_INVALID_COMMAND_FORMAT, GroupCommand.MESSAGE_USAGE);
    private GroupCommandParser parser = new GroupCommandParser();

    @Test
    public void parse_invalidPreamble_failure() {

        //no index
        assertParseFailure(parser, " " + GROUP_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // negative index
        assertParseFailure(parser, "-6" + GROUP_DESC_AMY, MESSAGE_INVALID_FORMAT);

        // zero index
        assertParseFailure(parser, "0" + GROUP_DESC_BOB, MESSAGE_INVALID_FORMAT);

        // invalid arguments being parsed as preamble
        assertParseFailure(parser, "some random string", MESSAGE_INVALID_FORMAT);

        // invalid prefix being parsed as preamble
        assertParseFailure(parser, "1 i/ string", MESSAGE_INVALID_FORMAT);

    }

    @Test
    public void parse_noPrefix_failure() {

        assertParseFailure(parser, "1" + VALID_GROUP_AMY, MESSAGE_INVALID_FORMAT);
    }

    @Test
    public void parse_showAllString_success() {
        List<Index> index = new ArrayList<>();
        GroupCommand expectedCommand = new GroupCommand(index, "SHOWALL");
        assertParseSuccess(parser, "SHOWALL", expectedCommand);
    }

    @Test
    public void parse_oneIndex_success() throws IllegalValueException {

        List<Index> index = new ArrayList<>();
        index.add(ParserUtil.parseIndex("1"));

        GroupCommand expectedCommand = new GroupCommand(index, VALID_GROUP_AMY);
        assertParseSuccess(parser, "1" + GROUP_DESC_AMY, expectedCommand);
    }

    @Test
    public void parse_multipleIndex_success() throws IllegalValueException {

        List<Index> index = new ArrayList<>();

        //to group a smaller group of 3 person
        index.add(ParserUtil.parseIndex("1"));
        index.add(ParserUtil.parseIndex("3"));
        index.add(ParserUtil.parseIndex("4"));

        GroupCommand expectedCommand = new GroupCommand(index, VALID_GROUP_AMY);
        assertParseSuccess(parser, "1 3 4" + GROUP_DESC_AMY, expectedCommand);

        //to group a bigger group of 15 person
        for (int i = 5; i < 17; i++) {
            index.add(ParserUtil.parseIndex(Integer.toString(i)));
        }
        expectedCommand = new GroupCommand(index, VALID_GROUP_AMY);
        String userInput = "1 3 4 5 6 7 8 9 10 11 12 13 14 15 16";
        assertParseSuccess(parser, userInput + GROUP_DESC_AMY, expectedCommand);

    }
}
```
###### /java/seedu/address/logic/commands/ListCommandTest.java
``` java
/**
 * Contains integration tests (interaction with the Model) and unit tests for ListCommand.
 */
public class ListCommandTest {

    private Model expectedModel;
    private ListCommand listCommand;
    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalEventBook(), new UserPrefs(),
            new Account(), new Config());

    @Test
    public void execute_listIsNotFiltered_showsSameList() {
        expectedModel = new ModelManager(model.getAddressBook(), model.getEventBook(), new UserPrefs(), new Account(),
                new Config());
        listCommand = new ListCommand("");
        UserPrefs userPrefs = new UserPrefs();
        Config config = new Config();
        Logic logic = null;
        listCommand.setData(model, new CommandHistory(), new UndoRedoStack(), new Config(),
                new UiManager(logic, config, userPrefs));
        assertCommandSuccess(listCommand, model, ListCommand.MESSAGE_LIST_ALL_SUCCESS, expectedModel);
    }

    @Test
    public void execute_listIsFiltered_showsEverything() {
        showFirstPersonOnly(model);
        String expectedMessage = ListCommand.MESSAGE_LIST_ALL_SUCCESS;
        listCommand = prepareCommand("");
        assertListCommandSuccess(listCommand, expectedMessage, model.getFilteredPersonList());
    }


    @Test
    public void execute_listIsFiltered_showsSpecifiedGroup() {
        String expectedMessage = listCommand.MESSAGE_LIST_GROUP_SUCCESS + "NUS";
        listCommand = prepareCommand("g/NUS");
        assertListCommandSuccess(listCommand, expectedMessage, Arrays.asList(ELLE, FIONA));

        expectedMessage = listCommand.MESSAGE_LIST_GROUP_SUCCESS;
        listCommand = prepareCommand("g/");
        assertListCommandSuccess(listCommand, expectedMessage, Arrays.asList(ALICE));
    }

    /**
     * Parses {@code userInput} into a {@code ListCommand}.
     */
    private ListCommand prepareCommand(String userInput) {
        ListCommand command = new ListCommand(userInput);
        UserPrefs userPrefs = new UserPrefs();
        Config config = new Config();
        Logic logic = null;
        command.setData(model, new CommandHistory(), new UndoRedoStack(), new Config(),
                new UiManager(logic, config, userPrefs));
        return command;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     * - the command feedback is equal to {@code expectedMessage}<br>
     * - the {@code FilteredList<ReadOnlyPerson>} is equal to {@code expectedList}<br>
     * - the {@code AddressBook} in model remains the same after executing the {@code command}
     */
    private void assertListCommandSuccess(ListCommand command, String expectedMessage,
                                          List<ReadOnlyPerson> expectedList) {
        AddressBook expectedAddressBook = new AddressBook(model.getAddressBook());

        ContainsKeywordsPredicate.setPredicateType('g');
        CommandResult commandResult = command.execute();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
        assertEquals(expectedAddressBook, model.getAddressBook());
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

        model = new ModelManager(getTypicalAddressBook(), getTypicalEventBook(), new UserPrefs(), new Account(),
                new Config());
        expectedModel = new ModelManager(model.getAddressBook(), model.getEventBook(), new UserPrefs(), new Account(),
                new Config());

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


    /**
     * Generates a new OrderCommand
     */
    private OrderCommand prepareCommand(String parameter) {
        OrderCommand orderCommand = new OrderCommand(parameter);
        UserPrefs userPrefs = new UserPrefs();
        Config config = new Config();
        Logic logic = null;
        orderCommand.setData(model, new CommandHistory(), new UndoRedoStack(), new Config(),
                new UiManager(logic, config, userPrefs));
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
###### /java/seedu/address/logic/commands/GroupCommandTest.java
``` java
public class GroupCommandTest {

    private Model model = new ModelManager(getTypicalAddressBook(), getTypicalEventBook(), new UserPrefs(), new
            Account(), new Config());

    @Test
    public void execute_showAllGroups_success() throws Exception {
        List<Index> indexes = new ArrayList<>();
        String showAll = "SHOWALL";

        GroupCommand groupCommand = prepareCommand(indexes, showAll);

        String expectedMessage = "Groups:  " + model.getGroupList().toString().replaceAll("\\[", "")
                .replaceAll("\\]", "");

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()),
                new EventBook(model.getEventBook()), new UserPrefs(), new Account(), new Config());
        expectedModel.getGroupList();

        assertCommandSuccess(groupCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_groupOnePerson_success() throws Exception {
        Person groupedPerson = new PersonBuilder(model.getFilteredPersonList()
                .get(TypicalIndexes.INDEX_FIRST_PERSON.getZeroBased()))
                .withGroup("JUNITTest").build();
        List<Index> indexes = new ArrayList<>();
        indexes.add(INDEX_FIRST_PERSON);

        GroupCommand groupCommand = prepareCommand(indexes, groupedPerson.getGroup().value);

        String expectedMessage = GroupCommand.MESSAGE_GROUP_PERSON_SUCCESS + "JUNITTest";

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()),
                new EventBook(model.getEventBook()), new UserPrefs(), new Account(), new Config());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), groupedPerson);

        assertCommandSuccess(groupCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_groupMultiPersons_success() throws Exception {
        Person groupedPersonOne = new PersonBuilder(model.getFilteredPersonList()
                .get(TypicalIndexes.INDEX_FIRST_PERSON.getZeroBased()))
                .withGroup("JUNITTest").build();
        Person groupedPersonTwo = new PersonBuilder(model.getFilteredPersonList()
                .get(TypicalIndexes.INDEX_SECOND_PERSON.getZeroBased()))
                .withGroup("JUNITTest").build();
        Person groupedPersonThree = new PersonBuilder(model.getFilteredPersonList()
                .get(TypicalIndexes.INDEX_THIRD_PERSON.getZeroBased()))
                .withGroup("JUNITTest").build();

        List<Index> indexes = new ArrayList<>();
        indexes.add(INDEX_FIRST_PERSON);
        indexes.add(INDEX_SECOND_PERSON);
        indexes.add(INDEX_THIRD_PERSON);

        GroupCommand groupCommand = prepareCommand(indexes, groupedPersonOne.getGroup().value);

        String expectedMessage = GroupCommand.MESSAGE_GROUP_PERSON_SUCCESS + "JUNITTest";

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()),
                new EventBook(model.getEventBook()), new UserPrefs(), new Account(), new Config());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), groupedPersonOne);
        expectedModel.updatePerson(model.getFilteredPersonList().get(1), groupedPersonTwo);
        expectedModel.updatePerson(model.getFilteredPersonList().get(2), groupedPersonThree);


        assertCommandSuccess(groupCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_ungroupOnePerson_success() throws Exception {
        Person groupedPerson = new PersonBuilder(model.getFilteredPersonList()
                .get(TypicalIndexes.INDEX_FIRST_PERSON.getZeroBased()))
                .withGroup("").build();
        List<Index> indexes = new ArrayList<>();
        indexes.add(INDEX_FIRST_PERSON);

        GroupCommand groupCommand = prepareCommand(indexes, "");

        String expectedMessage = GroupCommand.MESSAGE_UNGROUP_PERSON_SUCCESS;

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()),
                new EventBook(model.getEventBook()), new UserPrefs(), new Account(), new Config());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), groupedPerson);

        assertCommandSuccess(groupCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_ungroupMultiPersons_success() throws Exception {
        Person groupedPersonOne = new PersonBuilder(model.getFilteredPersonList()
                .get(TypicalIndexes.INDEX_FIRST_PERSON.getZeroBased()))
                .withGroup("").build();
        Person groupedPersonTwo = new PersonBuilder(model.getFilteredPersonList()
                .get(TypicalIndexes.INDEX_SECOND_PERSON.getZeroBased()))
                .withGroup("").build();
        Person groupedPersonThree = new PersonBuilder(model.getFilteredPersonList()
                .get(TypicalIndexes.INDEX_THIRD_PERSON.getZeroBased()))
                .withGroup("").build();
        List<Index> indexes = new ArrayList<>();
        indexes.add(INDEX_FIRST_PERSON);
        indexes.add(INDEX_SECOND_PERSON);
        indexes.add(INDEX_THIRD_PERSON);

        GroupCommand groupCommand = prepareCommand(indexes, groupedPersonOne.getGroup().value);

        String expectedMessage = GroupCommand.MESSAGE_UNGROUP_PERSON_SUCCESS;

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()),
                new EventBook(model.getEventBook()), new UserPrefs(), new Account(), new Config());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), groupedPersonOne);
        expectedModel.updatePerson(model.getFilteredPersonList().get(1), groupedPersonTwo);
        expectedModel.updatePerson(model.getFilteredPersonList().get(2), groupedPersonThree);


        assertCommandSuccess(groupCommand, model, expectedMessage, expectedModel);
    }

    @Test
    public void execute_filteredList_success() throws Exception {
        showFirstPersonOnly(model);

        ReadOnlyPerson personInFilteredList = model.getFilteredPersonList()
                .get(TypicalIndexes.INDEX_FIRST_PERSON.getZeroBased());
        Person groupedPerson = new PersonBuilder(personInFilteredList).withGroup("JUNITTest").build();
        List<Index> indexes = new ArrayList<>();
        indexes.add(INDEX_FIRST_PERSON);

        GroupCommand groupCommand = prepareCommand(indexes, groupedPerson.getGroup().value);

        String expectedMessage = GroupCommand.MESSAGE_GROUP_PERSON_SUCCESS + "JUNITTest";

        Model expectedModel = new ModelManager(new AddressBook(model.getAddressBook()),
                new EventBook(model.getEventBook()), new UserPrefs(), new Account(), new Config());
        expectedModel.updatePerson(model.getFilteredPersonList().get(0), groupedPerson);

        assertCommandSuccess(groupCommand, model, expectedMessage, expectedModel);
    }


    @Test
    public void execute_invalidPersonIndexUnfilteredList_failure() throws Exception {
        Index outOfBoundIndex = Index.fromOneBased(model.getFilteredPersonList().size() + 1);
        List<Index> indexes = new ArrayList<>();
        indexes.add(outOfBoundIndex);

        GroupCommand groupCommand = prepareCommand(indexes, VALID_GROUP_AMY);

        assertCommandFailure(groupCommand, model,
                "Index " + outOfBoundIndex.toString() + " is invalid!");
    }

    /**
     * Edit filtered list where index is larger than size of filtered list,
     * but smaller than size of address book
     */
    @Test
    public void execute_invalidPersonIndexFilteredList_failure() throws Exception {
        showFirstPersonOnly(model);
        Index outOfBoundIndex = TypicalIndexes.INDEX_SECOND_PERSON;
        List<Index> indexes = new ArrayList<>();
        indexes.add(outOfBoundIndex);

        //ensures that outOfBoundIndex is still in bounds of address book list
        assertTrue(outOfBoundIndex.getZeroBased() < model.getAddressBook().getPersonList().size());

        GroupCommand groupCommand = prepareCommand(indexes, VALID_GROUP_AMY);

        assertCommandFailure(groupCommand, model,
                "Index " + outOfBoundIndex.toString() + " is invalid!");
    }


    @Test
    public void equals() {
        List<Index> indexes = new ArrayList<>();
        indexes.add(INDEX_FIRST_PERSON);
        final GroupCommand standardCommand = new GroupCommand(indexes, ALICE.getGroup().value);


        // same values -> returns true
        GroupCommand commandWithSameValues = new GroupCommand(indexes,  ALICE.getGroup().value);
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        //null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> return false
        assertFalse(standardCommand.equals(new RemarkCommand(INDEX_FIRST_PERSON,
                new Remark(VALID_REMARK_AMY))));

        // different index -> returns false
        List<Index> tempIndexes = new ArrayList<>();
        tempIndexes.add(INDEX_SECOND_PERSON);
        GroupCommand commandWithDiffIndex = new GroupCommand(tempIndexes, ALICE.getGroup().value);
        assertFalse(standardCommand.equals(commandWithDiffIndex));

        // different descriptor -> returns false
        tempIndexes.remove(INDEX_SECOND_PERSON);
        tempIndexes.add(INDEX_FIRST_PERSON);
        GroupCommand commandWithNoDesc = new GroupCommand(tempIndexes, BENSON.getGroup().value);
        assertFalse(standardCommand.equals(commandWithNoDesc));
    }

    /**
     * Returns an {@code GroupCommand} with parameters {@code index} and {@code Group}
     */
    private GroupCommand prepareCommand(List<Index> index, String group) {
        GroupCommand groupCommand = new GroupCommand(index, group);
        UserPrefs userPrefs = new UserPrefs();
        Config config = new Config();
        Logic logic = null;
        groupCommand.setData(model, new CommandHistory(), new UndoRedoStack(), new Config(),
                new UiManager(logic, config, userPrefs));
        return groupCommand;
    }
}
```
###### /java/seedu/address/model/group/GroupTest.java
``` java
public class GroupTest {
    @Test
    public void isValidGroup() {

        //valid group
        assertTrue(Group.isValidGroup(" ")); //blank group
        assertTrue(Group.isValidGroup("")); //empty group
        assertTrue(Group.isValidGroup("012345678901234")); // fifteen characters
        assertTrue(Group.isValidGroup("012345678901234567890123456789")); // thirty characters
        assertTrue(Group.isValidGroup("A*&_)><.?/1 ")); //any characters

        //invalid group
        assertFalse(Group.isValidGroup("0123456789012345678901234567890")); //thirty-one characters
        assertFalse(Group.isValidGroup("01234567890123456789012345678901")); //thirty-one characters
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
        assertFalse(Birthday.isValidBirthday("12- -1996")); // no month
        assertFalse(Birthday.isValidBirthday("-12-1996")); // no date
        assertFalse(Birthday.isValidBirthday("1-12-1996")); // date less than 2 digit
        assertFalse(Birthday.isValidBirthday("01-2-1996")); // month less than 2 digit
        assertFalse(Birthday.isValidBirthday("01-12-199")); // year less than 4 digit
        assertFalse(Birthday.isValidBirthday("01121996")); // missing '-'
        assertFalse(Birthday.isValidBirthday("12/12/1996")); // '/' invalid


        // valid Birthday
        assertTrue(Birthday.isValidBirthday("12-12-1994")); // exact dd-mm-yyyy format
        assertTrue(Birthday.isValidBirthday(""));
    }
}
```
###### /java/seedu/address/model/person/ContainsKeywordsPredicateTest.java
``` java
    @Test
    public void test_groupContainsKeywords_returnsTrue() {
        ContainsKeywordsPredicate.setPredicateType('g');

        // Zero keywords
        ContainsKeywordsPredicate predicate = new ContainsKeywordsPredicate(Arrays.asList(""));
        assertTrue(predicate.test(new PersonBuilder().withGroup("").build()));

        // One keyword
        predicate = new ContainsKeywordsPredicate(Collections.singletonList("TEST"));
        assertTrue(predicate.test(new PersonBuilder().withGroup("TEST").build()));

    }

    @Test
    public void test_groupDoesNotContainKeywords_returnsFalse() {
        ContainsKeywordsPredicate.setPredicateType('g');

        // Non-matching keyword
        ContainsKeywordsPredicate predicate = new ContainsKeywordsPredicate(Arrays.asList("TEST2"));
        assertFalse(predicate.test(new PersonBuilder().withGroup("").build()));

        // Keywords case sensitive
        predicate = new ContainsKeywordsPredicate(Arrays.asList("TEST"));
        assertFalse(predicate.test(new PersonBuilder().withGroup("test").build()));
    }
}
```
###### /java/seedu/address/testutil/PersonBuilder.java
``` java

    /**
     * Sets the {@code Birthday} of the {@code Person} that we are building.
     */
    public PersonBuilder withBirthday(String birthday) {
        try {
            this.person.setBirthday(new Birthday(birthday));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("birthday is expected to be unique.");
        }
        return this;
    }

    /**
     * Sets the {@code Group} of the {@code Person} that we are building.
     */
    public PersonBuilder withGroup(String group) {
        try {
            this.person.setGroup(new Group(group));
        } catch (IllegalValueException ive) {
            throw new IllegalArgumentException("group name cannot exceed 30 characters");
        }

        return this;
    }
```

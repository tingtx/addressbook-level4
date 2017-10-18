package seedu.address.logic.commands;

import static junit.framework.TestCase.assertFalse;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandSuccess;
import static seedu.address.testutil.TypicalPersons.ALICE;
import static seedu.address.testutil.TypicalPersons.BENSON;
import static seedu.address.testutil.TypicalPersons.CARL;
import static seedu.address.testutil.TypicalPersons.DANIEL;
import static seedu.address.testutil.TypicalPersons.ELLE;
import static seedu.address.testutil.TypicalPersons.FIONA;
import static seedu.address.testutil.TypicalPersons.GEORGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import seedu.address.logic.CommandHistory;
import seedu.address.logic.UndoRedoStack;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.person.ReadOnlyPerson;

public class OrderCommandTest {

    private Model model;
    private Model expectedModel;

    private String firstParameter;
    private String secondParameter;
    private String thirdParameter;

    @Before
    public void setUp() {
        firstParameter = "NAME";
        secondParameter = "ADDRESS";
        thirdParameter = "TAG";

        model = new ModelManager(getTypicalAddressBook(), new UserPrefs());
        expectedModel = new ModelManager(model.getAddressBook(), new UserPrefs());

    }

    //Interaction with model
    @Test
    public void execute_listIsOrdered_showsEverything() {
        OrderCommand command = prepareCommand(firstParameter);
        assertCommandSuccess(command, model, OrderCommand.MESSAGE_SORT_SUCCESS
                        + firstParameter, expectedModel);
    }

    @Test
    public void execute_zeroParameter_listNotSorted() {
        OrderCommand command = prepareCommand(" ");
        assertOrderSuccess(command, OrderCommand.MESSAGE_SORT_WRONG_PARAMETER,
                Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }

    @Test
    public void execute_nameParameter_listSorted() {
        OrderCommand command = prepareCommand(firstParameter);
        assertOrderSuccess(command, OrderCommand.MESSAGE_SORT_SUCCESS + firstParameter,
                Arrays.asList(ALICE, BENSON, CARL, DANIEL, ELLE, FIONA, GEORGE));
    }


    @Test
    public void execute_addressParameter_listSorted() {
        OrderCommand command = prepareCommand(secondParameter);
        assertOrderSuccess(command, OrderCommand.MESSAGE_SORT_SUCCESS + secondParameter,
                Arrays.asList(DANIEL, ALICE, BENSON, GEORGE, FIONA, ELLE, CARL));
    }

    @Test
    public void execute_tagParameter_listSorted() {
        OrderCommand command = prepareCommand(thirdParameter);
        assertOrderSuccess(command, OrderCommand.MESSAGE_SORT_SUCCESS + thirdParameter,
                Arrays.asList(ALICE, CARL, DANIEL, ELLE, FIONA, GEORGE, BENSON));
    }

    @Test
    public void equals() {
        final OrderCommand standardCommand = new OrderCommand(firstParameter + " " + secondParameter);

        // save values -> returns true
        OrderCommand commandWithSameValues = new OrderCommand("NAME ADDRESS");
        assertTrue(standardCommand.equals(commandWithSameValues));

        // same object -> returns true
        assertTrue(standardCommand.equals(standardCommand));

        //null -> returns false
        assertFalse(standardCommand.equals(null));

        // different types -> return false
        assertFalse(standardCommand.equals(new ClearCommand()));

        // different parameter -> returns false
        assertFalse(standardCommand.equals(new OrderCommand(firstParameter + " " + thirdParameter)));

        //different order of parameter -> return false
        assertFalse(standardCommand.equals(new OrderCommand(secondParameter + " " + firstParameter)));

    }

    private OrderCommand prepareCommand(String parameter) {
        OrderCommand orderCommand = new OrderCommand(parameter);
        orderCommand.setData(model, new CommandHistory(), new UndoRedoStack());
        return orderCommand;
    }

    /**
     * Asserts that {@code command} is successfully executed, and<br>
     ** - the command feedback is equal to {@code expectedMessage}<br>
     * - the {@code FilteredList<ReadOnlyPerson>} is equal to {@code expectedList}<br>
     */
    private void assertOrderSuccess(OrderCommand command, String expectedMessage, List<ReadOnlyPerson> expectedList) {
        CommandResult commandResult = command.executeUndoableCommand();

        assertEquals(expectedMessage, commandResult.feedbackToUser);
        assertEquals(expectedList, model.getFilteredPersonList());
    }
}

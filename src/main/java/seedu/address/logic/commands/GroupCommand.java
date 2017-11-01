package seedu.address.logic.commands;

import seedu.address.commons.core.index.Index;
import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.group.Group;
import seedu.address.model.person.Person;
import seedu.address.model.person.ReadOnlyPerson;
import seedu.address.model.person.exceptions.PersonNotFoundException;

import java.util.List;

import static java.util.Objects.requireNonNull;
import static seedu.address.logic.parser.CliSyntax.PREFIX_GROUP;
import static seedu.address.model.Model.PREDICATE_SHOW_ALL_PERSONS;

//@@author tingtx

/**
 * Group person(s) into user defined group.
 */
public class GroupCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "group";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Group the person(s) identified "
            + "by the index number used in the last person listing.\n "
            + "Person(s) will be grouped to group name specified.\n"
            + "Parameters: INDEX_1 [INDEX_2...] (must be a positive integer) "
            + PREFIX_GROUP + "GROUPNAME "
            + "Example: " + COMMAND_WORD + " 1 3 4 "
            + PREFIX_GROUP + "FAMILY";

    public static final String MESSAGE_GROUP_PERSON_SUCCESS = "Grouped Person(s) to ";
    public static final String MESSAGE_UNGROUP_PERSON_SUCCESS = "Person(s) removed from group.";

    private final List<Index> indexes;
    private final Group group;

    /**
     * @param indexes of the person in the filtered person list to edit
     * @param group   group name to group the person with
     */
    public GroupCommand(List<Index> indexes, Group group) {
        requireNonNull(indexes);
        requireNonNull(group);

        this.indexes = indexes;
        this.group = group;
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        for (Index index : indexes) {

            if (index.getZeroBased() >= lastShownList.size()) {
                throw new CommandException("Index " + index.toString() + " is invalid!");
            }

            Person personToGroup = (Person) lastShownList.get(index.getZeroBased());

            try {
                model.groupPerson(personToGroup, group);
            } catch (PersonNotFoundException pnfe) {
                throw new AssertionError("The person with index " + index.toString()
                        + " cannot be missing");
            }

        }
        model.updateFilteredPersonList(PREDICATE_SHOW_ALL_PERSONS);
        return new CommandResult(generateSuccessMessage());
    }

    /**
     * @return the generated message
     */
    private String generateSuccessMessage() {
        if (!group.value.isEmpty()) {
            return MESSAGE_GROUP_PERSON_SUCCESS + group.value;
        } else {
            return MESSAGE_UNGROUP_PERSON_SUCCESS;
        }
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof GroupCommand)) {
            return false;
        }

        // state check
        GroupCommand groupCommand = (GroupCommand) other;
        return indexes.equals(groupCommand.indexes)
                && group.equals(groupCommand.group);
    }
}

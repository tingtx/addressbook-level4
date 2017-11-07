# tingtx
###### /java/seedu/address/commons/core/index/Index.java
``` java
    @Override
    public String toString() {
        return Integer.toString(getOneBased());
    }
}
```
###### /java/seedu/address/logic/parser/GroupCommandParser.java
``` java

/**
 * Parses input arguments and creates a new GroupCommand object
 */
public class GroupCommandParser implements Parser<GroupCommand> {

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean isPrefixesPresent(ArgumentMultimap argumentMultimap, Prefix prefix) {
        return Stream.of(prefix).allMatch(groupPrefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses the given {@code String} of arguments in the context of the GroupCommand
     * and returns an GroupCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public GroupCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_GROUP);

        String preamble;
        String[] indexStr;
        List<Index> indexes = new ArrayList<>();
        try {
            preamble = argMultimap.getPreamble();
            indexStr = preamble.split("\\s+");
            for (String index : indexStr) {
                indexes.add(ParserUtil.parseIndex(index));
            }

        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, GroupCommand.MESSAGE_USAGE));
        }

        if (!isPrefixesPresent(argMultimap, PREFIX_GROUP)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, GroupCommand.MESSAGE_USAGE));
        }

        String group = argMultimap.getValue(PREFIX_GROUP).get();

        return new GroupCommand(indexes, new Group(group));
    }
}
```
###### /java/seedu/address/logic/commands/OrderCommand.java
``` java
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OrderCommand // instanceof handles nulls
                && this.orderParameter.equals(((OrderCommand) other).orderParameter)); // state check
    }
}
```
###### /java/seedu/address/logic/commands/GroupCommand.java
``` java

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
```
###### /java/seedu/address/model/group/Group.java
``` java
package seedu.address.model.group;

import static java.util.Objects.requireNonNull;

/**
 * Represents a Person's group in the address book.
 */
public class Group {

    public static final String MESSAGE_GROUP_CONSTRAINTS =
            "Group name can take any values";

    public final String value;

    public Group(String group) {
        requireNonNull(group);
        this.value = group;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Group //instanceof handles null
                && this.value.equals(((Group) other).value)); // state check
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
```
###### /java/seedu/address/model/person/ReadOnlyPerson.java
``` java
    ObjectProperty<Birthday> birthdayProperty();

    Birthday getBirthday();

    ObjectProperty<Group> groupProperty();

    Group getGroup();
```
###### /java/seedu/address/model/person/UniquePersonList.java
``` java

    /**
     * Group the person {@code target} in the list to {@code group}.
     *
     * @throws PersonNotFoundException if {@code target} could not be found in the list.
     */
    public void groupPerson(Person target, Group group) throws PersonNotFoundException {
        requireNonNull(group);
        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new PersonNotFoundException();
        }

        target.setGroup(group);

    }
```
###### /java/seedu/address/model/person/UniquePersonList.java
``` java

    /**
     * Order the list.
     */
    public void orderBy(String parameter) throws UnrecognisedParameterException {
        Comparator<Person> orderByName = (Person a, Person b) -> a.getName().toString()
                .compareToIgnoreCase(b.getName().toString());
        Comparator<Person> orderByAddress = (Person a, Person b) -> a.getAddress().toString()
                .compareToIgnoreCase(b.getAddress().toString());
        Comparator<Person> orderByBirthday = comparing(a -> a.getBirthday().getReformatDate(),
                nullsLast(naturalOrder()));
        Comparator<Person> orderByTag = (Person a, Person b) -> a.getTags().toString()
                .compareToIgnoreCase(b.getTags().toString());

        switch (parameter) {
        case "NAME":
            internalList.sort(orderByName);
            break;

        case "ADDRESS":
            internalList.sort(orderByAddress);
            break;

        case "BIRTHDAY":
            internalList.sort(orderByBirthday);
            break;

        case "TAG":
            internalList.sort(orderByTag);
            break;

        case "NAME ADDRESS":
            internalList.sort(orderByName.thenComparing(orderByAddress));
            break;

        case "ADDRESS NAME":
            internalList.sort(orderByName.thenComparing(orderByTag));
            break;

        case "TAG NAME":
            internalList.sort(orderByTag.thenComparing(orderByName));
            break;

        case "NAME TAG":
            internalList.sort(orderByName.thenComparing(orderByTag));
            break;

        case "NAME BIRTHDAY":
            internalList.sort(orderByName.thenComparing(orderByBirthday));
            break;

        case "BIRTHDAY NAME":
            internalList.sort(orderByBirthday.thenComparing(orderByName));
            break;

        case "ADDRESS BIRTHDAY":
            internalList.sort(orderByAddress.thenComparing(orderByBirthday));
            break;

        case "BIRTHDAY ADDRESS":
            internalList.sort(orderByBirthday.thenComparing(orderByAddress));
            break;

        case "BIRTHDAY TAG":
            internalList.sort(nullsLast(orderByBirthday.thenComparing(orderByTag)));
            break;

        case "TAG BIRTHDAY":
            internalList.sort(orderByTag.thenComparing(orderByBirthday));
            break;

        default:
            throw new UnrecognisedParameterException();
        }

    }
```
###### /java/seedu/address/model/person/Person.java
``` java
    public void setBirthday(Birthday birthday) {
        this.birthday.set(requireNonNull(birthday));
    }

    @Override
    public ObjectProperty<Group> groupProperty() {
        return group;
    }

    @Override
    public Group getGroup() {
        return group.get();
    }

    public void setGroup(Group group) {
        this.group.set(requireNonNull(group));
    }
```
###### /java/seedu/address/model/Model.java
``` java

    /**
     * Group the given person(s)
     */
    void groupPerson(Person target, Group group) throws PersonNotFoundException;
```

# kaiyu92
###### /java/seedu/address/commons/util/StringUtil.java
``` java
    /**
     * Returns true if the {@code sentence} contains the {@code word}.
     * Ignores case, but a full word match is required.
     * <br>examples:<pre>
     *       containsWordIgnoreCase("ABc def", "abc") == true
     *       containsWordIgnoreCase("ABc def", "DEF") == true
     *       containsWordIgnoreCase("ABc def", "AB") == false //not a full word match
     *       </pre>
     *
     * @param sentence cannot be null
     * @param word     cannot be null, cannot be empty, must be a single word
     */
    public static boolean containsWordIgnoreCase(String sentence, String word) {
        requireNonNull(sentence);
        requireNonNull(word);

        String preppedWord = word.trim();
        checkArgument(!preppedWord.isEmpty(), "Word parameter cannot be empty");
        checkArgument(preppedWord.split("\\s+").length == 1, "Word parameter should be a single word");

        String preppedSentence = sentence;
        String[] wordsInPreppedSentence = preppedSentence.split("\\s+");

        for (String wordInSentence : wordsInPreppedSentence) {
            if (wordInSentence.toLowerCase().indexOf(preppedWord.toLowerCase()) >= 0) {
                return true;
            }
            //if (wordInSentence.equalsIgnoreCase(preppedWord)) {
            //      return true;
            //}
        }
        return false;
    }

    /**
     * Returns a detailed message of the t, including the stack trace.
     */
    public static String getDetails(Throwable t) {
        requireNonNull(t);
        StringWriter sw = new StringWriter();
        t.printStackTrace(new PrintWriter(sw));
        return t.getMessage() + "\n" + sw.toString();
    }

    /**
     * Returns true if {@code s} represents a non-zero unsigned integer
     * e.g. 1, 2, 3, ..., {@code Integer.MAX_VALUE} <br>
     * Will return false for any other non-null string input
     * e.g. empty string, "-1", "0", "+1", and " 2 " (untrimmed), "3 0" (contains whitespace), "1 a" (contains letters)
     *
     * @throws NullPointerException if {@code s} is null.
     */
    public static boolean isNonZeroUnsignedInteger(String s) {
        requireNonNull(s);

        try {
            int value = Integer.parseInt(s);
            return value > 0 && !s.startsWith("+"); // "+1" is successfully parsed by Integer#parseInt(String)
        } catch (NumberFormatException nfe) {
            return false;
        }
    }
}
```
###### /java/seedu/address/logic/commands/AddEventCommand.java
``` java
/**
 * Adds a event to the event book.
 */
public class AddEventCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "addevent";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Adds a event to the event book. "
            + "Parameters: "
            + PREFIX_TITLE + "NAME "
            + PREFIX_DESCRIPTION + "DESCRIPTION "
            + PREFIX_LOCATION + "LOCATION "
            + PREFIX_DATETIME + "DATETIME\n"
            + "Example: " + COMMAND_WORD + " "
            + PREFIX_TITLE + "Halloween Horror Night "
            + PREFIX_DESCRIPTION + "Horrifying night "
            + PREFIX_LOCATION + "Universal Studio "
            + PREFIX_DATETIME + "13/10/17 2359";

    public static final String MESSAGE_SUCCESS = "New event added: %1$s";
    public static final String MESSAGE_DUPLICATE_EVENT = "This event already exists in the event book";

    private final Event toAdd;

    /**
     * Creates an AddEventCommand to add the specified {@code ReadOnlyEvent}
     */
    public AddEventCommand(ReadOnlyEvent event) {
        toAdd = new Event(event);
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        requireNonNull(model);
        try {
            model.addEvent(toAdd);
            return new CommandResult(String.format(MESSAGE_SUCCESS, toAdd));
        } catch (DuplicateEventException e) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        }
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof AddEventCommand // instanceof handles nulls
                && toAdd.equals(((AddEventCommand) other).toAdd));
    }
}
```
###### /java/seedu/address/logic/commands/DeleteEventCommand.java
``` java
/**
 * Deletes a event identified using it's last displayed index from the event book.
 */
public class DeleteEventCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "deleteevent";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Deletes the event identified by the index number used in the last event listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_DELETE_EVENT_SUCCESS = "Deleted Event: %1$s";

    private final Index targetIndex;

    public DeleteEventCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyEvent> lastShownList = model.getFilteredEventList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        ReadOnlyEvent eventToDelete = lastShownList.get(targetIndex.getZeroBased());

        try {
            model.deleteEvent(eventToDelete);
        } catch (EventNotFoundException enfe) {
            assert false : "The target event cannot be missing";
        }

        return new CommandResult(String.format(MESSAGE_DELETE_EVENT_SUCCESS, eventToDelete));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof DeleteEventCommand // instanceof handles nulls
                && this.targetIndex.equals(((DeleteEventCommand) other).targetIndex)); // state check
    }
}
```
###### /java/seedu/address/logic/commands/EditEventCommand.java
``` java
/**
 * Edits the details of an existing event in the event book.
 */
public class EditEventCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "editevent";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the details of the event identified "
            + "by the index number used in the last event listing. "
            + "Existing values will be overwritten by the input values.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + "[" + PREFIX_TITLE + "TITLE] "
            + "[" + PREFIX_DESCRIPTION + "DESCRIPTION] "
            + "[" + PREFIX_LOCATION + "LOCATION] "
            + "[" + PREFIX_DATETIME + "DATETIME]...\n "
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DESCRIPTION + "one for one free drinks "
            + PREFIX_LOCATION + "Tampines Mall Basement";

    public static final String MESSAGE_EDIT_EVENT_SUCCESS = "Edited Event: %1$s";
    public static final String MESSAGE_NOT_EDITED = "At least one field to edit must be provided.";
    public static final String MESSAGE_DUPLICATE_EVENT = "This event already exists in the event book.";

    private final Index index;
    private final EditEventDescriptor editEventDescriptor;

    /**
     * @param index               of the event in the filtered event list to edit
     * @param editEventDescriptor details to edit the event with
     */
    public EditEventCommand(Index index, EditEventDescriptor editEventDescriptor) {
        requireNonNull(index);
        requireNonNull(editEventDescriptor);

        this.index = index;
        this.editEventDescriptor = editEventDescriptor;
    }

    /**
     * Creates and returns a {@code Event} with the details of {@code eventToEdit}
     * edited with {@code editEventDescriptor}.
     */
    private static Event createEditedEvent(ReadOnlyEvent eventToEdit,
                                           EditEventDescriptor editEventDescriptor) {
        assert eventToEdit != null;

        Title updatedTitle = editEventDescriptor.getTitle().orElse(eventToEdit.getTitle());
        Description updatedDescription = editEventDescriptor.getDescription().orElse(eventToEdit.getDescription());
        Location updatedLocation = editEventDescriptor.getLocation().orElse(eventToEdit.getLocation());
        Datetime updatedDatetime = editEventDescriptor.getDatetime().orElse(eventToEdit.getDatetime());

        return new Event(updatedTitle, updatedDescription, updatedLocation, updatedDatetime);
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyEvent> lastShownList = model.getFilteredEventList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        ReadOnlyEvent eventToEdit = lastShownList.get(index.getZeroBased());
        Event editedEvent = createEditedEvent(eventToEdit, editEventDescriptor);

        try {
            model.updateEvent(eventToEdit, editedEvent);
        } catch (DuplicateEventException dee) {
            throw new CommandException(MESSAGE_DUPLICATE_EVENT);
        } catch (EventNotFoundException enfe) {
            throw new AssertionError("The target event cannot be missing");
        }
        model.updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
        return new CommandResult(String.format(MESSAGE_EDIT_EVENT_SUCCESS, editedEvent));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EditEventCommand)) {
            return false;
        }

        // state check
        EditEventCommand e = (EditEventCommand) other;
        return index.equals(e.index)
                && editEventDescriptor.equals(e.editEventDescriptor);
    }

    /**
     * Stores the details to edit the event with. Each non-empty field value will replace the
     * corresponding field value of the event.
     */
    public static class EditEventDescriptor {
        private Title title;
        private Description description;
        private Location location;
        private Datetime datetime;

        public EditEventDescriptor() {
        }

        public EditEventDescriptor(EditEventDescriptor toCopy) {
            this.title = toCopy.title;
            this.description = toCopy.description;
            this.location = toCopy.location;
            this.datetime = toCopy.datetime;
        }

        /**
         * Returns true if at least one field is edited.
         */
        public boolean isAnyFieldEdited() {
            return CollectionUtil.isAnyNonNull(this.title, this.description, this.location, this.datetime);
        }

        public Optional<Title> getTitle() {
            return Optional.ofNullable(title);
        }

        public void setTitle(Title title) {
            this.title = title;
        }

        public Optional<Description> getDescription() {
            return Optional.ofNullable(description);
        }

        public void setDescription(Description description) {
            this.description = description;
        }

        public Optional<Location> getLocation() {
            return Optional.ofNullable(location);
        }

        public void setLocation(Location location) {
            this.location = location;
        }

        public Optional<Datetime> getDatetime() {
            return Optional.ofNullable(datetime);
        }

        public void setDatetime(Datetime datetime) {
            this.datetime = datetime;
        }

        @Override
        public boolean equals(Object other) {
            // short circuit if same object
            if (other == this) {
                return true;
            }

            // instanceof handles nulls
            if (!(other instanceof EditEventDescriptor)) {
                return false;
            }

            // state check
            EditEventDescriptor e = (EditEventDescriptor) other;

            return getTitle().equals(e.getTitle())
                    && getDescription().equals(e.getDescription())
                    && getLocation().equals(e.getLocation())
                    && getDatetime().equals(e.getDatetime());
        }
    }
}
```
###### /java/seedu/address/logic/commands/FindEventCommand.java
``` java
/**
 * Finds and lists all events in event book whose name contains any of the argument keywords.
 * Keyword matching is case sensitive.
 */
public class FindEventCommand extends Command {

    public static final String COMMAND_WORD = "findevent";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Finds all events whose title or description contain any "
            + "of the specified keywords (case-sensitive) and displays them as a list with index numbers.\n"
            + "Parameters: et/KEYWORD [MORE_KEYWORDS]... for title OR ed/KEYWORD [MORE_KEYWORDS]... for description\n"
            + "Example: " + COMMAND_WORD + " et/sentosa deepavali";

    private final TitleContainsKeywordsPredicate predicate;

    public FindEventCommand(TitleContainsKeywordsPredicate predicate) {
        this.predicate = predicate;
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredEventList(predicate);
        return new CommandResult(getMessageForEventListShownSummary(model.getFilteredEventList().size()));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof FindEventCommand // instanceof handles nulls
                && this.predicate.equals(((FindEventCommand) other).predicate)); // state check
    }
}
```
###### /java/seedu/address/logic/commands/ListEventCommand.java
``` java
/**
 * Lists all events in the event book to the user.
 */
public class ListEventCommand extends Command {
    public static final String COMMAND_WORD = "listevent";

    public static final String MESSAGE_SUCCESS = "Listed all events";

    public static String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public CommandResult execute() {
        model.updateFilteredEventList(PREDICATE_SHOW_ALL_EVENTS);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### /java/seedu/address/logic/commands/RemarkCommand.java
``` java
/**
 * Changes the remark of an existing person in the address book.
 */
public class RemarkCommand extends UndoableCommand {

    public static final String COMMAND_WORD = "remark";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Edits the remark of the person identified "
            + "by the index number used in the last person listing. "
            + "Existing remark will be overwritten by the input.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_REMARK + "[REMARK]\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_REMARK + "Likes to swim.";

    public static final String MESSAGE_ADD_REMARK_SUCCESS = "Added remark to Person: %1$s";
    public static final String MESSAGE_DELETE_REMARK_SUCCESS = "Removed remark from Person: %1$s";
    public static final String MESSAGE_DUPLICATE_PERSON = "This person already exists in the address book.";

    private final Index index;
    private final Remark remark;

    /**
     * @param index  of the person in the filtered person list to edit the remark
     * @param remark of the person
     */
    public RemarkCommand(Index index, Remark remark) {
        requireNonNull(index);
        requireNonNull(remark);

        this.index = index;
        this.remark = remark;
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    protected CommandResult executeUndoableCommand() throws CommandException {
        List<ReadOnlyPerson> lastShownList = model.getFilteredPersonList();

        if (index.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_PERSON_DISPLAYED_INDEX);
        }

        ReadOnlyPerson personToEdit = lastShownList.get(index.getZeroBased());
        Person editedPerson = new Person(personToEdit.getName(), personToEdit.getPhone(), personToEdit.getEmail(),
                personToEdit.getAddress(), personToEdit.getBirthday(), personToEdit.getGroup(),
                remark, personToEdit.getTags());

        try {
            model.updatePerson(personToEdit, editedPerson);
        } catch (DuplicatePersonException dpe) {
            throw new CommandException(MESSAGE_DUPLICATE_PERSON);
        } catch (PersonNotFoundException pnfe) {
            throw new AssertionError("The target person cannot be missing");
        }
        model.updateFilteredListToShowAll();

        return new CommandResult(generateSuccessMessage(editedPerson));
    }

    /**
     * @param personToEdit
     * @return the generated message
     */
    private String generateSuccessMessage(ReadOnlyPerson personToEdit) {
        if (!remark.value.isEmpty()) {
            return String.format(MESSAGE_ADD_REMARK_SUCCESS, personToEdit);
        } else {
            return String.format(MESSAGE_DELETE_REMARK_SUCCESS, personToEdit);
        }
    }

    @Override
    public boolean equals(Object other) {
        //short circuit if same object
        if (other == this) {
            return true;
        }

        //instanceof handles nulls
        if (!(other instanceof RemarkCommand)) {
            return false;
        }

        //state check
        RemarkCommand newRemark = (RemarkCommand) other;
        return index.equals(newRemark.index)
                && remark.equals(newRemark.remark);
    }
}
```
###### /java/seedu/address/logic/commands/SelectEventCommand.java
``` java
/**
 * Selects an event identified using it's last displayed index from the event book.
 */
public class SelectEventCommand extends Command {

    public static final String COMMAND_WORD = "selectevent";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Selects the event identified by the index number used in the last event listing.\n"
            + "Parameters: INDEX (must be a positive integer)\n"
            + "Example: " + COMMAND_WORD + " 1";

    public static final String MESSAGE_SELECT_EVENT_SUCCESS = "Selected Event: %1$s";

    private final Index targetIndex;

    public SelectEventCommand(Index targetIndex) {
        this.targetIndex = targetIndex;
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public CommandResult execute() throws CommandException {

        List<ReadOnlyEvent> lastShownList = model.getFilteredEventList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_EVENT_DISPLAYED_INDEX);
        }

        //EventsCenter.getInstance().post(new JumpToListRequestEvent(targetIndex));
        return new CommandResult(String.format(MESSAGE_SELECT_EVENT_SUCCESS, targetIndex.getOneBased()));

    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof SelectEventCommand // instanceof handles nulls
                && this.targetIndex.equals(((SelectEventCommand) other).targetIndex)); // state check
    }
}
```
###### /java/seedu/address/logic/commands/SwitchCommand.java
``` java
/**
 * switch between the addressbook and eventbook tab
 */
public class SwitchCommand extends Command {

    public static final String COMMAND_WORD = "switch";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Switch between the addressbook and eventbook UI tab";

    public static final String MESSAGE_SUCCESS = "Switched to the other tab";

    private final TabPane tabPane;

    public SwitchCommand(TabPane tabPane) {
        this.tabPane = tabPane;
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }

    @Override
    public CommandResult execute() throws CommandException {
        SingleSelectionModel<Tab> selectionModel = tabPane.getSelectionModel();
        int selectedIndex = selectionModel.getSelectedIndex();
        if (selectedIndex == 0) {
            selectedIndex = 1;
        } else {
            selectedIndex = 0;
        }
        selectionModel.select(selectedIndex);
        return new CommandResult(MESSAGE_SUCCESS);
    }
}
```
###### /java/seedu/address/logic/parser/AddEventCommandParser.java
``` java
/**
 * Parses input arguments and creates a new AddEventCommand object
 */
public class AddEventCommandParser implements Parser<AddEventCommand> {

    /**
     * Returns true if none of the prefixes contains empty {@code Optional} values in the given
     * {@code ArgumentMultimap}.
     */
    private static boolean arePrefixesPresent(ArgumentMultimap argumentMultimap, Prefix... prefixes) {
        return Stream.of(prefixes).allMatch(prefix -> argumentMultimap.getValue(prefix).isPresent());
    }

    /**
     * Parses the given {@code String} of arguments in the context of the AddEventCommand
     * and returns an AddEventCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public AddEventCommand parse(String args) throws ParseException {
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TITLE, PREFIX_DESCRIPTION, PREFIX_LOCATION, PREFIX_DATETIME);

        if (!arePrefixesPresent(argMultimap, PREFIX_TITLE, PREFIX_DESCRIPTION, PREFIX_LOCATION, PREFIX_DATETIME)) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, AddEventCommand.MESSAGE_USAGE));
        }

        try {
            Title title = ParserUtil.parseTitle(argMultimap.getValue(PREFIX_TITLE)).get();
            Description description = ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION)).get();
            Location location = ParserUtil.parseLocation(argMultimap.getValue(PREFIX_LOCATION)).get();
            Datetime datetime = ParserUtil.parseDatetime(argMultimap.getValue(PREFIX_DATETIME)).get();

            ReadOnlyEvent event = new Event(title, description, location, datetime);

            return new AddEventCommand(event);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }
    }
}
```
###### /java/seedu/address/logic/parser/CalendarViewStateParser.java
``` java
/**
 * Parses user input for the calendar UI state.
 */
public class CalendarViewStateParser {

    private static UserPrefs userPrefs;
    private static final Pattern BASIC_COMMAND_FORMAT = Pattern.compile("(?<commandWord>\\S+)(?<arguments>.*)");

    private final Model model;
    private CalendarView calendarView;


    public CalendarViewStateParser(UserPrefs userPrefs, Model model, CalendarView calendarView) {
        this.userPrefs = userPrefs;
        this.model = model;
        this.calendarView = calendarView;
    }

    /**
     * update the state of the calendar UI object with reference to the user input
     * @param userInput
     * @throws ParseException
     */
    public void updateViewState(String userInput) throws ParseException {

        //Check whether CalendarView is a null object
        if (calendarView == null) {
            return;
        }

        final Matcher matcher = BASIC_COMMAND_FORMAT.matcher(userInput.trim());
        if (!matcher.matches()) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE));
        }

        final String commandWord = matcher.group("commandWord");
        final String arguments = matcher.group("arguments");

        AliasSettings aliasSettings = userPrefs.getAliasSettings();

        if (commandWord.equals(AddEventCommand.COMMAND_WORD)
                || commandWord.equals(aliasSettings.getAddEventCommand().getAlias())
                || commandWord.equals(DeleteEventCommand.COMMAND_WORD)
                || commandWord.equals(aliasSettings.getDeleteEventCommand().getAlias())
                || commandWord.equals(EditEventCommand.COMMAND_WORD)
                || commandWord.equals(aliasSettings.getEditEventCommand().getAlias())
                || commandWord.equals(ListEventCommand.COMMAND_WORD)
                || commandWord.equals(aliasSettings.getListEventCommand().getAlias())
                || commandWord.equals(OrderEventCommand.COMMAND_WORD)
                || commandWord.equals(aliasSettings.getOrderEventCommand().getAlias())) {
            UpdateCalendarView.updateViewState(calendarView);
        }  else if (commandWord.equals(FindEventCommand.COMMAND_WORD)
                || commandWord.equals(aliasSettings.getFindEventCommand().getAlias())) {
            UpdateCalendarView.updateFindState(calendarView, model);
        } else if (commandWord.equals(SelectEventCommand.COMMAND_WORD)
                || commandWord.equals(aliasSettings.getSelectEventCommand().getAlias())) {
            try {
                Index index = ParserUtil.parseIndex(arguments);
                UpdateCalendarView.updateSelectState(calendarView, model, index);
            } catch (IllegalValueException ive) {
                throw new ParseException(
                        String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectEventCommand.MESSAGE_USAGE));
            }
        }
    }
}
```
###### /java/seedu/address/logic/parser/CliSyntax.java
``` java
/**
 * Contains Command Line Interface (CLI) syntax definitions common to multiple commands
 */
public class CliSyntax {

    /* Prefix definitions */
    public static final Prefix PREFIX_NAME = new Prefix("n/");
    public static final Prefix PREFIX_PHONE = new Prefix("p/");
    public static final Prefix PREFIX_EMAIL = new Prefix("e/");
    public static final Prefix PREFIX_ADDRESS = new Prefix("a/");
    public static final Prefix PREFIX_BIRTHDAY = new Prefix("b/");
    public static final Prefix PREFIX_GROUP = new Prefix("g/");
    public static final Prefix PREFIX_REMARK = new Prefix("r/");
    public static final Prefix PREFIX_TAG = new Prefix("t/");
    public static final Prefix PREFIX_USERID = new Prefix("u/");
    public static final Prefix PREFIX_PASSWORD = new Prefix("p/");
    public static final Prefix PREFIX_TITLE = new Prefix("et/");
    public static final Prefix PREFIX_DESCRIPTION = new Prefix("ed/");
    public static final Prefix PREFIX_LOCATION = new Prefix("el/");
    public static final Prefix PREFIX_DATETIME = new Prefix("edt/");
    public static final Prefix PREFIX_COMMAND = new Prefix("c/");
    public static final Prefix PREFIX_ALIAS = new Prefix("al/");
}
```
###### /java/seedu/address/logic/parser/DeleteEventCommandParser.java
``` java
/**
 * Parses input arguments and creates a new DeleteEventCommand object
 */
public class DeleteEventCommandParser implements Parser<DeleteEventCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the DeleteEventCommand
     * and returns an DeleteEventCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public DeleteEventCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new DeleteEventCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, DeleteEventCommand.MESSAGE_USAGE));
        }
    }
}
```
###### /java/seedu/address/logic/parser/EditEventCommandParser.java
``` java
/**
 * Parses input arguments and creates a new EditEventCommand object
 */
public class EditEventCommandParser implements Parser<EditEventCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the EditEventCommand
     * and returns an EditEventCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    @Override
    public EditEventCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap =
                ArgumentTokenizer.tokenize(args, PREFIX_TITLE, PREFIX_DESCRIPTION, PREFIX_LOCATION, PREFIX_DATETIME);

        Index index;

        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, EditEventCommand.MESSAGE_USAGE));
        }

        EditEventDescriptor editEventDescriptor = new EditEventDescriptor();
        try {
            ParserUtil.parseTitle(argMultimap.getValue(PREFIX_TITLE)).ifPresent(editEventDescriptor::setTitle);
            ParserUtil.parseDescription(argMultimap.getValue(PREFIX_DESCRIPTION))
                    .ifPresent(editEventDescriptor::setDescription);
            ParserUtil.parseLocation(argMultimap.getValue(PREFIX_LOCATION)).ifPresent(editEventDescriptor::setLocation);
            ParserUtil.parseDatetime(argMultimap.getValue(PREFIX_DATETIME)).ifPresent(editEventDescriptor::setDatetime);
        } catch (IllegalValueException ive) {
            throw new ParseException(ive.getMessage(), ive);
        }

        if (!editEventDescriptor.isAnyFieldEdited()) {
            throw new ParseException(EditEventCommand.MESSAGE_NOT_EDITED);
        }

        return new EditEventCommand(index, editEventDescriptor);
    }
}
```
###### /java/seedu/address/logic/parser/FindEventCommandParser.java
``` java
/**
 * Parses input arguments and creates a new FindEventCommand object
 */
public class FindEventCommandParser implements Parser<FindEventCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the FindEventCommand
     * and returns an FindCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public FindEventCommand parse(String args) throws ParseException {
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindEventCommand.MESSAGE_USAGE));
        }
        if (trimmedArgs.substring(0, 2).equals("et")) {
            TitleContainsKeywordsPredicate.setPredicateType("et");
        } else if (trimmedArgs.substring(0, 3).equals("edt")) {
            TitleContainsKeywordsPredicate.setPredicateType("edt");
        } else if (trimmedArgs.substring(0, 2).equals("ed")) {
            TitleContainsKeywordsPredicate.setPredicateType("ed");
        }  else if (trimmedArgs.substring(0, 2).equals("em")) {
            TitleContainsKeywordsPredicate.setPredicateType("em");
        } else {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, FindEventCommand.MESSAGE_USAGE));
        }

        if (trimmedArgs.substring(0, 3).equals("edt")) {
            trimmedArgs = trimmedArgs.substring(4).trim();
        } else {
            trimmedArgs = trimmedArgs.substring(3).trim();
        }

        String[] titleKeywords = trimmedArgs.split("\\s+");

        return new FindEventCommand(new TitleContainsKeywordsPredicate(Arrays.asList(titleKeywords)));
    }
}
```
###### /java/seedu/address/logic/parser/OrderEventCommandParser.java
``` java
/**
 * Parses input arguments and creates a new OrderEventCommand object
 */
public class OrderEventCommandParser implements Parser<OrderEventCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the OrderEventCommand
     * and returns an OrderEventCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public OrderEventCommand parse(String args) throws ParseException {
        requireNonNull(args);
        String trimmedArgs = args.trim();
        if (trimmedArgs.isEmpty()) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, OrderEventCommand.MESSAGE_USAGE));
        }
        String upperCaseParameter = trimmedArgs.toUpperCase();
        return new OrderEventCommand(upperCaseParameter);
    }
}
```
###### /java/seedu/address/logic/parser/RemarkCommandParser.java
``` java
/**
 * Parses input arguments and creates a new RemarkCommand object
 */
public class RemarkCommandParser implements Parser<RemarkCommand> {
    /**
     * Parses the given {@code String} of arguments in the context of the RemarkCommand
     * and returns an RemarkCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public RemarkCommand parse(String args) throws ParseException {
        requireNonNull(args);
        ArgumentMultimap argMultimap = ArgumentTokenizer.tokenize(args, PREFIX_REMARK);

        Index index;
        try {
            index = ParserUtil.parseIndex(argMultimap.getPreamble());
        } catch (IllegalValueException ive) {
            throw new ParseException(String.format(MESSAGE_INVALID_COMMAND_FORMAT, RemarkCommand.MESSAGE_USAGE));
        }

        String remark = argMultimap.getValue(PREFIX_REMARK).orElse("");

        return new RemarkCommand(index, new Remark(remark));
    }
}
```
###### /java/seedu/address/logic/parser/SelectEventCommandParser.java
``` java
/**
 * Parses input arguments and creates a new SelectEventCommand object
 */
public class SelectEventCommandParser implements Parser<SelectEventCommand> {

    /**
     * Parses the given {@code String} of arguments in the context of the SelectEventCommand
     * and returns an SelectEventCommand object for execution.
     *
     * @throws ParseException if the user input does not conform the expected format
     */
    public SelectEventCommand parse(String args) throws ParseException {
        try {
            Index index = ParserUtil.parseIndex(args);
            return new SelectEventCommand(index);
        } catch (IllegalValueException ive) {
            throw new ParseException(
                    String.format(MESSAGE_INVALID_COMMAND_FORMAT, SelectEventCommand.MESSAGE_USAGE));
        }
    }
}
```
###### /java/seedu/address/model/event/Datetime.java
``` java
/**
 * Represents a Event's Datetime in the event book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDatetime(String)}
 */
public class Datetime {

    public static final String MESSAGE_DATETIME_CONSTRAINTS =
            "Event datetime should only contain dd-mm-yyyy hhmm";
    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String DATETIME_VALIDATION_REGEX = "[^\\s].*";

    public final String value;

    /**
     * Validates given datetime.
     *
     * @throws IllegalValueException if given datetime string is invalid.
     */
    public Datetime(String datetime) throws IllegalValueException {
        requireNonNull(datetime);
        if (!isValidDatetime(datetime)) {
            throw new IllegalValueException(MESSAGE_DATETIME_CONSTRAINTS);
        }
        this.value = datetime;
    }

    /**
     * Returns true if a given string is a valid event datetime.
     */
    public static boolean isValidDatetime(String test) {
        return test.matches(DATETIME_VALIDATION_REGEX);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Datetime // instanceof handles nulls
                && this.value.equals(((Datetime) other).value)); // state check
    }

    @Override
    public String toString() {
        return value;
    }
}
```
###### /java/seedu/address/model/event/Description.java
``` java
/**
 * Represents a Event's Description in the event book.
 * Guarantees: immutable; is valid as declared in {@link #isValidDescription(String)}
 */
public class Description {

    public static final String MESSAGE_DESCRIPTION_CONSTRAINTS =
            "Event descriptions should only contain alphanumeric characters and spaces, and it should not be blank";
    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String DESCRIPTION_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String value;

    /**
     * Validates given description.
     *
     * @throws IllegalValueException if given description string is invalid.
     */
    public Description(String description) throws IllegalValueException {
        requireNonNull(description);
        if (!isValidDescription(description)) {
            throw new IllegalValueException(MESSAGE_DESCRIPTION_CONSTRAINTS);
        }
        this.value = description;
    }

    /**
     * Returns true if a given string is a valid event description.
     */
    public static boolean isValidDescription(String test) {
        return test.matches(DESCRIPTION_VALIDATION_REGEX);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Description // instanceof handles nulls
                && this.value.equals(((Description) other).value)); // state check
    }

    @Override
    public String toString() {
        return value;
    }
}
```
###### /java/seedu/address/model/event/Event.java
``` java
/**
 * Represents a Event in the event book.
 * Guarantees: details are present and not null, field values are validated.
 */
public class Event implements ReadOnlyEvent {

    private ObjectProperty<Title> title;
    private ObjectProperty<Description> description;
    private ObjectProperty<Location> location;
    private ObjectProperty<Datetime> datetime;

    /**
     * Every field must be present and not null.
     */
    public Event(Title title, Description description, Location location, Datetime datetime) {
        requireAllNonNull(title, description, location, datetime);
        this.title = new SimpleObjectProperty<>(title);
        this.description = new SimpleObjectProperty<>(description);
        this.location = new SimpleObjectProperty<>(location);
        this.datetime = new SimpleObjectProperty<>(datetime);
    }

    /**
     * Creates a copy of the given ReadOnlyEvent.
     */
    public Event(ReadOnlyEvent source) {
        this(source.getTitle(), source.getDescription(), source.getLocation(), source.getDatetime());
    }

    @Override
    public ObjectProperty<Title> titleProperty() {
        return title;
    }

    @Override
    public Title getTitle() {
        return title.get();
    }

    public void setTitle(Title title) {
        this.title.set(requireNonNull(title));
    }

    @Override
    public ObjectProperty<Description> descriptionProperty() {
        return description;
    }

    @Override
    public Description getDescription() {
        return description.get();
    }

    public void setDescription(Description description) {
        this.description.set(requireNonNull(description));
    }

    @Override
    public ObjectProperty<Location> locationProperty() {
        return location;
    }

    @Override
    public Location getLocation() {
        return location.get();
    }

    public void setLocation(Location location) {
        this.location.set(requireNonNull(location));
    }

    @Override
    public ObjectProperty<Datetime> datetimeProperty() {
        return datetime;
    }

    @Override
    public Datetime getDatetime() {
        return datetime.get();
    }

    public void setDatetime(Datetime datetime) {
        this.datetime.set(requireNonNull(datetime));
    }

    @Override
    public int hashCode() {
        // use this method for custom fields hashing instead of implementing your own
        return Objects.hash(title, description, location, datetime);
    }

    @Override
    public String toString() {
        return getAsText();
    }
}
```
###### /java/seedu/address/model/event/Location.java
``` java
/**
 * Represents a Event's Location in the event book.
 * Guarantees: immutable; is valid as declared in {@link #isValidLocation(String)}
 */
public class Location {

    public static final String MESSAGE_LOCATION_CONSTRAINTS =
            "Event location should only contain alphanumeric characters and spaces, and it should not be blank";
    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String LOCATION_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String value;

    /**
     * Validates given description.
     *
     * @throws IllegalValueException if given location string is invalid.
     */
    public Location(String location) throws IllegalValueException {
        requireNonNull(location);
        if (!isValidLocation(location)) {
            throw new IllegalValueException(MESSAGE_LOCATION_CONSTRAINTS);
        }
        this.value = location;
    }

    /**
     * Returns true if a given string is a valid event location.
     */
    public static boolean isValidLocation(String test) {
        return test.matches(LOCATION_VALIDATION_REGEX);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Location // instanceof handles nulls
                && this.value.equals(((Location) other).value)); // state check
    }

    @Override
    public String toString() {
        return value;
    }
}
```
###### /java/seedu/address/model/event/ReadOnlyEvent.java
``` java
/**
 * A read-only immutable interface for a event in the eventbook.
 * Implementations should guarantee: details are present and not null, field values are validated.
 */
public interface ReadOnlyEvent {

    ObjectProperty<Title> titleProperty();

    Title getTitle();

    ObjectProperty<Description> descriptionProperty();

    Description getDescription();

    ObjectProperty<Location> locationProperty();

    Location getLocation();

    ObjectProperty<Datetime> datetimeProperty();

    Datetime getDatetime();

    /**
     * Returns true if both have the same state. (interfaces cannot override .equals)
     */
    default boolean isSameStateAs(ReadOnlyEvent other) {
        return other == this // short circuit if same object
                || (other != null // this is first to avoid NPE below
                && other.getTitle().equals(this.getTitle()) // state checks here onwards
                && other.getDescription().equals(this.getDescription())
                && other.getLocation().equals(this.getLocation())
                && other.getDatetime().equals(this.getDatetime()));
    }

    /**
     * Formats the person as text, showing all contact details.
     */
    default String getAsText() {
        final StringBuilder builder = new StringBuilder();
        builder.append(getTitle())
                .append(" Description: ")
                .append(getDescription())
                .append(" Location: ")
                .append(getLocation())
                .append(" Datetime: ")
                .append(getDatetime());
        return builder.toString();
    }
}
```
###### /java/seedu/address/model/event/Title.java
``` java
/**
 * Represents a Event's title in the event book.
 * Guarantees: immutable; is valid as declared in {@link #isValidTitle(String)}
 */
public class Title {

    public static final String MESSAGE_TITLE_CONSTRAINTS =
            "Event titles should only contain alphanumeric characters and spaces, and it should not be blank";
    /*
     * The first character of the address must not be a whitespace,
     * otherwise " " (a blank string) becomes a valid input.
     */
    public static final String TITLE_VALIDATION_REGEX = "[\\p{Alnum}][\\p{Alnum} ]*";

    public final String value;

    /**
     * Validates given title.
     *
     * @throws IllegalValueException if given title string is invalid.
     */
    public Title(String title) throws IllegalValueException {
        requireNonNull(title);
        if (!isValidTitle(title)) {
            throw new IllegalValueException(MESSAGE_TITLE_CONSTRAINTS);
        }
        this.value = title;
    }

    /**
     * Returns true if a given string is a valid event title.
     */
    public static boolean isValidTitle(String test) {
        return test.matches(TITLE_VALIDATION_REGEX);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof Title // instanceof handles nulls
                && this.value.equals(((Title) other).value)); // state check
    }

    @Override
    public String toString() {
        return value;
    }
}
```
###### /java/seedu/address/model/event/TitleContainsKeywordsPredicate.java
``` java
/**
 * Tests that a {@code ReadOnlyEvent}'s {@code Title} matches any of the keywords given.
 */
public class TitleContainsKeywordsPredicate implements Predicate<ReadOnlyEvent> {
    private static String predicateType = "et";
    private final List<String> keywords;

    public TitleContainsKeywordsPredicate(List<String> keywords) {
        this.keywords = keywords;
    }

    public static void setPredicateType(String predicateType) {
        TitleContainsKeywordsPredicate.predicateType = predicateType;
    }

    @Override
    public boolean test(ReadOnlyEvent event) {
        if (predicateType.equals("et")) {
            return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(event.getTitle().value, keyword));
        }

        if (predicateType.equals("edt")) {
            return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(event.getDatetime().value, keyword));
        }

        if (predicateType.equals("ed")) {
            return keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(event.getDescription().value, keyword));
        }

        if (predicateType.equals("em")) {
            return (keywords.stream()
                    .anyMatch(keyword -> StringUtil.containsWordIgnoreCase(event.getAsText(), keyword)));
        }

        return false;
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof TitleContainsKeywordsPredicate // instanceof handles nulls
                && this.keywords.equals(((TitleContainsKeywordsPredicate) other).keywords)); // state check
    }
}
```
###### /java/seedu/address/model/event/UniqueEventList.java
``` java
/**
 * A list of persons that enforces uniqueness between its elements and does not allow nulls.
 * <p>
 * Supports a minimal set of list operations.
 *
 * @see Event#equals(Object)
 * @see CollectionUtil#elementsAreUnique(Collection)
 */
public class UniqueEventList implements Iterable<Event> {

    private final ObservableList<Event> internalList = FXCollections.observableArrayList();
    // used by asObservableList()
    private final ObservableList<ReadOnlyEvent> mappedList = EasyBind.map(internalList, (event) -> event);

    /**
     * Returns true if the list contains an equivalent event as the given argument.
     */
    public boolean contains(ReadOnlyEvent toCheck) {
        requireNonNull(toCheck);
        return internalList.contains(toCheck);
    }

    /**
     * Adds a event to the list.
     *
     * @throws DuplicateEventException if the event to add is a duplicate of an existing event in the list.
     */
    public void add(ReadOnlyEvent toAdd) throws DuplicateEventException {
        requireNonNull(toAdd);
        if (contains(toAdd)) {
            throw new DuplicateEventException();
        }
        internalList.add(new Event(toAdd));
    }

    /**
     * Replaces the event {@code target} in the list with {@code editedEvent}.
     *
     * @throws DuplicateEventException if the replacement is equivalent to another existing event in the list.
     * @throws EventNotFoundException  if {@code target} could not be found in the list.
     */
    public void setEvent(ReadOnlyEvent target, ReadOnlyEvent editedEvent)
            throws DuplicateEventException, EventNotFoundException {
        requireNonNull(editedEvent);

        int index = internalList.indexOf(target);
        if (index == -1) {
            throw new EventNotFoundException();
        }

        if (!target.equals(editedEvent) && internalList.contains(editedEvent)) {
            throw new DuplicateEventException();
        }

        internalList.set(index, new Event(editedEvent));
    }

    /**
     * Removes the equivalent event from the list.
     *
     * @throws EventNotFoundException if no such event could be found in the list.
     */
    public boolean remove(ReadOnlyEvent toRemove) throws EventNotFoundException {
        requireNonNull(toRemove);
        final boolean eventFoundAndDeleted = internalList.remove(toRemove);
        if (!eventFoundAndDeleted) {
            throw new EventNotFoundException();
        }
        return eventFoundAndDeleted;
    }

    public void setEvents(UniqueEventList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setEvents(List<? extends ReadOnlyEvent> events) throws DuplicateEventException {
        final UniqueEventList replacement = new UniqueEventList();
        for (final ReadOnlyEvent event : events) {
            replacement.add(new Event(event));
        }
        setEvents(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ReadOnlyEvent> asObservableList() {
        return FXCollections.unmodifiableObservableList(mappedList);
    }

    /**
     * Order the list.
     */
    public void orderBy(String parameter) throws UnrecognisedParameterException {
        requireNonNull(parameter);
        Comparator<Event> orderByTitle = (Event a, Event b) -> a.getTitle().toString()
                .compareToIgnoreCase(b.getTitle().toString());
        Comparator<Event> orderByLocation = (Event a, Event b) -> a.getLocation().toString()
                .compareToIgnoreCase(b.getLocation().toString());
        Comparator<Event> orderByDatetime = (Event a, Event b) -> {

            SimpleDateFormat formatDate = new SimpleDateFormat("dd-MM-yyyy hhmm");
            try {
                Date datetime1 = formatDate.parse(a.getDatetime().value);
                Date datetime2 = formatDate.parse(b.getDatetime().value);

                return datetime2.compareTo(datetime1);

            } catch (ParseException e) {
                e.printStackTrace();
            }
            return 0;

        };

        switch (parameter) {
        case "TITLE":
            internalList.sort(orderByTitle);
            break;

        case "LOCATION":
            internalList.sort(orderByLocation);
            break;

        case "DATETIME":
            internalList.sort(orderByDatetime);
            break;

        default:
            throw new UnrecognisedParameterException();
        }


    }

    @Override
    public Iterator<Event> iterator() {
        return internalList.iterator();
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniqueEventList // instanceof handles nulls
                && this.internalList.equals(((UniqueEventList) other).internalList));
    }
}
```
###### /java/seedu/address/model/EventBook.java
``` java
/**
 * Wraps all data at the event-book level
 * Duplicates are not allowed (by .equals comparison)
 */
public class EventBook implements ReadOnlyEventBook {

    private final UniqueEventList events;

    /*
     * The 'unusual' code block below is an non-static initialization block, sometimes used to avoid duplication
     * between constructors. See https://docs.oracle.com/javase/tutorial/java/javaOO/initial.html
     *
     * Note that non-static init blocks are not recommended to use. There are other ways to avoid duplication
     *   among constructors.
     */
    {
        events = new UniqueEventList();
    }

    public EventBook() {
    }

    /**
     * Creates an EventBook using the Events in the {@code toBeCopied}
     */
    public EventBook(ReadOnlyEventBook toBeCopied) {
        this();
        resetData(toBeCopied);
    }

    //// list overwrite operations

    public void setEvents(List<? extends ReadOnlyEvent> events) throws DuplicateEventException {
        this.events.setEvents(events);
    }

    /**
     * Resets the existing data of this {@code EventBook} with {@code newData}.
     */
    public void resetData(ReadOnlyEventBook newData) {
        requireNonNull(newData);
        try {
            setEvents(newData.getEventList());
        } catch (DuplicateEventException e) {
            assert false : "EventBooks should not have duplicate events";
        }
    }

    //// person-level operations

    /**
     * Adds an event to the event book.
     *
     * @throws DuplicateEventException if an equivalent event already exists.
     */
    public void addEvent(ReadOnlyEvent e) throws DuplicateEventException {
        Event newEvent = new Event(e);
        events.add(newEvent);
    }

    /**
     * Replaces the given event {@code target} in the list with {@code editedReadOnlyEvent}.
     *
     * @throws DuplicateEventException if updating the event's details causes the event to be equivalent to
     *                                 another existing person in the list.
     * @throws EventNotFoundException  if {@code target} could not be found in the list.
     */
    public void updateEvent(ReadOnlyEvent target, ReadOnlyEvent editedReadOnlyEvent)
            throws DuplicateEventException, EventNotFoundException {
        requireNonNull(editedReadOnlyEvent);

        Event editedPerson = new Event(editedReadOnlyEvent);
        events.setEvent(target, editedPerson);
    }

    /**
     * Removes {@code key} from this {@code EventBook}.
     *
     * @throws EventNotFoundException if the {@code key} is not in this {@code EventBook}.
     */
    public boolean removeEvent(ReadOnlyEvent key) throws EventNotFoundException {
        if (events.remove(key)) {
            return true;
        } else {
            throw new EventNotFoundException();
        }
    }

    /**
     * Order list of all events in the event Book based on the parameter.
     */
    public void orderList(String parameter) throws UnrecognisedParameterException {
        events.orderBy(parameter);
    }

    //// util methods

    @Override
    public int hashCode() {
        return Objects.hash(events);
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof EventBook // instanceof handles nulls
                && this.events.equals(((EventBook) other).events));
    }

    @Override
    public String toString() {
        return events.asObservableList().size() + " events";
    }

    @Override
    public ObservableList<ReadOnlyEvent> getEventList() {
        return events.asObservableList();
    }
}
```
###### /java/seedu/address/model/ReadOnlyEventBook.java
``` java
/**
 * Unmodifiable view of an event book
 */
public interface ReadOnlyEventBook {

    /**
     * Returns an unmodifiable view of the events list.
     * This list will not contain any duplicate events.
     */
    ObservableList<ReadOnlyEvent> getEventList();

}
```
###### /java/seedu/address/storage/EventBookStorage.java
``` java
/**
 * Represents a storage for {@link seedu.address.model.EventBook}.
 */
public interface EventBookStorage {

    /**
     * Returns the file path of the data file.
     */
    String getEventBookFilePath();

    /**
     * Returns AddressBook data as a {@link ReadOnlyEventBook}.
     * Returns {@code Optional.empty()} if storage file is not found.
     *
     * @throws DataConversionException if the data in storage is not in the expected format.
     * @throws IOException             if there was any problem when reading from the storage.
     */
    Optional<ReadOnlyEventBook> readEventBook() throws DataConversionException, IOException;

    /**
     * @see #getEventBookFilePath()
     */
    Optional<ReadOnlyEventBook> readEventBook(String filePath) throws DataConversionException, IOException;

    /**
     * Saves the given {@link ReadOnlyEventBook} to the storage.
     *
     * @param eventBook cannot be null.
     * @throws IOException if there was any problem writing to the file.
     */
    void saveEventBook(ReadOnlyEventBook eventBook) throws IOException;

    /**
     * @see #saveEventBook(ReadOnlyEventBook)
     */
    void saveEventBook(ReadOnlyEventBook eventBook, String filePath) throws IOException;

    /**
     * @see #saveEventBook(ReadOnlyEventBook)
     */
    void backupEventBook(ReadOnlyEventBook eventBook) throws IOException;

}
```
###### /java/seedu/address/storage/XmlAdaptedEvent.java
``` java
/**
 * JAXB-friendly version of the Event.
 */
public class XmlAdaptedEvent {

    @XmlElement(required = true)
    private String title;
    @XmlElement(required = true)
    private String description;
    @XmlElement(required = true)
    private String location;
    @XmlElement(required = true)
    private String datetime;

    /**
     * Constructs an XmlAdaptedEvent.
     * This is the no-arg constructor that is required by JAXB.
     */
    public XmlAdaptedEvent() {
    }

    public XmlAdaptedEvent(ReadOnlyEvent source) {
        title = source.getTitle().value;
        description = source.getDescription().value;
        location = source.getLocation().value;
        datetime = source.getDatetime().value;
    }

    /**
     * Converts this jaxb-friendly adapted event object into the model's Event object.
     *
     * @throws IllegalValueException if there were any data constraints violated in the adapted event
     */
    public Event toModelType() throws IllegalValueException {
        final Title title = new Title(this.title);
        final Description description = new Description(this.description);
        final Location location = new Location(this.location);
        final Datetime datetime = new Datetime(this.datetime);
        return new Event(title, description, location, datetime);
    }
}
```
###### /java/seedu/address/storage/XmlEventBookStorage.java
``` java
/**
 * A class to access TunedIn EventBook data stored as an xml file on the hard disk.
 */
public class XmlEventBookStorage implements EventBookStorage {

    private static final Logger logger = LogsCenter.getLogger(XmlEventBookStorage.class);

    private String filePath;

    public XmlEventBookStorage(String filePath) {
        this.filePath = filePath;
    }

    @Override
    public String getEventBookFilePath() {
        return filePath;
    }

    @Override
    public Optional<ReadOnlyEventBook> readEventBook() throws DataConversionException, IOException {
        return readEventBook(filePath);
    }

    @Override
    public Optional<ReadOnlyEventBook> readEventBook(String filePath) throws DataConversionException,
            FileNotFoundException {
        requireNonNull(filePath);

        File eventBookFile = new File(filePath);

        if (!eventBookFile.exists()) {
            logger.info("EventBook file " + eventBookFile + " not found");
            return Optional.empty();
        }

        ReadOnlyEventBook eventBookOptional = XmlFileStorage.loadEventDataFromSaveFile(new File(filePath));

        return Optional.of(eventBookOptional);
    }

    @Override
    public void saveEventBook(ReadOnlyEventBook eventBook) throws IOException {
        saveEventBook(eventBook, filePath);
    }

    @Override
    public void saveEventBook(ReadOnlyEventBook eventBook, String filePath) throws IOException {
        requireNonNull(eventBook);
        requireNonNull(filePath);

        File file = new File(filePath);
        FileUtil.createIfMissing(file);
        XmlFileStorage.saveDataToFile(file, new XmlSerializableEventBook(eventBook));
    }

    @Override
    public void backupEventBook(ReadOnlyEventBook eventBook) throws IOException {
        saveEventBook(eventBook, filePath.substring(0, filePath.indexOf('.')) + "_backup.xml");
    }
}
```
###### /java/seedu/address/storage/XmlSerializableEventBook.java
``` java
/**
 * An Immutable AddressBook that is serializable to XML format
 */
@XmlRootElement(name = "eventbook")
public class XmlSerializableEventBook implements ReadOnlyEventBook {

    @XmlElement
    private List<XmlAdaptedEvent> events;

    /**
     * Creates an empty XmlSerializableEventBook.
     * This empty constructor is required for marshalling.
     */
    public XmlSerializableEventBook() {
        events = new ArrayList<>();
    }

    /**
     * Conversion
     */
    public XmlSerializableEventBook(ReadOnlyEventBook src) {
        this();
        events.addAll(src.getEventList().stream().map(XmlAdaptedEvent::new).collect(Collectors.toList()));
    }

    @Override
    public ObservableList<ReadOnlyEvent> getEventList() {
        final ObservableList<ReadOnlyEvent> events = this.events.stream().map(p -> {
            try {
                return p.toModelType();
            } catch (IllegalValueException e) {
                e.printStackTrace();
                //TODO: better error handling
                return null;
            }
        }).collect(Collectors.toCollection(FXCollections::observableArrayList));
        return FXCollections.unmodifiableObservableList(events);
    }
}
```
###### /java/seedu/address/ui/CalendarView.java
``` java
/**
 * The CalendarView UI Component
 */
public class CalendarView {

    private ArrayList<AnchorPaneNode> allCalendarDays = new ArrayList<>(35);
    private VBox view;
    private Text calendarTitle;
    private YearMonth currentYearMonth;

    private ObservableList<ReadOnlyEvent> eventList;
    private Logic logic;
    private final Logger logger = LogsCenter.getLogger(CommandBox.class);
    /**
     * Create a calendar view
     * @param eventList contains the events of the event book
     * @param yearMonth year month to create the calendar of
     */
    public CalendarView(Logic logic, ObservableList<ReadOnlyEvent> eventList, YearMonth yearMonth) {

        this.logic = logic;
        this.eventList = eventList;
        currentYearMonth = yearMonth;
        // Create the calendar grid pane
        GridPane calendar = new GridPane();
        calendar.setPrefSize(600, 400);
        // Create rows and columns with anchor panes for the calendar

        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 7; j++) {
                AnchorPaneNode ap = new AnchorPaneNode();
                ap.getStyleClass().add("anchor");
                ap.setPrefSize(200, 200);
                calendar.add(ap, j, i);
                allCalendarDays.add(ap);
            }
        }
        // Days of the week labels
        Text[] dayNames = new Text[]{ new Text("Sunday"), new Text("Monday"), new Text("Tuesday"),
            new Text("Wednesday"), new Text("Thursday"), new Text("Friday"),
            new Text("Saturday") };
        GridPane dayLabels = new GridPane();
        dayLabels.setPrefWidth(600);
        Integer col = 0;
        for (Text txt : dayNames) {
            txt.setFill(Color.WHITE);
            AnchorPane ap = new AnchorPane();
            ap.setPrefSize(200, 10);
            ap.setBottomAnchor(txt, 5.0);
            ap.getChildren().add(txt);
            dayLabels.add(ap, col++, 0);
        }
        // Create calendarTitle and buttons to change current month
        calendarTitle = new Text();
        calendarTitle.setFill(Color.WHITE);
        Button previousMonth = new Button("<<");
        previousMonth.setOnAction(e -> previousMonth());
        Button nextMonth = new Button(">>");
        nextMonth.setOnAction(e -> nextMonth());
        HBox titleBar = new HBox(previousMonth, calendarTitle, nextMonth);
        HBox.setMargin(calendarTitle, new Insets(0, 15, 0, 15));
        titleBar.setAlignment(Pos.BASELINE_CENTER);
        // Populate calendar with the appropriate day numbers
        populateCalendar(yearMonth, null);
        // Create the calendar view
        view = new VBox(titleBar, dayLabels, calendar);
        VBox.setMargin(titleBar, new Insets(0, 0, 15, 0));
    }

    /**
     * Set the days of the calendar to correspond to the appropriate date
     * @param yearMonth year and month of month to render
     */
    public void populateCalendar(YearMonth yearMonth, Index targetIndex) {
        // Get the date we want to start with on the calendar
        LocalDate calendarDate = LocalDate.of(yearMonth.getYear(), yearMonth.getMonthValue(), 1);
        // Dial back the day until it is SUNDAY (unless the month starts on a sunday)
        while (!calendarDate.getDayOfWeek().toString().equals("SUNDAY")) {
            calendarDate = calendarDate.minusDays(1);
        }

        // Populate the calendar with day numbers
        for (AnchorPaneNode ap : allCalendarDays) {
            if (ap.getChildren().size() != 0) {
                ap.getChildren().remove(0);
            }

            String dayValue = calendarDate.getDayOfMonth() + "";
            String monthValue = calendarDate.getMonthValue() + "";
            String yearValue = calendarDate.getYear() + "";

            boolean eventExist = false;

            if (targetIndex == null) {
                eventExist = eventList.stream()
                        .anyMatch(e -> checkEventDay(e, dayValue)
                                && checkEventMonth(e, monthValue)
                                && checkEventYear(e, yearValue));
            } else {
                ReadOnlyEvent e = eventList.get(targetIndex.getZeroBased());

                if (checkEventDay(e, dayValue)
                        && checkEventMonth(e, monthValue)
                        && checkEventYear(e, yearValue)) {
                    eventExist = true;
                }
            }



            Text txt = new Text(String.valueOf(calendarDate.getDayOfMonth()));
            txt.setFill(Color.WHITE);
            ap.setDate(calendarDate);
            ap.setTopAnchor(txt, 5.0);
            ap.setLeftAnchor(txt, 5.0);

            if (eventExist) {
                ap.setOnMouseClicked(ev -> {
                    String commandText = FindEventCommand.getCommandWord()
                            + " edt/" + getFormartDate(dayValue, monthValue, yearValue);
                    try {
                        CommandResult commandResult = logic.execute(commandText);
                        logger.info("Result: " + commandResult.feedbackToUser);

                    } catch (CommandException | ParseException e) {
                        logger.info("Invalid command: " + commandText);
                    } catch (DuplicateUserException e) {
                        logger.info("Duplicated User");
                    }
                });
                ap.setStyle("-fx-background-color: #3543CB");
            } else {
                ap.setStyle("-fx-background-color: transparent");
            }

            ap.getChildren().add(txt);
            calendarDate = calendarDate.plusDays(1);
        }

        // Change the title of the calendar
        calendarTitle.setText(yearMonth.getMonth().toString() + " " + String.valueOf(yearMonth.getYear()));
    }

    /**
     * Move the month back by one. Repopulate the calendar with the correct dates.
     */
    private void previousMonth() {
        currentYearMonth = currentYearMonth.minusMonths(1);
        populateCalendar(currentYearMonth, null);
    }

    /**
     * Move the month forward by one. Repopulate the calendar with the correct dates.
     */
    private void nextMonth() {
        currentYearMonth = currentYearMonth.plusMonths(1);
        populateCalendar(currentYearMonth, null);
    }

    public YearMonth getCurrentYearMonth() {
        return currentYearMonth;
    }

    public void setCurrentYearMonth(YearMonth currentYearMonth) {
        this.currentYearMonth = currentYearMonth;
    }

    public VBox getView() {
        return view;
    }

    public ArrayList<AnchorPaneNode> getAllCalendarDays() {
        return allCalendarDays;
    }

    public void setAllCalendarDays(ArrayList<AnchorPaneNode> allCalendarDays) {
        this.allCalendarDays = allCalendarDays;
    }

    /**
     * Check whether the event Day matches the input dayValue
     * @param event
     * @param dayValue
     * @return
     */
    private boolean checkEventDay(ReadOnlyEvent event, String dayValue) {

        if (dayValue.length() == 1) {
            return event.getDatetime().value.substring(0, 2).equals("0" + dayValue);
        } else {
            return event.getDatetime().value.substring(0, 2).equals(dayValue);
        }
    }

    /**
     * Check whether the event Day matches the input monthValue
     * @param event
     * @param monthValue
     * @return
     */
    private boolean checkEventMonth(ReadOnlyEvent event, String monthValue) {

        if (monthValue.length() == 1) {
            return event.getDatetime().value.substring(3, 5).equals("0" + monthValue);
        } else {
            return event.getDatetime().value.substring(3, 5).equals(monthValue);
        }
    }

    /**
     * Check whether the event Day matches the input yearValue
     * @param event
     * @param yearValue
     * @return
     */
    private boolean checkEventYear(ReadOnlyEvent event, String yearValue) {
        return event.getDatetime().value.substring(6, 10).equals(yearValue);
    }

    private String getFormartDate(String day, String month, String year) {
        if (day.length() == 1) {
            day = "0" + day;
        }

        if (month.length() == 1) {
            month = "0" + month;
        }

        return day + "-" + month + "-" + year;
    }
}
```
###### /java/seedu/address/ui/CalendarViewPane.java
``` java
/**
 * The UI component that is responsible containing the CalendarView
 */
public class CalendarViewPane extends UiPart<Region> {

    private static final String FXML = "CalendarView.fxml";

    @FXML
    private Pane calendarPane;

    private CalendarView calendarView;
    private Logic logic;

    public CalendarViewPane(Logic logic) {
        super(FXML);
        this.logic = logic;
        setConnections();;
    }

    private void setConnections() {
        calendarView = new CalendarView(logic, logic.getFilteredEventList(), YearMonth.now());
        calendarPane.getChildren().add(calendarView.getView());
    }

    public CalendarView getCalendarPane() {
        return calendarView;
    }
}
```
###### /java/seedu/address/ui/EventCard.java
``` java
/**
 * An UI component that displays information of a {@code Event}.
 */
public class EventCard extends UiPart<Region> {

    private static final String FXML = "EventListCard.fxml";

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The issue on AddressBook level 4</a>
     */

    public final ReadOnlyEvent event;

    @FXML
    private HBox cardPane;
    @FXML
    private Label title;
    @FXML
    private Label id;
    @FXML
    private Label description;
    @FXML
    private Label eventLocation;
    @FXML
    private Label datetime;

    public EventCard(ReadOnlyEvent event, int displayedIndex) {
        super(FXML);
        id.setText(displayedIndex + ". ");
        this.event = event;
        bindListeners(event);
    }

    /**
     * Binds the individual UI elements to observe their respective {@code Event} properties
     * so that they will be notified of any changes.
     */
    private void bindListeners(ReadOnlyEvent event) {
        title.textProperty().bind(Bindings.convert(event.titleProperty()));
        description.textProperty().bind(Bindings.convert(event.descriptionProperty()));
        eventLocation.textProperty().bind(Bindings.convert(event.locationProperty()));
        datetime.textProperty().bind(Bindings.convert(event.datetimeProperty()));
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof EventCard)) {
            return false;
        }

        // state check
        EventCard card = (EventCard) other;
        return id.getText().equals(card.id.getText())
                && event.equals(card.event);
    }
}
```
###### /java/seedu/address/ui/EventListPanel.java
``` java
/**
 * Panel containing the list of events.
 */
public class EventListPanel extends UiPart<Region> {
    private static final String FXML = "EventListPanel.fxml";
    private final Logger logger = LogsCenter.getLogger(EventListPanel.class);

    @FXML
    private ListView<EventCard> eventListView;

    public EventListPanel(ObservableList<ReadOnlyEvent> eventList) {
        super(FXML);
        setConnections(eventList);
        registerAsAnEventHandler(this);
    }

    private void setConnections(ObservableList<ReadOnlyEvent> eventList) {
        ObservableList<EventCard> mappedList = EasyBind.map(
                eventList, (event) -> new EventCard(event, eventList.indexOf(event) + 1));
        eventListView.setItems(mappedList);
        eventListView.setCellFactory(listView -> new EventListViewCell());
        setEventHandlerForSelectionChangeEvent();
    }

    private void setEventHandlerForSelectionChangeEvent() {
        eventListView.getSelectionModel().selectedItemProperty()
                .addListener((observable, oldValue, newValue) -> {
                    if (newValue != null) {
                        logger.fine("Selection in person list panel changed to : '" + newValue + "'");
                        raise(new EventPanelSelectionChangedEvent(newValue));
                    }
                });
    }

    /**
     * Scrolls to the {@code PersonCard} at the {@code index} and selects it.
     */
    private void scrollTo(int index) {
        Platform.runLater(() -> {
            eventListView.scrollTo(index);
            eventListView.getSelectionModel().clearAndSelect(index);
        });
    }

    @Subscribe
    private void handleJumpToListRequestEvent(JumpToListRequestEvent event) {
        logger.info(LogsCenter.getEventHandlingLogMessage(event));
        scrollTo(event.targetIndex);
    }

    /**
     * Custom {@code ListCell} that displays the graphics of a {@code EventCard}.
     */
    class EventListViewCell extends ListCell<EventCard> {

        @Override
        protected void updateItem(EventCard event, boolean empty) {
            super.updateItem(event, empty);

            if (empty || event == null) {
                setGraphic(null);
                setText(null);
            } else {
                setGraphic(event.getRoot());
            }
        }
    }
}
```
###### /resources/view/MainWindow.fxml
``` fxml
            <TabPane fx:id="tabPane" VBox.vgrow="ALWAYS" tabClosingPolicy="UNAVAILABLE">
                <tabs>
                    <Tab fx:id="contactTab" text="Contacts">
                        <StackPane fx:id="personListPanelPlaceholder" VBox.vgrow="ALWAYS"/>
                    </Tab>
                    <Tab fx:id="eventTab" text="Events">
                        <StackPane fx:id="eventListPanelPlaceholder" VBox.vgrow="ALWAYS"/>
                    </Tab>
                </tabs>
            </TabPane>

        </VBox>
    </SplitPane>

    <StackPane VBox.vgrow="NEVER" fx:id="commandBoxPlaceholder" styleClass="pane-with-border">
        <padding>
            <Insets top="5" right="10" bottom="5" left="10"/>
        </padding>
    </StackPane>

    <StackPane fx:id="statusbarPlaceholder" VBox.vgrow="NEVER">
        <padding>
            <Insets top="0" right="23" bottom="5" left="23"/>
        </padding>
    </StackPane>
</VBox>
```

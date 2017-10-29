# tingtx
###### /java/seedu/address/logic/commands/OrderCommand.java
``` java
    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof OrderCommand // instanceof handles nulls
                && this.orderParameter.equals(((OrderCommand) other).orderParameter)); // state check
    }

    public static String getCommandWord() {
        return COMMAND_WORD;
    }
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
        Comparator<Person>orderByBirthday = comparing(a->a.getBirthday().getReformatDate(), nullsLast(naturalOrder()));
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
    //@@ author

    public void setPersons(UniquePersonList replacement) {
        this.internalList.setAll(replacement.internalList);
    }

    public void setPersons(List<? extends ReadOnlyPerson> persons) throws DuplicatePersonException {
        final UniquePersonList replacement = new UniquePersonList();
        for (final ReadOnlyPerson person : persons) {
            replacement.add(new Person(person));
        }
        setPersons(replacement);
    }

    /**
     * Returns the backing list as an unmodifiable {@code ObservableList}.
     */
    public ObservableList<ReadOnlyPerson> asObservableList() {
        return FXCollections.unmodifiableObservableList(mappedList);
    }

    @Override
    public Iterator<Person> iterator() {
        return internalList.iterator();
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof UniquePersonList // instanceof handles nulls
                && this.internalList.equals(((UniquePersonList) other).internalList));
    }

    @Override
    public int hashCode() {
        return internalList.hashCode();
    }
}
```

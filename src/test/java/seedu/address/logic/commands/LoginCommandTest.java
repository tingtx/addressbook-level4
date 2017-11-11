package seedu.address.logic.commands;

//@@author quanle1994

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import seedu.address.logic.commands.lockmodelstub.ModelStub;
import seedu.address.model.user.ReadOnlyUser;
import seedu.address.model.user.User;
import seedu.address.model.user.exceptions.DuplicateUserException;

/**
 * Test Login Command
 */
public class LoginCommandTest {
    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void execute_loginSuccessful() throws Exception {
        ModelStubAcceptingUserAdded modelStub = new ModelStubAcceptingUserAdded();

        new LockCommandTest().getLockCommand("test", "123", modelStub).execute();

        new LockCommandTest().getLockCommand("lequangquan", "123", modelStub).execute();
    }

    private class ModelStubAcceptingUserAdded extends ModelStub {
        private User userAdded;

        @Override
        public void persistUserAccount(ReadOnlyUser user) throws DuplicateUserException {
            userAdded = new User(user);
        }
    }
}

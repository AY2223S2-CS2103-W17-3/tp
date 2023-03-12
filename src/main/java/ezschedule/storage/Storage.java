package ezschedule.storage;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Optional;

import ezschedule.commons.exceptions.DataConversionException;
import ezschedule.model.ReadOnlyAddressBook;
import ezschedule.model.ReadOnlyUserPrefs;
import ezschedule.model.UserPrefs;

/**
 * API of the Storage component
 */
public interface Storage extends AddressBookStorage, UserPrefsStorage {

    @Override
    Optional<UserPrefs> readUserPrefs() throws DataConversionException, IOException;

    @Override
    void saveUserPrefs(ReadOnlyUserPrefs userPrefs) throws IOException;

    @Override
    Path getAddressBookFilePath();

    @Override
    Optional<ReadOnlyAddressBook> readAddressBook() throws DataConversionException, IOException;

    @Override
    void saveAddressBook(ReadOnlyAddressBook addressBook) throws IOException;

}

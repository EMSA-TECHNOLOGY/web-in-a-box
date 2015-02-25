package net.springfieldusa.registration.comp.junit.tests;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import net.springfieldusa.credentials.Credential;
import net.springfieldusa.password.EncryptionException;
import net.springfieldusa.password.PasswordService;
import net.springfieldusa.registration.comp.UserRegistrationComponent;

import org.eclipselabs.emongo.MongoDatabaseProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;

public class TestUserRegistrationComponent
{
  private UserRegistrationComponent userRegistrationComponent;
  private PasswordService passwordService;
  private MongoDatabaseProvider mongoDatabaseProvider;
  private DB db;
  private DBCollection collection;
  private String email;
  private String password;
  private String encryptedPassword;
  private byte[] salt;

  @Before
  public void setUp() throws EncryptionException
  {
    email = "nobody@nowhere.org";
    password = "password";
    salt = new byte[] {0, 1, 2};
    encryptedPassword = "encryptedPassword";
    passwordService = mock(PasswordService.class);
    mongoDatabaseProvider = mock(MongoDatabaseProvider.class);
    db = mock(DB.class);
    collection = mock(DBCollection.class);
    
    userRegistrationComponent = new UserRegistrationComponent();
    userRegistrationComponent.bindMongoDatabaseProvider(mongoDatabaseProvider);
    userRegistrationComponent.bindPasswordService(passwordService);
    
    when(mongoDatabaseProvider.getDB()).thenReturn(db);
    when(db.getCollection("registrations")).thenReturn(collection);
    when(passwordService.createSalt()).thenReturn(salt);
    when(passwordService.encryptPassword(password, salt)).thenReturn(encryptedPassword.getBytes());
  }

  @Test
  public void testRegisterUser() throws EncryptionException
  {
    Credential userRegistration = new Credential(email, password);
    userRegistrationComponent.registerUser(userRegistration);
    ArgumentCaptor<DBObject> argument = ArgumentCaptor.forClass(DBObject.class);
    verify(collection).insert(argument.capture());
    assertThat(argument.getValue().get("email"), is(email));
    assertThat(argument.getValue().get("password"), is(encryptedPassword.getBytes()));
  }
}

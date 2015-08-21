package net.springfieldusa.registration.comp.junit.tests;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;

import net.springfieldusa.credentials.Credential;
import net.springfieldusa.password.EncryptionException;
import net.springfieldusa.password.PasswordService;
import net.springfieldusa.registration.RegistrationException;
import net.springfieldusa.registration.comp.UserRegistrationComponent;
import net.springfieldusa.storage.StorageService;

public class TestUserRegistrationComponent
{
  private UserRegistrationComponent userRegistrationComponent;
  private PasswordService passwordService;
  private StorageService storageService;
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
    storageService = mock(StorageService.class);
    
    userRegistrationComponent = new UserRegistrationComponent();
    userRegistrationComponent.bindStorageService(storageService);
    userRegistrationComponent.bindPasswordService(passwordService);
    
    when(passwordService.createSalt()).thenReturn(salt);
    when(passwordService.encryptPassword(password, salt)).thenReturn(encryptedPassword.getBytes());
  }

  @Test
  public void testRegisterUser() throws EncryptionException, RegistrationException, JSONException
  {
    Credential userRegistration = new Credential(email, password);
    userRegistrationComponent.registerUser(userRegistration);
    ArgumentCaptor<JSONObject> argument = ArgumentCaptor.forClass(JSONObject.class);
    verify(storageService).create(eq("registrations"), argument.capture());
    assertThat(argument.getValue().get("userId"), is(email));
    assertThat(argument.getValue().get("password"), is(encryptedPassword.getBytes()));
  }
}

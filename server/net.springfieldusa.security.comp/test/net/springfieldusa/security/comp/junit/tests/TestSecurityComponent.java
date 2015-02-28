package net.springfieldusa.security.comp.junit.tests;

import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.fail;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.Principal;

import net.springfieldusa.credentials.CredentialsService;
import net.springfieldusa.password.EncryptionException;
import net.springfieldusa.security.comp.SecurityComponent;

import org.junit.Before;
import org.junit.Test;

public class TestSecurityComponent
{
  private String email;
  private String password;
  private Principal principal;
  private CredentialsService credentialsService;
  private SecurityComponent securityComponent;
  
  @Before
  public void setUp() throws EncryptionException
  {
    securityComponent = new SecurityComponent();
    principal = mock(Principal.class);
    credentialsService = mock(CredentialsService.class);
    securityComponent.bindCredentialService(credentialsService);
    
    when(credentialsService.authenticate(email, password)).thenReturn(principal);
  }
  
  @Test
  public void testAuthenticate()
  {
    assertThat(securityComponent.authenticate(email, password), is(principal));
    assertThat(securityComponent.authenticate(email, "bad"), is(nullValue()));
    assertThat(securityComponent.authenticate("bad", password), is(nullValue()));
  }
  
  @Test
  public void testAuthorize()
  {
    fail("not implemented");
  }
}

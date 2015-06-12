package net.springfieldusa.jwt.comp;

import java.io.IOException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.Principal;
import java.security.SignatureException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.log.LogService;

import com.auth0.jwt.JWTSigner;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.JWTVerifyException;

import net.springfieldusa.comp.AbstractComponent;
import net.springfieldusa.credentials.Credential;
import net.springfieldusa.jwt.TokenService;
import net.springfieldusa.security.SecurityService;

@Component(service = TokenService.class)
public class TokenComponent extends AbstractComponent implements TokenService
{
  private volatile SecurityService securityService;
  private JWTSigner signer;
  private JWTVerifier verifier;
  
  @Activate
  public void activate()
  {
    signer = new JWTSigner("super-simple-secret");
    verifier = new JWTVerifier("super-simple-secret");
  }
  
  @Override
  public String createToken(Credential credentials)
  {
    Principal principal = securityService.authenticate(credentials);
    
    if(principal == null)
      return null;
    
    Collection<String> roles = securityService.getRoles(principal);
    
    Map<String, Object> claims = new HashMap<>();
    claims.put("userId", principal.getName());
    claims.put("roles", roles);
    return signer.sign(claims);
  }

  @Override
  public Map<String, Object> verifyToken(String token)
  {
    try
    {
      return verifier.verify(token);
    }
    catch (InvalidKeyException | NoSuchAlgorithmException | IllegalStateException | SignatureException | IOException | JWTVerifyException e)
    {
      log(LogService.LOG_DEBUG, "JWT token verification exception", e);
      // TODO: better exception handling
      return null;
    }
  }
  
  @Reference(unbind = "-")
  public void bindSecurityService(SecurityService securityService)
  {
    this.securityService = securityService;
  }
}

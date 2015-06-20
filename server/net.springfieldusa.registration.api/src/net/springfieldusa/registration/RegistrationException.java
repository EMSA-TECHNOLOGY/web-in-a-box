package net.springfieldusa.registration;

public class RegistrationException extends Exception
{
  private static final long serialVersionUID = 8754976822232183915L;

  public RegistrationException()
  {}

  public RegistrationException(String message)
  {
    super(message);
  }

  public RegistrationException(Throwable cause)
  {
    super(cause);
  }

  public RegistrationException(String message, Throwable cause)
  {
    super(message, cause);
  }

  public RegistrationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
  {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}

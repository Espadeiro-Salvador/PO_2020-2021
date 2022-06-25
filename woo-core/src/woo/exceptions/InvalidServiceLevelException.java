package woo.exceptions;

public class InvalidServiceLevelException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202010311350L;

  private String _serviceLevel;

  public InvalidServiceLevelException(String serviceLevel) {
    _serviceLevel = serviceLevel;
  }

  public String getServiceLevel() {
    return _serviceLevel;
  }
}

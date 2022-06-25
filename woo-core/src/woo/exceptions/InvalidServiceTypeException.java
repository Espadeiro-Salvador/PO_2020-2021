package woo.exceptions;

public class InvalidServiceTypeException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202010311350L;

  private String _serviceType;

  public InvalidServiceTypeException(String serviceType) {
    _serviceType = serviceType;
  }

  public String getServiceType() {
    return _serviceType;
  }
}

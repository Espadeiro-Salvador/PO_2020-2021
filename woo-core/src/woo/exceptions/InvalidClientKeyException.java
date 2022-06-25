package woo.exceptions;

public class InvalidClientKeyException extends Exception {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202010311350L;

  private String _key;

  public InvalidClientKeyException(String key) {
    _key = key;
  }

  public String getKey() {
    return _key;
  }
}

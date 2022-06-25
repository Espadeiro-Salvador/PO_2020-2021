package woo.exceptions;

public class UnavailableClientKeyException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202010311350L;

  private String _key;

  public UnavailableClientKeyException(String key) {
    _key = key;
  }

  public String getKey() {
    return _key;
  }
}

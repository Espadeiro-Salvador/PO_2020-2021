package woo.exceptions;

public class InvalidTransactionKeyException extends Exception {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202010311350L;

  private int _key;

  public InvalidTransactionKeyException(int key) {
    _key = key;
  }

  public int getKey() {
    return _key;
  }
}

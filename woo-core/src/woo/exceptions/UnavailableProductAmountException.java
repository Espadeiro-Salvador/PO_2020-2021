package woo.exceptions;

public class UnavailableProductAmountException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 20201031613L;

  private String _key;
  private int _available;

  public UnavailableProductAmountException(String key, int available) {
    _key = key;
    _available = available;
  }

  public String getKey() {
    return _key;
  }

  public int getAvailableAmount() {
    return _available;
  }
}
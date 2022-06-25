package woo.exceptions;

public class UnknownDataException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202010311350L;

  private String _data;

  public UnknownDataException(String data) {
    _data = data;
  }

  public String getData() {
    return _data;
  }
}

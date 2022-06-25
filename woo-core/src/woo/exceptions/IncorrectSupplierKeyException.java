package woo.exceptions;

public class IncorrectSupplierKeyException extends Exception {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202010311350L;

  private String _supplierKey;
  private String _productKey;

  public IncorrectSupplierKeyException(String supplierKey, String productKey) {
    _productKey = productKey;
    _supplierKey = supplierKey;
  }

  public String getSupplierKey() {
    return _supplierKey;
  }

  public String getProductKey() {
    return _productKey;
  }
}
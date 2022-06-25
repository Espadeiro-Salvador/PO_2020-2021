package woo.notifications;

import java.io.Serializable;

import woo.Product;

public class Notification implements Serializable {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202012051631L;

  /** Enum for the diferrent types of notifications */
  public enum NotificationType {
    NEW, BARGAIN
  };

  private NotificationType _type;
  private String _productKey;
  private int _price;

  public Notification(NotificationType type, Product product) {
    _type = type;
    _productKey = product.getKey();
    _price = product.getPrice();
  }

  @Override
  public String toString() {
      return String.format("%s|%s|%d", _type, _productKey, _price);
  }
}

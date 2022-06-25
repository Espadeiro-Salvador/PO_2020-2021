package woo;

import java.io.Serializable;
import java.util.ArrayList;

import woo.exceptions.UnavailableProductAmountException;
import woo.notifications.Notification;
import woo.notifications.NotificationReceiver;
import woo.notifications.Notification.NotificationType;

public abstract class Product implements Serializable {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202011011137L;
  
  private String _name;
  private String _key;
  private int _price;
  private int _criticalLevel;
  private int _stock;
  private String _supplierKey;
  /** Number of days used for getting the value of a sale */
  private int _timeLimit;

  /** Collection of notification receivers (observers) */
  private ArrayList<NotificationReceiver> _observers = new ArrayList<NotificationReceiver>();

  public Product(String key, int price, int criticalLevel, String supplierKey, String name, int timeLimit) {
    this(key, price, criticalLevel, supplierKey, 0, name, timeLimit);
  }

  public Product(String key, int price, int criticalLevel, String supplierKey, int stock, String name, int timeLimit) {
    _key = key;
    _price = price;
    _criticalLevel = criticalLevel;
    _stock = stock;
    _supplierKey = supplierKey;
    _name = name;
    _timeLimit = timeLimit;
  }

  public boolean toggleNotifications(NotificationReceiver observer) {
    if (_observers.contains(observer)) {
      _observers.remove(observer);
      return false;
    }
    _observers.add(observer);
    return true;
  }

  /** Updates all the notification receivers */

  private void notify(Notification notification) {
    for (NotificationReceiver observer : _observers) {
      observer.update(notification);
    }
  }

  public void addStock(int amount) {
    if (amount > 0) {
      if (_stock == 0)
        notify(new Notification(NotificationType.NEW, this));
      _stock += amount;
    }
  }

  public void removeStock(int amount) throws UnavailableProductAmountException {
    if (_stock - amount < 0) {
      throw new UnavailableProductAmountException(_key, _stock);
    }
    _stock -= amount;
  }

  public String getKey() {
    return _key;
  }

  public int getPrice() {
    return _price;
  }

  public void setPrice(int price) {
    if (price <= 0) return;

    int change = price - _price;
    _price = price;
    
    // Send notification when price is decreased
    if (change < 0)
      notify(new Notification(NotificationType.BARGAIN, this));
  }

  public int getStock() {
    return _stock;
  }

  public String getSupplierKey() {
    return _supplierKey;
  }

  public int getTimeLimit() {
    return _timeLimit;
  }

  @Override
  public String toString() {
    return String.format("%s|%s|%s|%d|%d|%d", _name, _key, _supplierKey, _price, _criticalLevel, _stock);
  }
}

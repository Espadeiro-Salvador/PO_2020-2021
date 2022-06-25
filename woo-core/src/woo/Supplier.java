package woo;

import java.io.Serializable;
import java.util.TreeMap;
import woo.notifications.NotificationReceiver;

public class Supplier extends NotificationReceiver implements Serializable {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202011011137L;

  private String _key;
  private String _name;
  private String _address;
  private boolean _active;

  /** Collection of orders involving this supplier */
  private TreeMap<Integer, Order> _orders = new TreeMap<Integer, Order>();

  public Supplier(String key, String name, String address) {
    _key = key;
    _name = name;
    _address = address;
    _active = true;
  }

  public void addOrder(Order order) {
    _orders.put(order.getId(), order);
  }

  public Iterable<Order> getTransactions() {
    return _orders.values();
  }

  public void toggle() {
    _active = !_active;
  }
  
  public boolean isActive() {
    return _active;
  }
  
  public String getKey() {
    return _key;
  }

  @Override
  public String toString() {
    return String.format("%s|%s|%s|", _key, _name, _address);
  }
}

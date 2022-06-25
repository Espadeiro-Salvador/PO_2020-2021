package woo;

import java.util.TreeMap;

import woo.notifications.NotificationReceiver;
import woo.status.NormalStatus;
import woo.status.Status;

public class Client extends NotificationReceiver {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202011011137L;

  private String _key;
  private String _name;
  private String _address;
  private Status _status;

  private float _totalPaid = 0;
  private int _salesValue = 0;

  private TreeMap<Integer, Sale> _sales = new TreeMap<Integer, Sale>();

  public Client(String key, String name, String address) {
    _key = key;
    _name = name;
    _address = address;
    _status = new NormalStatus(this);
  }

  public String getKey() {
    return _key;
  }

  public void addSale(Sale sale) {
    _sales.put(sale.getId(), sale);
    _salesValue += sale.getBaseCost();
  }

  public Iterable<Sale> getTransactions() {
    return _sales.values();
  }

  public float getCurrentSaleCost(Sale sale, Sale.TimePeriod period, int deadlineDistance) {
    return _status.getCurrentCost(sale, period, deadlineDistance);
  }

  public float pay(Sale sale, Sale.TimePeriod period, int deadlineDistance) {
    float paid = _status.pay(sale, period, deadlineDistance);
    _totalPaid += paid;
    return paid;
  }

  public void setStatus(Status status) {
    _status = status;
  }

  @Override
  public String toString() {
    int paid = Math.round(_totalPaid);
    return String.format("%s|%s|%s|%s|%d|%d", _key, _name, _address, _status, _salesValue, paid);
  }
}

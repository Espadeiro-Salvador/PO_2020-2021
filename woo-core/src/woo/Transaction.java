package woo;

import java.io.Serializable;

public abstract class Transaction implements Serializable {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202011071631L;

  private int _id;
  private int _baseCost = 0;
  private int _paymentDate;

  public Transaction(int id) {
    _id = id;
  }

  public int getId() {
    return _id;
  }

  /** Returns the actual value of a transaction, positive if it is a sale or negative if it is an order */
  public abstract float getValue(int currentDate);

  public int getBaseCost() {
    return _baseCost;
  }

  protected void setBaseCost(int baseCost) {
    _baseCost = baseCost;
  }

  public int getPaymentDate() {
    return _paymentDate;
  }

  public int getDeadLine() {
    return _paymentDate;
  }

  public boolean isPaid() {
    return true;
  }

  public void setPaymentDate(int date) {
    _paymentDate = date;
  }

  public float pay(int currentDate) {
    return 0f;
  }

  @Override
  public String toString() {
    return String.format("%d", _id);
  }
}

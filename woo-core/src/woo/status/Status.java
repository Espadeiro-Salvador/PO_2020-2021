package woo.status;

import java.io.Serializable;
import woo.Client;
import woo.Sale;

public abstract class Status implements Serializable {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202011271723L;
  
  private Client _client;
  private int _points;

  public Status(Client client) {
    this(client, 0);
  }

  protected Status(Client client, int points) {
    _client = client;
    _points = points;
  }

  /** Gets the current cost of the sale, according to the current date */
  public abstract float getCurrentCost(Sale sale, Sale.TimePeriod period, int deadlineDistance);

  /** Pays a sale and updates client points and status */
  public float pay(Sale sale, Sale.TimePeriod period, int deadlineDistance) {
    float paidValue = getCurrentCost(sale, period, deadlineDistance);
    updatePoints(period, paidValue, deadlineDistance);

    return paidValue;
  }
  
  protected void updatePoints(Sale.TimePeriod period, float paidValue, int deadlineDistance) {
    if (period == Sale.TimePeriod.P1 || period == Sale.TimePeriod.P2) {
      _points += (int)paidValue * 10;
    }
  }

  protected int getPoints() {
    return _points;
  }

  protected void setPoints(int points) {
    _points = points;
  }
  
  /** Upgrades the client status, by default does nothing */
  protected void upgradeStatus() {}

  /** Downgrades the client status, by default does nothing */
  protected void downgradeStatus() {}

  protected void updateStatus(Status status) {
    _client.setStatus(status);
  }

  protected Client getClient() {
    return _client;
  }
}

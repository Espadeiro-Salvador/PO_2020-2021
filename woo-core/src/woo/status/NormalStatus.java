package woo.status;

import woo.Client;
import woo.Sale;
import woo.Sale.TimePeriod;

public class NormalStatus extends Status {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202011011137L;

  public NormalStatus(Client client) {
    super(client);
  }

  protected NormalStatus(Client client, int points) {
    super(client, points);
  }

  @Override
  public float getCurrentCost(Sale sale, TimePeriod period, int deadlineDistance) {
    if (period == Sale.TimePeriod.P1) {
      return 0.9f * sale.getBaseCost();
    } else if (period == Sale.TimePeriod.P2) {
      return sale.getBaseCost();
    } else if (period == Sale.TimePeriod.P3) {
      return (1f + 0.05f * deadlineDistance) * sale.getBaseCost();
    } else {
      return (1f + 0.1f * deadlineDistance) * sale.getBaseCost();
    }
  }

  @Override
  protected void updatePoints(Sale.TimePeriod period, float paidValue, int deadlineDistance) {
    super.updatePoints(period, paidValue, deadlineDistance);
    
    if (getPoints() > 2000) {
      upgradeStatus();
    }

    if ((period == Sale.TimePeriod.P3 && deadlineDistance > 2) || period == Sale.TimePeriod.P4) {
      setPoints((int)(getPoints() * 0.1f));
    }
  }
  
  @Override
  protected void upgradeStatus() {
    if (getPoints() > 25000)
      updateStatus(new EliteStatus(getClient(), getPoints()));
    else
      updateStatus(new SelectionStatus(getClient(), getPoints()));
  }
  
  @Override
  public String toString() {
    return "NORMAL";
  }
}

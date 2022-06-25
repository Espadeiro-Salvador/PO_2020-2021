package woo.status;

import woo.Client;
import woo.Sale;
import woo.Sale.TimePeriod;

public class SelectionStatus extends Status {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202011011137L;

  protected SelectionStatus(Client client, int points) {
    super(client, points);
  }

  @Override
  public float getCurrentCost(Sale sale, TimePeriod period, int deadlineDistance) {
    if (period == Sale.TimePeriod.P1) {
      return 0.9f * sale.getBaseCost();
    } else if (period == Sale.TimePeriod.P2 && deadlineDistance >= 2) {
      return 0.95f * sale.getBaseCost();
    } else if (period == Sale.TimePeriod.P2 || (period == Sale.TimePeriod.P3 && deadlineDistance <= 1)) {
      return sale.getBaseCost();
    } else if (period == Sale.TimePeriod.P3) {
      return (1f + 0.02f * deadlineDistance) * sale.getBaseCost();
    } else {
      return (1f + 0.05f * deadlineDistance) * sale.getBaseCost();
    }
  }

  @Override
  protected void updatePoints(Sale.TimePeriod period, float paidValue, int deadlineDistance) {
    super.updatePoints(period, paidValue, deadlineDistance);

    if (getPoints() > 25000) {
      upgradeStatus();
    } else if ((period == Sale.TimePeriod.P3 && deadlineDistance > 2) || period == Sale.TimePeriod.P4) {
      setPoints((int) (getPoints() * 0.1f));
      downgradeStatus();
    }
  }

  @Override
  protected void upgradeStatus() {
    updateStatus(new EliteStatus(getClient(), getPoints()));
  }

  @Override
  protected void downgradeStatus() {
    updateStatus(new NormalStatus(getClient(), getPoints()));
  }

  @Override
  public String toString() {
    return "SELECTION";
  }
}

package woo.status;

import woo.Client;
import woo.Sale;
import woo.Sale.TimePeriod;

public class EliteStatus extends Status {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202011011137L;

  protected EliteStatus(Client client, int points) {
    super(client, points);
  }

  @Override
  public float getCurrentCost(Sale sale, TimePeriod period, int deadlineDistance) {
    if (period == Sale.TimePeriod.P1 || period == Sale.TimePeriod.P2) {
      return 0.9f * sale.getBaseCost();
    } else if (period == Sale.TimePeriod.P3) {
      return 0.95f * sale.getBaseCost();
    } else {
      return sale.getBaseCost();
    }
  }

  @Override
  protected void updatePoints(Sale.TimePeriod period, float paidValue, int deadlineDistance) {
    super.updatePoints(period, paidValue, deadlineDistance);
    if ((period == Sale.TimePeriod.P3 || period == Sale.TimePeriod.P4) && deadlineDistance > 15) {
      setPoints((int)(getPoints() * 0.25f));
      downgradeStatus();
    }
  }

  @Override
  protected void downgradeStatus() {
    updateStatus(new SelectionStatus(getClient(), getPoints()));
  }

  @Override
  public String toString() {
    return "ELITE";
  }
}

package woo;

public class Sale extends Transaction {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202012051431L;

  private int _quantity;
  private boolean _paid = false;
  private int _deadline;

  private float _currentCost;
  private float _totalCost;

  private Client _client;
  private Product _product;

  /** Time periods used for getting a sale value */
  public static enum TimePeriod {
    P1, P2, P3, P4
  }

  public Sale(int id, Client client, Product product, int quantity, int deadline) {
    super(id);
    setBaseCost(product.getPrice() * quantity);
    _quantity = quantity;
    _client = client;
    _product = product;
    _deadline = deadline;
  }

  public Product getProduct() {
    return _product;
  }
  
  private TimePeriod getTimePeriod(int currentDate) {
    if (currentDate <= _deadline - _product.getTimeLimit()) {
      return TimePeriod.P1;
    } else if (currentDate <= _deadline) {
      return TimePeriod.P2;
    } else if (currentDate <= _deadline + _product.getTimeLimit()) {
      return TimePeriod.P3;
    } else {
      return TimePeriod.P4;
    }
  }

  private int getDeadlineDistance(int currentDate) {
    return Math.abs(currentDate - _deadline);
  }

  @Override
  public int getDeadLine() {
    return _deadline;
  } 

  @Override
  public float getValue(int currentDate) {
    if (_paid)
      return _totalCost;

    TimePeriod period = getTimePeriod(currentDate);
    _currentCost = _client.getCurrentSaleCost(this, period, getDeadlineDistance(currentDate));

    return _currentCost;
  }

  @Override
  public float pay(int currentDate) {
    if (_paid)
      return 0f;

    _paid = true;
    setPaymentDate(currentDate);

    TimePeriod period = getTimePeriod(currentDate);
    _totalCost = _client.pay(this, period, getDeadlineDistance(currentDate));

    return _totalCost;
  }

  @Override
  public boolean isPaid() {
    return _paid;
  }

  @Override
  public String toString() {
    String text = String.format("|%s|%s|%d|%d|%d|%d", _client.getKey(), _product.getKey(), _quantity, getBaseCost(),
        Math.round(_currentCost), _deadline);
    if (_paid)
      text += String.format("|%d", getPaymentDate());
    return super.toString() + text;
  }
}

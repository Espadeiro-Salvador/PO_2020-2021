package woo;

import woo.exceptions.InvalidServiceTypeException;

public class Box extends Product {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202011011137L;

  private enum ServiceType {
    NORMAL, AIR, EXPRESS, PERSONAL
  }

  private ServiceType _serviceType;

  public Box(String key, int price, int criticalLevel, String supplierKey, String serviceType)
      throws InvalidServiceTypeException {
    this(key, price, criticalLevel, supplierKey, serviceType, 0);
  }

  public Box(String key, int price, int criticalLevel, String supplierKey, String serviceType, int stock)
      throws InvalidServiceTypeException {
    this(key, price, criticalLevel, supplierKey, serviceType, stock, "BOX", 5);
  }

  /** Constructor only used by subclasses and other constructors */
  protected Box(String key, int price, int criticalLevel, String supplierKey, String serviceType, int stock,
      String name, int n) throws InvalidServiceTypeException {
      super(key, price, criticalLevel, supplierKey, stock, name, n);

      try {
        _serviceType = ServiceType.valueOf(serviceType);
      } catch (IllegalArgumentException e) {
        throw new InvalidServiceTypeException(serviceType);
      }
  }

  @Override
  public String toString() {
    return super.toString() + "|" + _serviceType;
  }
}

package woo;

import woo.exceptions.InvalidServiceLevelException;
import woo.exceptions.InvalidServiceTypeException;

class Container extends Box {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202011011137L;
    
  private enum ServiceLevel {
    B4, C4, C5, DL
  }
  private ServiceLevel _serviceLevel;

  public Container(String key, int price, int criticalLevel, String supplierKey, String serviceType,
      String serviceLevel) throws InvalidServiceLevelException, InvalidServiceTypeException {
    this(key, price, criticalLevel, supplierKey, serviceType, serviceLevel, 0);
  }

  public Container(String key, int price, int criticalLevel, String supplierKey, String serviceType,
      String serviceLevel, int stock) throws InvalidServiceLevelException, InvalidServiceTypeException {
    super(key, price, criticalLevel, supplierKey, serviceType, stock, "CONTAINER", 8);

    try {
      _serviceLevel = ServiceLevel.valueOf(serviceLevel);
    } catch (IllegalArgumentException e) {
      throw new InvalidServiceLevelException(serviceLevel);
    }
  }

  @Override
  public String toString() {
    return super.toString() + "|" + _serviceLevel;
  }
}

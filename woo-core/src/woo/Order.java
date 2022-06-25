package woo;

import java.util.TreeMap;

import woo.exceptions.InactiveSupplierException;
import woo.exceptions.IncorrectSupplierKeyException;

public class Order extends Transaction {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202012051431L;

  /** Class for holding a product and its quantity */
  private class ProductData {
    private Product _product;
    private int _quantity;

    public ProductData(Product product, int quantity) {
      _product = product;
      _quantity = quantity;
    }
  }

  /** Collection that contains the prodcts of the order and their quantities */
  private TreeMap<String, ProductData> _products = new TreeMap<String, ProductData>(String.CASE_INSENSITIVE_ORDER);
  private Supplier _supplier;
  
  public Order(int id, Supplier supplier) throws InactiveSupplierException {
    super(id);
    setBaseCost(0);
    
    _supplier = supplier;

    if (!supplier.isActive()) {
      throw new InactiveSupplierException(_supplier.getKey());
    }
  }

  public void addProduct(Product product, int quantity) throws IncorrectSupplierKeyException {
    if (!product.getSupplierKey().equals(_supplier.getKey())) {
      throw new IncorrectSupplierKeyException(product.getSupplierKey(), product.getKey());
    }

    ProductData data = _products.get(product.getKey());
    if (data == null) {
      _products.put(product.getKey(), new ProductData(product, quantity));
    } else {
      data._quantity += quantity;
    }

    setBaseCost(getBaseCost() + quantity * product.getPrice());
  }

  /** Closes the order and adds it to the supplier data */
  public void close(int currentDate) {
    setPaymentDate(currentDate);
    for (ProductData data : _products.values()) {
      data._product.addStock(data._quantity);
    }
    _supplier.addOrder(this);
  }

  @Override
  public float getValue(int currentDate) {
    return -getBaseCost();
  }

  @Override
  public String toString() {
    String text = super.toString();
    text += String.format("|%s|%d|%d", _supplier.getKey(), getBaseCost(), getPaymentDate());

    for (ProductData data : _products.values()) {
      text += String.format("\n%s|%d", data._product.getKey(), data._quantity);
    }

    return text;
  }
}

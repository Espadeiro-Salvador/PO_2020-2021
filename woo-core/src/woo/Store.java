package woo;

import java.io.IOException;
import java.io.Serializable;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.TreeMap;
import woo.exceptions.*;
import woo.notifications.Notification;

/**
 * Class Store implements a store.
 */
class Store implements Serializable {

  /** Serial number for serialization. */
  private static final long serialVersionUID = 202009192006L;

  /** Actual date */
  private int _date = 0;
  /** Available balance of the store */
  private float _availableBalance = 0f;
  /** Accounting balance of the store */
  private float _accountingBalance = 0f;
  
  /** Collection for storing clients and indexing them by their key */
  private TreeMap<String, Client> _clients = new TreeMap<String, Client>(String.CASE_INSENSITIVE_ORDER);
  /** Collection for storing suppliers and indexing them by their key */
  private TreeMap<String, Supplier> _suppliers = new TreeMap<String, Supplier>(String.CASE_INSENSITIVE_ORDER);
  /** Collection for storing products and indexing them by their key */
  private TreeMap<String, Product> _products = new TreeMap<String, Product>(String.CASE_INSENSITIVE_ORDER);
  /** Collection for storing transactions and indexing them by their id */
  private TreeMap<Integer, Transaction> _transactions = new TreeMap<Integer, Transaction>();

  /** Id of the next transaction to be created */
  private int _nextTransactionId = 0;
  /** Stores an order while it is not complete */
  private Order _openOrder; 

  // Default constructor
  public Store() {}

  /**
   * Returns the current date.
   * 
   * @return the current date
   */
  public int getCurrentDate() {
    return _date;
  }

  /**
   * Advances the current date.
   *
   * @param daysToAdvance the number of days to advance the current date
   */
  public void advanceDate(int daysToAdvance) {
    _accountingBalance = 0f;
    _date += daysToAdvance;
    for (Transaction transaction : _transactions.values()) {
      _accountingBalance += transaction.getValue(_date);
    }
  }

  /**
   * Return the clients of the store.
   * 
   * @return all of the clients
   */
  public Iterable<Client> getClients() {
    return _clients.values();
  }

  /**
   * Returns a client associated with the given key.
   * 
   * @param key the key of the client
   * @return the client with said key
   * @throws InvalidClientKeyException
   */
  public Client getClient(String key) throws InvalidClientKeyException {
    if (!_clients.containsKey(key)) {
      throw new InvalidClientKeyException(key);
    }
    
    return _clients.get(key);
  }

  public Iterable<Client> getClientsByProduct(String key) throws InvalidProductKeyException {
    Product product = _products.get(key);
    ArrayList<Client> clients = new ArrayList<Client>();
    if (product == null) {
      throw new InvalidProductKeyException(key);
    }
    for (Client client : _clients.values()) {
      for (Sale s : client.getTransactions()) {
        if (s.getProduct() == product) {
          clients.add(client);
          break;
        }
      }
    }
    return clients;
  }
  public Iterable<Sale> getClientTransactions(String key) throws InvalidClientKeyException {
    Client client = getClient(key);
    return client.getTransactions();
  }

  public Iterable<Transaction> getPaidLate() {
    ArrayList<Transaction> transactions = new ArrayList<Transaction>();
    for (Transaction t : _transactions.values()) {
      if (t.getPaymentDate() > t.getDeadLine() && t.isPaid()) {
        transactions.add(t);
      }
    }
    return transactions;
  }

  public Iterable<Notification> getSupplierNotifications(String key) throws InvalidSupplierKeyException {
    return getSupplier(key).getNotifications();
  }

  public Iterable<Notification> getClientNotifications(String key) throws InvalidClientKeyException {
    return getClient(key).getNotifications();
  }

  public boolean toggleClientNotifications(String key, String productKey) throws InvalidClientKeyException, InvalidProductKeyException {
    Client client = getClient(key);
    Product product = _products.get(productKey);
    if (product == null) {
      throw new InvalidProductKeyException(productKey);
    }

    return product.toggleNotifications(client);
  }

  /**
   * Creates a client.
   * 
   * @param key     the key of the client
   * @param name    the name of the client
   * @param address the address of the client
   * @return the client that was created
   * @throws UnavailableClientKeyException
   */
  public Client createClient(String key, String name, String address) throws UnavailableClientKeyException {
    if (_clients.containsKey(key)) {
      throw new UnavailableClientKeyException(key);
    }

    Client client = new Client(key, name, address);
    _clients.put(key, client);

    for (Product product : _products.values()) {
      product.toggleNotifications(client);
    }

    return client;
  }

  /**
   * Returns the suppliers of the store.
   * 
   * @return all the suppliers
   */
  public Iterable<Supplier> getSuppliers() {
    return _suppliers.values();
  }

  /**
   * Returns a supplier associated eith the given key.
   * 
   * @param key the key of the supplier
   * @return the supplier with said key
   * @throws InvalidSupplierKeyException
   */
  public Supplier getSupplier(String key) throws InvalidSupplierKeyException {
    if (!_suppliers.containsKey(key)) {
      throw new InvalidSupplierKeyException(key);
    }

    return _suppliers.get(key);
  }

  /**
   * Creates a supplier.
   * 
   * @param key     the key of the supplier
   * @param name    the name of the supplier
   * @param address the address of the supplier
   * @return the supplier that was created
   * @throws UnavailableSupplierKeyException
   */
  public Supplier createSupplier(String key, String name, String address) throws UnavailableSupplierKeyException {
    if (_suppliers.containsKey(key)) {
      throw new UnavailableSupplierKeyException(key);
    }

    Supplier supplier = new Supplier(key, name, address);
    _suppliers.put(key, supplier);
    
    for (Product product : _products.values()) {
      product.toggleNotifications(supplier);
    }
    return supplier;
  }

  public Iterable<Order> getSupplierTransactions(String key) throws InvalidSupplierKeyException {
    Supplier supplier = getSupplier(key);
    return supplier.getTransactions();
  }


  public boolean toggleSupplierTransactions(String supplierKey) throws InvalidSupplierKeyException {
    Supplier supplier = _suppliers.get(supplierKey);

    if (supplier == null) {
      throw new InvalidSupplierKeyException(supplierKey);
    }

    supplier.toggle();
    return supplier.isActive();
  }

  /**
   * Verifies if a supplier is active.
   * 
   * @param supplier the supplier
   * @return true if the account can be removed, and false, otherwise
   */
  public boolean isSupplierActive(Supplier supplier) {
    return supplier.isActive();
  }

  /**
   * Returns the products of the store.
   * 
   * @return all the products
   */
  public Iterable<Product> getProducts() {
    return _products.values();
  }

  private void toggleNotifications(Product product) {
    for (Client client : _clients.values()) {
      product.toggleNotifications(client);
    }
  }

  /**
   * Creates a book.
   * 
   * @param key     the key of the book
   * @param price    the price of the book
   * @param criticalLevel the minimum stock level
   * @param supplierKey the key of the product supplier
   * @param title the title of the book
   * @param author the author of the book
   * @param isbn the isbn of the book
   * @param stock the initial stock
   * @return the product created
   * @throws InvalidSupplierKeyException
   * @throws UnavailableProductKeyException
   */
  public Product createProductBook(String key, int price, int criticalLevel, String supplierKey, String title, String author,
      String isbn, int stock) throws InvalidSupplierKeyException, UnavailableProductKeyException {
    if (_products.containsKey(key)) {
      throw new UnavailableProductKeyException(key);
    } else if (!_suppliers.containsKey(supplierKey)) {
      throw new InvalidSupplierKeyException(supplierKey);
    }

    Product book = new Book(key, price, criticalLevel, supplierKey, title, author, isbn, stock);
    _products.put(key, book);
    toggleNotifications(book);

    return book;
  }
  
   /**
   * Creates a box.
   * 
   * @param key     the key of the book
   * @param price    the name of the book
   * @param criticalLevel the minimum stock level
   * @param supplierKey the key of the product supplier
   * @param serviceType the service type of the box
   * @param stock the initial stock
   * @return the product created
   * @throws InvalidSupplierKeyException
   * @throws UnavailableProductKeyException 
   * @throws InvalidServiceTypeException
   */
  public Product createProductBox(String key, int price, int criticalLevel, String serviceType, String supplierKey, int stock)
      throws InvalidSupplierKeyException, UnavailableProductKeyException, InvalidServiceTypeException {
    if (_products.containsKey(key)) {
      throw new UnavailableProductKeyException(key);
    } else if (!_suppliers.containsKey(supplierKey)) {
      throw new InvalidSupplierKeyException(supplierKey);
    }

    Product box = new Box(key, price, criticalLevel, supplierKey, serviceType, stock);
    _products.put(key, box);
    toggleNotifications(box);

    return box;
  }


   /**
   * Creates a container.
   * 
   * @param key     the key of the book
   * @param price    the name of the book
   * @param criticalLevel the minimum stock level
   * @param supplierKey the key of the product supplier
   * @param serviceType the service type of the container
   * @param serviceLevel the service level of the container
   * @param stock the initial stock
   * @return the product created
   * @throws InvalidSupplierKeyException
   * @throws UnavailableProductKeyException 
   * @throws InvalidServiceTypeException
   */
  public Product createProductContainer(String key, int price, int criticalLevel, String serviceType, String serviceLevel,
      String supplierKey, int stock) throws UnavailableProductKeyException, InvalidSupplierKeyException,
      InvalidServiceTypeException, InvalidServiceLevelException {
    if (_products.containsKey(key)) {
      throw new UnavailableProductKeyException(key);
    } else if (!_suppliers.containsKey(supplierKey)) {
      throw new InvalidSupplierKeyException(supplierKey);
    }

    Product container = new Container(key, price, criticalLevel, supplierKey, serviceType, serviceLevel, stock);
    _products.put(key, container);
    toggleNotifications(container);

    return container;
  }

  public void updateProductPrice(String key, int price) throws InvalidProductKeyException {
    Product product = _products.get(key);
    if (product == null) {
      throw new InvalidProductKeyException(key);
    }

    product.setPrice(price);
  }

  public Transaction createSaleTransaction(String clientKey, int deadline, String productKey, int quantity)
      throws InvalidClientKeyException, InvalidProductKeyException, UnavailableProductAmountException {
    Client client = _clients.get(clientKey);
    if (client == null) {
      throw new InvalidClientKeyException(clientKey);
    }

    Product product = _products.get(productKey);
    if (product == null) {
      throw new InvalidProductKeyException(productKey);
    }

    product.removeStock(quantity);
    Sale sale = new Sale(_nextTransactionId++, client, product, quantity, deadline);
    client.addSale(sale);
    _transactions.put(sale.getId(), sale);
    _accountingBalance += sale.getValue(_date);

    return sale;
  }

  public Transaction getTransaction(int key) throws InvalidTransactionKeyException {
    Transaction transaction = _transactions.get(key);
    if (transaction == null) {
      throw new InvalidTransactionKeyException(key);
    }
    return transaction;
  }

  public Transaction openOrderTransaction(String supplierKey) throws InvalidSupplierKeyException, InactiveSupplierException {
    Supplier supplier = _suppliers.get(supplierKey);
    
    if (supplier == null) {
      throw new InvalidSupplierKeyException(supplierKey);
    }

    _openOrder = new Order(_nextTransactionId, supplier);
    return _openOrder;
  }

  public void addProductToOrderTransaction(String productKey, int quantity) throws IncorrectSupplierKeyException, InvalidProductKeyException {
    Product product = _products.get(productKey);
    if (product == null) {
      _openOrder = null;
      throw new InvalidProductKeyException(productKey);
    }

    _openOrder.addProduct(product, quantity);
  }

  public void closeOrderTransaction() {
    _transactions.put(_openOrder.getId(), _openOrder);
    _availableBalance -= _openOrder.getBaseCost();
    _accountingBalance -= _openOrder.getBaseCost();
    _openOrder.close(_date);
    _nextTransactionId++;
  }

  public void pay(int saleId) throws InvalidTransactionKeyException {
    Transaction transaction = _transactions.get(saleId);
    if (transaction == null) {
      throw new InvalidTransactionKeyException(saleId);
    }

    float value = transaction.pay(_date);
    _availableBalance += value;
  }

  public float getAvailableBalance() {
    return _availableBalance;
  }

  public float getAccountingBalance() {
    return _accountingBalance;
  }

  /** Gets a collection of the sales paid by a client */
  public Iterable<Sale> getClientPayments(String key) throws InvalidClientKeyException {
    Iterable<Sale> sales = getClientTransactions(key);
    ArrayList<Sale> paid = new ArrayList<Sale>();
    for (Sale sale : sales) {
      if (sale.isPaid())
        paid.add(sale);
    }

    return paid;
  }

  public Iterable<Product> getProductsUnderTopPrice(int topPrice) {
    Iterable<Product> allProducts = getProducts();
    ArrayList<Product> products = new ArrayList<Product>();
    for (Product product : allProducts) {
      if (product.getPrice() < topPrice)
        products.add(product);
    }
    
    return products;
  }

   public Iterable<Product> getProductsBougthByClient(String key) throws InvalidClientKeyException {
    ArrayList<Product> products = new ArrayList<Product>();
    Client client = getClient(key);
    Iterable<Sale> sales = client.getTransactions();
    for (Sale s : sales) {
      Product p = s.getProduct();
      if (!products.contains(p) && s.isPaid()) {
        products.add(p);
      }
    }
    return products;
  }
    
  /**
   * @param txtfile filename to be loaded.
   * @throws IOException
   * @throws BadEntryException
   */
  public void importFile(String txtfile) throws IOException, BadEntryException {
    BufferedReader reader = new BufferedReader(new FileReader(txtfile));
    String line;

    while ((line = reader.readLine()) != null) {
      String[] fields = line.split("\\|");
      registerFieldsData(fields);
    }

    reader.close();
  }
  
  /**
   * Creates an entity from a line of the imported file.
   * 
   * @param fields
   * @throws BadEntryException
   */
  private void registerFieldsData(String[] fields) throws BadEntryException {
    try {
      switch (fields[0]) {
        case "CLIENT":
          createClient(fields[1], fields[2], fields[3]);
          break;
        case "SUPPLIER":
          createSupplier(fields[1], fields[2], fields[3]);
          break;
        case "BOOK":
          createProductBook(fields[1], Integer.parseInt(fields[6]), Integer.parseInt(fields[7]), fields[5], fields[2],
              fields[3], fields[4], Integer.parseInt(fields[8]));
          break;
        case "BOX":
          createProductBox(fields[1], Integer.parseInt(fields[4]), Integer.parseInt(fields[5]), fields[2], fields[3],
              Integer.parseInt(fields[6]));
          break;
        case "CONTAINER":
          createProductContainer(fields[1], Integer.parseInt(fields[5]), Integer.parseInt(fields[6]), fields[2],
              fields[3], fields[4], Integer.parseInt(fields[7]));
          break;
        default:
          UnknownDataException uda = new UnknownDataException(fields[0]);
          throw new BadEntryException(uda.getData(), uda);
      }
    } catch (UnavailableClientKeyException e) {
      throw new BadEntryException(e.getKey(), e);
    } catch (UnavailableSupplierKeyException e) {
      throw new BadEntryException(e.getKey(), e);
    } catch (UnavailableProductKeyException e) {
      throw new BadEntryException(e.getKey(), e);
    } catch (InvalidSupplierKeyException e) {
      throw new BadEntryException(e.getKey(), e);
    } catch (InvalidServiceTypeException e) {
      throw new BadEntryException(e.getServiceType(), e);
    } catch (InvalidServiceLevelException e) {
      throw new BadEntryException(e.getServiceLevel(), e);
    }
  }
}

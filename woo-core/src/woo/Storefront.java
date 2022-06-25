package woo;

import java.io.IOException;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectInputStream;
import woo.exceptions.*;
import woo.notifications.Notification;

/**
 * Storefront: façade for the core classes.
 */
public class Storefront {

  /** Current filename. */
  private String _filename = "";

  /** The actual store. */
  private Store _store = new Store();

  /**
   * Returns the current date.
   * 
   * @return the current date
   */
  public int getCurrentDate() {
    return _store.getCurrentDate();
  }

  /**
   * Advances the current date.
   *
   * @param daysToAdvance the number of days to advance the current date
   */
  public void advanceDate(int daysToAdvance) {
    _store.advanceDate(daysToAdvance);
  }

  /**
   * Return the clients of the store.
   * 
   * @return all of the clients
   */
  public Iterable<Client> getClients() {
    return _store.getClients();
  }

  /**
   * Returns a client associated with the given key.
   * 
   * @param key the key of the client
   * @return the client with said key
   * @throws InvalidClientKeyException
   */
  public Client getClient(String key) throws InvalidClientKeyException {
    return _store.getClient(key);
  }

  public Iterable<Sale> getClientTransactions(String key) throws InvalidClientKeyException {
    return _store.getClientTransactions(key);
  }

  public Iterable<Notification> getClientNotifications(String key) throws InvalidClientKeyException {
    return _store.getClientNotifications(key);
  }

  public Iterable<Notification> getSupplierNotifications(String key) throws InvalidSupplierKeyException {
    return _store.getSupplierNotifications(key);
  }

  public Supplier getSupplier(String key) throws InvalidSupplierKeyException {
    return _store.getSupplier(key);
  }

  public boolean toggleClientNotifications(String key, String productKey)
      throws InvalidClientKeyException, InvalidProductKeyException {
    return _store.toggleClientNotifications(key, productKey);
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
  public Client registerClient(String key, String name, String address) throws UnavailableClientKeyException {
    return _store.createClient(key, name, address);
  }

  /**
   * Returns the suppliers of the store.
   * 
   * @return all the suppliers
   */
  public Iterable<Supplier> getSuppliers() {
    return _store.getSuppliers();
  }

  public Supplier registerSupplier(String key, String name, String address) throws UnavailableSupplierKeyException {
    return _store.createSupplier(key, name, address);
  }

  public Iterable<Order> getSupplierTransactions(String key) throws InvalidSupplierKeyException {
    return _store.getSupplierTransactions(key);
  }

  public boolean toggleSupplierTransactions(String supplierKey) throws InvalidSupplierKeyException {
    return _store.toggleSupplierTransactions(supplierKey);
  }

  /**
   * Verifies if a supplier is active.
   * 
   * @param supplier the supplier
   * @return true if the account can be removed, and false, otherwise
   */
  public boolean isSupplierActive(Supplier supplier) {
    return _store.isSupplierActive(supplier);
  }

  /**
   * Returns the products of the store.
   * 
   * @return all the products
   */
  public Iterable<Product> getProducts() {
    return _store.getProducts();
  }

  public Iterable<Product> getProductsBougthByClient(String key) throws InvalidClientKeyException {
    return _store.getProductsBougthByClient(key);
  }

  public Product registerProductBook(String key, int price, int criticalLevel, String supplierKey, String title,
      String author, String isbn) throws InvalidSupplierKeyException, UnavailableProductKeyException {
    return _store.createProductBook(key, price, criticalLevel, supplierKey, title, author, isbn, 0);
  }

  public Product registerProductBox(String key, int price, int criticalLevel, String serviceType, String supplierKey)
      throws InvalidSupplierKeyException, UnavailableProductKeyException, InvalidServiceTypeException {
    return _store.createProductBox(key, price, criticalLevel, serviceType, supplierKey, 0);
  }

  public Product registerProductContainer(String key, int price, int criticalLevel, String serviceType,
      String serviceLevel, String supplierKey) throws UnavailableProductKeyException, InvalidSupplierKeyException,
      InvalidServiceTypeException, InvalidServiceLevelException {
    return _store.createProductContainer(key, price, criticalLevel, serviceType, serviceLevel, supplierKey, 0);
  }

  public void changeProductPrice(String key, int price) throws InvalidProductKeyException {
    _store.updateProductPrice(key, price);
  }

  public Transaction getTransaction(int key) throws InvalidTransactionKeyException {
    return _store.getTransaction(key);
  }

  public Iterable<Transaction> getPaidLate() {
    return _store.getPaidLate();
  }

  public Transaction registerSaleTransaction(String clientKey, int deadline, String productKey, int quantity)
      throws InvalidClientKeyException, InvalidProductKeyException, UnavailableProductAmountException {
    return _store.createSaleTransaction(clientKey, deadline, productKey, quantity);
  }

  public void pay(int saleId) throws InvalidTransactionKeyException {
    _store.pay(saleId);
  }

  public Transaction openOrderTransaction(String supplierKey) throws InvalidSupplierKeyException, InactiveSupplierException{
    return _store.openOrderTransaction(supplierKey);
  }

  public void addProductToOrderTransaction(String productKey, int quantity) throws IncorrectSupplierKeyException, InvalidProductKeyException {
    _store.addProductToOrderTransaction(productKey, quantity);
  }

  public void closeOrderTransaction() {
    _store.closeOrderTransaction();
  }

  public float getAvailableBalance() {
    return _store.getAvailableBalance();
  }

  public float getAccountingBalance() {
    return _store.getAccountingBalance();
  }

  public Iterable<Client> getClientsByProduct(String key) throws InvalidProductKeyException {
    return _store.getClientsByProduct(key);
  }
  
  public Iterable<Sale> getClientPayments(String key) throws InvalidClientKeyException {
    return _store.getClientPayments(key);
  }

  public Iterable<Product> getProductsUnderTopPrice(int topPrice) {
    return _store.getProductsUnderTopPrice(topPrice);
  }
  
  /**
   * @throws IOException
   * @throws FileNotFoundException
   * @throws MissingFileAssociationException
   */
  public void save() throws IOException, FileNotFoundException, MissingFileAssociationException {
    if (_filename.equals("")) {
      throw new MissingFileAssociationException();
    }
    ObjectOutputStream outputfile = new ObjectOutputStream(new BufferedOutputStream(new FileOutputStream(_filename)));
    outputfile.writeObject(_store);
    outputfile.close();
  }

  /**
   * @param filename
   * @throws MissingFileAssociationException
   * @throws IOException
   * @throws FileNotFoundException
   */
  public void saveAs(String filename) throws MissingFileAssociationException, FileNotFoundException, IOException {
    _filename = filename;
    save();
  }

  /**
   * @param filename
   * @throws UnavailableFileException
   */
  public void load(String filename) throws UnavailableFileException {
    try {
      ObjectInputStream inputobject = new ObjectInputStream(new BufferedInputStream(new FileInputStream(filename)));
      _store = (Store) inputobject.readObject();
      inputobject.close();
      _filename = filename;
    } catch (FileNotFoundException e) {
      throw new UnavailableFileException(filename);
    } catch (ClassNotFoundException e) {
      throw new UnavailableFileException(filename);
    } catch (IOException e) {
      throw new UnavailableFileException(filename);
    }
  }

  /**
   * @param textfile
   * @throws ImportFileException
   */
  public void importFile(String textfile) throws ImportFileException {
    try {
      _store.importFile(textfile);
    } catch (IOException | BadEntryException e) {
      throw new ImportFileException(textfile);
    }
  }

}

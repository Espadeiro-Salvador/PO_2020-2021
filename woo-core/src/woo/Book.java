package woo;

class Book extends Product {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202011011137L;

  private String _title;
  private String _author;
  private String _isbn;

  public Book(String key, int price, int criticalLevel, String supplierKey, String title, String author, String isbn) {
    this(key, price, criticalLevel, supplierKey, title, author, isbn, 0);
  }

  public Book(String key, int price, int criticalLevel, String supplierKey, String title, String author, String isbn,
      int stock) {
    super(key, price, criticalLevel, supplierKey, stock, "BOOK", 3);
    _title = title;
    _author = author;
    _isbn = isbn;
  }
  
  @Override
  public String toString() {
    return super.toString() + "|" + String.format("%s|%s|%s", _title, _author, _isbn);
  }
}

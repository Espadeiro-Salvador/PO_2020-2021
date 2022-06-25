package woo.app.products;

import pt.tecnico.po.ui.Command; 
import pt.tecnico.po.ui.DialogException;
import woo.Storefront;                        
import woo.Product;

/**
 * Show all products.
 */
public class DoShowAllProducts extends Command<Storefront> {

  public DoShowAllProducts(Storefront receiver) {
    super(Label.SHOW_ALL_PRODUCTS, receiver);
  }

  @Override
  public final void execute() throws DialogException {
    Iterable<Product> products = _receiver.getProducts();

    for (Product p : products) {
      _display.addLine(p.toString());
    }
    
    _display.display();
  }

}

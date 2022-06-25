package woo.app.suppliers;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import woo.Storefront; 
import woo.Supplier;

/**
 * Show all suppliers.
 */
public class DoShowSuppliers extends Command<Storefront> {
  public DoShowSuppliers(Storefront receiver) {
    super(Label.SHOW_ALL_SUPPLIERS, receiver);
  }

  @Override
  public void execute() throws DialogException {
    Iterable<Supplier> suppliers = _receiver.getSuppliers();

    for (Supplier supplier : suppliers) {
      _display.addLine(supplier.toString());
      _display.add(_receiver.isSupplierActive(supplier) ? Message.yes() : Message.no());
    }
    
    _display.display();
  }
}

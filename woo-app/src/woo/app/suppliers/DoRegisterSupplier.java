package woo.app.suppliers;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.exceptions.UnavailableSupplierKeyException;
import woo.app.exceptions.DuplicateSupplierKeyException;

/**
 * Register supplier.
 */
public class DoRegisterSupplier extends Command<Storefront> {

  private Input<String> _key;
  private Input<String> _name;
  private Input<String> _address;

  public DoRegisterSupplier(Storefront receiver) {
    super(Label.REGISTER_SUPPLIER, receiver);
    _key = _form.addStringInput(Message.requestSupplierKey());
    _name = _form.addStringInput(Message.requestSupplierName());
    _address = _form.addStringInput(Message.requestSupplierAddress());
  }

  @Override
  public void execute() throws DialogException {
    try {
      _form.parse();
      _receiver.registerSupplier(_key.value(), _name.value(), _address.value());
    } catch (UnavailableSupplierKeyException e) {
      throw new DuplicateSupplierKeyException(e.getKey());
    }
  }

}

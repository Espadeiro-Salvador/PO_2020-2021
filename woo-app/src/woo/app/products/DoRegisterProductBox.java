package woo.app.products;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.exceptions.UnavailableProductKeyException;
import woo.exceptions.InvalidSupplierKeyException;
import woo.exceptions.InvalidServiceTypeException;
import woo.app.exceptions.UnknownSupplierKeyException;
import woo.app.exceptions.DuplicateProductKeyException;
import woo.app.exceptions.UnknownServiceTypeException;

/**
 * Register box.
 */
public class DoRegisterProductBox extends Command<Storefront> {

  private Input<String> _key;
  private Input<Integer> _price;
  private Input<Integer> _criticalLevel;
  private Input<String> _supplierKey;
  private Input<String> _serviceType;

  public DoRegisterProductBox(Storefront receiver) {
    super(Label.REGISTER_BOX, receiver);
    _key = _form.addStringInput(Message.requestProductKey());
    _price = _form.addIntegerInput(Message.requestPrice());
    _criticalLevel = _form.addIntegerInput(Message.requestStockCriticalValue());
    _supplierKey = _form.addStringInput(Message.requestSupplierKey());
    _serviceType = _form.addStringInput(Message.requestServiceType());
  }

  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      _receiver.registerProductBox(_key.value(), _price.value(), _criticalLevel.value(), _serviceType.value(),
          _supplierKey.value());
    } catch (UnavailableProductKeyException e) {
      throw new DuplicateProductKeyException(e.getKey());
    } catch (InvalidSupplierKeyException e) {
      throw new UnknownSupplierKeyException(e.getKey());
    } catch (InvalidServiceTypeException e) {
      throw new UnknownServiceTypeException(e.getServiceType());
    }
  }
}

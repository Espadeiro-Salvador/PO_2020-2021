package woo.app.products;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.exceptions.UnavailableProductKeyException;
import woo.exceptions.InvalidSupplierKeyException;
import woo.exceptions.InvalidServiceTypeException;
import woo.exceptions.InvalidServiceLevelException;
import woo.app.exceptions.UnknownSupplierKeyException;
import woo.app.exceptions.DuplicateProductKeyException;
import woo.app.exceptions.UnknownServiceTypeException;
import woo.app.exceptions.UnknownServiceLevelException;

/**
 * Register container.
 */
public class DoRegisterProductContainer extends Command<Storefront> {

  private Input<String> _key;
  private Input<Integer> _price;
  private Input<Integer> _criticalLevel;
  private Input<String> _supplierKey;
  private Input<String> _serviceType;
  private Input<String> _serviceLevel;

  public DoRegisterProductContainer(Storefront receiver) {
    super(Label.REGISTER_CONTAINER, receiver);
    _key = _form.addStringInput(Message.requestProductKey());
    _price = _form.addIntegerInput(Message.requestPrice());
    _criticalLevel = _form.addIntegerInput(Message.requestStockCriticalValue());
    _supplierKey = _form.addStringInput(Message.requestSupplierKey());
    _serviceType = _form.addStringInput(Message.requestServiceType());
    _serviceLevel = _form.addStringInput(Message.requestServiceLevel());
  }

  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      _receiver.registerProductContainer(_key.value(), _price.value(), _criticalLevel.value(), _serviceType.value(),
          _serviceLevel.value(), _supplierKey.value());
    } catch (UnavailableProductKeyException e) {
      throw new DuplicateProductKeyException(e.getKey());
    } catch (InvalidSupplierKeyException e) {
      throw new UnknownSupplierKeyException(e.getKey());
    } catch (InvalidServiceTypeException e) {
      throw new UnknownServiceTypeException(e.getServiceType());
    } catch (InvalidServiceLevelException e) {
      throw new UnknownServiceLevelException(e.getServiceLevel());
    }
  }
}

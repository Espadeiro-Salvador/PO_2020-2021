package woo.app.transactions;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Form;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.exceptions.InvalidSupplierKeyException;
import woo.exceptions.InactiveSupplierException;
import woo.exceptions.InvalidProductKeyException;
import woo.exceptions.IncorrectSupplierKeyException;
import woo.app.exceptions.UnknownSupplierKeyException;
import woo.app.exceptions.UnauthorizedSupplierException;
import woo.app.exceptions.UnknownProductKeyException;
import woo.app.exceptions.WrongSupplierException;

/**
 * Register order.
 */
public class DoRegisterOrderTransaction extends Command<Storefront> {

  private Input<String> _supplierKey;
  private Input<String> _productKey;
  private Input<Integer> _quantity;
  private Input<Boolean> _more;

  private Form _productForm;

  public DoRegisterOrderTransaction(Storefront receiver) {
    super(Label.REGISTER_ORDER_TRANSACTION, receiver);
    _supplierKey = _form.addStringInput(Message.requestSupplierKey());

    // Creates a form for adding products to an order
    _productForm = new Form();
    _productKey = _productForm.addStringInput(Message.requestProductKey());
    _quantity = _productForm.addIntegerInput(Message.requestAmount());
    _more = _productForm.addBooleanInput(Message.requestMore());
  }

  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      _productForm.parse();

      _receiver.openOrderTransaction(_supplierKey.value());
      _receiver.addProductToOrderTransaction(_productKey.value(), _quantity.value());

      while(_more.value()) {
        _productForm.parse();
        _receiver.addProductToOrderTransaction(_productKey.value(), _quantity.value());  
      }

      _receiver.closeOrderTransaction();
    } catch (InvalidSupplierKeyException e) {
      throw new UnknownSupplierKeyException(e.getKey());
    } catch (InactiveSupplierException e) {
      throw new UnauthorizedSupplierException(e.getKey());
    } catch (InvalidProductKeyException e) {
      throw new UnknownProductKeyException(e.getKey());
    } catch (IncorrectSupplierKeyException e) {
      throw new WrongSupplierException(_supplierKey.value(), _productKey.value());
    }
  }

}

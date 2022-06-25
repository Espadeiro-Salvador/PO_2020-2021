package woo.app.transactions;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.exceptions.InvalidClientKeyException;
import woo.exceptions.InvalidProductKeyException;
import woo.exceptions.UnavailableProductAmountException;
import woo.app.exceptions.UnavailableProductException;
import woo.app.exceptions.UnknownClientKeyException;
import woo.app.exceptions.UnknownProductKeyException;

/**
 * Register sale.
 */
public class DoRegisterSaleTransaction extends Command<Storefront> {

  private Input<String> _clientKey;
  private Input<Integer> _deadline;
  private Input<String> _productKey;
  private Input<Integer> _quantity;

  public DoRegisterSaleTransaction(Storefront receiver) {
    super(Label.REGISTER_SALE_TRANSACTION, receiver);
    _clientKey = _form.addStringInput(Message.requestClientKey());
    _deadline = _form.addIntegerInput(Message.requestPaymentDeadline());
    _productKey = _form.addStringInput(Message.requestProductKey());
    _quantity = _form.addIntegerInput(Message.requestAmount());
  }

  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      _receiver.registerSaleTransaction(_clientKey.value(), _deadline.value(), _productKey.value(), _quantity.value());
    } catch (InvalidClientKeyException e) {
      throw new UnknownClientKeyException(e.getKey());
    } catch (InvalidProductKeyException e) {
      throw new UnknownProductKeyException(e.getKey());
    } catch (UnavailableProductAmountException e) {
      throw new UnavailableProductException(e.getKey(), _quantity.value(), e.getAvailableAmount());
    }
  }
}

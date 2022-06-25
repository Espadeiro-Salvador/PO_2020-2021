package woo.app.transactions;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.exceptions.InvalidTransactionKeyException;
import woo.app.exceptions.UnknownTransactionKeyException;

/**
 * Pay transaction (sale).
 */
public class DoPay extends Command<Storefront> {

  private Input<Integer> _saleId;

  public DoPay(Storefront storefront) {
    super(Label.PAY, storefront);
    _saleId = _form.addIntegerInput(Message.requestTransactionKey());
  }

  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      _receiver.pay(_saleId.value());
    } catch (InvalidTransactionKeyException e) {
      throw new UnknownTransactionKeyException(e.getKey());
    }
  }

}

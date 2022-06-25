package woo.app.transactions;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.app.exceptions.UnknownTransactionKeyException;
import woo.exceptions.InvalidTransactionKeyException;

/**
 * Show specific transaction.
 */
public class DoShowTransaction extends Command<Storefront> {

  private Input<Integer> _id;

  public DoShowTransaction(Storefront receiver) {
    super(Label.SHOW_TRANSACTION, receiver);
    _id = _form.addIntegerInput(Message.requestTransactionKey());
  }

  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      _display.popup(_receiver.getTransaction(_id.value()));
    } catch (InvalidTransactionKeyException e) {
      throw new UnknownTransactionKeyException(e.getKey());
    }
  }

}

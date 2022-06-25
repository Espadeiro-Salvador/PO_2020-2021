package woo.app.clients;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.Transaction;
import woo.app.exceptions.UnknownClientKeyException;
import woo.exceptions.InvalidClientKeyException;

/**
 * Show all transactions for a specific client.
 */
public class DoShowClientTransactions extends Command<Storefront> {

  private Input<String> _key;

  public DoShowClientTransactions(Storefront storefront) {
    super(Label.SHOW_CLIENT_TRANSACTIONS, storefront);
    _key = _form.addStringInput(Message.requestClientKey());
  }

  @Override
  public void execute() throws DialogException {
    try {
      _form.parse();

      for (Transaction transaction : _receiver.getClientTransactions(_key.value())) {
        _display.addLine(transaction.toString());
      }
      
      _display.display();
    } catch (InvalidClientKeyException e) {
      throw new UnknownClientKeyException(e.getKey());
    }
  }

}

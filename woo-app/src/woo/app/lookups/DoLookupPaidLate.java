package woo.app.lookups;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.Transaction;
import woo.app.exceptions.UnknownClientKeyException;
import woo.exceptions.InvalidClientKeyException;

/**
 * Lookup payments by given client.
 */
public class DoLookupPaidLate extends Command<Storefront> {

  public DoLookupPaidLate(Storefront storefront) {
    super(Label.PAID_LATE, storefront);
  }

  @Override
  public void execute() throws DialogException {
    _form.parse();

    for (Transaction transaction : _receiver.getPaidLate()) {
      _display.addLine(transaction.toString());
    }
    
    _display.display();
  }

}
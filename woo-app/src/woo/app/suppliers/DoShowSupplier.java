package woo.app.suppliers;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront; 
import woo.Supplier;
import woo.notifications.Notification;
import woo.app.exceptions.UnknownSupplierKeyException;
import woo.exceptions.InvalidSupplierKeyException;

/**
 * Show all suppliers.
 */
public class DoShowSupplier extends Command<Storefront> {
  private Input<String> _key;

  public DoShowSupplier(Storefront storefront) {
    super(Label.SHOW_SUPPLIER, storefront);
    _key = _form.addStringInput(Message.requestSupplierKey());
  }

  @Override
  public void execute() throws DialogException {
    try {
      _form.parse();
      // Get client data and add it to the display
      _display.addLine(_receiver.getSupplier(_key.value()).toString());
      
      // Get client notifications and add them to the display
      Iterable<Notification> notifs = _receiver.getSupplierNotifications(_key.value());
      for (Notification notif : notifs) {
        _display.addLine(notif.toString());
      }
      
      _display.display();
    } catch (InvalidSupplierKeyException e) {
      throw new UnknownSupplierKeyException(e.getKey());
    } 
  }
}
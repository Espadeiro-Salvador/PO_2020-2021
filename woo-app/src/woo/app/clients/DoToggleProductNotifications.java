package woo.app.clients;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.app.exceptions.UnknownClientKeyException;
import woo.app.exceptions.UnknownProductKeyException;
import woo.exceptions.InvalidClientKeyException;
import woo.exceptions.InvalidProductKeyException;

/**
 * Toggle product-related notifications.
 */
public class DoToggleProductNotifications extends Command<Storefront> {

  private Input<String> _key;
  private Input<String> _productKey;

  public DoToggleProductNotifications(Storefront storefront) {
    super(Label.TOGGLE_PRODUCT_NOTIFICATIONS, storefront);
    _key = _form.addStringInput(Message.requestClientKey());
    _productKey = _form.addStringInput(Message.requestProductKey());
  }

  @Override
  public void execute() throws DialogException {
    try {
      _form.parse();

      // Toggle notifications and show their new state
      if (_receiver.toggleClientNotifications(_key.value(), _productKey.value())) {
        _display.popup(Message.notificationsOn(_key.value(), _productKey.value()));
      } else {
        _display.popup(Message.notificationsOff(_key.value(), _productKey.value()));
      }
    } catch (InvalidClientKeyException e) {
      throw new UnknownClientKeyException(e.getKey());
    } catch (InvalidProductKeyException e) {
      throw new UnknownProductKeyException(e.getKey());
    }
  }

}

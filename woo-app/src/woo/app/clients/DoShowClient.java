package woo.app.clients;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.app.exceptions.UnknownClientKeyException;
import woo.exceptions.InvalidClientKeyException;
import woo.notifications.Notification;

/**
 * Show client.
 */
public class DoShowClient extends Command<Storefront> {

  private Input<String> _key;

  public DoShowClient(Storefront storefront) {
    super(Label.SHOW_CLIENT, storefront);
    _key = _form.addStringInput(Message.requestClientKey());
  }

  @Override
  public void execute() throws DialogException {
    try {
      _form.parse();
      // Get client data and add it to the display
      _display.addLine(_receiver.getClient(_key.value()).toString());
      
      // Get client notifications and add them to the display
      Iterable<Notification> notifs = _receiver.getClientNotifications(_key.value());
      for (Notification notif : notifs) {
        _display.addLine(notif.toString());
      }
      
      _display.display();
    } catch (InvalidClientKeyException e) {
      throw new UnknownClientKeyException(e.getKey());
    } 
  }

}

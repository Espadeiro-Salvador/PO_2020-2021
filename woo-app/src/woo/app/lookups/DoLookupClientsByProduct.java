package woo.app.lookups;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.Client;
import woo.app.exceptions.UnknownProductKeyException;
import woo.exceptions.InvalidProductKeyException;


public class DoLookupClientsByProduct extends Command<Storefront> {

  private Input<String> _key;

  public DoLookupClientsByProduct(Storefront storefront) {
    super(Label.CLIENTS_BY_PRODUCT, storefront);
    _key = _form.addStringInput(Message.requestProductKey());
  }

  @Override
  public void execute() throws DialogException {
    try {
      _form.parse();

      for (Client client : _receiver.getClientsByProduct(_key.value())) {
        _display.addLine(client.toString());
      }
      
      _display.display();
    } catch (InvalidProductKeyException e) {
      throw new UnknownProductKeyException(e.getKey());
    }
  }

}

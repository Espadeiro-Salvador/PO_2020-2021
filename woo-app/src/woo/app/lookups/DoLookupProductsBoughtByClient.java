package woo.app.lookups;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Product;
import woo.Storefront;
import woo.exceptions.InvalidClientKeyException;
import woo.app.exceptions.UnknownClientKeyException;

public class DoLookupProductsBoughtByClient extends Command<Storefront> {

  Input<String> _key;

  public DoLookupProductsBoughtByClient(Storefront storefront) {
    super(Label.PRODUCTS_BY_CLIENT, storefront);
    _key =_form.addStringInput(Message.requestClientKey());
  }

  @Override
  public void execute() throws DialogException {
    try {
      _form.parse();

      for (Product product : _receiver.getProductsBougthByClient(_key.value())) {
        _display.addLine(product.toString());
      }

      _display.display();
    } catch (InvalidClientKeyException e) {
      throw new UnknownClientKeyException(e.getKey());
    }
    
  }
}
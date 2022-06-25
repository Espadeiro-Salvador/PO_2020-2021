package woo.app.products;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.app.exceptions.UnknownProductKeyException;
import woo.exceptions.InvalidProductKeyException;

/**
 * Change product price.
 */
public class DoChangePrice extends Command<Storefront> {

  private Input<String> _key;
  private Input<Integer> _price;

  public DoChangePrice(Storefront receiver) {
    super(Label.CHANGE_PRICE, receiver);
    _key = _form.addStringInput(Message.requestProductKey());
    _price = _form.addIntegerInput(Message.requestPrice());
  }

  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      _receiver.changeProductPrice(_key.value(), _price.value());
    } catch (InvalidProductKeyException e) {
      throw new UnknownProductKeyException(e.getKey());
    }
  }
}

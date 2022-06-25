package woo.app.products;

import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.app.exceptions.UnknownSupplierKeyException;
import woo.app.exceptions.DuplicateProductKeyException;
import woo.exceptions.InvalidSupplierKeyException;
import woo.exceptions.UnavailableProductKeyException;

/**
 * Register book.
 */
public class DoRegisterProductBook extends Command<Storefront> {

  private Input<String> _key;
  private Input<String> _title;
  private Input<String> _author;
  private Input<String> _isbn;
  private Input<Integer> _price;
  private Input<Integer> _criticalLevel;
  private Input<String> _supplierKey;

  public DoRegisterProductBook(Storefront receiver) {
    super(Label.REGISTER_BOOK, receiver);
    _key = _form.addStringInput(Message.requestProductKey());
    _title = _form.addStringInput(Message.requestBookTitle());
    _author = _form.addStringInput(Message.requestBookAuthor());
    _isbn = _form.addStringInput(Message.requestISBN());
    _price = _form.addIntegerInput(Message.requestPrice());
    _criticalLevel = _form.addIntegerInput(Message.requestStockCriticalValue());
    _supplierKey = _form.addStringInput(Message.requestSupplierKey());
  }

  @Override
  public final void execute() throws DialogException {
    try {
      _form.parse();
      _receiver.registerProductBook(_key.value(), _price.value(), _criticalLevel.value(), _supplierKey.value(),
          _title.value(), _author.value(), _isbn.value());
    } catch (UnavailableProductKeyException e) {
      throw new DuplicateProductKeyException(e.getKey());
    } catch (InvalidSupplierKeyException e) {
      throw new UnknownSupplierKeyException(e.getKey());
    }
  }
}

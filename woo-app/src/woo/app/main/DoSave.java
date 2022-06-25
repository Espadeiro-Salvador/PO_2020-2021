package woo.app.main;

import java.io.FileNotFoundException;
import java.io.IOException;
import pt.tecnico.po.ui.Command;
import pt.tecnico.po.ui.DialogException;
import pt.tecnico.po.ui.Input;
import woo.Storefront;
import woo.app.exceptions.FileOpenFailedException;
import woo.exceptions.MissingFileAssociationException;

/**
 * Save current state to file under current name (if unnamed, query for name).
 */
public class DoSave extends Command<Storefront> {

  private Input<String> _filename;

  /** @param receiver */
  public DoSave(Storefront receiver) {
    super(Label.SAVE, receiver);
  }

  /** @see pt.tecnico.po.ui.Command#execute() */
  @Override
  public final void execute() throws DialogException {
    try {
      _receiver.save();
    } catch (MissingFileAssociationException e) {
      try {
        _filename = _form.addStringInput(Message.newSaveAs());
        _form.parse();
        _receiver.saveAs(_filename.value());
      } catch (MissingFileAssociationException except) {
        throw new FileOpenFailedException(_filename.value());
      } catch (FileNotFoundException except) {
        throw new FileOpenFailedException(_filename.value());
      } catch (IOException except) {
        throw new FileOpenFailedException(_filename.value());
      }
    } catch (FileNotFoundException e) {
      throw new FileOpenFailedException(_filename.value());
    } catch (IOException e) {
      throw new FileOpenFailedException(_filename.value());
    }
  }
}

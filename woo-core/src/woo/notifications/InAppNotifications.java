package woo.notifications;

import java.io.Serializable;
import java.util.ArrayList;

public class InAppNotifications extends NotificationMethod implements Serializable {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202012051631L;

  /** List of notifications */
  private ArrayList<Notification> _notifications = new ArrayList<Notification>();

  @Override
  public Iterable<Notification> getNotifications() {
    Iterable<Notification> notifs = _notifications;
    _notifications = new ArrayList<Notification>();
    return notifs;
  }

  @Override
  public void sendNotification(Notification notification) {
    _notifications.add(notification);
  }
}

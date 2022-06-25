package woo.notifications;

import java.io.Serializable;

public abstract class NotificationReceiver implements Serializable {
  /** Serial number for serialization. */
  private static final long serialVersionUID = 202012051631L;

  private NotificationMethod _method;

  /** By default, notification method is set to InAppNotifications */
  public NotificationReceiver() {
    this(new InAppNotifications());
  }

  public NotificationReceiver(NotificationMethod method) {
    _method = method;
  }

  public void update(Notification notification) {
    _method.sendNotification(notification);
  }

  public Iterable<Notification> getNotifications() {
    return _method.getNotifications();
  }
}
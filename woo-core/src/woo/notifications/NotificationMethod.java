package woo.notifications;

public abstract class NotificationMethod {
    public abstract void sendNotification(Notification notification);

    /** Gets a collection of notififications if they are stored */
    public Iterable<Notification> getNotifications() {
        return null;
    }
}

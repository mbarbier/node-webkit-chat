
package mb.nodewebkitchat.server.model.event;

import mb.nodewebkitchat.server.model.Presence;
import mb.nodewebkitchat.server.model.User;

public class UserPresenceEvent {

    private final User user;
    private final Presence presence;

    public UserPresenceEvent(final User user, final Presence presence) {
        super();
        this.user = user;
        this.presence = presence;
    }

    public User getUser() {
        return user;
    }

    public Presence getPresence() {
        return presence;
    }

}

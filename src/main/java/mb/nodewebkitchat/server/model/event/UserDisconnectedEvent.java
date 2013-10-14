
package mb.nodewebkitchat.server.model.event;

import mb.nodewebkitchat.server.model.User;

public class UserDisconnectedEvent {

    private final User user;

    public UserDisconnectedEvent(final User user) {
        super();
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}

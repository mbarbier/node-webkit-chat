
package mb.nodewebkitchat.server.model.event;

import mb.nodewebkitchat.server.model.User;

public class UserConnectedEvent {

    private final User user;

    public UserConnectedEvent(final User user) {
        super();
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}


package mb.nodewebkitchat.server.model.event;

import mb.nodewebkitchat.server.model.User;

public class NewCallEvent {

    private final User user;

    public NewCallEvent(final User user) {
        super();
        this.user = user;
    }

    public User getUser() {
        return user;
    }

}

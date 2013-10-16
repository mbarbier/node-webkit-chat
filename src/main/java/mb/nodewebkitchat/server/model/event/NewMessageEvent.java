
package mb.nodewebkitchat.server.model.event;

import mb.nodewebkitchat.server.model.User;

public class NewMessageEvent {

    private final User user;
    private final String message;

    public NewMessageEvent(final User user, final String message) {
        super();
        this.user = user;
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public String getMessage() {
        return message;
    }

}

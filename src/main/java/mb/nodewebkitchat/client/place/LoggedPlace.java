
package mb.nodewebkitchat.client.place;

import mb.nodewebkitchat.server.model.User;

import com.ponysdk.core.place.Place;

public class LoggedPlace implements Place {

    public User name;

    public LoggedPlace(final User name) {
        this.name = name;
    }

}

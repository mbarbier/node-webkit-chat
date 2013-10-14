
package mb.nodewebkitchat.client.place;

import com.ponysdk.core.place.Place;

public class LoggedPlace implements Place {

    public String name;

    public LoggedPlace(final String name) {
        this.name = name;
    }

}

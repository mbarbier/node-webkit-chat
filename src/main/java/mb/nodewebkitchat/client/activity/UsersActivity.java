
package mb.nodewebkitchat.client.activity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import mb.nodewebkitchat.client.place.LoggedPlace;
import mb.nodewebkitchat.server.model.User;
import mb.nodewebkitchat.server.model.event.UserConnectedEvent;
import mb.nodewebkitchat.server.model.event.UserDisconnectedEvent;

import com.ponysdk.core.place.Place;
import com.ponysdk.ui.server.basic.IsPWidget;
import com.ponysdk.ui.server.basic.PFlowPanel;
import com.ponysdk.ui.server.basic.PHTML;
import com.ponysdk.ui.server.basic.PWidget;

public class UsersActivity extends NWCActivity {

    private PFlowPanel container;
    private final Map<String, PWidget> entryByUser = new HashMap<String, PWidget>();
    private String userName;

    @Override
    protected IsPWidget buildView() {
        container = new PFlowPanel();
        return container;
    }

    @Override
    protected void updateView(final Place place) {

        if (place instanceof LoggedPlace) {
            final LoggedPlace p = (LoggedPlace) place;
            userName = p.name;

            container.clear();
            final Collection<User> users = service.getConnectedUsers();
            for (final User user : users) {
                addEntry(user);
            }
        }

    }

    private void addEntry(final User user) {
        if (userName == null || userName.equals(user.name)) return;

        final PFlowPanel u = new PFlowPanel();
        u.add(new PHTML(user.name));
        container.add(u);
        entryByUser.put(user.name, u);
    }

    @Override
    public void onData(final Object data) {
        if (!started) return;
        if (data instanceof UserConnectedEvent) {
            final UserConnectedEvent e = (UserConnectedEvent) data;
            addEntry(e.getUser());
        } else if (data instanceof UserDisconnectedEvent) {
            final UserDisconnectedEvent e = (UserDisconnectedEvent) data;
            removeEntry(e.getUser());
        }
    }

    private void removeEntry(final User user) {
        if (userName == null || userName.equals(user.name)) return;

        final PWidget remove = entryByUser.remove(user.name);
        container.remove(remove);
    }
}


package mb.nodewebkitchat.client.activity;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import mb.nodewebkitchat.client.bootstrap.B;
import mb.nodewebkitchat.client.nodewebkit.NodeWebkit;
import mb.nodewebkitchat.client.nodewebkit.NodeWebkit.Handler;
import mb.nodewebkitchat.client.place.LoggedPlace;
import mb.nodewebkitchat.server.model.Presence;
import mb.nodewebkitchat.server.model.User;
import mb.nodewebkitchat.server.model.event.NewCallEvent;
import mb.nodewebkitchat.server.model.event.NewMessageEvent;
import mb.nodewebkitchat.server.model.event.UserConnectedEvent;
import mb.nodewebkitchat.server.model.event.UserDisconnectedEvent;
import mb.nodewebkitchat.server.model.event.UserPresenceEvent;

import org.json.JSONObject;

import com.ponysdk.core.place.Place;
import com.ponysdk.ui.server.basic.IsPWidget;
import com.ponysdk.ui.server.basic.PComplexPanel;
import com.ponysdk.ui.server.basic.PElement;
import com.ponysdk.ui.server.basic.PFlowPanel;
import com.ponysdk.ui.server.basic.PHTML;
import com.ponysdk.ui.server.basic.PHeaderPanel;
import com.ponysdk.ui.server.basic.PKeyCodes;
import com.ponysdk.ui.server.basic.PScrollPanel;
import com.ponysdk.ui.server.basic.PTextBox;
import com.ponysdk.ui.server.basic.PWidget;
import com.ponysdk.ui.server.basic.event.PClickEvent;
import com.ponysdk.ui.server.basic.event.PClickHandler;
import com.ponysdk.ui.server.basic.event.PKeyUpEvent;
import com.ponysdk.ui.server.basic.event.PKeyUpFilterHandler;

public class UsersActivity extends NWCActivity implements PClickHandler {

    private PHeaderPanel headerPanel;
    private PFlowPanel container;
    private PFlowPanel messageContainer;
    private PFlowPanel chatContainer;
    private PFlowPanel infoContainer;
    private PElement userContainer;
    private PHTML info;
    private User me;
    private final Map<String, PWidget> entryByUser = new HashMap<String, PWidget>();
    private PTextBox messageBox;

    @Override
    protected IsPWidget buildView() {
        info = new PHTML();
        info.setStyleName(B.ALERT);

        headerPanel = new PHeaderPanel();
        container = new PFlowPanel();
        userContainer = new PElement("ul");
        infoContainer = new PFlowPanel();
        messageContainer = new PFlowPanel();
        chatContainer = new PFlowPanel();
        infoContainer.add(info);
        container.add(infoContainer);
        container.add(userContainer);

        infoContainer.setStyleName(B.ROW);
        userContainer.setStyleName(B.LIST_GROUP);
        info.addStyleName("col-lg-10");

        final PScrollPanel scroll = new PScrollPanel();
        scroll.setHeight("100%");
        scroll.setWidth("100%");
        scroll.setWidget(messageContainer);

        headerPanel.setHeaderWidget(container);
        headerPanel.setContentWidget(scroll);
        headerPanel.setFooterWidget(chatContainer);

        addChat();
        addListener();
        return headerPanel;
    }

    private void addChat() {
        final PFlowPanel wrap = new PFlowPanel();
        chatContainer.add(wrap);

        messageBox = new PTextBox();
        messageBox.addStyleName(B.FORM_CONTROL);
        messageBox.setPlaceholder("Enter message ...");

        messageBox.addKeyUpHandler(new PKeyUpFilterHandler(PKeyCodes.ENTER) {

            @Override
            public void onKeyUp(final PKeyUpEvent keyUpEvent) {
                sendMessage();
            }
        });

        wrap.add(messageBox);
    }

    private void addListener() {
        final NodeWebkit nodeWebkit = NodeWebkit.get();
        if (nodeWebkit.isFunctional()) {
            nodeWebkit.getWin().addHandler("minimize", new Handler() {

                @Override
                public void onEvent(final String type, final JSONObject data) {
                    onMinimize();
                }
            });
            nodeWebkit.getWin().addHandler("restore", new Handler() {

                @Override
                public void onEvent(final String type, final JSONObject data) {
                    onRestore();
                }
            });
        }
    }

    protected void onMinimize() {
        service.updateUserPresence(me, Presence.AWAY);
    }

    protected void onRestore() {
        service.updateUserPresence(me, Presence.CONNECTED);
    }

    protected void sendMessage() {
        final String text = messageBox.getText();
        if (text.isEmpty()) return;

        service.sendMessage(me, text);
    }

    @Override
    protected void updateView(final Place place) {

        if (place instanceof LoggedPlace) {
            final LoggedPlace p = (LoggedPlace) place;
            me = p.name;

            userContainer.clear();
            final Collection<User> users = service.getConnectedUsers();
            for (final User user : users) {
                addEntry(user);
            }

            refreshInfo();
        }

    }

    private void addEntry(final User user) {
        if (me == null || me.name.equals(user.name)) return;

        final PElement u = new PElement("li");
        u.setStyleName(B.LIST_GROUP_ITEM);
        final PElement name = new PElement("span");
        name.setInnerText(user.name);
        final PElement presence = new PElement("span");
        presence.setInnerHTML("&nbsp;");
        presence.setStyleName(B.BADGE);
        presence.addStyleName(B.PULL_RIGHT);
        presence.addDomHandler(new PClickHandler() {

            @Override
            public void onClick(final PClickEvent event) {
                service.call(user);
            }
        }, PClickEvent.TYPE);

        u.add(name);
        u.add(presence);
        userContainer.add(u);
        entryByUser.put(user.name, u);

        setPresence(presence, service.getUserPresence(user));
        refreshInfo();
    }

    private void removeEntry(final User user) {
        if (me == null || me.name.equals(user.name)) return;

        final PWidget remove = entryByUser.remove(user.name);
        userContainer.remove(remove);

        refreshInfo();
    }

    private void updateEntry(final User user, final Presence presence) {
        if (me == null || me.name.equals(user.name)) return;

        // Get the badge
        final PComplexPanel w = (PComplexPanel) entryByUser.get(user.name);
        final PWidget badge = w.getWidget(1);
        setPresence(badge, presence);
    }

    private void setPresence(final PWidget presence, final Presence userPresence) {
        for (final Presence p : Presence.values()) {
            presence.removeStyleName(p.name());
        }
        presence.addStyleName(userPresence.name());
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
        } else if (data instanceof NewMessageEvent) {
            final NewMessageEvent e = (NewMessageEvent) data;
            addMessage(e.getUser(), e.getMessage());
        } else if (data instanceof UserPresenceEvent) {
            final UserPresenceEvent e = (UserPresenceEvent) data;
            updateEntry(e.getUser(), e.getPresence());
        } else if (data instanceof NewCallEvent) {
            onCall();
        }
    }

    private void onCall() {
        // focus window
        final NodeWebkit nodeWebkit = NodeWebkit.get();
        if (!nodeWebkit.isFunctional()) return;

        // nodeWebkit.getWin().show();
        nodeWebkit.getWin().restore();
    }

    private void addMessage(final User user, final String message) {
        String h = "<strong>" + user.name + ":</strong>";
        h += message;
        final PHTML f = new PHTML();
        f.setHTML(h);
        messageContainer.add(f);
    }

    private void refreshInfo() {
        if (entryByUser.isEmpty()) {
            info.setText("No one connected..");
            info.addStyleName(B.ALERT_WARN);
            info.removeStyleName(B.ALERT_SUCCESS);
        } else {
            info.setText(entryByUser.size() + " users connected");
            info.addStyleName(B.ALERT_SUCCESS);
            info.removeStyleName(B.ALERT_WARN);
        }
    }

    @Override
    public void onClick(final PClickEvent event) {}

}

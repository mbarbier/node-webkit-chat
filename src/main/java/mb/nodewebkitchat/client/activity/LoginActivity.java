
package mb.nodewebkitchat.client.activity;

import mb.nodewebkitchat.client.bootstrap.B;
import mb.nodewebkitchat.client.place.LoggedPlace;
import mb.nodewebkitchat.server.model.Presence;
import mb.nodewebkitchat.server.model.User;

import com.ponysdk.core.activity.Activity;
import com.ponysdk.core.socket.ConnectionListener;
import com.ponysdk.ui.server.basic.IsPWidget;
import com.ponysdk.ui.server.basic.PButton;
import com.ponysdk.ui.server.basic.PFlowPanel;
import com.ponysdk.ui.server.basic.PPusher;
import com.ponysdk.ui.server.basic.PTextBox;
import com.ponysdk.ui.server.basic.event.PClickEvent;
import com.ponysdk.ui.server.basic.event.PClickHandler;

public class LoginActivity extends NWCActivity implements PClickHandler, ConnectionListener {

    private Activity next;

    private PTextBox login;
    private PTextBox password;
    private PButton action;
    private String userName;

    public LoginActivity() {
        super();
        PPusher.get().addConnectionListener(this);
    }

    @Override
    protected IsPWidget buildView() {
        next = new UsersActivity();

        login = new PTextBox();
        login.addStyleName(B.FORM_CONTROL);
        login.setPlaceholder("Login");

        password = new PTextBox();
        password.addStyleName(B.FORM_CONTROL);
        password.setPlaceholder("Password");

        action = new PButton("Login");
        action.addClickHandler(this);
        action.setStyleName(B.BTN);
        action.addStyleName(B.BTN_P);
        action.addStyleName(B.PULL_RIGHT);

        final PFlowPanel container = new PFlowPanel();
        container.add(login);
        container.add(password);
        container.add(action);
        container.addStyleName("login");

        // final PSimplePanel p = new PSimplePanel();
        // p.addStyleName("row");
        // p.setWidget(container);

        return container;
    }

    @Override
    public void onClick(final PClickEvent event) {
        userName = login.getText();
        if (userName.isEmpty()) { return; }

        final User user = new User(userName);
        service.logon(userName);
        service.updateUserPresence(user, Presence.CONNECTED);

        next.start(world, new LoggedPlace(user));
    }

    @Override
    public void onOpen() {}

    @Override
    public void onClose() {
        if (userName != null) service.logout(userName);
    }

}

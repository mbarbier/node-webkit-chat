
package mb.nodewebkitchat.client.activity;

import mb.nodewebkitchat.client.place.LoggedPlace;

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
        login.setPlaceholder("Login");

        action = new PButton("Login");
        action.addClickHandler(this);

        final PFlowPanel container = new PFlowPanel();
        container.add(login);
        container.add(action);

        return container;
    }

    @Override
    public void onClick(final PClickEvent event) {
        userName = login.getText();
        if (userName.isEmpty()) { return; }

        service.logon(userName);

        next.start(world, new LoggedPlace(userName));
    }

    @Override
    public void onOpen() {}

    @Override
    public void onClose() {
        if (userName != null) service.logout(userName);
    }

}

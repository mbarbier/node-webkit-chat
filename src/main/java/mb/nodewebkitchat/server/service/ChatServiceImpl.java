
package mb.nodewebkitchat.server.service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

import mb.nodewebkitchat.server.model.User;
import mb.nodewebkitchat.server.model.event.NewMessageEvent;
import mb.nodewebkitchat.server.model.event.UserConnectedEvent;
import mb.nodewebkitchat.server.model.event.UserDisconnectedEvent;

import com.ponysdk.core.Application;
import com.ponysdk.core.UIContext;
import com.ponysdk.core.servlet.SessionManager;
import com.ponysdk.ui.server.basic.PPusher;
import com.ponysdk.ui.server.basic.PPusher.PusherState;

public class ChatServiceImpl implements ChatService {

    private final Executor executor = Executors.newSingleThreadExecutor();
    private final Map<String, User> users = new ConcurrentHashMap<String, User>();

    @Override
    public void logon(final String user) {
        final User u = new User(user);
        users.put(user, u);

        brodcastEvent(new UserConnectedEvent(u));
    }

    @Override
    public void logout(final String user) {
        final User u = users.remove(user);
        brodcastEvent(new UserDisconnectedEvent(u));
    }

    @Override
    public Collection<User> getConnectedUsers() {
        return users.values();
    }

    private void brodcastEvent(final Object event) {

        executor.execute(new Runnable() {

            @Override
            public void run() {
                final Collection<Application> applications = SessionManager.get().getApplications();
                for (final Application application : applications) {
                    final Collection<UIContext> contexts = application.getUIContexts();
                    for (final UIContext uiContext : contexts) {
                        final PPusher pusher = uiContext.getPusher();
                        if (pusher == null) continue;
                        if (pusher.getPusherState() != PusherState.STARTED) continue;

                        pusher.pushToClient(event);
                    }
                }
            }
        });
    }

    @Override
    public void sendMessage(final User user, final String message) {
        brodcastEvent(new NewMessageEvent(user, message));
    }
}

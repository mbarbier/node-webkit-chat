
package mb.nodewebkitchat.server.service;

import java.util.Collection;

import mb.nodewebkitchat.server.model.User;

import com.ponysdk.core.service.PonyService;

public interface ChatService extends PonyService {

    public void logon(String user);

    public void logout(String user);

    public Collection<User> getConnectedUsers();

    public void sendMessage(User user, String message);
}

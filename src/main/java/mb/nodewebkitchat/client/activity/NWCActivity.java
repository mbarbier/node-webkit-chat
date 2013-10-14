
package mb.nodewebkitchat.client.activity;

import mb.nodewebkitchat.server.service.ChatService;

import com.ponysdk.core.activity.AbstractActivity;
import com.ponysdk.core.place.Place;
import com.ponysdk.core.service.PonyServiceRegistry;
import com.ponysdk.ui.server.basic.DataListener;
import com.ponysdk.ui.server.basic.PPusher;

public abstract class NWCActivity extends AbstractActivity implements DataListener {

    protected ChatService service;

    public NWCActivity() {
        service = PonyServiceRegistry.getPonyService(ChatService.class);
        PPusher.get().addDataListener(this);
    }

    @Override
    protected void updateView(final Place place) {}

    @Override
    public void onData(final Object data) {}
}

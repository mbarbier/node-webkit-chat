
package mb.nodewebkitchat.client;

import mb.nodewebkitchat.client.activity.LoginActivity;
import mb.nodewebkitchat.client.nodewebkit.NodeWebkit;

import com.ponysdk.core.UIContext;
import com.ponysdk.core.activity.Activity;
import com.ponysdk.core.main.EntryPoint;
import com.ponysdk.core.place.Place;
import com.ponysdk.ui.server.basic.PPusher;
import com.ponysdk.ui.server.basic.PRootLayoutPanel;
import com.ponysdk.ui.server.basic.PSimpleLayoutPanel;

public class NWCEntryPoint implements EntryPoint {

    @Override
    public void restart(final UIContext ui) {
        start();
    }

    @Override
    public void start(final UIContext io) {
        start();
    }

    private void start() {
        PPusher.initialize();

        final PSimpleLayoutPanel root = new PSimpleLayoutPanel();
        final PRootLayoutPanel r = PRootLayoutPanel.get();
        r.add(root);

        final Activity activity = new LoginActivity();
        activity.start(root, Place.NOWHERE);

        NodeWebkit.initialize();
    }
}

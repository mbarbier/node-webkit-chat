
package mb.nodewebkitchat.client.nodewebkit;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ponysdk.core.UIContext;
import com.ponysdk.core.tools.ListenerCollection;
import com.ponysdk.ui.server.basic.PHTML;
import com.ponysdk.ui.server.basic.event.PNativeEvent;
import com.ponysdk.ui.server.basic.event.PNativeHandler;

public class NodeWebkit extends PHTML implements PNativeHandler {

    private static Logger log = LoggerFactory.getLogger(NodeWebkit.class);

    private static String NW = "NW";

    private String version;
    private Window win;

    private NodeWebkit() {}

    public static NodeWebkit initialize() {
        if (UIContext.get() == null) throw new RuntimeException("NO UIContext");
        if (UIContext.get().getAttribute(NW) != null) return get();
        final NodeWebkit nw = new NodeWebkit();
        nw.init();
        UIContext.get().setAttribute(NW, nw);
        return nw;
    }

    public static NodeWebkit get() {
        if (UIContext.get() == null) throw new RuntimeException("NO UIContext");
        final NodeWebkit nw = UIContext.get().getAttribute(NW);
        if (nw == null) { throw new RuntimeException("NodeWebkit must be initialize before using it"); }
        return nw;
    }

    private void init() {
        addNativeHandler(this);
        bindTerminalFunction("bindNW");
        win = new Window();
    }

    @Override
    public void onNativeEvent(final PNativeEvent event) {
        try {
            final JSONObject jso = event.getJsonObject();
            System.err.println("native: " + jso);
            if (jso.has("info")) {
                final JSONObject info = jso.getJSONObject("info");
                version = info.getString("nwversion");
            } else if (jso.has("win")) {
                final JSONObject w = jso.getJSONObject("win");
                final String eventType = w.getString("on");
                if (eventType != null) {
                    win.onEvent(eventType, null);
                }
            }
        } catch (final JSONException e) {
            log.error("", e);
        }
    }

    public interface Handler {

        public void onEvent(String type, JSONObject data);
    }

    public class Window {

        private final Map<String, ListenerCollection<Handler>> handlersByType = new HashMap<String, ListenerCollection<Handler>>();

        public void show() {
            send("show", true);
        }

        public void hide() {
            send("hide", true);
        }

        public void focus() {
            send("focus", true);
        }

        public void restore() {
            send("restore", true);
        }

        public void addHandler(final String type, final Handler handler) {
            send("addHandler", type);
            ensureHandlerList(type).add(handler);
        }

        protected void onEvent(final String type, final JSONObject data) {
            final ListenerCollection<Handler> collection = handlersByType.get(type);
            if (collection == null) return;

            for (final Handler handler : collection) {
                handler.onEvent(type, data);
            }
        }

        private ListenerCollection<Handler> ensureHandlerList(final String type) {
            ListenerCollection<Handler> collection = handlersByType.get(type);
            if (collection != null) return collection;

            collection = new ListenerCollection<NodeWebkit.Handler>();
            handlersByType.put(type, collection);
            return collection;
        }

        private void send(final String property, final Object value) {
            try {
                final JSONObject j = new JSONObject();
                j.put(property, value);
                send(j);
            } catch (final JSONException e) {
                log.error("", e);
            }
        }

        private void send(final JSONObject jso) {
            try {
                final JSONObject j = new JSONObject();
                j.put("win", jso);
                sendToNative(j);
            } catch (final JSONException e) {
                log.error("", e);
            }
        }

    }

    public boolean isFunctional() {
        return version != null;
    }

    public String getVersion() {
        return version;
    }

    public Window getWin() {
        return win;
    }

}

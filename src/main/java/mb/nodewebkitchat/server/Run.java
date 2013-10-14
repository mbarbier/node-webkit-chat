
package mb.nodewebkitchat.server;

import mb.nodewebkitchat.server.service.ChatServiceImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ponysdk.core.main.Main;
import com.ponysdk.core.service.PonyServiceRegistry;
import com.ponysdk.core.servlet.ApplicationLoader;
import com.ponysdk.core.servlet.HttpServlet;

public class Run {

    private static Logger log = LoggerFactory.getLogger(Run.class);

    public static void main(final String[] args) {
        try {
            // Register service
            PonyServiceRegistry.registerPonyService(new ChatServiceImpl());

            // Start webserver
            final ApplicationLoader applicationLoader = new ApplicationLoader();
            final HttpServlet httpServlet = new HttpServlet();
            httpServlet.setEntryPointClassName("com.ponysdk.sample.trading.client.TradingSampleEntryPoint");
            // final BootstrapServlet bootstrapServlet = new BootstrapServlet();
            // bootstrapServlet.addStylesheet("css/sample.less");
            // bootstrapServlet.addStylesheet("css/ponysdk.less");
            // bootstrapServlet.addJavascript("script/less.js");

            final Main main = new Main();
            main.setApplicationContextName("chat");
            main.setPort(8088);
            main.setHttpServlet(httpServlet);
            main.setHttpSessionListener(applicationLoader);
            main.setServletContextListener(applicationLoader);
            // main.setBootstrapServlet(bootstrapServlet);
            main.start();

        } catch (final Throwable e) {
            log.error("", e);
        }
    }

}

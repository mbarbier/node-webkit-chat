
package mb.nodewebkitchat.client.bootstrap;

import com.ponysdk.ui.server.basic.PWidget;

public class B {

    public static final String FORM_CONTROL = "form-control";
    public static final String BTN = "btn";
    public static final String BTN_P = "btn-primary";
    public static final String PULL_RIGHT = "pull-right";
    public static final String LABEL = "label";
    public static final String LABEL_P = "label-primary";
    public static final String ALERT = "alert";
    public static final String ALERT_DISMISS = "alert-dismissable";
    public static final String ALERT_WARN = "alert-warning";
    public static final String ALERT_SUCCESS = "alert-success";
    public static final String ROW = "row";
    public static final String LIST_GROUP = "list-group";
    public static final String LIST_GROUP_ITEM = "list-group-item";

    public static void setCollapse(final PWidget w, final String collapsableID) {
        w.setAttribute("data-toggle", "collapse");
        w.setAttribute("data-target", "#" + collapsableID);
    }

    public static void setCollapsable(final PWidget w, final String collapsableID, final boolean collapsed) {
        w.addStyleName("collapse");
        if (collapsed) w.addStyleName("in");
        w.setAttribute("id", collapsableID);
    }
}

package com.netcracker.errorHandling.rpc;

import com.netcracker.errorHandling.ApiError;
import com.vaadin.client.communication.RpcProxy;
import com.vaadin.server.Page;
import com.vaadin.shared.ui.Connect;
import com.vaadin.client.ui.AbstractComponentConnector;
import com.vaadin.ui.Notification;

/**
 * Created by V.Drabynka on 22.11.2017.
 */

@Connect(ApiErrorComponent.class)
public class ApiErrorConnector extends AbstractComponentConnector {
    ApiErrorServerRpc rpc = RpcProxy
            .create(ApiErrorServerRpc.class, this);

    public ApiErrorConnector() {
        registerRpc(ApiErrorClientRpc.class, new ApiErrorClientRpc() {
            public void putNotification(ApiError error) {
                Notification notification = new Notification("Error!", error.toString(), Notification.Type.ERROR_MESSAGE);
                notification.show(Page.getCurrent());
            }
        });

        /*UI.getCurrent().setErrorHandler(new DefaultErrorHandler() {
            @Override
            public void error(com.vaadin.server.ErrorEvent event) {
                rpc.sendError();
            }
        });*/
    }


}

package com.netcracker.errorHandling.rpc;

import com.netcracker.errorHandling.ApiError;
import com.vaadin.ui.AbstractComponent;

/**
 * Created by V.Drabynka on 22.11.2017.
 */
public class ApiErrorComponent extends AbstractComponent {

    private ApiError error;

    private ApiErrorServerRpc serverRpc = new ApiErrorServerRpc() {
        public void sendError(ApiError error) {
            if (error != null)
                getRpcProxy(ApiErrorClientRpc.class).putNotification(error);
        }
    };

    public ApiErrorComponent(ApiError error) {
        registerRpc(serverRpc);
        this.error = error;
    }

    public void sendError() {
        serverRpc.sendError(error);
    }
}
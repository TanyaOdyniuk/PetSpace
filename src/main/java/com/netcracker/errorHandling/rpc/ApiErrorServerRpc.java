package com.netcracker.errorHandling.rpc;

import com.netcracker.errorHandling.ApiError;
import com.vaadin.shared.communication.ServerRpc;

/**
 * Created by V.Drabynka on 22.11.2017.
 */
public interface ApiErrorServerRpc extends ServerRpc {
    void sendError(ApiError error);
}

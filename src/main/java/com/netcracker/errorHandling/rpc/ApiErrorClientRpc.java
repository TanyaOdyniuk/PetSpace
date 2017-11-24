package com.netcracker.errorHandling.rpc;

import com.netcracker.errorHandling.ApiError;
import com.vaadin.shared.communication.ClientRpc;

/**
 * Created by V.Drabynka on 21.11.2017.
 */
public interface ApiErrorClientRpc extends ClientRpc {
    void putNotification(ApiError error);
}

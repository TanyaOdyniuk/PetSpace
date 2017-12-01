package com.netcracker.ui;

import com.netcracker.error.ErrorMessage;
import com.netcracker.error.handler.ClientExceptionHandler;
import com.vaadin.ui.Button;
import org.springframework.web.client.HttpClientErrorException;

public abstract class AbstractClickListener implements Button.ClickListener{

    private String errorMessage = ErrorMessage.ERROR_UNKNOWN;

    @Override
    public void buttonClick(Button.ClickEvent clickEvent) {
        try{
            buttonClickListener();
        } catch(HttpClientErrorException ex){
            ClientExceptionHandler.handle(ex, errorMessage);
        }
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public abstract void buttonClickListener();
}

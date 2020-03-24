package com.github.blakelai.components;

import com.github.blakelai.security.SecurityUtils;
import com.vaadin.flow.component.AttachEvent;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.Tag;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.JsModule;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.server.VaadinServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.web.csrf.CsrfToken;

import javax.servlet.http.HttpServletRequest;

@Tag("google-sign-in")
@JsModule("./google-sign-in.js")
public class GoogleSignInComponent extends Component implements HasComponents {
    private static final Logger logger = LoggerFactory.getLogger(GoogleSignInComponent.class);

    protected void onAttach(AttachEvent attachEvent) {
        getUI().ifPresent(ui -> {
            if (SecurityUtils.isUserLoggedIn()) {
                HttpServletRequest request = VaadinServletRequest.getCurrent().getHttpServletRequest();
                CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());
                getElement().setProperty("logoutUrl", "/logout");
                getElement().setProperty("csrfParameterName", csrf.getParameterName());
                getElement().setProperty("csrfToken", csrf.getToken());
                getElement().setProperty("loggedIn", true);
            } else {
                Button loginButton = new Button("Login with Google");
                Anchor loginAnchor = new Anchor("/oauth2/authorization/google", loginButton);
                add(loginAnchor);
            }
        });

    }
}

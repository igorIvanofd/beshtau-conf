package com.terra.beshtau.conf.services;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.server.VaadinServletRequest;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class LogoutService {

    public Optional<UserDetails> getAuthUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        var principal = context.getAuthentication().getPrincipal();
        return Optional.ofNullable(principal)
                .map(pr-> {
                    if (pr instanceof UserDetails) {
                        return (UserDetails) pr;
                    }
                    return null;
                });
    }


    public void logout() {
        UI.getCurrent().getPage().setLocation("/");
        SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();
        logoutHandler.logout(
                VaadinServletRequest.getCurrent().getHttpServletRequest(), null, null
        );
    }
}

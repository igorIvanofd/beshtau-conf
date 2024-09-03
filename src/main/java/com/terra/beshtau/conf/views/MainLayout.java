package com.terra.beshtau.conf.views;

import com.terra.beshtau.conf.security.Roles;
import com.terra.beshtau.conf.views.upload.UploadView;
import com.terra.beshtau.conf.views.search.SearchView;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.spring.security.AuthenticationContext;
import com.vaadin.flow.theme.lumo.LumoUtility;
import org.vaadin.lineawesome.LineAwesomeIcon;

/**
 * The main view is a top-level placeholder for other views.
 */
public class MainLayout extends AppLayout {

    private H1 viewTitle;

    public MainLayout(AuthenticationContext authenticationContext) {
        setPrimarySection(Section.DRAWER);
        addDrawerContent(authenticationContext);
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.setAriaLabel("Menu toggle");

        viewTitle = new H1();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent(AuthenticationContext authenticationContext) {
        Span appName = new Span("Terra Configuration");
        appName.addClassNames(LumoUtility.FontWeight.SEMIBOLD, LumoUtility.FontSize.LARGE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation(authenticationContext));

        addToDrawer(header, scroller, createFooter());
    }

    private SideNav createNavigation(AuthenticationContext authenticationContext) {
        SideNav nav = new SideNav();

        if (authenticationContext.isAuthenticated()) {
            nav.addItem(new SideNavItem("Search", SearchView.class, LineAwesomeIcon.FILTER_SOLID.create()));
            if (authenticationContext.hasRole(Roles.ADMIN)) {
                nav.addItem(new SideNavItem("Upload", UploadView.class, LineAwesomeIcon.USER.create()));
            }
        }

        return nav;
    }

    private Footer createFooter() {
        Footer layout = new Footer();
        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "index" : title.value();
    }
}

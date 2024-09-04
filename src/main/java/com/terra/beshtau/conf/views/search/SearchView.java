package com.terra.beshtau.conf.views.search;

import com.terra.beshtau.conf.data.BeshtauEntity;
import com.terra.beshtau.conf.security.Roles;
import com.terra.beshtau.conf.services.BeshtauService;
import com.terra.beshtau.conf.views.MainLayout;
import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.MultiSelectComboBox;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.listbox.MultiSelectListBox;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.renderer.LitRenderer;
import com.vaadin.flow.data.selection.MultiSelect;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.spring.data.VaadinSpringDataHelpers;
import com.vaadin.flow.theme.lumo.LumoUtility;
import jakarta.annotation.security.RolesAllowed;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;

@PageTitle("Search")
@Route(value = "", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
@Uses(Icon.class)
@RolesAllowed({Roles.ADMIN, Roles.USER})
public class SearchView extends Div {

    private static final String LIT_TEMPLATE_HTML = """
            <vaadin-button title="Go to ..."
                           @click="${clickHandler}"
                           theme="tertiary-inline small link">
                ${item.name}
            </vaadin-button>""";

    private Grid<BeshtauEntity> grid;

    private Filters filters;
    protected final BeshtauService beshtauService;

    public SearchView(BeshtauService beshtauService) {
        this.beshtauService = beshtauService;
        setSizeFull();
        addClassNames("search-view");
        var entities = beshtauService.findAll();
        var os = entities.stream().map(BeshtauEntity::getOs).collect(Collectors.toSet());
        var category = entities.stream().map(BeshtauEntity::getCategory).collect(Collectors.toSet());
        filters = new Filters(os, category, () -> refreshGrid());
        VerticalLayout layout = new VerticalLayout(createMobileFilters(), filters, createGrid());
        layout.setSizeFull();
        layout.setPadding(false);
        layout.setSpacing(false);
        add(layout);
    }

    private HorizontalLayout createMobileFilters() {
        // Mobile version
        HorizontalLayout mobileFilters = new HorizontalLayout();
        mobileFilters.setWidthFull();
        mobileFilters.addClassNames(LumoUtility.Padding.MEDIUM, LumoUtility.BoxSizing.BORDER,
                LumoUtility.AlignItems.CENTER);
        mobileFilters.addClassName("mobile-filters");

        Icon mobileIcon = new Icon("lumo", "plus");
        Span filtersHeading = new Span("Filters");
        mobileFilters.add(mobileIcon, filtersHeading);
        mobileFilters.setFlexGrow(1, filtersHeading);
        mobileFilters.addClickListener(e -> {
            if (filters.getClassNames().contains("visible")) {
                filters.removeClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:plus");
            } else {
                filters.addClassName("visible");
                mobileIcon.getElement().setAttribute("icon", "lumo:minus");
            }
        });
        return mobileFilters;
    }

    public static class Filters extends Div implements Specification<BeshtauEntity> {

        private final TextField name = new TextField("Название");
        private final MultiSelectComboBox<String> osList = new MultiSelectComboBox<>();
        private final MultiSelectComboBox<String> categoryList = new MultiSelectComboBox<>();

        public Filters(Set<String> os, Set<String> categories, Runnable onSearch) {

            setWidthFull();
            addClassName("filter-layout");
            addClassNames(LumoUtility.Padding.Horizontal.LARGE, LumoUtility.Padding.Vertical.MEDIUM,
                    LumoUtility.BoxSizing.BORDER);
            name.setPlaceholder("Пару букв туда-сюда скажи");

            osList.setLabel("Choose OS");
            osList.setItems(os);
            categoryList.setLabel("Choose categories");
            categoryList.setItems(categories);

            // Action buttons
            Button resetBtn = new Button("Reset");
            resetBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);
            resetBtn.addClickListener(e -> {
                name.clear();
                osList.clear();
                categoryList.clear();
                onSearch.run();
            });
            Button searchBtn = new Button("Search");
            searchBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            searchBtn.addClickListener(e -> onSearch.run());

            Div actions = new Div(resetBtn, searchBtn);
            actions.addClassName(LumoUtility.Gap.SMALL);
            actions.addClassName("actions");

            add(name, osList, categoryList, actions);
        }

        @Override
        public Predicate toPredicate(Root<BeshtauEntity> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
            List<Predicate> predicates = new ArrayList<>();

            if (!name.isEmpty()) {
                String lowerCaseFilter = name.getValue().toLowerCase();
                Predicate firstNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("name")),
                        lowerCaseFilter + "%");
                Predicate lastNameMatch = criteriaBuilder.like(criteriaBuilder.lower(root.get("fullName")),
                        lowerCaseFilter + "%");
                predicates.add(criteriaBuilder.or(firstNameMatch, lastNameMatch));
            }

            if (!osList.getSelectedItems().isEmpty()) {
                String databaseColumn = "os";
                predicates.add(root.get(databaseColumn).in(osList.getSelectedItems()));
            }
            if (!categoryList.getSelectedItems().isEmpty()) {
                String databaseColumn = "category";
                predicates.add(root.get(databaseColumn).in(categoryList.getSelectedItems()));

            }
            return criteriaBuilder.and(predicates.toArray(Predicate[]::new));
        }
    }

    private Component createGrid() {
        grid = new Grid<>(BeshtauEntity.class, false);
        grid.addColumn(LitRenderer.<BeshtauEntity>of(LIT_TEMPLATE_HTML).withProperty("name", BeshtauEntity::getName)
                .withFunction("clickHandler", beshtauEntity -> {
                    Notification.show("Че интересно? Гони " + beshtauEntity.getAmount() + " за это дерьмо " + beshtauEntity.getName());
                })).setAutoWidth(true);
        grid.addColumn("fullName").setAutoWidth(true);
        grid.addColumn("os").setAutoWidth(true);
        grid.addColumn("category").setAutoWidth(true);
        grid.addColumn("amount").setAutoWidth(true);

        grid.setItems(query -> beshtauService.list(
                PageRequest.of(query.getPage(), query.getPageSize(), VaadinSpringDataHelpers.toSpringDataSort(query)),
                filters).stream());
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER);
        grid.addClassNames(LumoUtility.Border.TOP, LumoUtility.BorderColor.CONTRAST_10);

        return grid;
    }

    private void refreshGrid() {
        grid.getDataProvider().refreshAll();
    }

}

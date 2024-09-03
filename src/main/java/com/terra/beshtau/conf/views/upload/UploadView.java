package com.terra.beshtau.conf.views.upload;

import com.terra.beshtau.conf.security.Roles;
import com.terra.beshtau.conf.services.TableFileReader;
import com.terra.beshtau.conf.views.MainLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.component.upload.UploadI18N;
import com.vaadin.flow.component.upload.receivers.MultiFileMemoryBuffer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.InputStream;

@PageTitle("Upload")
@Route(value = "upload", layout = MainLayout.class)
@RolesAllowed(Roles.ADMIN)
public class UploadView extends Div {

    private final TableFileReader fileProcessor;

    public UploadView(TableFileReader fileProcessor) {
        this.fileProcessor = fileProcessor;
        MultiFileMemoryBuffer buffer = new MultiFileMemoryBuffer();
        Upload upload = new Upload(buffer);

        upload.setAcceptedFileTypes(
                // Microsoft Excel (.xls)
                //"application/vnd.ms-excel", ".xls",
                // Microsoft Excel (OpenXML, .xlsx)
                "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet",
                ".xlsx");
                //,
                // Comma-separated values (.csv)
               // "text/csv", ".csv");

        upload.addSucceededListener(event -> {
            String fileName = event.getFileName();
            InputStream inputStream = buffer.getInputStream(fileName);
            fileProcessor.invoke(inputStream);
        });

        add(upload);
    }
}

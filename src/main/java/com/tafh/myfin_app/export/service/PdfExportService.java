package com.tafh.myfin_app.export.service;

import com.tafh.myfin_app.transaction.model.TransactionEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.lowagie.text.Document;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PdfExportService {

    public ByteArrayInputStream buildPdf(List<TransactionEntity> data) throws Exception {

        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        PdfWriter.getInstance(document, out);
        document.open();

        document.add(new Paragraph("TRANSACTION REPORT"));
        document.add(new Paragraph(" "));

        for (TransactionEntity t : data) {
            document.add(new Paragraph(
                    t.getId() + " | " +
                            t.getCategory().getName() + " | " +
                            t.getAmount() + " | " +
                            t.getType()
            ));
        }

        document.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}

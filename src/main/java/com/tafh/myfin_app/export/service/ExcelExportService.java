package com.tafh.myfin_app.export.service;

import com.tafh.myfin_app.transaction.model.TransactionEntity;
import lombok.RequiredArgsConstructor;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ExcelExportService {

    public ByteArrayInputStream buildExcel(List<TransactionEntity> data) throws IOException {

        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Transactions");

        int rowIdx = 0;

        for (TransactionEntity t : data) {
            Row row = sheet.createRow(rowIdx++);

            row.createCell(0).setCellValue(t.getId());
            row.createCell(1).setCellValue(t.getCategory().getName());
            row.createCell(2).setCellValue(t.getAmount().doubleValue());
            row.createCell(3).setCellValue(t.getType().toString());
        }

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        workbook.write(out);
        workbook.close();

        return new ByteArrayInputStream(out.toByteArray());
    }
}

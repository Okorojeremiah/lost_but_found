package com.winners.lostbutfound.controllers;

import com.itextpdf.text.DocumentException;
import com.winners.lostbutfound.dtos.GenerateReportRequest;
import com.winners.lostbutfound.services.implementation.ReportService;
import freemarker.template.TemplateException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("api/v1/report")
public class PDFReportController {

    private final ReportService reportService;

    public PDFReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping("/generate-report")
    public ResponseEntity<byte[]> generateReport(@RequestBody GenerateReportRequest generateReportRequest)
            throws TemplateException, DocumentException, IOException {

        byte[] pdfBytes = reportService.generatePdfReport(generateReportRequest);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "Lost_and_Found_Report.pdf");

        return ResponseEntity.ok().headers(headers).body(pdfBytes);
    }
}

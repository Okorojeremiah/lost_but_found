package com.winners.lostbutfound.services.implementation;

import com.itextpdf.text.DocumentException;
import com.winners.lostbutfound.dtos.GenerateReportRequest;
import com.winners.lostbutfound.models.Item;
import com.winners.lostbutfound.services.ItemService;
import org.jsoup.helper.W3CDom;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.LocalDate;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.xhtmlrenderer.pdf.ITextRenderer;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

@Service
public class ReportService {

    private final ItemService itemService;
    private final Configuration freemarkerConfig;

    public ReportService(ItemService itemService) {
        this.itemService = itemService;
        this.freemarkerConfig = new Configuration(Configuration.VERSION_2_3_32);
        this.freemarkerConfig.setClassForTemplateLoading(ReportService.class, "/");
    }

    public byte[] generatePdfReport(GenerateReportRequest generateReportRequest) throws TemplateException, DocumentException, IOException {
        List<Item> filteredItems = itemService.findByDateRange(generateReportRequest.startDate(), generateReportRequest.endDate());
        return generatePdf(filteredItems, generateReportRequest.startDate(), generateReportRequest.endDate());
    }

    private byte[] generatePdf(List<Item> items, LocalDate startDate, LocalDate endDate) throws IOException, TemplateException, DocumentException {
        Map<String, Object> dataModel = prepareDataModel(items, startDate, endDate);

        File tempHtmlFile = createTempHtmlFile(dataModel);
        Document document = createWellFormedHtml(tempHtmlFile);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        xhtmlToPdf(document, byteArrayOutputStream);

        return byteArrayOutputStream.toByteArray();
    }

    private Map<String, Object> prepareDataModel(List<Item> items, LocalDate startDate, LocalDate endDate) throws IOException {
        Map<String, Object> dataModel = new HashMap<>();
        dataModel.put("items", items);
        dataModel.put("base64ImageData", encodeImageToBase64(
        ));
        dataModel.put("currentDate", LocalDate.now());
        dataModel.put("startDate", startDate);
        dataModel.put("endDate", endDate);
        dataModel.put("totalItems", itemService.countByDateBetween(startDate, endDate));
        dataModel.put("itemsClaimed", itemService.countByDateBetweenForClaimedItems(startDate, endDate));
        dataModel.put("itemsUnclaimed", itemService.countByDateBetweenForUnclaimedItems(startDate, endDate));
        dataModel.put("claimsByDate", itemService.countClaimsByDate(startDate, endDate));
        dataModel.put("claimsByMonth", itemService.countClaimsByMonth(startDate, endDate));
        dataModel.put("unclaimedItemsSummary", itemService.unclaimedItemSummary(
                itemService.countByDateBetweenForUnclaimedItems(startDate, endDate)));
        return dataModel;
    }

    private File createTempHtmlFile(Map<String, Object> dataModel) throws IOException, TemplateException {
        Template template = freemarkerConfig.getTemplate("report.ftl");
        File tempHtmlFile = File.createTempFile("report", ".html");
        try (Writer out = new FileWriter(tempHtmlFile)) {
            template.process(dataModel, out);
        }
        return tempHtmlFile;
    }

    private static Document createWellFormedHtml(File inputHTML) throws IOException {
        Document document = Jsoup.parse(inputHTML, "UTF-8");
        document.outputSettings().syntax(Document.OutputSettings.Syntax.xml);
        return document;
    }

    private static void xhtmlToPdf(Document doc, ByteArrayOutputStream byteArrayOutputStream) throws IOException, DocumentException {
        ITextRenderer renderer = new ITextRenderer();
        renderer.setDocument(new W3CDom().fromJsoup(doc), null);
        renderer.layout();
        renderer.createPDF(byteArrayOutputStream);
    }

    private static String encodeImageToBase64() throws IOException {
        try (FileInputStream imageInFile = new FileInputStream(
                "C:\\Users\\Jerry\\Desktop\\lostbutfound\\src\\main\\resources\\LFC_logo.png")) {
            byte[] imageData = imageInFile.readAllBytes();
            return Base64.getEncoder().encodeToString(imageData);
        }
    }
}

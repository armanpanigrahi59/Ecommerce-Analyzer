package com.ecommerce.analyzer.service;

import com.ecommerce.analyzer.model.ProductDetail;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileOutputStream;
import java.util.List;

/**
 * Export Service
 * Handles PDF and Excel export functionality
 */
public class ExportService {

    /**
     * Sanitize text for PDF export (remove unsupported characters)
     */
    private String sanitizeForPDF(String text) {
        if (text == null || text.isEmpty()) {
            return "N/A";
        }
        // Remove emojis and special Unicode characters
        return text.replaceAll("[^\\x00-\\x7F]", "")
                .replaceAll("[\\p{Cntrl}&&[^\n\t]]", "")
                .trim();
    }

    /**
     * Export product comparison to PDF
     */
    public boolean exportToPDF(List<ProductDetail> productDetails, String productName, File outputFile) {
        if (productDetails == null || productDetails.isEmpty()) {
            System.err.println("No product details to export");
            return false;
        }

        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage();
            document.addPage(page);
            PDPageContentStream contentStream = new PDPageContentStream(document, page);

            PDType1Font boldFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA_BOLD);
            PDType1Font regularFont = new PDType1Font(Standard14Fonts.FontName.HELVETICA);

            // Title
            contentStream.setFont(boldFont, 18);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 750);
            contentStream.showText(sanitizeForPDF("PRICE VERSE - Product Comparison Report"));
            contentStream.endText();

            // Product name
            contentStream.setFont(regularFont, 14);
            contentStream.beginText();
            contentStream.newLineAtOffset(50, 720);
            contentStream.showText(sanitizeForPDF("Product: " + productName));
            contentStream.endText();

            // Draw line
            contentStream.moveTo(50, 710);
            contentStream.lineTo(550, 710);
            contentStream.stroke();

            // Product details
            float yPosition = 680;

            for (ProductDetail detail : productDetails) {
                // Check if we need a new page
                if (yPosition < 100) {
                    contentStream.close();
                    page = new PDPage();
                    document.addPage(page);
                    contentStream = new PDPageContentStream(document, page);
                    yPosition = 750;
                }

                // Platform header
                contentStream.setFont(boldFont, 12);
                contentStream.beginText();
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText(sanitizeForPDF("Platform: " + detail.getPlatform()));
                contentStream.endText();
                yPosition -= 20;

                // Details
                String[] lines = {
                        "Price: Rs. " + formatPrice(detail.getPrice()),
                        "Rating: " + formatRating(detail.getRating()) + "/5",
                        "Seller: " + sanitizeForPDF(detail.getSeller()),
                        "Delivery: " + sanitizeForPDF(detail.getDeliveryTime()),
                        "Return Policy: " + sanitizeForPDF(detail.getReturnPolicy()),
                        "Warranty: " + sanitizeForPDF(detail.getWarranty()),
                        "Offers: " + sanitizeForPDF(detail.getOffers())
                };

                contentStream.setFont(regularFont, 10);
                for (String line : lines) {
                    if (yPosition < 50) {
                        contentStream.close();
                        page = new PDPage();
                        document.addPage(page);
                        contentStream = new PDPageContentStream(document, page);
                        contentStream.setFont(regularFont, 10);  // RESET FONT!
                        yPosition = 750;
                    }

                    contentStream.beginText();
                    contentStream.newLineAtOffset(70, yPosition);
                    contentStream.showText(sanitizeForPDF(line));
                    contentStream.endText();
                    yPosition -= 15;
                }

                yPosition -= 10;
            }

            contentStream.close();
            document.save(outputFile);
            System.out.println("✓ PDF exported successfully: " + outputFile.getAbsolutePath());
            return true;

        } catch (Exception e) {
            System.err.println("✗ PDF export failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Export product comparison to Excel
     */
    public boolean exportToExcel(List<ProductDetail> productDetails, String productName, File outputFile) {
        if (productDetails == null || productDetails.isEmpty()) {
            System.err.println("No product details to export");
            return false;
        }

        try (Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("Product Comparison");

            CellStyle headerStyle = workbook.createCellStyle();
            Font headerFont = workbook.createFont();
            headerFont.setBold(true);
            headerFont.setFontHeightInPoints((short) 12);
            headerStyle.setFont(headerFont);
            headerStyle.setFillForegroundColor(IndexedColors.LIGHT_BLUE.getIndex());
            headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

            Row titleRow = sheet.createRow(0);
            Cell titleCell = titleRow.createCell(0);
            titleCell.setCellValue("PRICE VERSE - Product Comparison: " + productName);
            titleCell.setCellStyle(headerStyle);
            sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(0, 0, 0, 8));

            Row headerRow = sheet.createRow(2);
            String[] headers = {"Platform", "Price (Rs.)", "Rating", "Seller", "Delivery",
                    "Return Policy", "Warranty", "Offers", "Link"};

            for (int i = 0; i < headers.length; i++) {
                Cell cell = headerRow.createCell(i);
                cell.setCellValue(headers[i]);
                cell.setCellStyle(headerStyle);
            }

            int rowNum = 3;
            for (ProductDetail detail : productDetails) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(safeString(detail.getPlatform()));
                row.createCell(1).setCellValue(safeDouble(detail.getPrice()));
                row.createCell(2).setCellValue(safeDouble(detail.getRating()));
                row.createCell(3).setCellValue(safeString(detail.getSeller()));
                row.createCell(4).setCellValue(safeString(detail.getDeliveryTime()));
                row.createCell(5).setCellValue(safeString(detail.getReturnPolicy()));
                row.createCell(6).setCellValue(safeString(detail.getWarranty()));
                row.createCell(7).setCellValue(safeString(detail.getOffers()));
                row.createCell(8).setCellValue(safeString(detail.getProductLink()));
            }

            for (int i = 0; i < headers.length; i++) {
                sheet.autoSizeColumn(i);
            }

            try (FileOutputStream fileOut = new FileOutputStream(outputFile)) {
                workbook.write(fileOut);
            }

            System.out.println("✓ Excel exported successfully: " + outputFile.getAbsolutePath());
            return true;

        } catch (Exception e) {
            System.err.println("✗ Excel export failed: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    private String formatPrice(Double price) {
        if (price == null) return "0.00";
        return String.format("%.2f", price);
    }

    private String formatRating(Double rating) {
        if (rating == null) return "0.0";
        return String.format("%.1f", rating);
    }

    private String safeString(String value) {
        return (value == null || value.isEmpty()) ? "N/A" : value;
    }

    private double safeDouble(Double value) {
        return (value == null) ? 0.0 : value;
    }

    public File getDefaultExportDirectory() {
        String userHome = System.getProperty("user.home");
        File exportDir = new File(userHome, "PriceVerse_Exports");
        if (!exportDir.exists()) {
            exportDir.mkdirs();
        }
        return exportDir;
    }

    public String generateFileName(String productName, String extension) {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String sanitizedName = productName.replaceAll("[^a-zA-Z0-9]", "_");
        return "PriceVerse_" + sanitizedName + "_" + timestamp + "." + extension;
    }
}

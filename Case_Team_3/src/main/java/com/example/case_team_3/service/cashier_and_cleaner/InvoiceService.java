package com.example.case_team_3.service.cashier_and_cleaner;
import com.example.case_team_3.model.cashier.Invoice;
import com.example.case_team_3.model.cashier.TransactionHistory;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import org.springframework.stereotype.Service;

import java.io.ByteArrayOutputStream;
import java.time.LocalDate;
@Service
public class InvoiceService {
    public byte[] generateInvoice(Invoice invoice) {
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDoc = new PdfDocument(writer);
            Document document = new Document(pdfDoc);

            document.add(new Paragraph("HÓA ĐƠN THANH TOÁN").setBold());
            document.add(new Paragraph("Mã hóa đơn: " + invoice.getInvoiceId()));
            document.add(new Paragraph("Khách hàng: " + invoice.getCustomerName()));
            document.add(new Paragraph("Ngày lập: " + LocalDate.now()));
            document.add(new Paragraph("=========================="));

            for (TransactionHistory transaction : invoice.getTransactions()) {
                document.add(new Paragraph("Ngày: " + transaction.getTransactionTime()));
                document.add(new Paragraph("Số tiền: " + invoice.getTotalAmount() + " VNĐ"));
                document.add(new Paragraph("--------------------------------"));
            }

            document.add(new Paragraph("Tổng tiền: " + invoice.getTotalAmount() + " VNĐ").setBold());

            document.close();
            return outputStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
}
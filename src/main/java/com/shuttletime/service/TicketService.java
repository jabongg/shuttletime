package com.shuttletime.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Image;
import com.itextpdf.io.image.ImageDataFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import com.shuttletime.model.entity.Booking;
import org.springframework.stereotype.Service;

@Service
public class TicketService {

    public byte[] generateTicket(Booking booking) throws IOException, WriterException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PdfWriter writer = new PdfWriter(baos);
        PdfDocument pdf = new PdfDocument(writer);
        Document document = new Document(pdf);

        // Title
        document.add(new Paragraph("🏸 ShuttleTime Booking Ticket")
                .setFontSize(18));

        document.add(new Paragraph("Booking ID: " + booking.getId()));
        document.add(new Paragraph("Name: " + booking.getUser().getUsername()));
        document.add(new Paragraph("Venue: " + booking.getBadmintonCourt().getVenue().getName()));
        document.add(new Paragraph("Court: " + booking.getBadmintonCourt().getCourtName()));
        document.add(new Paragraph("Date & Time: " + booking.getStartTime() + " - " + booking.getEndTime()));
        document.add(new Paragraph("Price: ₹" + booking.getBadmintonCourt().getPrice()));

        // Generate QR Code (bookingId)
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        BitMatrix bitMatrix = qrCodeWriter.encode(booking.getId().toString(), BarcodeFormat.QR_CODE, 150, 150);
        ByteArrayOutputStream pngOutputStream = new ByteArrayOutputStream();
        MatrixToImageWriter.writeToStream(bitMatrix, "PNG", pngOutputStream);

        Image qrImage = new Image(ImageDataFactory.create(pngOutputStream.toByteArray()));
        document.add(qrImage);

        document.close();

        return baos.toByteArray();
    }
}

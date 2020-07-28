package myCredit.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import myCredit.domain.Credit;
import myCredit.domain.Person;
import myCredit.domain.User;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.List;

import static myCredit.utils.PrintCredit.*;

@Component
public class PdfCreator {
    public static final String FONT = "./src/main/resources/fonts/tnr2.ttf";
    private BaseFont bf = BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
    private Font titleFont = new Font(bf, 30, Font.BOLDITALIC);
    private Font bigFont = new Font(bf, 16, Font.BOLD);
    private Font smallFont = new Font(bf, 12, Font.NORMAL);


    public PdfCreator() throws IOException, DocumentException {
    }

    @SneakyThrows
    public ByteArrayInputStream createPersonPdf(Person person) {
        var title = new Paragraph(person.getName(), titleFont);
        java.util.List<Credit> credits = person.getCredits();
        String name = person.getName() + "_credits.pdf";
        Image image;
        if (person.getImage().isEmpty()) {
            image = Image.getInstance(person.getImageFile());}
        else {
            image = Image.getInstance(new URL(person.getImage()));}

        return getInputStreamDocument(title, credits, name, image);
    }

    private ByteArrayInputStream getInputStreamDocument(Paragraph title, List<Credit> credits, String name, Image image) throws DocumentException {
        Document document = new Document();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        document.addTitle(name);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        document.add(new Paragraph("Общая сумма: " + printSum(credits) + "грн", bigFont));
        document.add(new Paragraph("Сумма в месяц: " + printMonthPay(credits) + "грн", bigFont));
        if (image != null) addImage(document, image);
        addTable(document, credits, bigFont, smallFont);
        document.close();
        writer.close();
        return new ByteArrayInputStream(out.toByteArray());
    }


    @SneakyThrows
    public ByteArrayInputStream createUserPdf(User user) {
        String name = user.getUsername() + "_credits.pdf";
        var title = new Paragraph(user.getUsername(), titleFont);
        java.util.List<Credit> credits = printCredits(user);
        Image image = Image.getInstance("src/main/resources/static/img/avatar1.png");
        if (user.getPicture()!= null){
       image = Image.getInstance(new URL(user.getPicture()));}
        return getInputStreamDocument(title, credits, name, image);
    }

    @SneakyThrows
    public void addImage(Document document, Image image) {
            image.scaleAbsolute(140, 140);
            image.setAlignment(Element.ALIGN_RIGHT);
            document.add(image);
    }

    @SneakyThrows
    public void addTable(Document document, List<Credit> credits, Font bigFont, Font smallFont) {
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100); //Width 100%
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(new PdfPCell(new Phrase("#", bigFont)));
        table.addCell(new PdfPCell(new Phrase("Банк", bigFont)));
        table.addCell(new PdfPCell(new Phrase("Название", bigFont)));
        table.addCell(new PdfPCell(new Phrase("Дата начала", bigFont)));
        table.addCell(new PdfPCell(new Phrase("Дата окончания", bigFont)));
        table.addCell(new PdfPCell(new Phrase("Сумма", bigFont)));
        table.addCell(new PdfPCell(new Phrase("Ежемесячный платеж", bigFont)));

        table.setHeaderRows(1);
        int count = 1;
        for (Credit credit : credits) {
            table.addCell(new PdfPCell(new Phrase(String.valueOf(count), smallFont)));
            count++;
            table.addCell(new PdfPCell(new Phrase(credit.getBank(), smallFont)));
            table.addCell(new PdfPCell(new Phrase(credit.getTitle(), smallFont)));
            table.addCell(new PdfPCell(new Phrase(credit.getDateStart().toString(), smallFont)));
            table.addCell(new PdfPCell(new Phrase(credit.getDateEnd().toString(), smallFont)));
            table.addCell(new PdfPCell(new Phrase(credit.getCost().toString(), smallFont)));
            table.addCell(new PdfPCell(new Phrase(credit.getMonthPay().toString(), smallFont)));
        }
        document.add(table);
    }
}

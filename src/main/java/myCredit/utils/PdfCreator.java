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
import java.net.URL;
import java.util.List;

import static myCredit.utils.PrintCredit.*;

@Component
public class PdfCreator {
    public static final String FONT = "./src/main/resources/fonts/tnr2.ttf";

    @SneakyThrows
    public ByteArrayInputStream createPersonPdf(Person person){
        String name = person.getName() +"_credits.pdf";
        Document document = new Document();
        BaseFont bf=BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font titleFont = new Font(bf,30,Font.BOLDITALIC);
        Font bigFont=new Font(bf,16,Font.BOLD);
        Font smallFont=new Font(bf,12,Font.NORMAL);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        document.addTitle(name);
        var title = new Paragraph(person.getName(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        java.util.List<Credit> credits = person.getCredits();
        document.add(new Paragraph("Общая сумма: "+printSum(credits) + "грн", bigFont));
        document.add(new Paragraph("Сумма в месяц: "+printMonthPay(credits) + "грн", bigFont));
        addImage(document, person);
        addTable(document, credits, bigFont, smallFont);
        document.close();
        writer.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    @SneakyThrows
    public ByteArrayInputStream createUserPdf(User user){
        String name = user.getUsername() +"_credits.pdf";
        Document document = new Document();
        BaseFont bf=BaseFont.createFont(FONT, BaseFont.IDENTITY_H, BaseFont.EMBEDDED);
        Font titleFont = new Font(bf,30,Font.BOLDITALIC);
        Font bigFont=new Font(bf,16,Font.BOLD);
        Font smallFont=new Font(bf,12,Font.NORMAL);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        PdfWriter writer = PdfWriter.getInstance(document, out);
        document.open();
        document.addTitle(name);
        var title = new Paragraph(user.getUsername(), titleFont);
        title.setAlignment(Element.ALIGN_CENTER);
        document.add(title);
        java.util.List<Credit> credits = printCredits(user);
        document.add(new Paragraph("Общая сумма: "+printSum(credits) + "грн", bigFont));
        document.add(new Paragraph("Сумма в месяц: "+printMonthPay(credits) + "грн", bigFont));
        addTable(document, credits, bigFont, smallFont);
        document.close();
        writer.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    @SneakyThrows
    public void addImage(Document document, Person person){
        if(person.getImage().isEmpty()){
        Image image1 = Image.getInstance(person.getImageFile());
        image1.scaleAbsolute(140, 140);
        image1.setAlignment(Element.ALIGN_RIGHT);
        document.add(image1);
        }
        else{
        Image image2 = Image.getInstance(new URL(person.getImage()));
        image2.scaleAbsolute(140,140);
        image2.setAlignment(Element.ALIGN_RIGHT);
        document.add(image2);}
    }

    @SneakyThrows
    public void addTable(Document document, List<Credit> credits, Font bigFont, Font smallFont){
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100); //Width 100%
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        table.addCell(new PdfPCell(new Phrase("#", bigFont)));
        table.addCell(new PdfPCell(new Phrase("Банк", bigFont)));
        table.addCell( new PdfPCell(new Phrase("Название", bigFont)));
        table.addCell(new PdfPCell(new Phrase("Дата начала", bigFont)));
        table.addCell(new PdfPCell(new Phrase("Дата окончания", bigFont)));
        table.addCell(new PdfPCell(new Phrase("Сумма", bigFont)));
        table.addCell(new PdfPCell(new Phrase("Ежемесячный платеж", bigFont)));

        table.setHeaderRows(1);
        int count = 1;
        for(Credit credit : credits){
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

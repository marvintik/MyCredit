package myCredit.utils;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.BaseFont;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lombok.SneakyThrows;
import myCredit.domain.Credit;
import myCredit.domain.Person;
import org.springframework.stereotype.Component;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.net.URL;

@Component
public class PdfCreator {
    public static final String FONT = "./src/main/resources/fonts/tnr2.ttf";

    @SneakyThrows
    public ByteArrayInputStream createPdf(Person person){
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
        sumCostAndMonthPay(document, person, bigFont);
        addImage(document, person);
        addTable(document, person, bigFont, smallFont);
        document.close();
        writer.close();
        return new ByteArrayInputStream(out.toByteArray());
    }

    @SneakyThrows
    private void sumCostAndMonthPay(Document document, Person person, Font bigFont) {
        var credits = person.getCredits();
        int sumCost=0;
        int sumMonthPay=0;
        for (Credit credit : credits) {
            if (credit.getCost() != null) {
                sumCost += credit.getCost();
            }
            if (credit.getMonthPay() != null) {
                sumMonthPay += credit.getMonthPay();
            }
        }
        document.add(new Paragraph("Общая сумма: "+sumCost + "грн", bigFont));
        document.add(new Paragraph("Сумма в месяц: "+sumMonthPay + "грн", bigFont));
    }

    private String sumCost() {
        return "";
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
    public void addTable(Document document, Person person, Font bigFont, Font smallFont){
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100); //Width 100%
        table.setSpacingBefore(10f); //Space before table
        table.setSpacingAfter(10f);
        table.setHorizontalAlignment(Element.ALIGN_CENTER);

        PdfPCell h1 = new PdfPCell(new Phrase("#", bigFont));
        PdfPCell h2 = new PdfPCell(new Phrase("Банк", bigFont));
        PdfPCell h3 = new PdfPCell(new Phrase("Название", bigFont));
        PdfPCell h4 = new PdfPCell(new Phrase("Дата начала", bigFont));
        PdfPCell h5 = new PdfPCell(new Phrase("Дата окончания", bigFont));
        PdfPCell h6 = new PdfPCell(new Phrase("Сумма", bigFont));
        PdfPCell h7 = new PdfPCell(new Phrase("Ежемесячный платеж", bigFont));
        table.addCell(h1);
        table.addCell(h2);
        table.addCell(h3);
        table.addCell(h4);
        table.addCell(h5);
        table.addCell(h6);
        table.addCell(h7);
        table.setHeaderRows(1);
        int count = 1;
        for(Credit credit : person.getCredits()){
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

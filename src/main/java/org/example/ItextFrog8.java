package org.example;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.barcodes.qrcode.EncodeHintType;
import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.kernel.colors.CalRgb;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ItextFrog8 {

    static int safeMargin = 4;

    // 基本入门案例
    public static void main(String[] args) {
        List<Item> data = new ArrayList<>();
        for (int i = 0; i < 8; i++) {
            Item item = new Item("028"+String.valueOf(10001000+i+1));
            data.add(item);
        }

        Document document = null;
        try {
//            // 创建并初始化一个PDF文档
            PdfDocument pdf = new PdfDocument(new PdfWriter("C:\\Users\\\\jilijili\\Desktop\\金蟾寻宝Frog.pdf"));
//            // 初始化文档
            document = new Document(pdf, PageSize.A4.rotate());
            //default 32f
            document.setMargins(safeMargin,safeMargin,safeMargin,safeMargin);
            int pageMaxSize = 8;
            int totalPages = (data.size() + pageMaxSize - 1) / pageMaxSize;
            for (int i = 0; i < totalPages; i++) {
                int fromIndex = i * pageMaxSize;
                int toIndex = Math.min((i + 1) * pageMaxSize, data.size());
                List<Item> pageItems=data.subList(fromIndex, toIndex);
                Table table= new ItextFrog8().page(pageItems,pdf);
                document.add(table);
            }
        } catch (IOException | java.io.IOException e) {
            throw new RuntimeException(e);
        } catch (Exception e) {
            throw new RuntimeException(e);
        } finally {
            //关闭文档
            if (document != null) {
                document.close();
            }
        }
    }


    private Table page(java.util.List<Item> items,PdfDocument pdf) throws Exception{
        int columns = 4;
        int rows=2;
        if (items.size()>columns*rows){
            throw new Exception("每页最多"+columns*rows+"条数据");
        }
        // 创建一个 FlexContainer
        Table page = new Table(columns);
        page.setMargin(0);
        page.setPadding(0);
        //page.useAllAvailableWidth();
        page.setHeight(PageSize.A4.rotate().getHeight());
        float tableWidth = PageSize.A4.rotate().getWidth() - 2*safeMargin;
        page.setWidth(tableWidth); // 限制表格宽度
        page.setVerticalAlignment(VerticalAlignment.MIDDLE);
        page.setHorizontalAlignment(HorizontalAlignment.CENTER);
        //page.setBackgroundColor(DeviceRgb.BLUE);

        float divWidth = (PageSize.A4.rotate().getWidth()-(columns-1)*24-safeMargin*2) / columns;
        float divHeight = (PageSize.A4.rotate().getHeight() - (rows-1)*24-safeMargin*2) / rows;
        float sideLength=divWidth<divHeight?divWidth:divHeight;

        // 将 Div 添加到 FlexContainer 中
        for (int i = 0; i < items.size(); i++) {
            // 创建 8 个 Div 实例
            //正方形
            Div itemedDiv = itemDiv(sideLength,items.get(i),pdf);
            // 调整 Div 的宽度，使其一行能容纳两个 Div
            itemedDiv.setWidth(divWidth);
            // 调整 Div 的高度，使其一列能容纳三个 Div
            itemedDiv.setHeight(divHeight);
            Cell cell=new Cell();
            Border noBorder = new SolidBorder(ColorConstants.WHITE, 0); // 创建透明边框
            cell.setBorder(noBorder);
            cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
            cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
            cell.add(itemedDiv);
//            if (i==0){
//                cell.setBackgroundColor(DeviceRgb.RED);
//            }
//            if (i==3){
//                cell.setBackgroundColor(DeviceRgb.RED);
//            }
            page.addCell(cell);
        }
        return page;
    }

    private  Div itemDiv(float sideLength,Item data,PdfDocument pdf) throws Exception{
        Div itemDiv = new Div();
        itemDiv.setMargin(0);
        itemDiv.setPadding(0);
        itemDiv.setBorder(new SolidBorder(ColorConstants.BLACK, 0.5f));
        // 添加垂直对齐属性
        itemDiv.setHorizontalAlignment(HorizontalAlignment.CENTER);
        itemDiv.setVerticalAlignment(VerticalAlignment.MIDDLE);
        // 添加一段中文（itext无法支持中文字体，需要设置字体）
        PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
        // 设置文本的字体、大小、颜色、背景颜色、对齐方式、垂直对其方式
        //Chunk chunk1 = new Chunk("th");
        Paragraph title = new Paragraph(data.getTitle())
                .setFont(font)
                .setFontSize(18)
                //.setBold()
                .setMargin(0)
                .setPadding(0)
                .setFontColor(DeviceRgb.BLACK)
                //.setBackgroundColor(new DeviceRgb(187, 255, 255))
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.MIDDLE)
                ;

        itemDiv.add(title);

        BarcodeQRCode qrcode = new BarcodeQRCode(data.getUrl()+data.getCode());
        Map<EncodeHintType, Object> qrcodeHints = new java.util.HashMap<>();
        qrcodeHints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        qrcode.setHints(qrcodeHints);
        PdfFormXObject qrcodeImage = qrcode.createFormXObject(pdf);
        Image image=new Image(qrcodeImage);
        image.setHeight(sideLength);
        image.setWidth(sideLength);
        image.setMargins(0f,0,0,0);
        image.setPadding(0f);
        image.setHorizontalAlignment(HorizontalAlignment.CENTER);
        image.setAutoScale(false);
        //image.setBackgroundColor(new DeviceRgb(187, 255, 255));
        itemDiv.add(image);


        Paragraph code = new Paragraph("编码: "+data.getCode())
                .setFont(font)
                .setFontSize(12)
                .setMargin(0)
                .setPadding(0)
                .setFontColor(DeviceRgb.BLACK)
                //.setBackgroundColor(new DeviceRgb(187, 255, 255))
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.BOTTOM) // 修改为底部对齐
                ;
        itemDiv.add(code);

        Paragraph slogan = new Paragraph(data.getSlogan())
                .setFont(font)
                .setFontSize(14)
                .setMargin(0)
                .setPadding(0)
                .setFontColor(DeviceRgb.BLACK)
               // .setBackgroundColor(new DeviceRgb(187, 255, 255))
                .setTextAlignment(TextAlignment.CENTER);
                //.setVerticalAlignment(VerticalAlignment.MIDDLE);
        itemDiv.add(slogan);
        return itemDiv;
    }



}




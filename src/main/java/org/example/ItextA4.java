package org.example;

import com.itextpdf.barcodes.BarcodeQRCode;
import com.itextpdf.barcodes.qrcode.EncodeHintType;
import com.itextpdf.io.exceptions.IOException;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;


import com.itextpdf.barcodes.BarcodeEAN;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.xobject.PdfFormXObject;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.properties.UnitValue;
import io.micronaut.http.server.netty.MicronautHttpData;


import java.awt.*;
import java.io.FileNotFoundException;
import java.util.Map;

import static com.itextpdf.kernel.pdf.PdfName.FontFamily;
import static com.itextpdf.kernel.pdf.PdfName.Pages;

public class ItextA4 {
    // 基本入门案例
    public static void main(String[] args) {
        Document document = null;
        try {
//            // 创建并初始化一个PDF文档
            PdfDocument pdf = new PdfDocument(new PdfWriter("C:\\Users\\bbbang\\Desktop\\简单示例.pdf"));
//            // 初始化文档
            document = new Document(pdf, PageSize.A4);
            // 创建一个 FlexContainer
            Table grid = new Table(2);
            grid.setMargin(0);
            grid.setPadding(0);
            grid.useAllAvailableWidth();
            grid.setHeight(PageSize.A4.getHeight());
            grid.setVerticalAlignment(VerticalAlignment.MIDDLE);
            grid.setHorizontalAlignment(HorizontalAlignment.CENTER);
            //flexContainer.setWidth(PageSize.A4.getWidth()-120);
            //flexContainer.setHeight(PageSize.A4.getHeight()-180);
            //flexContainer.setHorizontalBorderSpacing(60);
            //flexContainer.setVerticalBorderSpacing(60);

            // 将 Div 添加到 FlexContainer 中
           for (int i = 0; i < 6; i++) {
               // 创建 6 个 Div 实例
               Div div1 = grid(i,pdf);
               // 调整 Div 的宽度，使其一行能容纳两个 Div
               float divWidth = (PageSize.A4.getWidth()-120) / 2;
               div1.setWidth(divWidth);
               // 调整 Div 的高度，使其一列能容纳三个 Div
               float divHeight = (PageSize.A4.getHeight() - 180) / 3;
               div1.setHeight(divHeight);
               Cell cell=new Cell();
               Border noBorder = new SolidBorder(ColorConstants.WHITE, 0); // 创建透明边框
                // Border solidBorder = new SolidBorder(ColorConstants.BLACK, 0); // 创建透明边框
               cell.setBorder(noBorder);
//               if(i%2==0 ){
//                   cell.setBorderRight(solidBorder);
//               }
//               if (i==0 || i==1){
//                   cell.setBorderTop(solidBorder);
//               }
              // cell.setBorderBottom(solidBorder);
               cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
               cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
               cell.add(div1);
               grid.addCell(cell);
           }
            // 将 FlexContainer 添加到文档
            document.add(grid);



        } catch (IOException | java.io.IOException e) {
            throw new RuntimeException(e);
        } finally {
            //关闭文档
            if (document != null) {
                document.close();
            }
        }
    }

    public static Div grid(int i,PdfDocument pdf) throws IOException, java.io.IOException{
        Div divA = new Div();
        divA.setMargin(0);
        divA.setPadding(0);
        divA.setBorder(new SolidBorder(ColorConstants.BLACK, 0.5f));
        // 添加垂直对齐属性
        divA.setHorizontalAlignment(HorizontalAlignment.CENTER);
        divA.setVerticalAlignment(VerticalAlignment.MIDDLE);
        // 添加一段中文（itext无法支持中文字体，需要设置字体）
        PdfFont font = PdfFontFactory.createFont("STSongStd-Light", "UniGB-UCS2-H");
        // 设置文本的字体、大小、颜色、背景颜色、对齐方式、垂直对其方式
        //Chunk chunk1 = new Chunk("th");
        Paragraph title = new Paragraph("金蟾寻宝 扫码探索")
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

        divA.add(title);

        BarcodeQRCode qrcode = new BarcodeQRCode("http://prize.jxyuan.cn?code=028"+(10001000+i));
        Map<EncodeHintType, Object> hints = new java.util.HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        qrcode.setHints(hints);
        PdfFormXObject qrcodeImage = qrcode.createFormXObject(pdf);
        Image image=new Image(qrcodeImage);
        image.setHeight(148);
        image.setWidth(148);
        image.setMargins(0,0,0,0);
        image.setPadding(0);
        image.setHorizontalAlignment(HorizontalAlignment.CENTER);
        image.setAutoScale(false);
        //image.setBackgroundColor(new DeviceRgb(187, 255, 255));
        divA.add(image);


        Paragraph code = new Paragraph("编码: 028"+(10001000+i))
                .setFont(font)
                .setFontSize(12)
                .setMargin(0)
                .setPadding(0)
                .setFontColor(DeviceRgb.BLACK)
                //.setBackgroundColor(new DeviceRgb(187, 255, 255))
                .setTextAlignment(TextAlignment.CENTER)
                .setVerticalAlignment(VerticalAlignment.BOTTOM) // 修改为底部对齐
                ;
        divA.add(code);

        Paragraph slogan = new Paragraph("做最好的户外探索游戏")
                .setFont(font)
                .setFontSize(14)
                .setMargin(0)
                .setPadding(0)
                .setFontColor(DeviceRgb.BLACK)
               // .setBackgroundColor(new DeviceRgb(187, 255, 255))
                .setTextAlignment(TextAlignment.CENTER);
                //.setVerticalAlignment(VerticalAlignment.MIDDLE);
        divA.add(slogan);

     


        
        return divA;
    }



}




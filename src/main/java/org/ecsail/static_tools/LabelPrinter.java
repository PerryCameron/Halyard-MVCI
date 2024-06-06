package org.ecsail.static_tools;

import org.ecsail.dto.LabelDTO;
import org.ecsail.mvci_membership.MembershipView;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.print.PrintService;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.print.*;
import java.util.Arrays;

public class LabelPrinter {

    private static final Logger logger = LoggerFactory.getLogger(LabelPrinter.class);
    static LabelDTO label;

    public static void printMembershipLabel(LabelDTO incomingLabel) {
        label = incomingLabel;

        PrintService[] ps = PrinterJob.lookupPrintServices();
        if (ps.length == 0) {
            throw new IllegalStateException("No Printer found");

        } else {
            logger.info("Print Services found!");
        }

        PrintService myService = Arrays.stream(ps)
                .filter((p) -> p.getName().equals("DYMO LabelWriter 450"))
                .findFirst().orElse(null);

        if (myService == null) {
            logger.info("Printer: DYMO LabelWriter 450 not found, check printer ");
        } else {
            Thread t = new Thread(() -> printLabel(myService));
            t.start();
        }

    }


    private static void printLabel(PrintService myService) {
        PrinterJob pj = PrinterJob.getPrinterJob();
        try {
            pj.setPrintService(myService);
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }

        PageFormat pf = pj.defaultPage();
        Paper paper = pf.getPaper();
        double width = fromCMToPPI(3.5);
        double height = fromCMToPPI(8.8);
        paper.setSize(width, height);
        paper.setImageableArea(
                fromCMToPPI(0.25),
                fromCMToPPI(0.5),
                width - fromCMToPPI(0.35),
                height - fromCMToPPI(1));
        pf.setOrientation(PageFormat.LANDSCAPE);
        pf.setPaper(paper);
//        PageFormat validatePage = pj.validatePage(pf);
        pj.setPrintable(new MyLabelPrintable(), pf);
        try {
            pj.print();
        } catch (PrinterException ex) {
            ex.printStackTrace();
        }
    }


    protected static double fromCMToPPI(double cm) {
        return toPPI(cm * 0.393700787);
    }

    protected static double toPPI(double inch) {
        return inch * 72d;
    }

    public static class MyLabelPrintable implements Printable {

        @Override
        public int print(Graphics graphics, PageFormat pageFormat, int pageIndex) {
            int result = NO_SUCH_PAGE;
            if (pageIndex < 1) {
                Graphics2D g2d = (Graphics2D) graphics;
                double width = pageFormat.getImageableWidth();
                double height = pageFormat.getImageableHeight();
                g2d.translate((int) pageFormat.getImageableX(),
                        (int) pageFormat.getImageableY());
                g2d.draw(new Rectangle2D.Double(2, 22, width - 20, height - 23));
//                FontMetrics fm = g2d.getFontMetrics();
//                g2d.drawString("Testing", 0, fm.getAscent());
                g2d.drawString(label.getCity(), 5, 35);
                g2d.drawString(label.getNameAndMemId(), 5, 51);
                g2d.drawString(label.getExpires(), 5, 67);
                g2d.drawString(label.getMember(), 5, 83);
                result = PAGE_EXISTS;
            }
            return result;
        }
    }
}
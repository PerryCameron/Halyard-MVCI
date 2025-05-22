package org.ecsail.pdf;

import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.VerticalAlignment;
import org.ecsail.fx.MembershipFx;
import org.ecsail.fx.PersonFx;
import org.ecsail.mvci_membership.MembershipModel;
import org.ecsail.static_tools.ByteTools;
import org.ecsail.static_tools.HalyardPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;

public class PDF_Envelope {
	public static Logger logger = LoggerFactory.getLogger(PDF_Envelope.class);
	private final Image ecscLogo = new Image(ImageDataFactory.create(ByteTools.toByteArray(Objects.requireNonNull(getClass().getResourceAsStream("/images/EagleCreekLogoForPDF.png")))));

	private int current_membership_id;
	private final PdfFont font;
	private final MembershipFx membershipFx;
	private Optional<PersonFx> primary;

	public PDF_Envelope(boolean iom, MembershipModel model) throws IOException {
		logger.info("Creating envelope");
		if (model == null) {
			System.out.println("MembershipModel is null");
		}
		this.membershipFx = model.membershipProperty().get();
		this.primary = membershipFx != null && membershipFx.getPeople() != null
				? membershipFx.getPeople().stream()
				.filter(person -> person != null && person.getMemberType() == 1)
				.findFirst()
				: Optional.empty();


		HalyardPaths.checkPath(HalyardPaths.ECSC_HOME);
//		this.membershipChair = Objects.requireNonNull(dataBaseService).getCurrentMembershipChair();
		this.current_membership_id = Objects.requireNonNull(model).membershipProperty().get().membershipIdProperty().get();

		if(System.getProperty("os.name").equals("Windows 10"))
		FontProgramFactory.registerFont("c:/windows/fonts/times.ttf", "times");
		else if(System.getProperty("os.name").equals("Mac OS X"))
		FontProgramFactory.registerFont("/Library/Fonts/SF-Compact-Display-Regular.otf", "times");


		this.font = PdfFontFactory.createRegisteredFont("times");
		// Initialize PDF writer

		// Envelope sizing https://www.paperpapers.com/envelope-size-chart.html
		// No. 6 1/4 (#6-1/4) Envelope inches: 3.5 x 6 (mm: 88.9 x 152.4)
		// 6 inch x 72 points = 432 points (the width)
		// 3.5 inch x 72 points = 252 points (the height)

		if(model.isEnvelopeIsCatalogue()) {
			create6x9 ();
		} else {
			create4x9 ();
		}
		logger.info("destination=" + HalyardPaths.ECSC_HOME + "_envelopes.pdf");
		File file = new File(HalyardPaths.ECSC_HOME + "_envelopes.pdf");
		Desktop desktop = Desktop.getDesktop(); // Gui_Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()
		// Open the document
		try {
			desktop.open(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	// #10 Envelope inches: 4.125 x 9.5 (mm: 104.775 x 241.3)
	// 9.5 x 72 points = 684 points (the width)
	// 4.125 x 72 points = 297 points (the height)
	public void create4x9 () throws FileNotFoundException {
		PdfWriter writer = new PdfWriter(HalyardPaths.ECSC_HOME + "_envelopes.pdf");
		// Initialize PDF document
		PdfDocument pdf = new PdfDocument(writer);
		Rectangle envelope = new Rectangle(684, 297);
		// Initialize document
		Document doc = new Document(pdf, new PageSize(envelope));
		doc.setTopMargin(0);
		doc.setLeftMargin(0.25f);
//		if(isOneMembership) {
//			membershipListDTO = dataBaseService.getMembershipListByIdAndYear(current_membership_id, year);
		doc.add(createReturnAddress());
		doc.add(new Paragraph(new Text("\n\n\n\n\n")));
		doc.add(createAddress());
//		} else {
//			buildAddress(doc);
//		}
		doc.close();
	}

	// #1 Catalog inches: 6 x 9 (mm: 152.4 x 228.6)
	// 9 x 72 points = 648 (the width)
	// 6 x 72 points = 432 (the height)
	public void create6x9 () throws FileNotFoundException {
		PdfWriter writer = new PdfWriter(HalyardPaths.ECSC_HOME + "_envelopes.pdf");
		// Initialize PDF document
		PdfDocument pdf = new PdfDocument(writer);
		Rectangle envelope = new Rectangle(648, 432);
		// Initialize document
		Document doc = new Document(pdf, new PageSize(envelope));
		doc.setTopMargin(0);
		doc.setLeftMargin(0.25f);
//		if(isOneMembership) {
//			membershipListDTO = dataBaseService.getMembershipListByIdAndYear(current_membership_id, year);
		doc.add(createReturnAddress());
		doc.add(new Paragraph(new Text("\n\n\n\n\n\n\n\n\n")));
		doc.add(createAddress());
//		} else {
//			buildAddress(doc);
//		}
		doc.close();
	}

	private void buildAddress(Document doc) {
////		List<MembershipIdDTO> membershipIdDTOS = dataBaseService.getAllMembershipIdsByYear(year);
//		membershipIdDTOS.sort(Comparator.comparing(MembershipIdDTO::getMembershipId));
//		for(MembershipIdDTO id: membershipIdDTOS) {
//			current_membership_id = id.getMembershipId();
////			membershipListDTO = dataBaseService.getMembershipListByIdAndYear(current_membership_id, year);
//			doc.add(createReturnAddress());
//			doc.add(new Paragraph(new Text("\n\n\n\n\n")));
//			doc.add(createAddress());
//			doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
//		}
	}

	public Table createReturnAddress() {
		Table mainTable = new Table(2);
		mainTable.setWidth(290);
		ecscLogo.scale(0.25f, 0.25f);
		Cell cell = new Cell(3,1);
		cell.setWidth(40);
		cell.setBorder(Border.NO_BORDER);
		cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
		cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
		cell.add(ecscLogo);
		mainTable.addCell(cell);
		mainTable.addCell(newAddressCell(10,10,"ECSC Membership"));
//		mainTable.addCell(newAddressCell(10,10,membershipChair.getAddress()));
//		mainTable.addCell(newAddressCell(10,10,membershipChair.getCityStateZip()));
		return mainTable;
	}

	private Cell newAddressCell(int fontSize, int fixedLeading, String paragraphContent) {
		Paragraph p;
		Cell cell = new Cell();
		p = new Paragraph(paragraphContent);
		p.setFont(font);
		p.setFontSize(fontSize);
		p.setFixedLeading(fixedLeading);
		cell.setBorder(Border.NO_BORDER);
		cell.add(p);
		return cell;
	}

	public Table createAddress() {
		Table mainTable = new Table(2);
		mainTable.setWidth(590);
//		ecscLogo.scale(0.25f, 0.25f);

		Cell cell = new Cell(3,1);
		cell.setWidth(260);
		cell.setBorder(Border.NO_BORDER);
		mainTable.addCell(cell);
		mainTable.addCell(newAddressCell(16,14,primary.get().getFirstName()
				+ " " + primary.get().getLastName() + " #" + current_membership_id));
		mainTable.addCell(newAddressCell(16,14,membershipFx.getAddress()));
		mainTable.addCell(newAddressCell(16,14,membershipFx.getCity() + ", "
				+ membershipFx.getState() + " " + membershipFx.getZip()));
		return mainTable;
	}
}

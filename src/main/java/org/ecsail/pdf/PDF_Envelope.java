package org.ecsail.pdf;

import org.ecsail.mvci_membership.MembershipModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.*;

public class PDF_Envelope {
//	public static Logger logger = LoggerFactory.getLogger(PDF_Envelope.class);
//	AppSettingsRepository appSettingsRepository;
//	MembershipRepository membershipRepository;
//	MembershipIdRepository membershipIdRepository;
//	Image ecscLogo = new Image(ImageDataFactory.create(PDF_DepositReport.toByteArray(Objects.requireNonNull(getClass().getResourceAsStream("/EagleCreekLogoForPDF.png")))));
//	MembershipListDTO membershipListDTO;
//	MembershipDTO membershipChair;
//	private final int year;
//	private int current_membership_id;
//	private List<MembershipIdDTO> membershipIdDTOS = new ArrayList<MembershipIdDTO>();
//	PdfFont font;
//	private boolean isOneMembership;



//	public PDF_Envelope(boolean iom, boolean isCatalog, String membershipId) throws IOException { }
	public PDF_Envelope(boolean iom, boolean isCatalog, MembershipModel model) throws IOException { }
//		this.year= LocalDate.now().getYear();
//		this.appSettingsRepository = new AppSettingsRepositoryImpl();
//		this.membershipRepository = new MembershipRepositoryImpl();
//		this.membershipIdRepository = new MembershipIdRepositoryImpl();
//		HalyardPaths.checkPath(HalyardPaths.ECSC_HOME);
//		this.current_membership_id = Integer.parseInt(membershipId);
//		this.isOneMembership = iom;
//		this.membershipChair = membershipRepository.getCurrentMembershipChair();
//
//		if(System.getProperty("os.name").equals("Windows 10"))
//		FontProgramFactory.registerFont("c:/windows/fonts/times.ttf", "times");
//		else if(System.getProperty("os.name").equals("Mac OS X"))
//		FontProgramFactory.registerFont("/Library/Fonts/SF-Compact-Display-Regular.otf", "times");
//
//
//		this.font = PdfFontFactory.createRegisteredFont("times");
//		// Initialize PDF writer
//
//		// Envelope sizeing https://www.paperpapers.com/envelope-size-chart.html
//		// No. 6 1/4 (#6-1/4) Envelope inches: 3.5 x 6 (mm: 88.9 x 152.4)
//		// 6 inch x 72 points = 432 points (the width)
//		// 3.5 inch x 72 points = 252 points (the height)
//
//		if(isCatalog) {
//			create6x9 ();
//		} else {
//			create4x9 ();
//		}
//		logger.info("destination=" + HalyardPaths.ECSC_HOME + "_envelopes.pdf");
//		File file = new File(HalyardPaths.ECSC_HOME + "_envelopes.pdf");
//		Desktop desktop = Desktop.getDesktop(); // Gui_Main.class.getProtectionDomain().getCodeSource().getLocation().getPath()
//		// Open the document
//		try {
//			desktop.open(file);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
//	}
//
//	// #10 Envelope inches: 4.125 x 9.5 (mm: 104.775 x 241.3)
//	// 9.5 x 72 points = 684 points (the width)
//	// 4.125 x 72 points = 297 points (the height)
//	public void create4x9 () throws FileNotFoundException {
//		PdfWriter writer = new PdfWriter(HalyardPaths.ECSC_HOME + "_envelopes.pdf");
//		// Initialize PDF document
//		PdfDocument pdf = new PdfDocument(writer);
//		Rectangle envelope = new Rectangle(684, 297);
//		// Initialize document
//		Document doc = new Document(pdf, new PageSize(envelope));
//		doc.setTopMargin(0);
//		doc.setLeftMargin(0.25f);
//		if(isOneMembership) {
//			membershipListDTO = membershipRepository.getMembershipListByIdAndYear(current_membership_id, year);
//		doc.add(createReturnAddress());
//		doc.add(new Paragraph(new Text("\n\n\n\n\n")));
//		doc.add(createAddress());
//		} else {
//			buildAddress(doc);
//		}
//		doc.close();
//	}
//
//	// #1 Catalog inches: 6 x 9 (mm: 152.4 x 228.6)
//	// 9 x 72 points = 648 (the width)
//	// 6 x 72 points = 432 (the height)
//	public void create6x9 () throws FileNotFoundException {
//		PdfWriter writer = new PdfWriter(HalyardPaths.ECSC_HOME + "_envelopes.pdf");
//		// Initialize PDF document
//		PdfDocument pdf = new PdfDocument(writer);
//		Rectangle envelope = new Rectangle(648, 432);
//		// Initialize document
//		Document doc = new Document(pdf, new PageSize(envelope));
//		doc.setTopMargin(0);
//		doc.setLeftMargin(0.25f);
//		if(isOneMembership) {
//			membershipListDTO = membershipRepository.getMembershipListByIdAndYear(current_membership_id, year);
//		doc.add(createReturnAddress());
//		doc.add(new Paragraph(new Text("\n\n\n\n\n\n\n\n\n")));
//		doc.add(createAddress());
//		} else {
//			buildAddress(doc);
//		}
//		doc.close();
//	}
//
//	private void buildAddress(Document doc) {
//		membershipIdDTOS = membershipIdRepository.getAllMembershipIdsByYear(year);
//		Collections.sort(membershipIdDTOS, Comparator.comparing(MembershipIdDTO::getMembershipId));
//		for(MembershipIdDTO id: membershipIdDTOS) {
//			current_membership_id = Integer.parseInt(id.getMembershipId());
//			membershipListDTO = membershipRepository.getMembershipListByIdAndYear(current_membership_id, year);
//			doc.add(createReturnAddress());
//			doc.add(new Paragraph(new Text("\n\n\n\n\n")));
//			doc.add(createAddress());
//			doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
//		}
//	}
//
//	public Table createReturnAddress() {
//		Table mainTable = new Table(2);
//		mainTable.setWidth(290);
//		ecscLogo.scale(0.25f, 0.25f);
//		Cell cell = new Cell(3,1);
//		cell.setWidth(40);
//		cell.setBorder(Border.NO_BORDER);
//		cell.setVerticalAlignment(VerticalAlignment.MIDDLE);
//		cell.setHorizontalAlignment(HorizontalAlignment.CENTER);
//		cell.add(ecscLogo);
//		mainTable.addCell(cell);
//		mainTable.addCell(newAddressCell(10,10,"ECSC Membership"));
//		mainTable.addCell(newAddressCell(10,10,membershipChair.getAddress()));
//		mainTable.addCell(newAddressCell(10,10,membershipChair.getCityStateZip()));
//		return mainTable;
//	}
//
//	private Cell newAddressCell(int fontSize, int fixedLeading, String paragraphContent) {
//		Paragraph p;
//		Cell cell = new Cell();
//		p = new Paragraph(paragraphContent);
//		p.setFont(font);
//		p.setFontSize(fontSize);
//		p.setFixedLeading(fixedLeading);
//		cell.setBorder(Border.NO_BORDER);
//		cell.add(p);
//		return cell;
//	}
//
//	public Table createAddress() {
//		Table mainTable = new Table(2);
//		mainTable.setWidth(590);
//		ecscLogo.scale(0.25f, 0.25f);
//
//		Cell cell = new Cell(3,1);
//		cell.setWidth(260);
//		cell.setBorder(Border.NO_BORDER);
//		mainTable.addCell(cell);
//		mainTable.addCell(newAddressCell(16,14,membershipListDTO.getFirstName()
//				+ " " + membershipListDTO.getLastName() + " #" + current_membership_id));
//		mainTable.addCell(newAddressCell(16,14,membershipListDTO.getAddress()));
//		mainTable.addCell(newAddressCell(16,14,membershipListDTO.getCity() + ", "
//				+ membershipListDTO.getState() + " " + membershipListDTO.getZip()));
//		return mainTable;
//	}
}

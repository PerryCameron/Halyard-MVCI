package org.ecsail.static_calls;


import javafx.collections.ObservableList;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ecsail.dto.*;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.mvci_roster.SaveFileChooser;
import org.ecsail.repository.interfaces.EmailRepository;
import org.ecsail.repository.interfaces.MembershipIdRepository;
import org.ecsail.repository.interfaces.PhoneRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;



public class Xls_roster implements ConfigFilePaths {

	private static final Logger logger = LoggerFactory.getLogger(Xls_roster.class);
	private final EmailRepository emailRepo;
	private final PhoneRepository phoneRepo;
	private final MembershipIdRepository membershipIdRepo;
	ObservableList<DbRosterSettingsDTO> rosterSettings;
	String selectedYear = "";
	
	public Xls_roster(ObservableList<MembershipListDTO> rosters,
					  ObservableList<DbRosterSettingsDTO> rosterSettings,
					  String rosterType,
					  EmailRepository emailRepo,
					  PhoneRepository phoneRepo,
					  MembershipIdRepository membershipIdRepo) {
		this.rosterSettings = rosterSettings;
		this.emailRepo = emailRepo;
		this.phoneRepo = phoneRepo;
		this.membershipIdRepo = membershipIdRepo;
		ArrayList<String> headers = getHeaders();
		if(rosters.size() > 1) this.selectedYear = rosters.get(0).getSelectedYear();
		logger.info("Creating Roster..");
		//String[] columnHeads = {"Membership ID", "Last Name", "First Name", "Join Date", "Street Address","City","State","Zip"};

        // Create a Workbook
        Workbook workBook = new XSSFWorkbook(); // new HSSFWorkbook() for generating `.xls` file

        /* CreationHelper helps us create instances of various things like DataFormat,
           Hyperlink, RichTextString etc, in a format (HSSF, XSSF) independent way */
        // CreationHelper createHelper = workbook.getCreationHelper();

        // Create a Sheet
        Sheet sheet = workBook.createSheet(selectedYear + " Roster");

        // Create a Font for styling header cells
        Font headerFont = workBook.createFont();
        headerFont.setBold(true);
        headerFont.setFontHeightInPoints((short) 12);
        headerFont.setColor(IndexedColors.BLACK.getIndex());

        // Create a CellStyle with the font
        CellStyle headerCellStyle = workBook.createCellStyle();
        headerCellStyle.setFont(headerFont);

        // Create a Row
        Row headerRow = sheet.createRow(0);

        // Create the header of the sheet
        for(int i = 0; i < headers.size(); i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers.get(i));
            cell.setCellStyle(headerCellStyle);
        }

        // prints the main body of information
        int rowNum = 1;
        for(MembershipListDTO m: rosters) {
            createRow(sheet,rowNum,m);
            rowNum++;
        }

		// makes the columns nice widths for the data
		for (int i = 0; i < headers.size(); i++) {
			sheet.autoSizeColumn(i);
		}

		File file = new SaveFileChooser(HalyardPaths.ROSTERS + "/",
				selectedYear + "_" + rosterType.replace(" ","_"),
				"Excel Files", "*.xlsx").getFile();

		if (file != null) {
			FileOutputStream fileOut = getFileOutPutStream(file);
			writeToWorkbook(workBook, fileOut);
			closeFileStream(fileOut);
			closeWorkBook(workBook);
		}

	}

	private void closeWorkBook(Workbook workBook) {
		try {
			workBook.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	//////////////////////////////  CLASS METHODS /////////////////////////////

	private void closeFileStream(FileOutputStream fileOut) {
		try {
			fileOut.close();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}

	private void writeToWorkbook(Workbook workbook, FileOutputStream fileOut) {
		try {
			workbook.write(fileOut);
		} catch (IOException e2) {
			// TODO Auto-generated catch block
			e2.printStackTrace();
		}
	}

	private FileOutputStream getFileOutPutStream(File file) {
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(file);
			System.out.println("Creating " + file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return fileOut;
	}

	private String getFileName(String rosterType) {
		HalyardPaths.checkPath(ROSTERS);
		String fileName = rosterType;
		System.out.println("Filename " + rosterType + " is selected");
		return fileName += " Roster.xlsx";
	}

	private Object getField(MembershipListDTO m, DbRosterSettingsDTO dto) {
		Object obj;
		try {
			System.out.println(dto.getGetter());
			Method method = m.getClass().getMethod(dto.getGetter());
			obj = method.invoke(m);
		} catch (NoSuchMethodException e) {
			throw new RuntimeException(e);
		} catch (InvocationTargetException e) {
			throw new RuntimeException(e);
		} catch (IllegalAccessException e) {
			throw new RuntimeException(e);
		}
		return obj;
	}

	private Row createRow(Sheet sheet, int rowNum, MembershipListDTO m) {
		Row row = sheet.createRow(rowNum);
		int cellNumber = 0;
		for(DbRosterSettingsDTO dto: rosterSettings) {
			if(dto.isExportable()) {
				Object result = getField(m, dto);
				if (result instanceof String) {
					String strResult = (String) result;
					row.createCell(cellNumber).setCellValue(strResult);
				} else if (result instanceof Integer) {
					int intResult = (int) result;
					row.createCell(cellNumber).setCellValue(intResult);
				}
				cellNumber++;
			}
		}
        return row;
	}

	private ArrayList<String> getHeaders() {
		ArrayList<String> headers = new ArrayList<>();
		for(DbRosterSettingsDTO dto: rosterSettings) {
			if (dto.isExportable()) {
				headers.add(dto.getName());
			}
		}
		return headers;
	}

	private String getPhone(int p_id) {
		String phoneString = "";
		ArrayList<PhoneDTO> phones = (ArrayList<PhoneDTO>) phoneRepo.getPhoneByPid(p_id);
		if (phones != null) {
			for (PhoneDTO p : phones) {
				if (p.getPhoneType().equals("C")) {  // we prefer a cell phone
					phoneString = p.getPhoneNumber();
					break;
				} else if (p.getPhoneType().contentEquals("H")) { // if home phone is all that is available we go with it
					phoneString = p.getPhoneNumber();
				}
			}
		}
		return phoneString;
	}

	private String getEmail(int p_id) {
		String emailString = "";
		ArrayList<EmailDTO> email = (ArrayList<EmailDTO>) emailRepo.getEmail(p_id);
		if (email != null) {
			for (EmailDTO e: email) {
				if(e.isPrimaryUse()) {
					emailString = e.getEmail();
					break;
				} else {
					emailString = e.getEmail();
				}

			}
		}
		return emailString;
	}

//	private String getSubleaser(MembershipListDTO owner) { // SqlMembership_Id.getId(owner.getSubleaser());
//		MembershipIdDTO membershipIdDTO;
//		if(owner.getSubleaser() != 0) {
//			membershipIdDTO = membershipIdRepo.getId(owner.getSubleaser());
//		}
//		return membershipIdDTO.getMs_;
//	}
}




















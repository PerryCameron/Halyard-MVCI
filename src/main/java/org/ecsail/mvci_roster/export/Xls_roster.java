package org.ecsail.mvci_roster.export;


import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.ecsail.dto.DbRosterSettingsDTO;
import org.ecsail.dto.MembershipListDTO;
import org.ecsail.interfaces.ConfigFilePaths;
import org.ecsail.mvci_roster.RosterModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;


public class Xls_roster implements ConfigFilePaths {

	private static final Logger logger = LoggerFactory.getLogger(Xls_roster.class);
	private final RosterModel rosterModel;

	public Xls_roster(RosterModel rosterModel) {
		this.rosterModel = rosterModel;
		ArrayList<String> headers = (ArrayList<String>) getHeaders();
		Integer selectedYear = 0;
		if(rosterModel.getRosters().size() > 1)
			selectedYear = rosterModel.getRosters().get(0).getSelectedYear();
		logger.info("Creating Roster..");
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
		createSheetHeader(headers, headerCellStyle, headerRow);
		// prints the main body of information
		createRows(sheet);
		// makes the columns nice widths for the data
		setProperColumnWithToMatchDataSize(headers, sheet);
		createFile(rosterModel, workBook);
	}

	private void createFile(RosterModel rosterModel, Workbook workBook) {
		if (rosterModel.getFileToSave() != null) {
			FileOutputStream fileOut = getFileOutPutStream(rosterModel.getFileToSave());
			writeToWorkbook(workBook, fileOut);
			closeFileStream(fileOut);
			closeWorkBook(workBook);
		}
	}

	private static void createSheetHeader(ArrayList<String> headers, CellStyle headerCellStyle, Row headerRow) {
		for(int i = 0; i < headers.size(); i++) {
			Cell cell = headerRow.createCell(i);
			cell.setCellValue(headers.get(i));
			cell.setCellStyle(headerCellStyle);
		}
	}

	private void createRows(Sheet sheet) {
		int initialRowNum = 1;
		IntStream.range(0, rosterModel.getRosters().size())
				.forEach(i -> createRow(sheet, initialRowNum + i, rosterModel.getRosters().get(i)));
	}

	private static void setProperColumnWithToMatchDataSize(ArrayList<String> headers, Sheet sheet) {
		IntStream.range(0, headers.size()).forEach(i -> sheet.autoSizeColumn(i));
	}

	private void closeWorkBook(Workbook workBook) {
		try {
			workBook.close();
		} catch (IOException e1) {
			logger.error(e1.getMessage());
			e1.printStackTrace();
		}
	}

	//////////////////////////////  CLASS METHODS /////////////////////////////

	private void closeFileStream(FileOutputStream fileOut) {
		try {
			fileOut.close();
		} catch (IOException e1) {
			logger.error(e1.getMessage());
			e1.printStackTrace();
		}
	}

	private void writeToWorkbook(Workbook workbook, FileOutputStream fileOut) {
		try {
			workbook.write(fileOut);
		} catch (IOException e2) {
			logger.error(e2.getMessage());
			e2.printStackTrace();
		}
	}

	private FileOutputStream getFileOutPutStream(File file) {
		FileOutputStream fileOut = null;
		try {
			fileOut = new FileOutputStream(file);
			logger.info("Creating " + file);
		} catch (FileNotFoundException e1) {
			logger.error(e1.getMessage());
			e1.printStackTrace();
		}
		return fileOut;
	}

//	private String getFileName(String rosterType) {
//		HalyardPaths.checkPath(ROSTERS);
//		String fileName = rosterType;
//		System.out.println("Filename " + rosterType + " is selected");
//		return fileName += " Roster.xlsx";
//	}

	private Object getField(MembershipListDTO m, DbRosterSettingsDTO dto) {
		Object obj;
		try {
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
		for(DbRosterSettingsDTO dto: rosterModel.getRosterSettings()) {
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

	private List<String> getHeaders() {
		return rosterModel.getRosterSettings().stream()
				.filter(DbRosterSettingsDTO::isExportable)
				.map(DbRosterSettingsDTO::getName)
				.collect(Collectors.toList());
	}

//	private String getPhone(int p_id) {
//		String phoneString = "";
//		ArrayList<PhoneDTO> phones = (ArrayList<PhoneDTO>) phoneRepo.getPhoneByPid(p_id);
//		if (phones != null) {
//			for (PhoneDTO p : phones) {
//				if (p.getPhoneType().equals("C")) {  // we prefer a cell phone
//					phoneString = p.getPhoneNumber();
//					break;
//				} else if (p.getPhoneType().contentEquals("H")) { // if home phone is all that is available we go with it
//					phoneString = p.getPhoneNumber();
//				}
//			}
//		}
//		return phoneString;
//	}

//	private String getEmail(int p_id) {
//		String emailString = "";
//		ArrayList<EmailDTO> email = (ArrayList<EmailDTO>) emailRepo.getEmail(p_id);
//		if (email != null) {
//			for (EmailDTO e: email) {
//				if(e.isPrimaryUse()) {
//					emailString = e.getEmail();
//					break;
//				} else {
//					emailString = e.getEmail();
//				}
//
//			}
//		}
//		return emailString;
//	}

//	private String getSubleaser(MembershipListDTO owner) { // SqlMembership_Id.getId(owner.getSubleaser());
//		MembershipIdDTO membershipIdDTO;
//		if(owner.getSubleaser() != 0) {
//			membershipIdDTO = membershipIdRepo.getId(owner.getSubleaser());
//		}
//		return membershipIdDTO.getMs_;
//	}
}




















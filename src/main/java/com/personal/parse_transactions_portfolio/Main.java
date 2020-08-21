package com.personal.parse_transactions_portfolio;

import java.io.*;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.Date;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Scanner;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import org.apache.poi.hssf.usermodel.HSSFDateUtil;



public class Main {

	private static String[] portfolioHeader = {"Index Portefeuille", "Date", "Souscription/Rachat Prix Unitaire", "Souscription/Rachat Prix Total Gross", "Souscription/Rachat Prix Total Net", "Frais Operationnels Prix Unitaire", "Frais Operationnels Prix Total Gross", "Frais Operationnels Prix Total Net","Frais de Gestion Prix Unitaire", " Frais de Gestion Prix Total Gross", "Frais de Gestion Prix Total Net"};
	//private static String[] securitiesHeader = {"Bloomberg Global", "Isin", "Devise", "Date","Type", "OrderId", "Side", "Statut", "Quantite", "Prix en Devise", "Valeur Unitaire Gross en EUR", "Valeur Totale Gross en EUR", "Valeur Totale Net en EUR"};
	
	public static void main(String[] args) {
		Scanner scanner = new Scanner(System.in);
		System.out.println("Prendre comme example l'onglet \"copie en dur\" du fichier :");
		System.out.println("Entrez path/filename du fichier à extraire :");
		String filename = scanner.nextLine();
		
		System.out.println("Entrez path/filename du fichier de destination :");
		String exportFilename = scanner.nextLine();
		
		//System.out.println("Souhitez-vous extraire les transactions? oui/non");
		//boolean parse_transactions = scanner.nextLine().contentEquals("oui");
		scanner.close();
		
		File xlsx = new File(filename);
		try(FileInputStream is = new FileInputStream(xlsx);
				XSSFWorkbook wb = new XSSFWorkbook(is);) {

			XSSFSheet sheet = wb.getSheetAt(1);
			ArrayList<RowBlock> rows = splitTransactionSheet(sheet);
			rows.remove(0);
			
			Portfolio portfolio = sortRows(rows);
			
			printExcelFile(exportFilename, portfolio);
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}




	/**
	 * 
	 * @param sheet
	 * @return
	 */
	private static ArrayList<RowBlock> splitTransactionSheet(XSSFSheet sheet) {
		ArrayList<RowBlock> rows = new ArrayList<RowBlock>();

		Iterator<Row> rowIterator = sheet.iterator();

		// skip functor line
		//Row currentRow = rowIterator.next();
		//skip explanation row
		//currentRow = rowIterator.next();

		while(rowIterator.hasNext()) {
			RowBlock rowBlocks = new RowBlock();
			Row currentRow = rowIterator.next();
			Iterator<Cell> cellIterator = currentRow.cellIterator();
			while(cellIterator.hasNext()) {
				String cellValue = null;
				Cell currentCell = cellIterator.next();
				if(currentCell.getCellType().equals(CellType.NUMERIC)) {
					
					if (HSSFDateUtil.isCellDateFormatted(currentCell)) {
			           SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			           cellValue = dateFormat.format(currentCell.getDateCellValue());
			        } else {
			        	double d = currentCell.getNumericCellValue();
			        	cellValue = Double.toString(d);
			        }
				} else if(currentCell.getCellType().equals(CellType.STRING)) {
					cellValue = currentCell.getStringCellValue();
				}

				rowBlocks.addRowElement(cellValue);
			}
			rows.add(rowBlocks);
		}
		return rows;
	}
	
	private static Portfolio sortRows(ArrayList<RowBlock> rows) throws ParseException  {
		int portfolioIndex = Integer.parseInt(rows.get(0).getRowElement(0));
		Portfolio portfolio = new Portfolio(portfolioIndex);
		PortfolioDaily currentDay = new PortfolioDaily();
		portfolio.addPortfolioDay(currentDay);
		
		for(RowBlock currentRow : rows) {
			/*if(currentRow.getRowElement(3).matches("TRANSACTION(_SHORT)?")) {
				SecurityFee securityFee = parseSecurityFee(currentRow);
				addSecurityOrder(portfolio, securityFee, 0);
			} else if(currentRow.getRowElement(3).matches("PROVISION_COUPON_DIVIDEND") && currentRow.getRowElement(4).matches("Entr.e")) {
				SecurityFee securityFee = parseSecurityFee(currentRow);
				addSecurityOrder(portfolio, securityFee, 1);
			} else if(currentRow.getRowElement(3).matches("MARGIN_CALL_COMPENSATION")) {
				SecurityFee securityFee = parseSecurityFee(currentRow);
				addSecurityOrder(portfolio, securityFee, 2);
				* not yet treat different currency 
				 * because transactions are the only considered orders not always in EUR
				 *
			} else */ if(currentRow.getRowElement(3).matches("CASH(_WITHDRAWALSEPA)?")) {
				PortfolioFee portfolioFee = parsePortfolioOrder(currentRow);
				addOrderPortfolio(portfolio, portfolioFee, 0);
			} else if(currentRow.getRowElement(3).matches("PROVISION_ADMIN_FEES") && currentRow.getRowElement(4).matches("Entr.e")) {
				PortfolioFee portfolioFee = parsePortfolioOrder(currentRow);
				addOrderPortfolio(portfolio, portfolioFee, 1);
			} else if(currentRow.getRowElement(3).matches("PROVISION_MANAGEMENT_.*") && currentRow.getRowElement(4).matches("Entr.e")) {
				PortfolioFee portfolioFee = parsePortfolioOrder(currentRow);
				addOrderPortfolio(portfolio, portfolioFee, 2);
			} else if(currentRow.getRowElement(3).matches("PROVISION_PERFORMANCE_.*") && currentRow.getRowElement(4).matches("Entr.e")) {
				PortfolioFee portfolioFee = parsePortfolioOrder(currentRow);
				addOrderPortfolio(portfolio, portfolioFee, 2);
			} else if(currentRow.getRowElement(3).matches("PROVISION_ACQUIRED_PERFORMANCE_.*") && currentRow.getRowElement(4).matches("Entr.e")) {
				PortfolioFee portfolioFee = parsePortfolioOrder(currentRow);
				addOrderPortfolio(portfolio, portfolioFee, 2);
			}
		}
		
		return portfolio;
	}


	private static SecurityFee parseSecurityFee(RowBlock currentRow) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date currentDate = dateFormat.parse(currentRow.getRowElement(1));
		
		double quantity = Double.parseDouble(currentRow.getRowElement(6));
		double feesInCurr = Double.parseDouble(currentRow.getRowElement(8));
		double unitPriceGrossFeesCurr = Double.parseDouble(currentRow.getRowElement(10));
		double totalValGrossFeesEur = Double.parseDouble(currentRow.getRowElement(12));
		double totalValNetEur = Double.parseDouble(currentRow.getRowElement(14)); 
		
		SecurityFee securityFee = new SecurityFee(currentDate);
		securityFee.setOrderId(currentRow.getRowElement(2));
		securityFee.setSide(currentRow.getRowElement(4));
		securityFee.setStatus(currentRow.getRowElement(5));
		securityFee.setQuantity(quantity);
		securityFee.setSecCurr(currentRow.getRowElement(7));
		securityFee.setPricesInCurr(feesInCurr);
		securityFee.setUnitGrossVal(unitPriceGrossFeesCurr);
		securityFee.setTotalGrossVal(totalValGrossFeesEur);
		securityFee.setTotalNetVal(totalValNetEur);
		//securityFee.setIsin(currentRow.getRowElement(16));
		//securityFee.setBbGlobal(currentRow.getRowElement(17));
		
		return securityFee;
	}


	private static void addSecurityOrder(Portfolio portfolio, SecurityFee securityFee, int index) {
		String bbGlobal = securityFee.getBbGlobal();
		String isin = securityFee.getIsin();
		String currency = securityFee.getSecCurr();
		Security security = new Security(bbGlobal, isin, currency);
		
		SecurityDaily currentDay = null;
		if(!(portfolio.getSecurities().contains(security))) {
			currentDay = new SecurityDaily(securityFee.getDate());
			security.addSecurityDaily(currentDay);
			portfolio.addSecurity(security);
		} else {
			Security currSecurity = findSecurity(portfolio, security);
			
			currentDay = currSecurity.getSecurityDays().get(currSecurity.getSecurityDays().size()-1);
			Date currentDate = securityFee.getDate();
			
			if(!(currentDay.getDate().equals(currentDate))) {
				currentDay = new SecurityDaily(currentDate);
				currSecurity.addSecurityDaily(currentDay);
				//currentDay = currentDay;
			}
			
		}
		switch(index) {
		case 0:
			securityFee.setType("Transaction");
			currentDay.addTransaction(securityFee);
			break;
		case 1:
			securityFee.setType("Income");
			currentDay.addIncomeFee(securityFee);
			break;
		case 2:
			securityFee.setType("Coverage");
			currentDay.addCoverageFee(securityFee);
			break;
		}
		return;
	}


	private static void addOrderPortfolio(Portfolio portfolio, PortfolioFee portfolioFee, int index) throws ParseException {
		PortfolioDaily currentDay = portfolio.getPortfolioDays().get(portfolio.getPortfolioDays().size()-1);
		Date currentDate = portfolioFee.getDate();
		
		if(currentDay.getDate() == null) {
			currentDay.setDate(currentDate);
		} else if(!(currentDay.getDate().equals(currentDate))) {
			PortfolioDaily newDay = new PortfolioDaily();
			newDay.setDate(currentDate);
			portfolio.addPortfolioDay(newDay);
			currentDay = newDay;
		}

		switch(index) {
		case 0: 
			currentDay.addSubscriptionFee(portfolioFee);
			break;
		case 1:
			currentDay.addOperationalFee(portfolioFee);
			break;
		case 2:
			currentDay.addManagementFee(portfolioFee);
			break;
		}
		return;
	}



	private static PortfolioFee parsePortfolioOrder(RowBlock currentRow) throws ParseException {
		SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
		Date date = dateFormat.parse(currentRow.getRowElement(1));
		
		double unitPrice = Double.parseDouble(currentRow.getRowElement(10)); 
		double totalGrossValue = Double.parseDouble(currentRow.getRowElement(12)); 
		double totalNetValue = Double.parseDouble(currentRow.getRowElement(14)); 
		PortfolioFee portfolioFee = new PortfolioFee(date, unitPrice, totalGrossValue, totalNetValue);
		return portfolioFee;
	}

	
	private static Security findSecurity(Portfolio portfolio, Security security) {
		for(Security currentSec : portfolio.getSecurities()) {
			if(security.equals(currentSec))
				return currentSec;
		}
		return null;
	}
	
	
	
	
	private static void printExcelFile(String filename, Portfolio portfolio) {
		
		try(FileOutputStream fileOut = new FileOutputStream(new File(filename));
				Workbook wb = new XSSFWorkbook();) {

			Sheet portfolioSheet = wb.createSheet("PortfolioFees");
			//Sheet securitiesSheet = wb.createSheet("SecurityTransactions");
			
			
			// PRINT PORTFOLIO
			CreationHelper creationHelper = wb.getCreationHelper();
			// initialize portfolio sheet header row
			Row portfolioHeaderRow = portfolioSheet.createRow(0);
			
			for(int i = 0; i < portfolioHeader.length; i++) {
				Cell cell = portfolioHeaderRow.createCell(i);
				cell.setCellValue(portfolioHeader[i]);
			}
			
			int i=1;
			for(PortfolioDaily currentDay : portfolio.getPortfolioDays()) {
				Row currentRow = portfolioSheet.createRow(i);
				
				currentRow.createCell(0).setCellValue(portfolio.getIndex());
				Cell dateCell = currentRow.createCell(1);
				dateCell.setCellValue(currentDay.getDate());
				CellStyle style = wb.createCellStyle();
				style.setDataFormat(creationHelper.createDataFormat().getFormat("dd/mm/yyyy"));
				dateCell.setCellStyle(style);
				
				currentRow.createCell(2).setCellValue(currentDay.getSubscriptionFees().getUnitGrossFee());
				currentRow.createCell(3).setCellValue(currentDay.getSubscriptionFees().getTotalGrossFee());
				currentRow.createCell(4).setCellValue(currentDay.getSubscriptionFees().getTotalNetFee());
				currentRow.createCell(5).setCellValue(currentDay.getOperationalFees().getUnitGrossFee());
				currentRow.createCell(6).setCellValue(currentDay.getOperationalFees().getTotalGrossFee());
				currentRow.createCell(7).setCellValue(currentDay.getOperationalFees().getTotalNetFee());
				currentRow.createCell(8).setCellValue(currentDay.getManagementFees().getUnitGrossFee());
				currentRow.createCell(9).setCellValue(currentDay.getManagementFees().getTotalGrossFee());
				currentRow.createCell(10).setCellValue(currentDay.getManagementFees().getTotalNetFee());
				i++;
			}

			//portfolioSheet = writePortfolio(portfolio, wb, portfolioSheet);
			creationHelper = wb.getCreationHelper();
			CellStyle dateStyle = wb.createCellStyle();
			dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/mm/yyyy"));
			
			// initialize Securities sheet header row
			/*Row securitiesHeaderRow = securitiesSheet.createRow(0);
			
			for(i = 0; i < securitiesHeader.length; i++) {
				Cell cell = securitiesHeaderRow.createCell(i);
				cell.setCellValue(securitiesHeader[i]);
			}
			
			//securitiesSheet = writeSecurities(portfolio, wb, securitiesSheet);
			
			dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/mm/yyyy"));
			
			// initialize Securities sheet header row
			securitiesHeaderRow = securitiesSheet.createRow(0);
			
			for(i = 0; i < securitiesHeader.length; i++) {
				Cell cell = securitiesHeaderRow.createCell(i);
				cell.setCellValue(securitiesHeader[i]);
			}

			i = 1;
			for(Security currentSecurity : portfolio.getSecurities()) {
				for(SecurityDaily currentDay : currentSecurity.getSecurityDays()) {

					if(currentDay.getTransaction() != null) {

						Row currentRow = securitiesSheet.createRow(i);
						SecurityFee order = currentDay.getTransaction();

						currentRow.createCell(0).setCellValue(currentSecurity.getBbGlobal());
						currentRow.createCell(1).setCellValue(currentSecurity.getIsin());
						currentRow.createCell(2).setCellValue(currentSecurity.getCurr());

						Cell dateCell = currentRow.createCell(3);
						dateCell.setCellValue(order.getDate());
						dateCell.setCellStyle(dateStyle);

						//"Type", "OrderId", "Side", "Status", "Quantity", "Price In Currency", "Unit Gross Value in EUR", "Total Gross Value in EUR", "Total Net Value in EUR"
						currentRow.createCell(4).setCellValue(order.getType());
						currentRow.createCell(5).setCellValue(order.getOrderId());
						currentRow.createCell(6).setCellValue(order.getSide());
						currentRow.createCell(7).setCellValue(order.getStatus());
						currentRow.createCell(8).setCellValue(order.getQuantity());
						currentRow.createCell(9).setCellValue(order.getPricesInCurr());
						currentRow.createCell(10).setCellValue(order.getUnitGrossVal());
						currentRow.createCell(11).setCellValue(order.getTotalGrossVal());
						currentRow.createCell(12).setCellValue(order.getTotalNetVal());

						i++;
					}
					if(currentDay.getIncomeFee() != null) {

						Row currentRow = securitiesSheet.createRow(i);
						SecurityFee order = currentDay.getIncomeFee();

						currentRow.createCell(0).setCellValue(currentSecurity.getBbGlobal());
						currentRow.createCell(1).setCellValue(currentSecurity.getIsin());
						currentRow.createCell(2).setCellValue(currentSecurity.getCurr());

						Cell dateCell = currentRow.createCell(3);
						dateCell.setCellValue(order.getDate());
						dateCell.setCellStyle(dateStyle);

						//"Type", "OrderId", "Side", "Status", "Quantity", "Price In Currency", "Unit Gross Value in EUR", "Total Gross Value in EUR", "Total Net Value in EUR"
						currentRow.createCell(4).setCellValue(order.getType());
						currentRow.createCell(5).setCellValue(order.getOrderId());
						currentRow.createCell(6).setCellValue(order.getSide());
						currentRow.createCell(7).setCellValue(order.getStatus());
						currentRow.createCell(8).setCellValue(order.getQuantity());
						currentRow.createCell(9).setCellValue(order.getPricesInCurr());
						currentRow.createCell(10).setCellValue(order.getUnitGrossVal());
						currentRow.createCell(11).setCellValue(order.getTotalGrossVal());
						currentRow.createCell(12).setCellValue(order.getTotalNetVal());
						i++;
					}
					
					if(currentDay.getCoverageFee() != null) {
						Row currentRow = securitiesSheet.createRow(i);
						SecurityFee order = currentDay.getTransaction();

						currentRow.createCell(0).setCellValue(currentSecurity.getBbGlobal());
						currentRow.createCell(1).setCellValue(currentSecurity.getIsin());
						currentRow.createCell(2).setCellValue(currentSecurity.getCurr());

						Cell dateCell = currentRow.createCell(3);
						dateCell.setCellValue(order.getDate());
						dateCell.setCellStyle(dateStyle);

						//"Type", "OrderId", "Side", "Status", "Quantity", "Price In Currency", "Unit Gross Value in EUR", "Total Gross Value in EUR", "Total Net Value in EUR"
						currentRow.createCell(4).setCellValue(order.getType());
						currentRow.createCell(5).setCellValue(order.getOrderId());
						currentRow.createCell(6).setCellValue(order.getSide());
						currentRow.createCell(7).setCellValue(order.getStatus());
						currentRow.createCell(8).setCellValue(order.getQuantity());
						currentRow.createCell(9).setCellValue(order.getPricesInCurr());
						currentRow.createCell(10).setCellValue(order.getUnitGrossVal());
						currentRow.createCell(11).setCellValue(order.getTotalGrossVal());
						currentRow.createCell(12).setCellValue(order.getTotalNetVal());
						i++;
					}

				}
			}*/
			
	        wb.write(fileOut);

		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}




	/*private static Sheet writeSecurities(Portfolio portfolio, Workbook wb, Sheet securitiesSheet) {
		CreationHelper creationHelper = wb.getCreationHelper();
		CellStyle dateStyle = wb.createCellStyle();
		dateStyle.setDataFormat(creationHelper.createDataFormat().getFormat("dd/mm/yyyy"));
		
		// initialize Securities sheet header row
		Row securitiesHeaderRow = securitiesSheet.createRow(0);
		
		for(int i = 0; i < securitiesHeader.length; i++) {
			Cell cell = securitiesHeaderRow.createCell(i);
			cell.setCellValue(securitiesHeader[i]);
		}

		int i = 1;
		for(Security currentSecurity : portfolio.getSecurities()) {
			for(SecurityDaily currentDay : currentSecurity.getSecurityDays()) {

				if(currentDay.getTransaction() != null) {
				Row currentRow = securitiesSheet.createRow(i);
				currentRow = printFee(portfolio, dateStyle, currentSecurity, currentDay.getTransaction(), currentRow);
				i++;
				}
				if(currentDay.getIncomeFee() != null) {
				Row currentRow = securitiesSheet.createRow(i);
				currentRow = printFee(portfolio, dateStyle, currentSecurity, currentDay.getIncomeFee(), currentRow);
				i++;
				}
				if(currentDay.getCoverageFee() != null) {
				Row currentRow = securitiesSheet.createRow(i);
				currentRow = printFee(portfolio, dateStyle, currentSecurity, currentDay.getCoverageFee(), currentRow);
				i++;
				}

			}
		}
		return securitiesSheet;
	}*/




	private static Row printFee(Portfolio portfolio, CellStyle dateStyle, Security currentSecurity, SecurityFee order, Row currentRow) {
		
		currentRow.createCell(0).setCellValue(currentSecurity.getBbGlobal());
		currentRow.createCell(1).setCellValue(currentSecurity.getIsin());
		currentRow.createCell(2).setCellValue(currentSecurity.getCurr());

		Cell dateCell = currentRow.createCell(4);
		dateCell.setCellValue(order.getDate());
		dateCell.setCellStyle(dateStyle);
		
		//"Type", "OrderId", "Side", "Status", "Quantity", "Price In Currency", "Unit Gross Value in EUR", "Total Gross Value in EUR", "Total Net Value in EUR"
		currentRow.createCell(5).setCellValue(order.getType());
		currentRow.createCell(6).setCellValue(order.getOrderId());
		currentRow.createCell(7).setCellValue(order.getSide());
		currentRow.createCell(8).setCellValue(order.getStatus());
		currentRow.createCell(9).setCellValue(order.getQuantity());
		currentRow.createCell(10).setCellValue(order.getPricesInCurr());
		currentRow.createCell(11).setCellValue(order.getUnitGrossVal());
		currentRow.createCell(12).setCellValue(order.getTotalGrossVal());
		currentRow.createCell(13).setCellValue(order.getTotalNetVal());
		
		return currentRow;
	}




	/*private static Sheet writePortfolio(Portfolio portfolio, Workbook wb, Sheet portfolioSheet) {

		CreationHelper creationHelper = wb.getCreationHelper();
		// initialize portfolio sheet header row
		Row portfolioHeaderRow = portfolioSheet.createRow(0);
		
		for(int i = 0; i < portfolioHeader.length; i++) {
			Cell cell = portfolioHeaderRow.createCell(i);
			cell.setCellValue(portfolioHeader[i]);
		}
		
		int i=1;
		for(PortfolioDaily currentDay : portfolio.getPortfolioDays()) {
			Row currentRow = portfolioSheet.createRow(i);
			
			currentRow.createCell(0).setCellValue(portfolio.getIndex());
			Cell dateCell = currentRow.createCell(1);
			dateCell.setCellValue(currentDay.getDate());
			CellStyle style = wb.createCellStyle();
			style.setDataFormat(creationHelper.createDataFormat().getFormat("dd/mm/yyyy"));
			dateCell.setCellStyle(style);
			
			currentRow.createCell(2).setCellValue(currentDay.getSubscriptionFees().getUnitGrossFee());
			currentRow.createCell(3).setCellValue(currentDay.getSubscriptionFees().getTotalGrossFee());
			currentRow.createCell(4).setCellValue(currentDay.getSubscriptionFees().getTotalNetFee());
			currentRow.createCell(5).setCellValue(currentDay.getOperationalFees().getUnitGrossFee());
			currentRow.createCell(6).setCellValue(currentDay.getOperationalFees().getTotalGrossFee());
			currentRow.createCell(7).setCellValue(currentDay.getOperationalFees().getTotalNetFee());
			currentRow.createCell(8).setCellValue(currentDay.getManagementFees().getUnitGrossFee());
			currentRow.createCell(9).setCellValue(currentDay.getManagementFees().getTotalGrossFee());
			currentRow.createCell(10).setCellValue(currentDay.getManagementFees().getTotalNetFee());
			i++;
		}
		return portfolioSheet;
	}*/
	
	
}
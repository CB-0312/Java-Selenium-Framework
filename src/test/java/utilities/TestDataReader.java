package utilities;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Set;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;

public class TestDataReader {

	/*
	 * This Function will take a primary key if the condition is satisfied then the
	 * test data is mapped as hashmap. First row will be the keys Row matching the
	 * Primary key will be values we can merge the test data from multiple sheets
	 */
	public static Object[][] readTestData(String primaryKey, String sheetName, String testDataFile) throws IOException {
		HashMap<String, String> testData = new HashMap<String, String>();
		File f = new File(testDataFile);
		FileInputStream fis = new FileInputStream(f);
		Workbook wb = WorkbookFactory.create(fis);
		Sheet sheet = wb.getSheet(sheetName);

		int totalRows = sheet.getLastRowNum();
		int totalColumns = sheet.getRow(0).getLastCellNum();

		DataFormatter formate = new DataFormatter();

		for (int i = 1; i <= totalRows; i++) {
			for (int j = 0; j < totalColumns; j++) {
				if (formate.formatCellValue(sheet.getRow(i).getCell(0)).equals(primaryKey)) {
					testData.put(formate.formatCellValue(sheet.getRow(0).getCell(j)),
							formate.formatCellValue(sheet.getRow(i).getCell(j)));
				}
			}
		}

		Object[][] arr = testData.entrySet().stream().map(e -> new Object[] { e.getKey(), e.getValue() })
				.toArray(Object[][]::new);
		return arr;
	}

	public static Object[][] getAllData(Object... objects) {
		int rows = 1;
		Object[][] finalObject = new Object[rows][1];
		for (int j = 0; j < rows; j++) {
			Hashtable<String, String> finalTable = new Hashtable<String, String>();
			for (int i = 0; i < objects.length; i++) {
				Object[][] temp = (Object[][]) objects[i];
				if (j != temp.length) {
					try {
						for (Object[] row : temp) {
							if (row.length == 2) {
								finalTable.put((String) row[0], (String) row[1]);
							}
						}
					} catch (Exception ex) {
						// Not mandatory for all objects to contain more than one row
					}
				}
			}
			finalObject[j][0] = finalTable;
		}
		return finalObject;
	}

	public static Object[][] getAllData1(Object... objects) {
		int rows = ((Object[][]) objects[0]).length;
		Object[][] finalObject = new Object[rows][1];
		for (int j = 0; j < rows; j++) {
			Hashtable<String, String> finalTable = new Hashtable<String, String>();
			for (int i = 0; i < objects.length; i++) {
				Object[][] temp = (Object[][]) objects[i];
				if (j != temp.length) {
					try {
						Hashtable<String, String> table = (Hashtable) temp[j][0];
						Set<String> keys = table.keySet();
						for (String key : keys) {
							String value = table.get(key);
							finalTable.put(key, value);
						}
					} catch (Exception ex) {
						ex.printStackTrace();
						// Not mandatory for all objects to contain more than one row
					}
				}
			}
			finalObject[j][0] = finalTable;
		}
		return finalObject;
	}

	/*to merge data wrt policy numbers in test data*/
	public static synchronized Object[][] mergeData(Object[][] obj1, Object[][] obj2){
		Object[][] finalObject = new Object[obj1.length][1];
		for(int counter = 0; counter<obj1.length; counter++){
			Hashtable<String, String> table1 = (Hashtable)obj1[counter][0];
			String policyNumber = table1.get("Policy_Number_Txt");
			Hashtable<String, String> table2 = (Hashtable)findCorrespondingTable(obj2, policyNumber);
			Set<String> keys = table2.keySet();
			for(String key: keys){
				String value = table2.get(key);
				table1.put(key, value);
			}
			finalObject[counter][0] = table1;
		}
		return finalObject;
	}

	public static synchronized Hashtable<String, String> findCorrespondingTable(Object[][] obj, String policyNumber){
		for(int counter = 0; counter<obj.length; counter++){
			Hashtable<String, String> table1 = (Hashtable)obj[counter][0];
			if(policyNumber.equalsIgnoreCase(table1.get("Policy_Number_Txt"))){
				return table1;
			}
		}
		return new Hashtable<String, String>();
	}
}

package com.bezkoder.spring.jpa.postgresql.service;


	

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

@Service
public class ExcelService {

	
	  
	  @Autowired
	    private ObjectMapper objectMapper;


    
    public String convertExcelToJson(MultipartFile file) throws IOException {
        Workbook workbook = WorkbookFactory.create(file.getInputStream());
      Sheet sheet = workbook.getSheetAt(0);
      JSONObject result = new JSONObject();
      List<String> excelData = new ArrayList<>();
      Row headerRow = sheet.getRow(0);

      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
          Row row = sheet.getRow(i);
          if (row == null || isRowEmpty(row)) {
              continue;
          }
          Map<String, String> rowData = new HashMap<>();

          for (int j = 0; j < row.getLastCellNum(); j++) {
              Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
              String header = headerRow.getCell(j).getStringCellValue();
              String cellValue = cell.toString();
              if (header.equals("Property Address")) {
                  cellValue = cellValue.replace("\"", "");
                  cellValue = cellValue.replace("'", "");
              }
              if (cellValue.trim().isEmpty()) {
                  cellValue = "null";
              }
              rowData.put(header, cellValue);
          }

          // Convert row data to JSON string
          String json_row = objectMapper.writeValueAsString(rowData);
          excelData.add(json_row);
        }
      JSONArray jsonArray = new JSONArray(excelData.toString());
   
      result.put("data", jsonArray);
        workbook.close();
        return result.toString();
    }
    
    
    private boolean isRowEmpty(Row row) {
        for (int j = 0; j < row.getLastCellNum(); j++) {
            Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
            if (cell != null && cell.getCellType() != CellType.BLANK && !cell.toString().trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }
    
    
//    private void logProcess( String fileName, String status, String message) throws SQLException {
//        String logSql = "INSERT INTO log_table (filename, status, message) VALUES (?, ?, ?)";
//        try (PreparedStatement logStmt = connection.prepareStatement(logSql)) {
//            logStmt.setString(1, fileName);
//            logStmt.setString(2, status);
//            logStmt.setString(3, message);
//            logStmt.executeUpdate();
//        }
//    }
    
//  public String convertMultipleExcelFilesToJson(MultipartFile[] files) throws IOException {
//  List<String> jsonResults = new ArrayList<>();
//
//  for (MultipartFile file : files) {
//      Workbook workbook = WorkbookFactory.create(file.getInputStream());
//      Sheet sheet = workbook.getSheetAt(0);
//
//      List<String> excelData = new ArrayList<>();
//      Row headerRow = sheet.getRow(0);
//
//      for (int i = 1; i <= sheet.getLastRowNum(); i++) {
//          Row row = sheet.getRow(i);
//          Map<String, String> rowData = new HashMap<>();
//
//          for (int j = 0; j < row.getLastCellNum(); j++) {
//              Cell cell = row.getCell(j, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
//              String header = headerRow.getCell(j).getStringCellValue();
//              String cellValue = cell.toString();
//              if (header.equals("Property Address")) {
//                  cellValue = cellValue.replace("\"", "");
//                  cellValue = cellValue.replace("'", "");
//              }
//              if (cellValue.trim().isEmpty()) {
//                  cellValue = "null";
//              }
//              rowData.put(header, cellValue);
//          }
//
//          // Convert row data to JSON string
//          String json_row = objectMapper.writeValueAsString(rowData);
//          excelData.add(json_row);
//         
//      }
//      System.out.println("excel-------------------------------"+excelData);
//      // Convert the list of row JSON strings to a single JSON string
////      String jsonResult = excelData;
////      jsonResults.add(jsonResult);
//      jsonResults.addAll(excelData);
////  }	
//  System.out.println("result-----------------------------"+jsonResults);
//  return jsonResults.toString();
//}

//public void saveJsonData(String jsonData) {
//  String sql = "CALL insert_excel_data_from_json(?)";
//  jdbcTemplate.update(sql, jsonData);
//}

}




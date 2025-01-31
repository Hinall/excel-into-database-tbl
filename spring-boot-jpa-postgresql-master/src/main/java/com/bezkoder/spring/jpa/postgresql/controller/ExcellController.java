package com.bezkoder.spring.jpa.postgresql.controller;


import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.bezkoder.spring.jpa.postgresql.service.ExcelService;
import com.bezkoder.spring.jpa.postgresql.service.SPUtility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.support.JdbcDaoSupport;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

@RestController
@RequestMapping("/api")
public class ExcellController {
	  
		
	 

	   @Autowired
	    private JdbcTemplate jdbcTemplate;

	    @Autowired
	    private ObjectMapper objectMapper;

	    @Autowired
	    private ExcelService excelService;
    
    @GetMapping("/test")
    public String test() {
       return "test";
    }
    
    
    @PostMapping("/upload")
    public ResponseEntity<String> uploadFiles(@RequestParam("files") MultipartFile[] files) {
    	  
        try {
            for (MultipartFile file : files) {
                try {
                	 JSONObject logData = new JSONObject();  
                    String file_name = file.getOriginalFilename();
                   
                   
                    String jsonData = excelService.convertExcelToJson(file);
                    String insertResult = jdbcTemplate.queryForObject(SPUtility.INSERT_EXCEL, new Object[]{jsonData}, String.class);

                    JSONObject insertResultObj = new JSONObject(insertResult);
                    

                    String fileName = file.getOriginalFilename();

                    String message = insertResultObj.getString("responseMessage");
                    int status = insertResultObj.getInt("responseCode");

                                   
                    logData.put("filename", fileName);
                    logData.put("status", status==200 ? "true" : "false");
                    logData.put("message", message);
                    String str_logdata = logData.toString();
                    
                    String result = jdbcTemplate.queryForObject(SPUtility.INSERT_LOG, new Object[]{str_logdata}, String.class);
                    System.out.println(fileName);
                  
                

                } catch (Exception e) {
                    e.printStackTrace();
                    return ResponseEntity.status(500).body("Error processing file: " + file.getOriginalFilename());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to process files");
        }

        return ResponseEntity.ok("Files processed successfully");
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    

//    @PostMapping("/uploadMultiple")
//    public String uploadMultipleFiles(@RequestParam("files") MultipartFile[] files) {
//        try {
//            String jsonResults = excelService.convertMultipleExcelFilesToJson(files);
//            return jsonResults;
//        } catch (Exception e) {
//            //return ResponseEntity.status(500).body(null);
//        }
//		return null;
//    }

    
    
    
    
    
    
    
    
    
    
    
    
//    @PostMapping("/upload")
//    public ResponseEntity<String> uploadFiles(@RequestParam("files") List<MultipartFile> files) {
//        for (MultipartFile file : files) {
//            try {
//                String jsonData = excelService.convertExcelToJson((File) file);
//                
//                 String insert_result = jdbcTemplate.queryForObject(SPUtility.INSERT_EXCEL, new Object[] { jsonData },String.class);
//                 JSONObject jsonObject = new JSONObject(insert_result);
//               if (jsonObject.data.responseCode==200) {
//            	   String filename=file.getOriginalFilename(); 
//            	   String message=insert_result.data;
//            	   String logdata= {"filename":filename,"status":true,"message":message};
//            	   String result = jdbcTemplate.queryForObject(SPUtility.INSERT_LOG, new Object[] {logdata },String.class);
//               }
//               else {
//            	   String filename=file.getOriginalFilename(); 
//            	   String message=insert_result.data;
//            	   STring logdata= {"filename":filename,"status":false,"message":message};
//            	   String result = jdbcTemplate.queryForObject(SPUtility.INSERT_LOG, new Object[] {logdata },String.class);
//               }
//                 
//               
//                
//            } catch (IOException | SQLException e) {
//                e.printStackTrace();
//                return ResponseEntity.status(500).body("Error processing file: " + file.getOriginalFilename());
//            }
//        }
//        return ResponseEntity.ok("Files processed successfully");
//    }
//    
//    
 
}

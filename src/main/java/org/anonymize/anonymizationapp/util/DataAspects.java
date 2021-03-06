package org.anonymize.anonymizationapp.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.deidentifier.arx.Data;
import org.deidentifier.arx.DataHandle;
import org.deidentifier.arx.DataSource;
import org.anonymize.anonymizationapp.model.AnonymizationBase;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.deidentifier.arx.AttributeType.Hierarchy;
import org.deidentifier.arx.Data.DefaultData;
import org.deidentifier.arx.aggregates.HierarchyBuilderRedactionBased;
import org.deidentifier.arx.aggregates.HierarchyBuilderRedactionBased.Order;
import org.deidentifier.arx.io.CSVHierarchyInput;
import org.springframework.stereotype.Component;

//component used to create the data object upon call with the dataset name and the extension
@Component
public class DataAspects{
	//to create the data and hierarchies in one step	
	public Data createDataAndHierarchies(final String empOrg, final String fileName, final List<String> hierNames) throws IOException {

		String[] dataNameAndExtension = fileName.split("\\.");
		String dataset = dataNameAndExtension[0];//format is [dataset name].[extension]
		String extension = dataNameAndExtension[1];
		
		System.out.println("Set name is: " + dataset);
		System.out.println("Extension is: " + extension);
		
		 Data data = DefaultData.create();//create empty object with full scope to satisfy errors
	       if(extension.equals("csv")) {//to handle csv and excel files
	    	   data = Data.create("src/main/resources/templates/data/" + empOrg + "/" + fileName, StandardCharsets.UTF_8, ';');
	       }
	       /**
	        * Xls and dbs cannot be used as the sequence of fields in a data set cannot be determined through hierarchy file names anymore
	        * this is due to the columns being out of alphabetical order, and the file picker putting them into order regardless of choice
	        * ##Another reason is that not all hierarchies may be supplied for the data set
	        */
	       else if(extension.equals("xls") || extension.equals("xlsx")) {
	    	   DataSource dataExcel = DataSource.createExcelSource("src/main/resources/templates/data/" + empOrg + "/" + fileName, 0, true);
	    	   //need to specify column names in here before casting to type Data, so send field names shaved from
	    	   System.out.println("Creating the Excel data object");
	    	   System.out.println("Is it there " + dataExcel.toString());
	    	   //dataExcel.
	    	   //hierNames now in correct order due to pre-opening of file in main controller
	    	   for(String hierName : hierNames) {
	    		   dataExcel.addColumn(hierName);
	    	   }
	    	   data = Data.create(dataExcel);//input hierarchy files
	    	   System.out.println("Is data object there? " + data.toString());
	       }
	       
	       //try internal reference to method instead of included code, could cut codebase down by 100 or so
	       // Read generalization hierarchies
	       FilenameFilter hierarchyFilter = new FilenameFilter() {
	           @Override
	           public boolean accept(File dir, String name) {
	               if (name.matches(dataset + "_hierarchy_(.)+.csv")) {
	                   return true;
	               } else {
	                   return false;
	               }
	           }
	       };
	       // Create definition, hierarchies will always be .csv
	       File testDir = new File("src/main/resources/templates/hierarchy/" + empOrg + "/");
	       File[] genHierFiles = testDir.listFiles(hierarchyFilter);
	       Pattern pattern = Pattern.compile("_hierarchy_(.*?).csv");
	       for (File file : genHierFiles) {//need to determine how to identify which attributes have no hierarchy supplied
	           Matcher matcher = pattern.matcher(file.getName());
	           if (matcher.find()) {
	               CSVHierarchyInput hier = new CSVHierarchyInput(file, StandardCharsets.UTF_8, ';');
	               String attributeName = matcher.group(1);
	               System.out.println("Attribute Name: " + attributeName);
	               data.getDefinition().setAttributeType(attributeName, Hierarchy.create(hier.getHierarchy()));
	           }
	       }
	       System.out.println("Was able to create the Data object inside the method");
	       return data;
	   }
	//for db table handling
	public Data createDataAndHierarchies(final String empOrg, final String fileName, final List<String> hierNames, String table) throws IOException, SQLException {

		String[] dataNameAndExtension = fileName.split("\\.");
		String dataset = dataNameAndExtension[0];//format is [dataset name].[extension]
		String extension = dataNameAndExtension[1];
		
		 Data data = DefaultData.create();//create empty object with full scope to satisfy errors
		 if(extension.equals("db")) {
			DataSource dataDatabase = DataSource.createJDBCSource("src/main/resources/templates/data/" + empOrg + "/" + dataset + "." + extension, table);
			data = Data.create(dataDatabase);
		 }
		// Read generalization hierarchies
	       FilenameFilter hierarchyFilter = new FilenameFilter() {
	           @Override
	           public boolean accept(File dir, String name) {
	               if (name.matches(dataset + "_hierarchy_(.)+.csv")) {
	                   return true;
	               } else {
	                   return false;
	               }
	           }
	       };
	       // Create definition, hierarchies will always be .csv
	       File testDir = new File("src/main/resources/templates/hierarchy/" + empOrg + "/");
	       File[] genHierFiles = testDir.listFiles(hierarchyFilter);
	       Pattern pattern = Pattern.compile("_hierarchy_(.*?).csv");
	       for (File file : genHierFiles) {
	           Matcher matcher = pattern.matcher(file.getName());
	           if (matcher.find()) {
	               CSVHierarchyInput hier = new CSVHierarchyInput(file, StandardCharsets.UTF_8, ';');
	               String attributeName = matcher.group(1);
	               data.getDefinition().setAttributeType(attributeName, Hierarchy.create(hier.getHierarchy()));
	           }
	       }
		 return data;
	}
	//eventually intending to implement db file catering 
	public Data createDataAndHierarchies(final String empOrg, final String fileName, final List<String> hierNames, String table, String user, String password) throws IOException, SQLException {

		String[] dataNameAndExtension = fileName.split("\\.");
		String dataset = dataNameAndExtension[0];//format is [dataset name].[extension]
		String extension = dataNameAndExtension[1];
		
		 Data data = DefaultData.create();//create empty object with full scope to satisfy errors
		 if(extension.equals("db")) {
			DataSource dataDatabase = DataSource.createJDBCSource("src/main/resources/templates/data/" + empOrg + "/" + dataset + "." + extension, user, password, table);
			data = Data.create(dataDatabase);
		 }
		// Read generalization hierarchies
	       FilenameFilter hierarchyFilter = new FilenameFilter() {
	           @Override
	           public boolean accept(File dir, String name) {
	               if (name.matches(dataset + "_hierarchy_(.)+.csv")) {
	                   return true;
	               } else {
	                   return false;
	               }
	           }
	       };
	       // Create definition, hierarchies will always be .csv
	       File testDir = new File("src/main/resources/templates/hierarchy/" + empOrg + "/");
	       File[] genHierFiles = testDir.listFiles(hierarchyFilter);
	       Pattern pattern = Pattern.compile("_hierarchy_(.*?).csv");
	       for (File file : genHierFiles) {
	           Matcher matcher = pattern.matcher(file.getName());
	           if (matcher.find()) {
	               CSVHierarchyInput hier = new CSVHierarchyInput(file, StandardCharsets.UTF_8, ';');
	               String attributeName = matcher.group(1);
	               data.getDefinition().setAttributeType(attributeName, Hierarchy.create(hier.getHierarchy()));
	           }
	       }
		 return data;
	}
	
	//abstract creation of the dataRows string array list to the helper class for modularization
	public List<String[]> createDataRows(Data sourceData) {
		List<String[]> dataRows = new ArrayList<String[]>();
		
		DataHandle handle = sourceData.getHandle();//acquiring data handle
		
		System.out.println("inHandle rows number is " + handle.getNumRows());
	    System.out.println("inHandle columns number is " + handle.getNumColumns());

	    Iterator<String[]> itHandle = handle.iterator();
		String flubRow = Arrays.toString(itHandle.next());//get the header of the dataset to display in bold
		
		int i = 1;
		while((itHandle.hasNext()) && (i % 201 != 0)) {
			String row = Arrays.toString(itHandle.next());
			String[] dataTemp = row.split("[\\[\\],]");//all data needs formatting to remove empty columns
			String[] data = new String[dataTemp.length - 1];
			for(int j = 0; j < dataTemp.length - 1; j++) {
				data[j] = dataTemp[j+1].trim();
			}
			dataRows.add(data);
			++i;
		}
		handle = null;
		
		return dataRows;
	}
	
	//quick utility method used to determine if a headerRow field has a corresponding hierarchy file
	public int compareWithHierFileNames(String headerMember, List<String> hierFileList) {
		for(String hierFile : hierFileList) {
			if(hierFile.equals(headerMember)) {
				return 1;//break inner loop only, no need to keep searching if value already found
			}
		}
		return -1;
	}
	
	
	//implementing method to gather the values 
	public String[] getCertainFieldValues(String indexNeeded, List<String[]> dataRows) {
		String[] fieldValues = new String[dataRows.size()];
		
		int i = 0;
		for(String[] dataRow : dataRows) {//gets the value from desired index position for each attribute
			fieldValues[i] = dataRow[Integer.valueOf(indexNeeded)];
			++i;
		}
		return fieldValues;
	}
	
	/**
	  * Exemplifies the use of the redaction-based builder.
	  */
	public HierarchyBuilderRedactionBased<?> makeRedactionBasedHierarchy(String[] fieldData) {

	    // Create the builder
	    HierarchyBuilderRedactionBased<?> builder = HierarchyBuilderRedactionBased.create(Order.RIGHT_TO_LEFT,
	                                                                                Order.RIGHT_TO_LEFT,
	                                                                                ' ', '*');

	    System.out.println("-------------------------");
	    System.out.println("REDACTION-BASED HIERARCHY");
	    System.out.println("-------------------------");
	    System.out.println("");
	    System.out.println("SPECIFICATION");
	       
	    // Print info about resulting groups
	    System.out.println("Resulting levels: "+Arrays.toString(builder.prepare(fieldData)));
	       
	    System.out.println("");
	    System.out.println("RESULT");
	       
	    // Print resulting hierarchy
	    // was Tested with a test set, worked correctly
	    //printArray(builder.build().getHierarchy());
	    System.out.println("");
	    return builder;
	}
	
	public void deleteFiles(String empOrg) throws IOException, FileNotFoundException {
		//try/catch block for deleting the data file
		try {
		File dataDelete = new File("src/main/resources/templates/data/" + empOrg + "/");
	    FileUtils.cleanDirectory(dataDelete);
	    FileUtils.deleteDirectory(dataDelete);
		}
		catch(FileNotFoundException failure) {
			System.out.println("Deleting data file failed : " + failure.getLocalizedMessage());
		}
		//try/catch block for deleting the hierarchy files
		try {
	    File hierarchyDelete = new File("src/main/resources/templates/hierarchy/" + empOrg + "/");
	    FileUtils.cleanDirectory(hierarchyDelete);
	    FileUtils.deleteDirectory(hierarchyDelete);
		}
		catch(FileNotFoundException failure) {
			System.out.println("Deleting hierarchy file failed : " + failure.getLocalizedMessage());
		}
	}   
	
	//delete anonymized file from directory, may be used later on
		public void deleteAnonFile(String fullPath) throws IOException, FileNotFoundException {
			//very simple, name the file to be destroyed, it will be
			File outputDataDelete = new File(fullPath);
			try {
				FileUtils.cleanDirectory(outputDataDelete);
				FileUtils.deleteDirectory(outputDataDelete);
				System.out.println("Output Data has been cleared");
			} catch (FileNotFoundException failure) {
				System.out.println("Output Data may have already been cleared");
			}
		}
	
	//delete anonymized file from directory, may be used later on
	public void deleteAllFiles(String empOrg) throws IOException, FileNotFoundException {
		//very simple, name the file to be destroyed, it will be
		File outputDataDelete = new File("src/main/resources/templates/outputs/" + empOrg + "/");
	    File hierarchyDelete = new File("src/main/resources/templates/hierarchy/" + empOrg + "/");
	    File dataDelete = new File("src/main/resources/templates/data/" + empOrg + "/");
	    File imageDelete = new File("src/main/resources/META-INF/resources/images/" + empOrg + "/");
		try {
			if (outputDataDelete.isDirectory()) {
		        if (outputDataDelete.list().length > 0) {
		        	System.out.println("Output Data needs to be cleared");
		        	FileUtils.cleanDirectory(outputDataDelete);
		        	//FileUtils.deleteDirectory(outputDataDelete);
		        } else {
		        	System.out.println("Output Data just needs to be deleted");
		        	//FileUtils.deleteDirectory(outputDataDelete);
		        }
			}
			if (hierarchyDelete.isDirectory()) {
		        if (hierarchyDelete.list().length > 0) {
		        	System.out.println("Hierarchy Data needs to be cleared");
		        	FileUtils.cleanDirectory(hierarchyDelete);
		        	//FileUtils.deleteDirectory(hierarchyDelete);
		        } else {
		        	System.out.println("Hierarchy Data just needs to be deleted");
		        	//FileUtils.deleteDirectory(hierarchyDelete);
		        }
			}
			if (dataDelete.isDirectory()) {
		        if (dataDelete.list().length > 0) {
		        	System.out.println("Main Data needs to be cleared");
		        	FileUtils.cleanDirectory(dataDelete);
		        	//FileUtils.deleteDirectory(dataDelete);
		        } else {
		        	System.out.println("Main Data just needs to be deleted");
		        	FileUtils.deleteDirectory(dataDelete);
		        }
			}
			if (imageDelete.isDirectory()) {
		        if (imageDelete.list().length > 0) {
		        	System.out.println("Image Data needs to be cleared");
		        	FileUtils.cleanDirectory(imageDelete);
		        	FileUtils.deleteDirectory(imageDelete);
		        } else {
		        	System.out.println("Image Data just needs to be deleted");
		        	//FileUtils.deleteDirectory(imageDelete);
		        }
			}
	    } 
	    catch(FileNotFoundException failure) {
	    	System.out.println("Deleting all files failed : " + failure.getLocalizedMessage());
	    }
	    System.out.println("Finished deleting");
	}  
}
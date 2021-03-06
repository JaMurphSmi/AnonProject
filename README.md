# Application for the Anonymization of Relational <img align="right" src="https://i.imgur.com/hLCDoAZ.png"> Medical Research Data (AARMRD) Project 


Author: Jake Murphy Smith  

This Spring-Boot application is intended to provide users with access to a variety of utilities that provide and compliment the operation of data anonymization. This application is focused specifically at the anonymization of medical data for the purpose of secure distribution to third parties.  
  
If you download this project file structure to your desktop, it is an easy effort to run it, simply navigate to the root of the containing directory and enter the command mvn spring-boot:run  
The application should begin to build, and after ~20 seconds the application should be built to  
"https://localhost:8443". Upon loading this url you will be presented with the default login screen of the Spring-Boot-Security module. The username will be simply "user" and the password will  
be the procedurally generated string held in the command logs (usually visible within the screen)  

Once this is done, the user may proceed to the application itself and begin their anonymization ventures
  
To implement the anonymization functionality it is intended to make use of the open-source ARX Deidentification API library available @: https://github.com/arx-deidentifier/arx
ARX (C) 2012-2019 Fabian Prasser, Florian Kohlmayer and Contributors.  
  
In the future it is also hoped to incorporate other anonymization suites to extend functionality and diversify options.  
  

****File Format Notes:  
  
For best results  
  
----------------------------------------------------------------------------------------------------------------------------
Notes for input data file formats:
----------------------------------------------------------------------------------------------------------------------------.

CSV: Please ensure the data contained within each line is separated by a semi-colon, ';' character. This is to avoid errors caused by comma ',' separated data affecting names and large numbers.  
Protip: Most programs will save csv files delimited by a comma, it is recommended to use notepad or other such text editor that provides a 'replace all' function to rectify this  
	 Please ensure that your dataset contains a header line defining the headings of your fields at the top of the file ie age;gender;postcode;phoneno;salary  
	   
Excel: No special conditions other than header are imposed on an Excel file. Best results with a standard Excel Workbook .xlsx file.
	 
----------------------------------------------------------------------------------------------------------------------------
Notes for hierarchy file formats:
----------------------------------------------------------------------------------------------------------------------------.	 

The only format accepted currently is CSV  
CSV: Please name your file in the format of [your dataset file's name]_hierarchy_[field name].csv to make import as successful as possible  
No heading is required inside this data file, all that is required is your defined hierarchy, with values separated by a ';' ie age hierarchy: 34;<50;<=65;*  
  
----------------------------------------------------------------------------------------------------------------------------
Notes for output file formats:
----------------------------------------------------------------------------------------------------------------------------.
  
The standard format that the application will dispense upon completion is CSV  

## ABOUT ##
The Marine Motorsports Management Program aims to increase productivity, reliability, and management of tools in the Marine Motorsports BOCES class.
The application utilizes a barcode scanning tool to scan barcodes of tools that are added to the system and are given a unique id.
The application features a fully functional GUI for both Tool Masters (students) and Administrators (teachers).
Application data is stored in a relational database (Microsoft Access DB)

## ENV ##
The env file provides the application the admin user + pass. 
Additionally it provides the url for the database connection. 
Please ensure this url is accurately representing where the database is located.
The application will automatically look for the .env file in the documents folder of the current user.
The application will not start without the .env file

## Executables ##
MMMS.exe - Compiled by Launch4j with config file in the 'launch4j config' dir (you can import this config on Launch4j), only one instance may be ran at a time
MMMS.jar - Built from netbeans to include all dependencies, packages, and main class

**NOTE**
These files are seperate and can be ran seperately. 
.exe - double click or run from cmd
.jar - java -jar MMMS.jar (in dir)

## DB ##
DB directory must be set in .env file
Schema can be used to build a live db in Microsoft Access to place in the specified directory

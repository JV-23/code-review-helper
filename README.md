# Code Review Helper
Windows only, certain command line commands are called and don't work on Linux. 

How to run:
1) Open a command line
2) Go to the 'service' folder and run 'mvn install'
3) Go to the 'core' folder and run 'mvn compile'
4) In the same folder run 'mvn exec:java'
5) When prompted, choose option number 1, and then follow the instructions to download the analysed repository's files to your machine
6) Run 'mvn exec:java' again, and this time when prompted choose option number 2 and follow the instructions. This will run the analysis
7) Go to the 'presentation' folder and run 'mvn install'
8) Run 'mvn spring-boot:run'
9) The tool is now running, simply open a web browser and go to 'localhost:8080' to use it
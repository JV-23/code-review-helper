# Code Review Helper
Windows only, certain command line commands are called and don't work on Linux. 

Before running:
If you want to authenticate, go to the config.yml file on the 'core' folder, and replace the 'insert token here' text with your own git authentication token. Not doing this will limit the amount of requests the tool can make.

How to run:
1) Open a command line
2) Go to the 'service' folder and run 'mvn install'
3) Go to the 'core' folder and run 'mvn compile'
4) In the same folder run 'mvn exec:java'
5) When prompted, choose option number 1, and then follow the instructions to download the analysed repository's files to your machine
6) Run 'mvn exec:java' again, and this time when prompted choose option number 2 and follow the instructions. This will run the analysis
7) To generate the flame graphs, use the FlameGraph tool https://github.com/brendangregg/FlameGraph You should generate a stableflamegraph.svg and a pullrequestflamegraph.svg from the stableprofile and prprofile files, respectively.
7) Go to the 'presentation' folder and run 'mvn install'
8) Run 'mvn spring-boot:run'
9) The tool is now running, simply open a web browser and go to 'localhost:8080' to use it
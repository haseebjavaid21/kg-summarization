# KG-SUMMARIZATION

## Project Title
Knowledge Graph Summarization

## Description
Knowledge graphs (KG) are graphs/structures consisting of facts in the form of triples. Triples are a combination of subjects, objects and predicates. In general, knowledge graphs are huge in size and may contain redundant information. Knowledge Graphs’ humongous size makes it difficult to process and represent them. The goal of this project group is to obtain a pruned KG. This consists of selecting a sub knowledge graph based on endpoints and summarizing those subgraphs by considering facts based on ranks.

The UI implemented will allow the user to execute Summarization using LinkSum  for a selected entity using the selected mode and see a visualization of the summazrized entity. Whereas for HITS and SALSA Algorithms user can download the summarized ttl file.


## Installation / Project Setup
Setup Apache server on your local system.
In the root folder of the server clone the repository from https://github.com/haseebjavaid21/kg-summarization.git 
Import prpoject in your IDE with the folder KGSMRSTN as source folder.  
Place the .ttl files containing KG which needs to be summarized in KGSMRSTN folder and modify the file path accordingly in - AbstractSummarization Selector of respective algorithm.  
Run maven install.  
Run maven clean.  
Run maven build.  
Run Project as Java Application with KgsmrstnApplication.java.  
Create a Vitual Host on your local system and give the path to the directory "webapp".  
Start the Apache Server.  
Enter the URL specified for the Vitual Host in the browser.  

## Prerequisites
Java environment ( JDK ) should be setup on the machine.  
Source : https://www.oracle.com/java/technologies/javase-jdk14-downloads.html#license-lightbox  

Maven should be installed on the machine  
Source : https://apache.lauf-forum.at/maven/maven-3/3.6.3/binaries/apache-maven-3.6.3-bin.zip  
How to : https://maven.apache.org/install.html  

Setting up Apache Server:  
Windows: https://httpd.apache.org/docs/2.4/platform/windows.html  
Linux: http://httpd.apache.org/docs/2.4/install.html  

Setting Up Virtual Host:  
Windows: https://medium.com/d6-digital/create-virtual-host-for-apache-on-windows-10-by-using-xampp-8664b0427567  
Linux: https://support.rackspace.com/how-to/set-up-apache-virtual-hosts-on-the-ubuntu-operating-system/  

## Tests
To execute test for Knowledge Graph Summarization using HITS, run the class KgsmrstnApplicationTestHits.  
To execute test for Kniwledge Graph Summarization using SALSA, run the class KgsmrstnApplicationTestsSalSa.  
To execute test for Entity Summarization using LinkSum, run the class EntitysmrstnTests.  

## Built With
Java  
Maven - Dependency Management  
Apache Jena  

## Authors
Pavan Kumar Sheshanarayana\
Shreyas Kottur Shivananda  
Usman Ashraf  
Muhammad Haseeb Javaid  


## License
This project is licensed under the Dice Group  

## Acknowledgments
Diego Moussallem  
Dice Group  

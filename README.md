Sejal Divekar skd8508
#Text Mining Application
This is a Maven Appplication to cluster the documents.
It implements Kmeans and KMeansPlusPlus Algorithm for clustering the documents. 
The output is the labels for each document stating which folder it belongs to given that the original clustering documents in the folder was correct
it gives out the metrics of the performance of the algorithms. 

Output on console:
1. KMeans Centres
2. Original Lables
3. Kmeans Lables
4. Confusion Matrix for Kmeans Algorithm
5. Metrics for Algo: 
    1. Precision
    2. Recall
    3. F1-Score
7. KmeansPlusPlus Centres
6. KmeansPlusPlus Lables
7. Confusion Matrix for KmeansPlusPlus Algorithm
8. Metrics for Algo: 
    1. Precision
    2. Recall
    3. F1-Score   

It also gives the plots of the clusters generated.

#####Dependencies 
All the required dependencies in pom.xml. Maven would download all the dependencies once the application is built.
1. stanford-corenlp-4.4.0.jar
3. jama-1.0.3.jar
4. JMathPlot-1.0.1.jar

###Steps to run: 
If you have maven installed on the machine through command line:
On cmd:
1. Run **mvn clean install**.  The command must be executed in the directory which contains the pom file.

2. The PA-Assignment-1-1.0-SNAPSHOT.jar needs 3 parameters in the given order:

    i. Number of clusters default value is 3.
    ii. Path of the file(data.txt) which contains the path file which contain paths of all the input folders to process. 
    iii. Measure of Similarity either euclidean or cosine default is euclidian

On Eclipse IDE  
1.  Open project in the IDE as follows:
   - Launch Eclipse IDE and select **Import** from **File** menu.
   - In the displayed **Import** dialog, expand the **General** folder. Select **Projects from Folder or Archive** and click **Next**.
   - Browse the directory where the project is saved and select that folder.
   - Click **Finish**        

2.To run the project these paramters nees to be given as commandline arguments with the java application jar on cmd or 
        In eclipse it can be provided as:
        - Click on Run -> Run Configurations
        - Click on Arguments tab
        - In Program Arguments section , Enter your arguments.
        - Click Apply

 




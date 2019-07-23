# bigdata-architecture-dev

## **Download**

**------------------------------------**

**1-Kafka download link (version:2.1)**

https://www.apache.org/dist/kafka/2.1.1/

**2-Spark download link (version:2.4.0 hadoop version:2.7)**

https://archive.apache.org/dist/spark/spark-2.4.0/

**3-Human activity download link (version:v1.1)**

http://www.cis.fordham.edu/wisdm/dataset.php

**4-MongoDB download through terminal(latest version)**

sudo apt-get install mongodb

**5-MongoDB interface (latest version)**

https://nosqlbooster.com/downloads

**6-Python Programming Interface (latest version)**

https://download-cf.jetbrains.com/python/pycharm-professional-2019.1.tar.gz

**7-Android Studio download link (latest version)**

https://developer.android.com/studio/
<br/><br/><br/><br/><br/>

## **Configuration and Run**

------------------------------------



**1-Run Android App on Android Phone**

Load project through USB from Android Studio

**Explanation:**

Make sure the phone connected same internet with computer

**2-Create Port Listener**

nc -l -k 192.168.1.107 5000 >> ~/Desktop/listener.txt

**Explanation:**

Listen specific ip:port combination and write datas to file

      -l for listen mode

      -k to keep open always

      >>  to append to file

      and location of record file (~/Desktop/listener.txt)

**3-Setting up Crontab**

Open crontab with “ crontab -e “ then append  “ * * * * * ~/Desktop/script “

**Explanation:**

It works every minute to push data which comes from phone to kafka producer

            Check for more : https://crontab.guru/ **       **

**4-Start Zookeper**

bin/zookeeper-server-start.sh config/zookeeper.properties

 ![start-zookeper](https://github.com/mustafaglr/bigdata-architecture-dev/blob/master/images/1.png)

**Explanation:**

 If 2181 is already in use, change with 5181

 (2181 or 5181 (config/zookeper.properties and config/server.properties))

**5-Start Kafka Server**

bin/kafka-server-start.sh config/server.properties

 ![start-kafka-server](https://github.com/mustafaglr/bigdata-architecture-dev/blob/master/images/2.png)
**6-Create A Topic**

bin/kafka-topics.sh --create --bootstrap-server localhost:9092 --replication-factor 1 --partitions 1 --topic test

 ![create-topic](https://github.com/mustafaglr/bigdata-architecture-dev/blob/master/images/3.png)

**7-Check for Topic**

bin/kafka-topics.sh --list --bootstrap-server localhost:9092

 ![check-for-topic](https://github.com/mustafaglr/bigdata-architecture-dev/blob/master/images/4.png)

**8-Start Spark Streaming with jar file**

./bin/spark-submit --packages org.apache.spark:spark-streaming-kafka-0-10\_2.11:2.4.0,org.mongodb:mongo-java-driver:3.10.0 --class org.apache.spark.spark\_streaming\_kafka\_0\_10\_2.App ~/Desktop/jars/sparkJava.jar

 ![start-spark-streaming](https://github.com/mustafaglr/bigdata-architecture-dev/blob/master/images/5.png)

**Explanation:**

 A Consumer is created here to listen a producer

 --packages for unseen dependencies inside code

 --class for your java class groupId.artifactId.JavaFileName

 and location of Jar file (~/Desktop/jars/sparkJava.jar)

**9-Create A Producer To Send Messages**

bin/kafka-console-producer.sh --broker-list localhost:9092 --topic test

 ![create-producer](https://github.com/mustafaglr/bigdata-architecture-dev/blob/master/images/6.png)

**Explanation:**

 to send messages from bigdata file use: cat ~/WISDM_ar_v1.1_raw.txt | bin/kafka-console-producer.sh …

![produce-fromfile](https://github.com/mustafaglr/bigdata-architecture-dev/blob/master/images/7.png)

**10-Check For Status of Spark Streaming on localhost:4040**

![check-status](https://github.com/mustafaglr/bigdata-architecture-dev/blob/master/images/8.png)

**Active Batches;**

![active-batches](https://github.com/mustafaglr/bigdata-architecture-dev/blob/master/images/9.png)

**Completed Batches;**

 ![completed-batches](https://github.com/mustafaglr/bigdata-architecture-dev/blob/master/images/10.png)

<br/><br/><br/><br/><br/>
## **Storage**

------------------------------------

**1-Check For Datas**

db.collection.find({})

![check-db](https://github.com/mustafaglr/bigdata-architecture-dev/blob/master/images/11.png)

**Explanation:**

 collection is your collection name inside database

<br/><br/><br/><br/><br/>
## **Deep Learning**

------------------------------------

**1-Get Datas With Function From MongoDB**

main.py -> create_training_data()

**2-Create Model using Keras**

https://www.keras.io

**3-Get Loss and Accuracy Values From Learned Machine**

<br/><br/><br/><br/><br/>

## **Files**

------------------------------------

1-Deep Learning Train Machine (LSTM.py | KNN.py)

2-Java Project With Maven (sparkJava.jar | App.java | pom.xml)

3-Android Programming (activity_main.xml | MainActivity.java)

4- Exported data from MongoDB (mongo.json)

5-Crontab script (script)


<br/><br/><br/><br/><br/>

## **References**

------------------------------------

https://www.keras.io

https://spark.apache.org/docs/2.4.0/streaming-kafka-0-10-integration.html

https://kafka.apache.org

https://www.tutorialspoint.com/mongodb/mongodb\_java.htm

http://www.cis.fordham.edu/wisdm/dataset.php

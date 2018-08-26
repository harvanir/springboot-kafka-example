# springboot-kafka-example
Example of Spring Boot Kafka. 

## How to use
**1. Clone the application**

```bash
git clone https://github.com/harvanir/springboot-kafka-example.git
```

**2. Download & install kafka**

```bash
https://kafka.apache.org/downloads
https://kafka.apache.org/quickstart
```

**3. Build the application**

```bash
mvn clean install
```

**4. Go to kafka directory**

```bash
localDrive>cd D:\app\kafka_2.12-1.1.0
```

**5. Run the kafka server**

```bash
D:\app\kafka_2.12-1.1.0>.\bin\windows\zookeeper-server-start.bat .\config\zookeeper.properties
D:\app\kafka_2.12-1.1.0>.\bin\windows\kafka-server-start.bat .\config\server.properties
```

**6. Run the kafka consumer (listen message via console)**

```bash
D:\app\kafka_2.12-1.1.0>.\bin\windows\kafka-console-consumer.bat --bootstrap-server localhost:9092 --topic test --from-beginning
```

**7. Run the kafka producer (produce message via console)**

```bash
D:\app\kafka_2.12-1.1.0>.\bin\windows\kafka-console-producer.bat --broker-list localhost:9092 --topic test
```

**8. Run the spring boot application**

```bash
java -jar target/springboot-kafka-example-0.0.1-SNAPSHOT-exec.jar
```

**9. Access the application via web browser (produce message)**

```
http://localhost:8083/kafka/testconsumer/send/{any message}
```

**10. Access the application via web browser (get message)**

```
http://localhost:8083/kafka/testconsumer/list
```
# Kubernetes-Log-Monitoring-System

## Overview
This project deploys a robust and scalable log extraction and event sourcing system for microservices running in a Kubernetes cloud environment. The system leverages FluentBit for log extraction, Kafka for distributed message queuing, and an event sourcing solution implemented with Akka and Scala. Cassandra is used as the event store for tracking and reconstructing application states.

## Features
- **Scalable Log Extraction:** Utilizes FluentBit to capture logs from microservices in Kubernetes.
- **Distributed Message Queue:** Employs Kafka for efficient and scalable log transport within the Kubernetes cluster.
- **Event Sourcing:** Processes logs to generate events that describe entity state changes, stores these events in Cassandra, and provides the capability to reconstruct application states.
- **Enhanced Debugging:** Facilitates efficient debugging and monitoring by enabling visibility into the application's behavior over time.

## Architecture
### Log Extraction and Transport
1. **FluentBit:** Serves as the log agent, capturing logs from microservices running in the Kubernetes cluster.
2. **Kafka:** Acts as the distributed message queue within Kubernetes, ensuring high performance and scalability in log transport.

### Event Sourcing Solution
1. **Akka and Scala:** Used to build the event sourcing system, which processes the logs received from Kafka.
2. **Event Generation:** Extracted logs are analyzed to create events describing changes in entity states.
3. **Cassandra Event Store:** Events are persisted in Cassandra, allowing the reconstruction of application states for monitoring and debugging.

## Key Benefits
- **Scalability:** The use of Kubernetes, FluentBit, and Kafka ensures the system can handle a high volume of logs.
- **High Performance:** Efficient log transport and processing reduce latency in data handling.
- **Debugging Efficiency:** Event sourcing enables precise state reconstruction, improving visibility and troubleshooting.

## Installation
### Prerequisites
- Kubernetes cluster
- FluentBit installed as a DaemonSet
- Kafka deployed within the cluster
- Scala and Akka runtime environment
- Cassandra database setup

### Steps
1. **Deploy FluentBit:** Configure and deploy FluentBit as a DaemonSet in your Kubernetes cluster to capture logs from microservices. (Values for the configuration is in the k8s folder)
2. **Set Up Kafka:** Deploy Kafka in the cluster and configure FluentBit to send logs to Kafka. (the Deployement was done with Strimzi operator, the yaml configuration file is in k8s folder)
3. **Implement Event Sourcing Solution:** Run the Docker-compose File
4. **Verify Integration:** Test the end-to-end flow by generating sample logs and ensuring the events are created and stored correctly.

## Usage
1. **Log Monitoring:** Use FluentBit and Kafka to monitor and transport logs in real-time.
2. **Event Reconstruction:** Query the event store in Cassandra to reconstruct the state of any entity at a specific point in time.
3. **Application Debugging:** Leverage the event sourcing data to debug application behavior and monitor system health.

## Future Enhancements
- **Real-Time Dashboards:** Add a visualization layer to display real-time events and entity states.
- **Machine Learning Insights:** Integrate machine learning to analyze logs for anomaly detection and predictive insights.
- **Advanced Querying:** Implement advanced querying capabilities for complex event analysis.
## Contact
For any questions or support, feel free to contact Firas Frigui at firas.frigui@gmail.com


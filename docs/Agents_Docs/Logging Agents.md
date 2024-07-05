### What is a Logging Agent ?

Logging agents are software components that collect, aggregate, and forward log data to a central location for analysis and monitoring. You can integrate the logging agents into your Kubernetes architecture using DaemonSets, which runs a copy of the logging agent on each node in the cluster.
### Why use Logging Agents ?

- Deploying an agent is much simpler compared to deploying a library for each application. A single agent can efficiently handle log routing for an entire host with minimal setup and configuration.
- Agents offer advanced features that libraries may lack, including multi-threading, failure safety, diverse input options, and the ability to filter and parse logs.
- Developers can integrate agents independently without the need to add or modify existing code.
- Agents are compatible with various component's logs such as databases, message brokers, and system logs.
- Agents typically outperform libraries, particularly as the number of applications increases.


[[What Log agent should We choose]]

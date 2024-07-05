**Event Producers**:
These are systems or applications that generate events (Commands). In my case, anything that interacts with my object and modifies its state or invokes logs, these logs will be collected by the log agent FluentBit, which will be the  event producer.

**Event Store**:
The core of an event sourcing system, the event store, persists events in the sequence they occurred. This allows the system to reconstruct the state of an object by replaying its events.

**Snapshot Store**: 
To optimize performance, especially for aggregates with a long history of events, snapshotting is used. A snapshot store will save the current state of an aggregate at a given point in time, reducing the number of events that need to be replayed to rebuild state.

**Command Handlers**: 
These are components that handle incoming commands (actions that may change the system's state). They validate commands, generate events based on the current state and the command, and store those events in the event store (The Application).

**CQRS:** 
Stands for Command Query Responsibility Segregation. It's a design pattern that separates the read and write operations of a data storage into two distinct interfaces. This approach is based on the principle that the methods used for updating data (commands) should be separate from the methods used for reading data (queries), each possibly acting on different models or representations.

[[Architecture]]
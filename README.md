## Schedule Conductor Workflows
Schedule Conductor workflow is a _scheduler as a service_ that runs in the cloud with [Netflix conductor](https://github.com/Netflix/conductor)
embedded in it. It runs as an extension module of conductor.

### Use Case Solved
- In digital space there are many use cases that are solved by running schedulers. Some of the common cases are:
	1. publishing site maps for an e-commerce website.
	2. refresh cache everyday at a fix time.
	3. send an email notification to operations regarding health of system and many more.
- If your architecture is micro services based then you have only two options:
	1. to add scheduling capability (like Quartz, spring scheduler) in the service which needs to execute some scheduling task.
	2. to setup a separate micro service, which will solve all scheduling use cases. This service interact with other service for data needs through REST APIs.
- In the end we have unnecessary mesh of schedulers. After certain point the setup is completely out of control.
- Solution to the problem is _Schedule Conductor workflow_:
	- This can be deployed as replacement of conductor server.
	- Embedded conductor provides service orchestration capability.
	- Extension module provides scheduling capability.
	- Conductor workflow with cron expression can be scheduled through REST API.
	- Scheduled job spawned internally starts workflow at scheduled time.
- _Schedule Conductor workflow_ is extendable to work with all persistence stores supported by conductor.

*Quickly use able in _PRODUCTION_*:
- Scheduling module can be enabled with property `conductor.additional.modules= net.jas34.scheduledwf.config.ScheduledWfServerModule` 
- Deploy `scheduledwf-server` instead of `conductor-server`. 

You are done!

Schedule Conductor is compatible with Java 8 and has embedded Conductor v2.30.4


Architecture 
---------
### High Level Architecture

![Scheduled Conductor](docs/img/scheduled-wf-architecture.svg)

##### API
- Expose REST API interface for scheduling a workflow with metadata definition and cron expression (`Scheduling Workflows Management`)
- Expose REST API interface for managing running schedulers (`Scheduler Management`) 

##### SERVICE
- Consists of manager to spawn a scheduler process.
- One stop to manage complete life cycle of schedulers.

##### STORE
- Currently implemented for MYSQL.
- Can be extended to other persistence stores offered by conductor.

### Runtime Model

![Scheduled Conductor](docs/img/scheduled-wf-runtime-model.svg)
 
Component Level Details
----------------

### Scheduler Manager
- This component is meant to manage lifecycle of scheduled workflow.
- It takes lifecycle state decision for a scheduler with the help of scheduled jobs registry.
- It schedules the schedulers through scheduling assistant.
- It index the scheduling information through see the [IndexScheduledWfDAO](scheduledwf-core/src/main/java/net/jas34/scheduledwf/dao/IndexScheduledWfDAO.java) interface.
	##### Scheduled Jobs Registry
	- This registry act a single source of truth to know whether a particular workflow is required to be scheduled, 
	paused or deleted.
	- It can be customized by implementing [ScheduledProcessRegistry](scheduledwf-core/src/main/java/net/jas34/scheduledwf/execution/ScheduledProcessRegistry.java) interface.
	- The default implementation can be found here `net.jas34.scheduledwf.execution.DefaultScheduledProcessRegistry`.
	- It reads metadata for scheduling through `net.jas34.scheduledwf.execution.dao.ScheduledWfExecutionDAO`.

### Scheduling Assistant
- This is an abstract layer for job scheduling.
- Comes with default implementation of [DefaultWorkflowSchedulingAssistant](scheduledwf-core/src/main/java/net/jas34/scheduledwf/execution/DefaultWorkflowSchedulingAssistant.java).
- It:	
	1. creates scheduled jobs.
	2. pause scheduled jobs.
	3. delete scheduled jobs.
- It contains factory [WorkflowSchedulerFactory](scheduledwf-core/src/main/java/net/jas34/scheduledwf/scheduler/WorkflowSchedulerFactory.java) that returns an instance of [WorkflowScheduler](scheduledwf-core/src/main/java/net/jas34/scheduledwf/scheduler/WorkflowScheduler.java) interface.
- The assistant can be customized with the implementation of [WorkflowSchedulingAssistant](scheduledwf-core/src/main/java/net/jas34/scheduledwf/execution/WorkflowSchedulingAssistant.java) interface.
	##### WorkflowSchedulerFactory
	- This is core abstraction to define Scheduled process of your choice. (`WorkflowSchedulerFactory<T extends ScheduledProcess>`)
	- `ScheduledProcess` is one of the granular entity that is expected to have reference to scheduled process/thread. Currently implemented as [CronBasedScheduledProcess](scheduledwf-core/src/main/java/net/jas34/scheduledwf/scheduler/CronBasedScheduledProcess.java).
	- The default implementation can be found here [DefaultWorkflowSchedulerFactory](scheduledwf-core/src/main/java/net/jas34/scheduledwf/scheduler/DefaultWorkflowSchedulerFactory.java).
	- Currently returns [CronBasedWorkflowScheduler](scheduledwf-core/src/main/java/net/jas34/scheduledwf/scheduler/CronBasedWorkflowScheduler.java). 

### Cron Based Workflow Scheduler (Jobs Scheduling)
- The scheduling capability is completely customizable by implementing [WorkflowScheduler](scheduledwf-core/src/main/java/net/jas34/scheduledwf/scheduler/WorkflowScheduler.java) interface and by returning applicable instance from `WorkflowSchedulerFactory`.
- The default behaviour is to enable [CronBasedWorkflowScheduler](scheduledwf-core/src/main/java/net/jas34/scheduledwf/scheduler/CronBasedWorkflowScheduler.java).
- This is composed of [wisp](https://github.com/Coreoz/Wisp) scheduler. Wisp provides in memory scheduling capabilities. As a result, `CronBasedWorkflowScheduler` also schedules required in memory schedulers using wisp.
- Each scheduled job is provided an instance of `Runnable` task through [ScheduledTaskProvider](scheduledwf-core/src/main/java/net/jas34/scheduledwf/scheduler/ScheduledTaskProvider.java) interface. 
	##### ScheduledTaskProvider
	- The default implementation of task provider is [DefaultScheduledTaskProvider](scheduledwf-core/src/main/java/net/jas34/scheduledwf/scheduler/DefaultScheduledTaskProvider.java).
	- It creates fully flexible and customizable task for job before scheduling through `getTask()`.
	- It performs indexing of workflow start executions through callback to [IndexExecutionDataCallback](scheduledwf-core/src/main/java/net/jas34/scheduledwf/scheduler/IndexExecutionDataCallback.java). 
	- It uses [LockingService](scheduledwf-core/src/main/java/net/jas34/scheduledwf/concurrent/LockingService.java) 
	to prevent concurrent execution of same job on more than one server at scheduled time. 

### Locking Service
- This is composed of LockProvider available in conductor which can run in three modes

### Persistence Layer

----
1. multi node env
2. lock service

---
 	

 
 ```java
 	public class DefaultWorkflowSchedulingAssistant implements WorkflowSchedulingAssistant {
 		//methods implemented
 	}
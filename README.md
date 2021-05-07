## Schedule Conductor Workflows
Schedule Conductor workflow is a _scheduler as a service_ that runs in the cloud with [Netflix conductor](https://github.com/Netflix/conductor)
embedded in it. It runs as an extension module of conductor.

Schedule workflow has been developed as an extension of _Conductor_ which allow you to 
schedule a scheduler as per needs. _Scheduler_ in turn starts a conductor workflow at scheduled time. 

Quickly use able on _PRODUCTION_:
- Designed as extension module of Netflix conductor.
- Enabled with property `conductor.additional.modules=net.jas34.scheduledwf.config.ScheduledWfServerModule` 
- Deploy `scheduledwf-server` instead of conductor-server. 

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
- Consist of manager to spawn a scheduler process.
- One stop to manage complete life cycle of schedulers.

##### STORE
- Currently implemented for MYSQL.
- Can be extended to other persistence stores offered by conductor.

### Runtime Model

![Scheduled Conductor](docs/img/scheduled-wf-runtime-model.svg)
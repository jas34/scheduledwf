## Schedule Conductor Workflows
Schedule Conductor workflow is a scheduler as a service that runs in the cloud with [Neflix conductor](https://github.com/Netflix/conductor)
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

![Schedule Conductor](docs/img/scheduled-wf-architecture.svg)


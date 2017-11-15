# Tooling

## Execute the project 

To execute the project you have to build this one and to execute docker containers. You can do this using 2 scripts (don't forgot to start docker):
```bash
sh build.sh
sh run.sh
```

To stop the project, you just have to execute the ```stop.sh``` script.

## Modules 

Tooling project contains modules:
- **tooling-tenant**: allow to manage all tenants of the application
- **tooling-user**: security part of the application. It use Spring Security and JWT.  
- **tooling-zuul**: import tooling-user module and execute Netflix's Zuul project. This one allow to easily create an API gateway 
- **tooling-vacation**: vacation part of the application
- **tooling-project**: allow to manage projects
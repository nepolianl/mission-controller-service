# Mission Controller Service

A simple service to release waypoints one at a time to a robot in a way that allows changes or cancellations. The service is implemented with the following programming language and technologies:

1. Java 11 - Programming Language
2. Multithreading - To execute threads
3. Spring Boot - To create as a simple micro service
4. Spring MVC - To develop REST API
5. Spring Concurrency - Thread pool task executor
6. Spring Boot Test - Unit and integration tests
7. Apache Maven - Project and build management tool

# Objective

The program intented to work as a simple service to release waypoints one at a time to a robot in way that allow re routing the changes and/or cancellation.

### For instance, the process could be:

* The robot begins with coordinates (2, 1). 
* We submit a trajectory with points ((4, 3), (5, 3)).
* The robot starts to move to (4, 3).
* We submit a trajectory with points ((3, 3), (5, 4)).
* The robot starts to move to (3, 3) instead of (4, 3).
* The robot arrives at (3, 3), and starts to move towards (5, 4).
* The robot receives a trajectory with points ().
* The robot stops.

# Implementation
As the result, this service designed, organized, and developed using Spring boot, REST, services and simulated robot to implement the program objective.

To explain, the service exposes a REST endpoint which can receive trajectory with waypoints such as [[2,3],[3,3],[4,3],[5,3]] etc,. and submits the trajectory input as follows:

* URL: `/api/mission/setTrajectory`
* Method: POST
* Content-type: application/json
* Request: `[[1,3],[2,3],[3,3],[4,3],[5,3],[6,3],[7,3],[8,3],[9,3],[10,3],[11,3],[12,3]]`
* Response : 200 OK `<Received trajectory with way points>`

After a trajectory with points submitted, the mission control service manages the waypoints using mission control threads to send set of navigation commands using command service to the simulated robot. Accordinly, the robot starts to move to waypoints.

To change the waypoints for a robot, the API can be used to submit trajectory with points simultenously.

When any request submits another trajectory with waypoints, the mission control service will override the waypoints to re-routes and change the navigation commands to move the robot to latest trajectory set.

When the request receives a trajectory with empty points `[]`, the robot stops.

## Application Properties

```
# Change server port and context path
server.port=9091
server.servlet.context-path=/api

# Default configuration
robot.initial.position=2.0,1.0
robot.delay=true
robot.sleep=2000
mission.poll.sleep=1000
```

# Setup

Getting started with this service, the system or setup must have Java 11, Apache Maven 3.x (3.8.6 recommended) installed.

## Compile and build

Run `mvn clean install ` or `mvn clean package spring-boot:repackage`

## To execute the test cases

Open IDE and right click on the project, then choose `Run As -> Junit Test`, otherwise open the command prompt and navigate to the project location, then execute `mvn clean test`

## Run the application

Open the project source location in the command prompt and execute `mvn spring-boot:run`

## Deployment

After a successful compliation and build, the executable JAR will be generated and be available under `target` folder in the project location. The JAR for example `mission-control-service.jar` can be copied to respective local or remote server location, then started or executed using the command `java -jar mission-control-service.jar`

# Testing

To test this service which releases waypoints one at a time to a robot in a way that allows changes or cancellations, the following test scenarios with input and output are validated as below:

### Simultaneous request that submits trajectory with points

Input:

1. `[[2,3],[3,3],[4,3],[5,3]]`
2. `[[1,2],[2,2],[3,2],[4,2],[5,2],[6,2],[7,2],[8,2],[9,2],[10,2],[11,2],[12,2]]`
3. `[[5,1],[6,2],[7,3],[8,4]]`

Output:

```
2022-09-23 00:17:47.783  INFO 9804 --- [nio-9091-exec-5] c.n.l.m.controller.MissionController     : Received trajectory [[2.0, 3.0], [3.0, 3.0], [4.0, 3.0], [5.0, 3.0]]
2022-09-23 00:17:47.783  INFO 9804 --- [nio-9091-exec-5] c.n.l.m.service.impl.MissionServiceImpl  : Mission service received trajectory [[2.0, 3.0], [3.0, 3.0], [4.0, 3.0], [5.0, 3.0]]
2022-09-23 00:17:48.798  INFO 9804 --- [ AsyncThread -3] c.n.l.m.simulator.MissionControl         : Sending waypoint 0
2022-09-23 00:17:48.798  INFO 9804 --- [ AsyncThread -3] c.n.l.m.service.impl.CommandServiceImpl  : Commanding robot to move to [2.0, 3.0]
2022-09-23 00:17:48.799  INFO 9804 --- [ AsyncThread -7] c.n.l.m.simulator.SimulatedRobot         : Robot is now at <[2.0, 3.0]>
2022-09-23 00:17:49.819  INFO 9804 --- [ AsyncThread -3] c.n.l.m.simulator.MissionControl         : Sending waypoint 1
2022-09-23 00:17:49.819  INFO 9804 --- [ AsyncThread -3] c.n.l.m.service.impl.CommandServiceImpl  : Commanding robot to move to [3.0, 3.0]
2022-09-23 00:17:49.819  INFO 9804 --- [ AsyncThread -4] c.n.l.m.simulator.SimulatedRobot         : Robot is now at <[3.0, 3.0]>
2022-09-23 00:17:50.493  INFO 9804 --- [nio-9091-exec-1] c.n.l.m.controller.MissionController     : Received trajectory [[1.0, 2.0], [2.0, 2.0], [3.0, 2.0], [4.0, 2.0], [5.0, 2.0], [6.0, 2.0], [7.0, 2.0], [8.0, 2.0], [9.0, 2.0], [10.0, 2.0], [11.0, 2.0], [12.0, 2.0]]
2022-09-23 00:17:50.494  INFO 9804 --- [nio-9091-exec-1] c.n.l.m.service.impl.MissionServiceImpl  : Mission service received trajectory [[1.0, 2.0], [2.0, 2.0], [3.0, 2.0], [4.0, 2.0], [5.0, 2.0], [6.0, 2.0], [7.0, 2.0], [8.0, 2.0], [9.0, 2.0], [10.0, 2.0], [11.0, 2.0], [12.0, 2.0]]
2022-09-23 00:17:50.823  INFO 9804 --- [ AsyncThread -3] c.n.l.m.simulator.MissionControl         : The robot changes the direction to [1.0, 2.0]...
2022-09-23 00:17:51.505  INFO 9804 --- [ AsyncThread -2] c.n.l.m.simulator.MissionControl         : Sending waypoint 0
2022-09-23 00:17:51.505  INFO 9804 --- [ AsyncThread -2] c.n.l.m.service.impl.CommandServiceImpl  : Commanding robot to move to [1.0, 2.0]
2022-09-23 00:17:51.505  INFO 9804 --- [ AsyncThread -1] c.n.l.m.simulator.SimulatedRobot         : Robot is now at <[1.0, 2.0]>
2022-09-23 00:17:52.520  INFO 9804 --- [ AsyncThread -2] c.n.l.m.simulator.MissionControl         : Sending waypoint 1
2022-09-23 00:17:52.520  INFO 9804 --- [ AsyncThread -2] c.n.l.m.service.impl.CommandServiceImpl  : Commanding robot to move to [2.0, 2.0]
2022-09-23 00:17:52.521  INFO 9804 --- [AsyncThread -10] c.n.l.m.simulator.SimulatedRobot         : Robot is now at <[2.0, 2.0]>
2022-09-23 00:17:53.528  INFO 9804 --- [ AsyncThread -2] c.n.l.m.simulator.MissionControl         : Sending waypoint 2
2022-09-23 00:17:53.528  INFO 9804 --- [ AsyncThread -2] c.n.l.m.service.impl.CommandServiceImpl  : Commanding robot to move to [3.0, 2.0]
2022-09-23 00:17:53.529  INFO 9804 --- [ AsyncThread -5] c.n.l.m.simulator.SimulatedRobot         : Robot is now at <[3.0, 2.0]>
2022-09-23 00:17:54.543  INFO 9804 --- [ AsyncThread -2] c.n.l.m.simulator.MissionControl         : Sending waypoint 3
2022-09-23 00:17:54.543  INFO 9804 --- [ AsyncThread -2] c.n.l.m.service.impl.CommandServiceImpl  : Commanding robot to move to [4.0, 2.0]
2022-09-23 00:17:54.543  INFO 9804 --- [ AsyncThread -8] c.n.l.m.simulator.SimulatedRobot         : Robot is now at <[4.0, 2.0]>
2022-09-23 00:17:55.222  INFO 9804 --- [nio-9091-exec-2] c.n.l.m.controller.MissionController     : Received trajectory [[5.0, 1.0], [6.0, 2.0], [7.0, 3.0], [8.0, 4.0]]
2022-09-23 00:17:55.223  INFO 9804 --- [nio-9091-exec-2] c.n.l.m.service.impl.MissionServiceImpl  : Mission service received trajectory [[5.0, 1.0], [6.0, 2.0], [7.0, 3.0], [8.0, 4.0]]
2022-09-23 00:17:55.543  INFO 9804 --- [ AsyncThread -2] c.n.l.m.simulator.MissionControl         : The robot changes the direction to [5.0, 1.0]...
2022-09-23 00:17:56.238  INFO 9804 --- [ AsyncThread -9] c.n.l.m.simulator.MissionControl         : Sending waypoint 0
2022-09-23 00:17:56.238  INFO 9804 --- [ AsyncThread -9] c.n.l.m.service.impl.CommandServiceImpl  : Commanding robot to move to [5.0, 1.0]
2022-09-23 00:17:56.238  INFO 9804 --- [ AsyncThread -6] c.n.l.m.simulator.SimulatedRobot         : Robot is now at <[5.0, 1.0]>
2022-09-23 00:17:57.240  INFO 9804 --- [ AsyncThread -9] c.n.l.m.simulator.MissionControl         : Sending waypoint 1
2022-09-23 00:17:57.240  INFO 9804 --- [ AsyncThread -9] c.n.l.m.service.impl.CommandServiceImpl  : Commanding robot to move to [6.0, 2.0]
2022-09-23 00:17:57.240  INFO 9804 --- [ AsyncThread -7] c.n.l.m.simulator.SimulatedRobot         : Robot is now at <[6.0, 2.0]>
2022-09-23 00:17:58.240  INFO 9804 --- [ AsyncThread -9] c.n.l.m.simulator.MissionControl         : Sending waypoint 2
2022-09-23 00:17:58.240  INFO 9804 --- [ AsyncThread -9] c.n.l.m.service.impl.CommandServiceImpl  : Commanding robot to move to [7.0, 3.0]
2022-09-23 00:17:58.256  INFO 9804 --- [ AsyncThread -4] c.n.l.m.simulator.SimulatedRobot         : Robot is now at <[7.0, 3.0]>
2022-09-23 00:17:59.276  INFO 9804 --- [ AsyncThread -9] c.n.l.m.simulator.MissionControl         : Sending waypoint 3
2022-09-23 00:17:59.276  INFO 9804 --- [ AsyncThread -9] c.n.l.m.service.impl.CommandServiceImpl  : Commanding robot to move to [8.0, 4.0]
2022-09-23 00:17:59.276  INFO 9804 --- [ AsyncThread -3] c.n.l.m.simulator.SimulatedRobot         : Robot is now at <[8.0, 4.0]>
```

### When robot receives a trajectory with points []

Input:

1. `[[1,3],[2,3],[3,3],[4,3],[5,3],[6,3],[7,3],[8,3],[9,3],[10,3],[11,3],[12,3]]`
2. `[]`

Output:

```
2022-09-23 00:19:12.491  INFO 9804 --- [nio-9091-exec-4] c.n.l.m.controller.MissionController     : Received trajectory [[1.0, 3.0], [2.0, 3.0], [3.0, 3.0], [4.0, 3.0], [5.0, 3.0], [6.0, 3.0], [7.0, 3.0], [8.0, 3.0], [9.0, 3.0], [10.0, 3.0], [11.0, 3.0], [12.0, 3.0]]
2022-09-23 00:19:12.492  INFO 9804 --- [nio-9091-exec-4] c.n.l.m.service.impl.MissionServiceImpl  : Mission service received trajectory [[1.0, 3.0], [2.0, 3.0], [3.0, 3.0], [4.0, 3.0], [5.0, 3.0], [6.0, 3.0], [7.0, 3.0], [8.0, 3.0], [9.0, 3.0], [10.0, 3.0], [11.0, 3.0], [12.0, 3.0]]
2022-09-23 00:19:13.507  INFO 9804 --- [AsyncThread -10] c.n.l.m.simulator.MissionControl         : Sending waypoint 0
2022-09-23 00:19:13.507  INFO 9804 --- [AsyncThread -10] c.n.l.m.service.impl.CommandServiceImpl  : Commanding robot to move to [1.0, 3.0]
2022-09-23 00:19:13.508  INFO 9804 --- [ AsyncThread -9] c.n.l.m.simulator.SimulatedRobot         : Robot is now at <[1.0, 3.0]>
2022-09-23 00:19:14.520  INFO 9804 --- [AsyncThread -10] c.n.l.m.simulator.MissionControl         : Sending waypoint 1
2022-09-23 00:19:14.520  INFO 9804 --- [AsyncThread -10] c.n.l.m.service.impl.CommandServiceImpl  : Commanding robot to move to [2.0, 3.0]
2022-09-23 00:19:14.520  INFO 9804 --- [ AsyncThread -5] c.n.l.m.simulator.SimulatedRobot         : Robot is now at <[2.0, 3.0]>
2022-09-23 00:19:15.533  INFO 9804 --- [AsyncThread -10] c.n.l.m.simulator.MissionControl         : Sending waypoint 2
2022-09-23 00:19:15.533  INFO 9804 --- [AsyncThread -10] c.n.l.m.service.impl.CommandServiceImpl  : Commanding robot to move to [3.0, 3.0]
2022-09-23 00:19:15.534  INFO 9804 --- [ AsyncThread -8] c.n.l.m.simulator.SimulatedRobot         : Robot is now at <[3.0, 3.0]>
2022-09-23 00:19:16.551  INFO 9804 --- [AsyncThread -10] c.n.l.m.simulator.MissionControl         : Sending waypoint 3
2022-09-23 00:19:16.551  INFO 9804 --- [AsyncThread -10] c.n.l.m.service.impl.CommandServiceImpl  : Commanding robot to move to [4.0, 3.0]
2022-09-23 00:19:16.551  INFO 9804 --- [ AsyncThread -2] c.n.l.m.simulator.SimulatedRobot         : Robot is now at <[4.0, 3.0]>
2022-09-23 00:19:17.562  INFO 9804 --- [AsyncThread -10] c.n.l.m.simulator.MissionControl         : Sending waypoint 4
2022-09-23 00:19:17.562  INFO 9804 --- [AsyncThread -10] c.n.l.m.service.impl.CommandServiceImpl  : Commanding robot to move to [5.0, 3.0]
2022-09-23 00:19:17.563  INFO 9804 --- [ AsyncThread -6] c.n.l.m.simulator.SimulatedRobot         : Robot is now at <[5.0, 3.0]>
2022-09-23 00:19:17.626  INFO 9804 --- [nio-9091-exec-7] c.n.l.m.controller.MissionController     : Received trajectory []
2022-09-23 00:19:17.626  INFO 9804 --- [nio-9091-exec-7] c.n.l.m.service.impl.MissionServiceImpl  : Mission service received trajectory []
2022-09-23 00:19:18.571  INFO 9804 --- [AsyncThread -10] c.n.l.m.simulator.MissionControl         : The robot stops
```

# Clone

The repository URL is specified as below:
```
```

You can use the above repository URL to clone this project and setup it up local system by following the setup instructions.

# Conclusion

To conclude, this is a simple service which can be used to release waypoints to a simulated robot one at a time in way which allows changes or cancellation. 
# IOT: Remote Control Lights by Clapping
## Basic Idea
![Basic Idea](/img/idea.PNG)

## Use Case
1. Users want to join the IoT network, the bootstrap procedure allows the devices be accepted, be recognized and be served in the IoT network.
2. Users register into the network.
3. Users start the setting mode and set the clap sound for specific light.
4. Users do corresponding clapping sound and server will recognize these clapping sound and do execute responses on the specific light. 

## Function
2 modes:
1. Setting Mode
    * Press “Setting” button to set your own clapping sound for corresponding light.
2. Control Mode
    * Use your setted clapping sound to control the light.


## Architecture Diagram
![Architecture Diagram](/img/architecture.PNG)

## Server / Bootstrap Server
Bootstrap Server
* LwM2M bootstrap server(Leshan)
* Providing the clients with the information required to register the server.
Server
* LwM2M server(Leshan)
* Providing registration service for client
* Observing the data changes of the sound sensor client. 
* MongoDB database
  * Clients registration status
  * Clients device information
  * Clients setting data

## Client
* LWM2M Client (Leshan)
* SQLite Database:
  * Rigrester Record
  * Execute Record
* Raspberry Pi
![Client Diagram](/img/client.PNG)

## LWM2M Object
* Sound detector 
  * Mode, R
  * Sensor Value, R
  * Endpoint name, R

* Light 
  * Switch, E




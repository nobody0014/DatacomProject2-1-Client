Client for Project 2 phase 1
File for you to change the source code is in src/main/java/Client.java

to use:
mvn clean package  --> to create the target folder and the jar file in it

Posting parcels:
-o 1 -p <ParcelID> -s <StationID> 
--> however, i change my code in such a way that the station you post wont matter but it will post the parcel from station 0 to 100
--> you can change if you want


Observing parcel trail:
-o 2 -p <ParcelID>

Observing station stop count:
-o 3 -s <StationID>
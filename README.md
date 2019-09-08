# ECE496-PUT
ECE496 design project

## Project Overview:
* This project is to implement a IoT device which help users better cope with their household wastages within the fridge.
* This project consists of three parts:
    * Server (Zongxin Liu)
    * Database (Shifan Gu)
    * Android App (Zheping Jiang, Shifan Gu)

### Server:
* Developed a server(called a bridge) which handles incoming HTTP requests (e.g. the request are interpreted through the URL user has passed in) with Raspberry PI.
* Implemented the communication logic between the servers and the Android App.
* Implemented lock mechanism when there exists concurrent user requests.
* Implemented the internal light sensor logic which allows the camera to take photos after the user has open the door of the fridge.
* Implemented the internal temperature sensor logic which shows the user current temperature and set alert if the temperature becomes unreasonably high/low.
* Implemented the internal air quality sensor logic which allows the user to be notified if the air quality within the fridge becomes bad; this bascially means that the fridge contains excessive TVOC.
* Implemented the internal weight sensor logic which allows the user to see accurately within the app the weight of the food (this indicates how much the food is left).
* Implemented the Barcode Scanner which allows the user just to scan the barcode on the food, the server will try to find the cooresponding food category this food might belongs to, which significantly reduces the user input time.

### Database: 
* Developed a database using MySQL which stores data into the bridge; server connects to it by issuing the shell script.

### Android App: 
* Implement the communication logic between the Android App and the bridge, using 
* Implemented the DisplayFragment, which displays to user what is currently in the fridge; it also shows expiration date of the food, the amount of food that is left inside the fridge; it also provides easy deletion functionality to allow user delete items directly from this screen.
* Implement the AddFragment, which allows user to add a newly-bought item into the system.
* Implement the RemoveFragment, which allows user to remove item from the system once he/she has finished consuming it; it functionality can be replaced by the deletion functionality in the DisplayFragment.
* Implement the InfoFragment which displays several some other information like the temperature, weight, air quality, image of the fridge interior etc.
* Implement the TimelineFragment, which shows to the user his/her food consumption history.


## Final Result:


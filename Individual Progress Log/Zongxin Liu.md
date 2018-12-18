# Zongxin Liu
## Comming next
### MOSFET
* Working on USB power switches
* Working light sensors (LDR) to know if the fridge is on or off

## Dec. 17 2018
### Webserver act as API interface
* Be able to get sensor values using HTTP GET (temperature and interrior camera)
* Working on responsiveness
* Working on robustiveness and error handling

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/webapi.jpg?raw=true "website")

## Dec. 9 2018
### Exchange camera module with fisheye model

* Fisheye camera can reach 2 levels of fridge. Better than regualr models

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/egg_after.jpg?raw=true "egg after")

* Demo on that we can clearly see the number of eggs was more in the past

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/egg_before.jpg?raw=true "egg before")

## Nov. 29 2018
### Development of interiror camera
* The long extension cable works well
* Be able to shoot image inside the fridge

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/init_interior.jpg?raw=true "initial interior photo")

* Interconnect throught the door is working ok

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/IMG_0539.jpeg?raw=true "inter connect")

* Using tapes for initial developement

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/IMG_0540.jpeg?raw=true "camera placement")

* Update exterior look with added connections

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/IMG_0541.jpeg?raw=true "exterior update")

                                                                           

### Issues
* Camera in stock cannot shoot much -> buy a wideangle one
* Lighting is not ideal and it cannot switch on and off yet -> building MOSFET to switch it (controlled by one GPIO)

## Nov. 25 2018
### Development of demo mountware for development
* Breadboard and sensors are now going onto the fridge
* Temperature sensor keeps loging into a csv file with loging size control
* Figure out the real temperature inside my fridge is about 9~10 C.

### Example output of logging (date and time, temperature in C)
```
Sun 25 Nov 17:38:45 EST 2018, 9.687
Sun 25 Nov 17:38:46 EST 2018, 9.687
Sun 25 Nov 17:38:47 EST 2018, 9.687
Sun 25 Nov 17:38:48 EST 2018, 9.687
Sun 25 Nov 17:38:49 EST 2018, 9.625
Sun 25 Nov 17:38:50 EST 2018, 9.625
Sun 25 Nov 17:38:51 EST 2018, 9.625
Sun 25 Nov 17:38:52 EST 2018, 9.625
Sun 25 Nov 17:38:53 EST 2018, 9.687
Sun 25 Nov 17:38:54 EST 2018, 9.625
Sun 25 Nov 17:38:55 EST 2018, 9.625
Sun 25 Nov 17:38:55 EST 2018, 9.625
Sun 25 Nov 17:38:57 EST 2018, 9.625
```
### Plot 24h change in temperature

* Note the small glitch, that is temperature change when opeening the door

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/temp_log_demo_nov_2425.jpg?raw=true "24h plot")

### Mountware
![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/IMG_0517.jpeg?raw=true "mount")
### Interior
![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/IMG_0518.jpeg?raw=true "temp sensor inside")
## Nov. 11 2018
### Progress on temperature sensor
* The goal is the to be able to communicate with a DS18b20 sensor using Raspberry Pi.
* It requires one extra 4.7k resistor.
* Objective is to fully understand the bytes it sends and decode useing Perl instead of random Python code avalible online. 
* Sensor comes with no pins, already soldered jump wires for this in design centre.

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/IMG_0438.jpeg?raw=true "Sensor")


### Example output while pressing the sensor with hand
```
pi@raspberrypi:~/proj $ ./readout.pl 
Temperature is 26.312
pi@raspberrypi:~/proj $ ./readout.pl 
Temperature is 26.312
pi@raspberrypi:~/proj $ ./readout.pl 
Temperature is 26.312
pi@raspberrypi:~/proj $ ./readout.pl 
Temperature is 26.687
pi@raspberrypi:~/proj $ ./readout.pl 
Temperature is 27.187
pi@raspberrypi:~/proj $ ./readout.pl 
Temperature is 27.625
pi@raspberrypi:~/proj $ ./readout.pl 
Temperature is 28
pi@raspberrypi:~/proj $ 
```

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/IMG_0439.jpeg?raw=true "Configure sensor")

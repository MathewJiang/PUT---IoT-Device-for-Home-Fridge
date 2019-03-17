# Zongxin Liu
## Comming next
* Integrate barcode scanner

## Jan. 9 2019

* New cardboard all in one package

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/prototype.jpeg?raw=true "board")

## Jan. 6 2019
### Weight sensor
* Use sparkfan OpenScale, accracy is within 1g compare to reference scale.
* Machanical part is using one out the shelf scale but disassembed for this project.

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/scale.jpeg?raw=true "weight sensor")

### Air quality sensor
* TVOC can be helpful indicating if something goes bad inside a fridge.
* Test against one bag of organic garbage, readings reaches maxium otherwise very low.
* eCO2 can also change overtime, give an idea how CO2 is building up with bacteria.

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/css811.jpeg?raw=true "air quality sensor")

#### Both readings can be accessed from webserver

## Dec. 30 2018
### Sql interface enabled
* Build tunnel for APP to talk with DB.
* Update command documentation.

## Dec. 26 2018
### Photo taken with light sensor
* Finished inplementation of light sensor, so the fridge only takes photo after the door is opened and closed for 10s.
* Old photo is kept for future use.
* Light sensor relies on RC time from 0 to 1 with 1uF capacitor. RC time swings from 200 (complete dark) to 900 (somewhat bright after the door is open)
* Assume fridge performs ok so this swing is good enough so we can ignore temperature affect on resistance.

* Sensor inside the fridge

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/light_sensor.jpg?raw=true "light sensor")

* External look of the board

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/board_with_LDR.jpg?raw=true "pin 6 used")

## Dec. 17 2018
### Webserver act as API interface
* Be able to get sensor values using HTTP GET (temperature and interrior camera)
* Working on responsiveness
* Working on robustiveness and error handling

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/webapi.jpg?raw=true "website")

## Dec. 9 2018
### Exchange camera module with fisheye model

* Camera looks like this

![Alt Image Text](https://github.com/WyattLiu/ECE496-PUT/blob/master/Individual%20Progress%20Log/Zongxin%20Pictures/camera.jpg?raw=true "camera")

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

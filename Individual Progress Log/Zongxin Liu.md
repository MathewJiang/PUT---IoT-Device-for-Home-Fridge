# Zongxin Liu
## Comming next
### Camera module
* Ordered camera extension cable and working on light/flash system to take pictures

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

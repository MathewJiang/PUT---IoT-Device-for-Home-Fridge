# Zongxin Liu

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

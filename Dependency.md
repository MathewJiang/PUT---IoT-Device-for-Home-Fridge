Missing usb lib will have this error

```
cc  -g -O0 -Wall -Wextra -std=c99 -pedantic -DPROGRAM_VERSION=\"3c8b\" uhubctl.c -o uhubctl -Wl,-z,relro -lusb-1.0
uhubctl.c:35:31: fatal error: libusb-1.0/libusb.h: No such file or directory
 #include <libusb-1.0/libusb.h>
                               ^
compilation terminated.
Makefile:40: recipe for target 'uhubctl' failed
make: *** [uhubctl] Error 1
```

Use ``sudo apt-get install libusb-1.0-0-dev`` to fix

Rpi 3B+ got 2 USB hubs, use -l 1.1

Unit test USB light passes with above 2 fixed.

``dtoverlay=w1-gpio,gpiopin=6`` Must be configured for ``/boot/config.txt`` and one-wire must be enabled to get temperature sensor working, I configure it to 6 instead of 4 before as I soldered it there by mistake, so change in software.

``Can't locate HTTP/Server/Simple/CGI.pm in @INC ``
Fixing by 

```sudo apt-get install libcgi-session-perl
sudo cpan install HTTP::Server::Simple::CGI
Loading internal null logger. Install Log::Log4perl for logging messages

```

Use to initailize mysql
```
sudo apt-get install mysql-server python-mysqldb
```




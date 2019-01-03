## Install CCS811
* First, make sure Rpi is up to date

``sudo apt-get update``

* Second, get python lib installed 

``sudo apt-get install -y build-essential python-pip python-dev python-smbus git``

* Clong online interface

``git clone https://github.com/adafruit/Adafruit_Python_GPIO.git``

* Install online package

``sudo python setup.py install``

* Pull chip lib

``sudo pip install Adafruit_CCS811``

* Enable I2C

``sudo raspi-config``

* Slow done I2C using
``dtparam=i2c_baudrate=10000`` to file
``/boot/config.txt``

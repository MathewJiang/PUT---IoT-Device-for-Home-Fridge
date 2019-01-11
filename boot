cd ./simple_webserver
./server.pl &
cd ./camera_api
./light_goes_on_then_off_wait_10s_take_photo_log.pl ../../thirdparty/uhubctl/uhubctl ./lock & 
cd ../../sensor_codes
./polling_temperature.pl >/dev/null &
cd CCS811
./ccs811demo | egrep ".*,.*," >> css811.log &
cd ../../simple_webserver/key_value_pair_db/
./db.pl &

echo "Done"

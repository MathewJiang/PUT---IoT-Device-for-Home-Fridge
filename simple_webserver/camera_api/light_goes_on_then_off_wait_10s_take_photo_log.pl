#!/usr/bin/perl
use strict;
use warnings;
use English;
use Fcntl ':flock';

our $path = `pwd`;
chomp $path;
$path =~ s/\/ECE496-PUT\/.*//g;

my $light_sensor = "$path/ECE496-PUT/sensor_codes/Light_Sensor/light_sensor.py";

my $lock = $ARGV[1];
my $threshold = 900;
while(1) {
	my $rc_time = 0;
	do {
	$rc_time = `$light_sensor`;
	chomp $rc_time;
	#print "Dark\n";
	} while($rc_time > $threshold); # this is dark
LOOP:
	do {
	$rc_time = `$light_sensor`;
	chomp $rc_time;
	#print "Bright\n";
	} while($rc_time < $threshold); # this is bridgt and flash is done

	sleep(100);
	#print "take a photo\n";
	$rc_time = `$light_sensor`;

	if($rc_time < $threshold) {
		goto LOOP;
	}	
	my $lock_h =plock();
	take_photo();
	
	#print "$PID is taking photo\n";
	punlock($lock_h);
}


sub plock {
	open(my $fh, '>>', $lock) or die "Cannot lock $lock\n";
	flock($fh, LOCK_EX) or die "Cannot lock $lock 2\n";
	return $fh;
}

sub punlock {
	my $fh = shift;
	close($fh) or die "cannot unlock\n";
}

sub write_to_weight_file {
	`cat /dev/ttyUSB0 | $path/ECE496-PUT/sensor_codes/read_scale.pl >> $path/ECE496-PUT/sensor_codes/read_scale.log`;
}

sub take_photo {
	`sudo $ARGV[0] -l 1-1 -a on -p 2`;
	`raspistill -vf -hf -w 1920 -h 1080 -q 75 -o $path/ECE496-PUT/simple_webserver/camera_api/latest.jpg`;
	 write_to_weight_file();
	`sudo $ARGV[0] -l 1-1 -a off -p 2`;

	my $date = `$path/ECE496-PUT/simple_webserver/camera_api/date_photo.pl`;
	chomp $date;
	`cp $path/ECE496-PUT/simple_webserver/camera_api/latest.jpg $path/ECE496-PUT/simple_webserver/camera_api/$date.jpg`;
	}


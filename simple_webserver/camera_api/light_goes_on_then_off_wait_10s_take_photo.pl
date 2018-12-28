#!/usr/bin/perl
use strict;
use warnings;
use English;
use Fcntl ':flock';

my $light_sensor = "/home/pi/proj/gited/ECE496-PUT/sensor_codes/Light_Sensor/light_sensor.py";

my $lock = $ARGV[1];
my $threshold = 500;
while(1) {
	my $rc_time = 0;
	do {
	$rc_time = `$light_sensor`;
	chomp $rc_time;
	#print "Dark\n";
	} while($rc_time > $threshold); # this is dark
	
	do {
	$rc_time = `$light_sensor`;
	chomp $rc_time;
	#print "Bright\n";
	} while($rc_time < $threshold); # this is bridgt and flash is done

	sleep(10);
	#print "take a photo\n";	
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

sub take_photo {
	`sudo $ARGV[0] -a on -p 2`;
	`raspistill -vf -hf -w 640 -h 480 -q 75 -o /home/pi/proj/gited/ECE496-PUT/simple_webserver/camera_api/latest.jpg`;
	`sudo $ARGV[0] -a off -p 2`;
}
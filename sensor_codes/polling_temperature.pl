#!/usr/bin/perl
use strict;
use warnings;

# make sure the IO is ON
`./readout.pl`;

my $max_log_file_size = 10000;

`touch ./temp.log`;
open(my $fh, '>', "./temp.log");

while(1) {
	my $temp = `./fast_readout_temp.pl | awk '{print \$3}'`;
	my $date = `date`;
	chomp $date;
	print $fh "$date, $temp";
	my $log_size = `du ./temp.log | awk '{print \$1}'`;
	chomp $log_size;
	my $util = $log_size / $max_log_file_size;
	printf "Log size $log_size/$max_log_file_size ($util)\n";
}

#!/usr/bin/perl
use strict;
use warnings;
use Fcntl ':flock';

my $lock = $ARGV[0];

my $lock_h = plock($lock);
my $file = "camera_api/latest.jpg";
my $length = -s $file;
print "Content-type: image/jpg\n";
print "Content-length: $length \n\n";
binmode STDOUT;
open (FH,'<', $file) || die "Could not open $file: $!";
my $buffer = "";
while (read(FH, $buffer, 10240)) {
    print $buffer;
}

	punlock($lock_h);


sub plock {
	my $lock = shift;
	open(my $fh, '>>', $lock) or die "Cannot lock $lock\n";
	flock($fh, LOCK_EX) or die "Cannot lock $lock 2\n";
	return $fh;
}

sub punlock {
	my $fh = shift;
	close($fh) or die "cannot unlock\n";
}


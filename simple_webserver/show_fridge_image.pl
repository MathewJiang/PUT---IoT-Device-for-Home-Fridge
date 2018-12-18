#!/usr/bin/perl
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


#!/usr/bin/perl
use strict;
use warnings;
use Data::Dumper;

our %hash;
our $db_file = `readlink -f ./db.kvp`;
chomp $db_file;
print "db_file located at $db_file\n";
# read in db from file
open(my $fh,'<',$db_file) or die "Cannot open DB";
while(my $line = <$fh>) {
	if($line =~ /(.*)<MaGiCDiv>(.*)/) {
		my $key = $1;
		my $value = $2;
		$hash{$key} = $value;
	} else {
		print "Error: format\n";
	}	
}

close $fh;

print Dumper(\%hash);

my $cmd_in = $db_file;
$cmd_in =~ s/kvp/cmd_in/g;
my $data_out = $db_file;
$data_out =~ s/kvp/data_out/g;

print "cmd_in open at $cmd_in\ndata_out open at $data_out\n";
`mkfifo $cmd_in`;
`mkfifo $data_out`;

my $Div = "<MaGiCDiv>";
my $Op_div = "<OpDiv>";
while(1) {
	open(my $fi,'<',$cmd_in);
	open(my $fo,'>',$data_out);
	# permitting 4 OPs, insert, find, delete and update;
	my $data_in;
	my $counter = 0;
	while(my $line = <$fi>) {
		$data_in = $line;
		$counter ++;
	}
	if($counter != 1) {
		print $fo "Error: one line only, saw $counter lines\n";
	} else {
		# see what OP it is
		if($data_in =~ /(.*)$Op_div(.*)/) {
			my $op_code = $1;
			my $data = $2;
			if($op_code eq "insert") {
				if($data=~/(.*)$Div(.*)/) {
					my $key = $1;
					my $value = $2;
					if(exists $hash{$key}) {
						if($hash{$key} eq $value) {
							# this is ok but do nothing;
						} else {
							print $fo "Error: inserting existing entry, try update\n";
						}
					} else {
						# need to insert to hash and append to file.
						open(my $fh,'>>',$db_file) or die "Cannot open DB";
						print $fh "$data\n";
						close $fh;
						$hash{$key} = $value;
						print $fo "Success\n";
					}
				} else {
					print $fo "Error: insert format error\n";
				}	
			} elsif($op_code eq "find") {
				if(exists $hash{$data}) {
					print $fo $hash{$data};
				} else {
					print $fo "VOID\n";
				}
			} elsif($op_code eq "delete") {
				if(exists $hash{$data}) {
					delete $hash{$data};	
					delete_entry_in_file($data);
					print $fo "Success\n";
				} else {
					print $fo "Error; deleting non existing entry";
				}
				} elsif($op_code eq "update") {
				if($data=~/(.*)$Div(.*)/) {
					my $key = $1;
					my $value = $2;
					if(exists $hash{$key}) {
						if($hash{$key} eq $value) {
							# this is ok but do nothing;
						} else {
							$hash{$key} = $value;
						}
					} else {
						print $fo "Error: updating non-exisitng entry\n";
					}
				} else {
					print $fo "Error: insert format error\n";
				}
			} elsif($op_code eq "ls") {
				print $fo Dumper(\%hash);
			} else {
				print $fo "Error: unknown op\n";
			}
		} else {
			print $fo "Error: Operation format error\n";
		}
	}
	
	close $fi;
	close $fo;
}

sub delete_entry_in_file {
	my $entry = shift;
	my $swap_file = $db_file;
	$swap_file =~ s/kvp/swap/g;
	open(my $fd, '<', $db_file);
	open(my $sfd, '>', $swap_file);
	while(my $line = <$fd>) {
		if($line =~ /^$entry<MaGiCDiv>/) {
			next;
		}
		print $sfd $line;
	}		
	close $fd;
	close $sfd;
	`mv $swap_file $db_file`;
	
}

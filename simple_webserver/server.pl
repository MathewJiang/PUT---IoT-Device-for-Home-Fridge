#!/usr/bin/perl
use strict;
use warnings;
use threads;
use Fcntl qw< LOCK_EX SEEK_END >;

our $lock_file = `readlink -f ./camera_api/lock`;
chomp $lock_file;
our $usb_control = `readlink -f ../thirdparty/uhubctl/uhubctl`;
chomp $usb_control;
our $testfile = `readlink -f ./testfile.txt`;
our $data_delivery_on_path = "VOID";
our $raw_sql = `readlink -f ../db/db_scripts/putsDB.sh`;
our $barcode_reader = `readlink -f ../sensor_codes/read_barcode_real.sh`;
our $status_file = `readlink -f ./status_file`;
{
package PutsWebServerA1;
 
use HTTP::Server::Simple::CGI;
use base qw(HTTP::Server::Simple::CGI);

#our $usb_control = `readlink -f ../thirdparty/uhubctl/uhubctl`;
#chomp $usb_control;

#our $lock_file = `readlink -f ./camera_api/lock`;

#chomp $lock_file;
print "USB control located at $usb_control \n";
my %dispatch = (
    '/' => \&resp_root,
    '/show_fridge_image' => \&resp_image,
    '/hello_world' => \&resp_hello,
    '/read_testfile' => \&resp_read_testfile,
    '/show_temperature' => \&resp_show_temperature,
    '/show_latest_fridge_image_date' => \&resp_show_latest_fridge_image_date,
    '/get_weight' => \&resp_get_weight,
    '/ls_barcode_cache' => \&resp_ls_barcode_cache,
    '/get_ccs811' => \&resp_ccs811,
    '/turn_on_USB' => \&resp_USB_on,
    '/turn_off_USB'=> \&resp_USB_off,
    '/read_scanner' => \&read_barcode,
    '/get_alert' => \&get_error_msg
	# ...
);


 
sub handle_request {
    my $self = shift;
    my $cgi  = shift;
    my $path = $cgi->path_info();
    my $handler;
    if($path =~ /\/append_testfile\/(.*)/) {
	$handler = \&resp_append_testfile;
	$data_delivery_on_path = $1;
    } elsif($path =~ /\/write_testfile\/(.*)/) {
	$handler = \&resp_write_testfile;
	$data_delivery_on_path = $1;
    } elsif($path =~ /\/raw_sql\/(.*)/) {
	$handler = \&resp_raw_sql;
	$data_delivery_on_path = $1;	
    } elsif($path =~ /\/raw_sql_br\/(.*)/) {
	$handler = \&resp_raw_sql_br;
	$data_delivery_on_path = $1;	
    } elsif($path =~ /\/raw_sql_html\/(.*)/) {
	$handler = \&resp_raw_sql_html;
	$data_delivery_on_path = $1;	
    } elsif($path =~ /\/barcode_lookup\/(.*)/) {
	$handler = \&resp_barcode_lookup;
	$data_delivery_on_path = $1;	
    } else {
    	$handler = $dispatch{$path};
    }
    if (ref($handler) eq "CODE") {
        print "HTTP/1.0 200 OK\r\n";
        $handler->($cgi);
         
    } else {
        print "HTTP/1.0 404 Not found\r\n";
        print $cgi->header,
              $cgi->start_html('Error'),
              $cgi->h1('Not found'),
	      $cgi->h2("CODE is $path");
              $cgi->end_html;
    }
}

sub resp_ccs811 {
    my $cgi  = shift;   # CGI.pm object
    return if !ref $cgi;
    my $scale = `cat ../sensor_codes/CCS811/css811.log | tail -n 2 | head -n 1`;
	if($scale =~ /(.*),/) {
		$scale = $1;
	}
    my $who = $cgi->param('name');
    print $cgi->header,
          $cgi->start_html("ccs811 response"),
	  $cgi->h1($scale);
	  $cgi->end_html;


}

sub resp_barcode_lookup {
	#printf STDERR "DEBUG: inside handler\n";
    my $cgi  = shift;   # CGI.pm object
    return if !ref $cgi;
    my $in = `readlink -f ./key_value_pair_db/db.cmd_in`;
    chomp $in;
    my $out = `readlink -f ./key_value_pair_db/db.data_out`;
    chomp $out;
    my $digits = $data_delivery_on_path;
    # print STDERR "DEBUG: $digits\n";
    my $who = $cgi->param('name');
    my $command = "echo 'find<OpDiv>$digits' > $in; cat $out";
    #print STDERR "DEBUG: cmd is $command";
    my @search_result = `$command`;
    my $search_result = "";
    foreach my $line (@search_result) {
	chomp $line;
	$search_result .= $line;
    }
    my $response = "";
    #print STDERR "DEBUG: search_result is $search_result\n"; 
    if($search_result eq "VOID") {
	my @lookup = `echo $digits | ./key_value_pair_db/look_up_barcode.py`;
	if(scalar @lookup == 0) {
		$response = "Not found, consider insert one entry";
	} else {
		foreach my $line (@lookup) {
			$response .= $line;	
		}
	chomp $response;	
		my $command = "echo 'insert<OpDiv>$digits<MaGiCDiv>$response' > $in; cat $out";	
	#	print STDERR "DEBUG trying insert with $command\n";
		`$command`;
	}
    } else {
	$response = $search_result;
    }

    print $cgi->header,
          $cgi->start_html("barcode lookup response"),
	  $cgi->h1($response);
	  $cgi->end_html;


}

sub resp_ls_barcode_cache {
    my $in = `readlink -f ./key_value_pair_db/db.cmd_in`;
    chomp $in;
    my $out = `readlink -f ./key_value_pair_db/db.data_out`;
    chomp $out; 
    my $cgi  = shift;   # CGI.pm object
    return if !ref $cgi;
    my $who = $cgi->param('name');
    my @response = `echo 'ls<OpDiv>' > $in; cat $out`;
    print $cgi->header,
          $cgi->start_html("sql response");
	  print "<p>";
	  foreach my $line (@response) {
    		print $line;
		print "<br>";	
	  }
	print "</p>";
	  print $cgi->end_html;

}

sub resp_raw_sql {
	 
	my $cgi  = shift;   # CGI.pm object
    return if !ref $cgi;
    my $who = $cgi->param('name');
    my @response = `sudo mysql -e "$data_delivery_on_path" -N -s -r`;
    #print STDERR "DEBUG: data_delivery_on_path $data_delivery_on_path\n";
    my $response_string = "";
    foreach my $line (@response) {
	$response_string .=$line;
	$response_string .=";";
    }
    #print STDERR "DEBUG: response string is $response_string\n";
    print $cgi->header,
          $cgi->start_html("sql response"),
	  $cgi->h1($response_string);
	  $cgi->end_html;

}

sub resp_raw_sql_html {
	 
	my $cgi  = shift;   # CGI.pm object
    return if !ref $cgi;
    my $who = $cgi->param('name');
    my @response = `sudo mysql -e "$data_delivery_on_path" -s -r -H`;
    #print STDERR "DEBUG: data_delivery_on_path $data_delivery_on_path\n";
    my $response_string = "";
    foreach my $line (@response) {
		$response_string .=$line;
    }
    #print STDERR "DEBUG: response string is $response_string\n";
    print $cgi->header,
          $cgi->start_html("sql response"),
	  $cgi->h1($response_string);
	  $cgi->end_html;

}
sub resp_raw_sql_br {
	 
	my $cgi  = shift;   # CGI.pm object
    return if !ref $cgi;
    my $who = $cgi->param('name');
    my @response = `sudo mysql -e "$data_delivery_on_path" -N -s -r -vvv`;
    #print STDERR "DEBUG: data_delivery_on_path $data_delivery_on_path\n";
    my $response_string = "";
    my $inner_entries = 0;
    foreach my $line (@response) {
	
	if($line =~ /\+\-/) {
		if($inner_entries == 0) {
			$inner_entries = 1;
		} else {
			$inner_entries = 0;
		}
		next;
	}
	if($inner_entries == 1) {
		chomp $line;
		$response_string .=$line;
		$response_string .="<br>";
	}
    }
    #print STDERR "DEBUG: response string is $response_string\n";
    print $cgi->header,
          $cgi->start_html("sql response"),
	  $cgi->h1($response_string);
	  $cgi->end_html;

}

sub resp_get_weight {
	 
	my $cgi  = shift;   # CGI.pm object
    return if !ref $cgi;
    my $scale = `cat ../sensor_codes/read_scale.log | tail -n 1`;
	if($scale =~ /(.*),/) {
		$scale = $1;
	}
    my $who = $cgi->param('name');
    print $cgi->header,
          $cgi->start_html("scale response"),
	  $cgi->h1($scale);
	  $cgi->end_html;

}

sub resp_show_temperature {
    
my $cgi  = shift;   # CGI.pm object
    return if !ref $cgi;
    #`./camera_api/take_a_photo.pl $usb_control`;

    my $who = $cgi->param('name');
    
    my $temperature = `cat ../sensor_codes/temp.log | tail -n 1`;
    chomp $temperature;
    print $cgi->header,
          $cgi->start_html("sensor"),
	  $cgi->h1("$temperature");
	  $cgi->end_html;
}
sub resp_show_latest_fridge_image_date {
    
my $cgi  = shift;   # CGI.pm object
    return if !ref $cgi;
    #`./camera_api/take_a_photo.pl $usb_control`;

    my $who = $cgi->param('name');
    
    my $time_stamp = `ls -lt ./camera_api/latest.jpg | awk '{print \$6,\$7,\$8}'`;
    chomp $time_stamp;
    print $cgi->header,
          $cgi->start_html("image date"),
	  $cgi->h1("$time_stamp");
	  $cgi->end_html;
}

sub resp_root {
    
my $cgi  = shift;   # CGI.pm object
    return if !ref $cgi;
    #`./camera_api/take_a_photo.pl $usb_control`;

    my $who = $cgi->param('name');
    
    my $temperature = `cat ../sensor_codes/temp.log | tail -n 1`;
    my $scale = `cat ../sensor_codes/read_scale.log | tail -n 1`;
    my $time_stamp = `ls -lt ./camera_api/latest.jpg | awk '{print \$6,\$7,\$8}'`;
    chomp $temperature;
    chomp $scale;
    chomp $time_stamp;
    print $cgi->header,
          $cgi->start_html("Root"),
          $cgi->h1("ECE496 Puts projects low level interfaces server"),
	  $cgi->h3("Temperature is $temperature C, Scale is $scale");
	  print "<p>picture is taken at $time_stamp</p>";
	print "<style>
        	* {
            margin: 0;
            padding: 0;
        }
        .imgbox {
            display: grid;
            height: 100%;
        }
        .center-fit {
            max-width: 100%;
            max-height: 100vh;
            margin: auto;
        }
    	</style>";
	  print "<html><body><div class='imgbox'><img class='center-fit 'src=\"show_fridge_image\"></div></body></html>";
	  
	  $cgi->end_html;
}
sub resp_read_testfile {
	my $file_content;
	open (my $fh, '<', $testfile);
	while(<$fh>) {
		$file_content.=$_;
	}
	close $fh;

	my $cgi = shift;
	 return if !ref $cgi;
    	my $who = $cgi->param('name');
   	 print $cgi->header,
          $cgi->start_html("test communication"),
          $cgi->h1($file_content),
	  $cgi->end_html;
	
}

sub resp_append_testfile {
	# this is write only, response success_append_testfile
	my $cgi = shift;
	 return if !ref $cgi;
    	my $who = $cgi->param('name');
   	 print $cgi->header,
          $cgi->start_html("test communication"),
          $cgi->h1("success_append_testfile"),
	  $cgi->end_html;
	
	open(my $tfh, '>>', $testfile);
	print $tfh $data_delivery_on_path;
	close $tfh;	
	$data_delivery_on_path = "VOID";
}
sub resp_write_testfile {
	# this is write only, response success_append_testfile
	my $cgi = shift;
	 return if !ref $cgi;
    	my $who = $cgi->param('name');
   	 print $cgi->header,
          $cgi->start_html("test communication"),
          $cgi->h1("success_write_testfile"),
	  $cgi->end_html;
	
	open(my $tfh, '>', $testfile);
	print $tfh $data_delivery_on_path;
	close $tfh;	
	$data_delivery_on_path = "VOID";
}
sub resp_hello {
	my $cgi = shift;
	 return if !ref $cgi;
    my $who = $cgi->param('name');
    print $cgi->header,
          $cgi->start_html("test communication"),
          $cgi->h1("Hi, Android"),
	  $cgi->end_html;
}
sub resp_image {
	#`./camera_api/take_a_photo.pl $usb_control`;
	my @output = `./show_fridge_image.pl $lock_file`;
	print @output;
}

sub resp_USB_on {
	`sudo $usb_control -l 1-1 -a on -p 2`;
}

sub resp_USB_off {
        `sudo $usb_control -l 1-1 -a off -p 2`;
}

sub read_barcode {
	my $barcode = `$barcode_reader`;
        my @codes = split(/, /,$barcode);
	my $cgi = shift;
	 return if !ref $cgi;
         my $who = $cgi->param('name');
         print $cgi->header,
	 $cgi->start_html("barcode"),
	 $cgi->h1($codes[0]),
	 $cgi->h2($codes[1]),
	 $cgi->end_html;
}
sub get_error_msg {
	my $msg = `cat $status_file`;
	my $cgi = shift;
	 return if !ref $cgi;
    my $who = $cgi->param('name');
    print $cgi->header,
          $cgi->start_html("alert"),
          $cgi->h1($msg),
	  $cgi->end_html;
}

# new server code here



} 
 
# start the server on port 8080
my $pid = PutsWebServerA1->new(8080)->background();
print "Use 'kill $pid' to stop server.\n";
print "lock_file is $lock_file\n";
print "Testfile is $testfile\n";
#async {`./camera_api/light_goes_on_then_off_wait_10s_take_photo_log.pl $usb_control $lock_file`};
#MyWebServer->new(8080);




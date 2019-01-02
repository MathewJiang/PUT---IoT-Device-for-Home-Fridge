#!/usr/bin/perl
use threads;
our $lock_file = `readlink -f ./camera_api/lock`;
chomp $lock_file;
our $usb_control = `readlink -f ../thirdparty/uhubctl/uhubctl`;
chomp $usb_control;
our $testfile = `readlink -f ./testfile.txt`;
our $data_delivery_on_path = "VOID";
our $raw_sql = "readlink -f ../db/db_scripts/putsDB.sh";
{
package PutsWebServer;
 
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
    '/show_latest_fridge_image_date' => \&resp_show_latest_fridge_image_date
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

sub resp_raw_sql {
	 
	my $cgi  = shift;   # CGI.pm object
    return if !ref $cgi;
    my $who = $cgi->param('name');
    my @response = `sudo mysql -e "$data_delivery_on_path" -N -s -r`;
    print STDERR "DEBUG: data_delivery_on_path $data_delivery_on_path\n";
    my $response_string = "";
    foreach my $line (@response) {
	$response_string .=$line;
	$response_string .=";";
    }
    print STDERR "DEBUG: response string is $response_string\n";
    print $cgi->header,
          $cgi->start_html("sql response"),
	  $cgi->h1($response_string);
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
    my $time_stamp = `ls -lt ./camera_api/latest.jpg | awk '{print \$6,\$7,\$8}'`;
    chomp $temperature;
    chomp $time_stamp;
    print $cgi->header,
          $cgi->start_html("Root"),
          $cgi->h1("ECE496 Puts projects low level interfaces tester server(Production)"),
	  $cgi->h3("$temperature C");
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
sub resp_root {
    
my $cgi  = shift;   # CGI.pm object
    return if !ref $cgi;
    #`./camera_api/take_a_photo.pl $usb_control`;

    my $who = $cgi->param('name');
    
    my $temperature = `cat ../sensor_codes/temp.log | tail -n 1`;
    my $time_stamp = `ls -lt ./camera_api/latest.jpg | awk '{print \$6,\$7,\$8}'`;
    chomp $temperature;
    chomp $time_stamp;
    print $cgi->header,
          $cgi->start_html("Root"),
          $cgi->h1("ECE496 Puts projects low level interfaces tester server(Testing)"),
	  $cgi->h3("$temperature C");
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

sub plock {
	open(my $fh, '>>', $lock_) or die "Cannot lock $lock_\n";
	my $ret = flock($fh, LOCK_EX);
	return $fh;
}

sub punlock {
	my $fh = shift;
	close($fh) or die "cannot unlock\n";
}
} 
 
# start the server on port 8080
my $pid = PutsWebServer->new(8080)->background();
print "Use 'kill $pid' to stop server.\n";
print "lock_file is $lock_file\n";
print "Testfile is $testfile\n";
#async {`./camera_api/light_goes_on_then_off_wait_10s_take_photo_log.pl $usb_control $lock_file`};
#MyWebServer->new(8080);



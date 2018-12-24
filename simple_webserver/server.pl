#!/usr/bin/perl
use threads;
our $lock_file = `readlink -f ./camera_api/lock`;
chomp $lock_file;
our $usb_control = `readlink -f ../thirdparty/uhubctl/uhubctl`;
chomp $usb_control;

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
    # ...
);
 
sub handle_request {
    my $self = shift;
    my $cgi  = shift;
   
    my $path = $cgi->path_info();
    my $handler = $dispatch{$path};
 
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
          $cgi->h1("ECE496 Puts projects low level interfaces tester server(production)"),
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
async {`./camera_api/spin_taking_photo.pl $usb_control $lock_file`};
#MyWebServer->new(8080);



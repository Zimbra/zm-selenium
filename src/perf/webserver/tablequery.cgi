#!/usr/bin/perl -w
# 
# ***** BEGIN LICENSE BLOCK *****
# Zimbra Collaboration Suite Server
# Copyright (C) 2012, 2013, 2014, 2016 Synacor, Inc.
#
# This program is free software: you can redistribute it and/or modify it under
# the terms of the GNU General Public License as published by the Free Software Foundation,
# version 2 of the License.
#
# This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
# without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
# See the GNU General Public License for more details.
# You should have received a copy of the GNU General Public License along with this program.
# If not, see <https://www.gnu.org/licenses/>.
# ***** END LICENSE BLOCK *****
# 

use strict;
use DBI;
use CGI::Session;
use Data::Dumper;
use GD::Graph::lines;
use MIME::Base64;

my $session = new CGI::Session(undef, undef, { Directory => '/tmp'} );
my $sessionid = $session->id();
my $counter = 0;




# DB VARIABLES
my $dbh;
my $sql;
my $sth;

# CONFIG VARIABLES
my $host = "10.137.244.6";
my $database = "perf";
my $tablename = "perf";
my $user = "perf";
my $pw = "perf";

my %apps = ();
my %browsers = ();
my %builds = ();
my %messages = ();
my %milestones = ();

sub radio {
	my $name = $_[0];
	my $checked = $_[1];
	my (%h) = %{$_[2]};

	my $value = "";
	
	foreach my $k (keys %h) {
		if ( defined($checked) && ($checked eq $h{$k}) ) {
			$value = $value . "<input type='radio' checked name='$name' value='$k'/>$h{$k}<br/>\n";
		} else {
			$value = $value . "<input type='radio' name='$name' value='$k'/>$h{$k}<br/>\n";
		}
	}

	return $value;
	
}

sub select {
	my $name = $_[0];
	my $id = $_[1];
	my $multiple = $_[2];
	my $size = $_[3];
	my $valuesort = $_[4];
	my (%h) = %{$_[5]};
	
	
	my $multipleAttr = "";
	if ( $multiple ) {
		$multipleAttr = "multiple='multiple'";
	}

	my $sizeAttr = "";
	if ( $size ) {
		$sizeAttr = "size='$size'";
	}



	my $value = "";
	
	$value = $value .  "<select name='$name' id='$id' $multipleAttr $sizeAttr >\n";

	if (($name eq 'build1') || ($name eq 'build2')){
		my @sorted = sort { $h{$b} cmp $h{$a} } keys %h;
		if ( defined($valuesort) ) {
			@sorted = sort { $h{$b} cmp $h{$a} } keys %h;
		}
		foreach my $k (@sorted) {
			$value = $value . "<option value='$k'>$h{$k}</option>\n";
		}
	} else {
		my @sorted = sort { $h{$a} cmp $h{$b} } keys %h;
        	if ( defined($valuesort) ) {
                	@sorted = sort { $h{$a} cmp $h{$b} } keys %h;
        	}
       		 foreach my $k (@sorted) {
                	$value = $value . "<option value='$k'>$h{$k}</option>\n";
        	}

	}
	$value = $value . "</select>\n";

	return $value;
}

sub page {

	print "Content-type: text/html\n\n" ;
	print "<html >\n";
	print "<header><title>Custom table</title></header>\n";
	print "<body>\n";

	print "<h2>Create a perf table</h2>\n";

	print "<form name='input' action='table.cgi' method='get'>\n";
	print "<fieldset>\n";
	print "<legend>Restrict To:</legend>\n";

	print "<table >\n";

	print "<tr>\n";
		
		print "<th align='left'>Apps:</th>\n";
		print "<th align='left'>Browsers:</th>\n";
		print "<th align='left'>Actions:</th>\n";

	print "</tr>\n";

	print "<tr>\n";

		print "<td valign='top'>".  &select("apps", "apps", undef, 5, undef, \%apps) ."</td>\n";
		print "<td valign='top'>".  &select("browsers", "browsers", 1, 5, 1, \%browsers) ."</td>\n";
		print "<td valign='top'>".  &select("messages", "messages", 1, 10, undef, \%messages) ."</td>\n";

	print "</tr>\n";
	print "</table>\n";
	print "</fieldset>\n";

	print "<fieldset>\n";
	print "<legend>Plot:</legend>\n";
	print "<table>\n";

	print "<tr>\n";

		print "<th align='left'>Build #1:</th>\n";
		print "<th align='left'>Build #2:</th>\n";

	print "</tr>\n";

	print "<tr>\n";

		print "<td valign='top'>".  &select("build1", "build1", undef, 5, undef, \%builds) ."</td>\n";
		print "<td valign='top'>".  &select("build2", "build2", undef, 5, undef, \%builds) ."</td>\n";

	print "</tr>\n";

	print "</table>\n";
	print "</fieldset>\n";




	print "<input type='submit' value='Execute'/>\n";
	print "</form>\n";
	print "</body>\n";
	print "</html>\n";

}

sub main {

	# PERL MYSQL CONNECT
	$dbh = DBI->connect( "DBI:mysql:$database;host=$host", "$user", "$pw" )
		|| die "Could not connect to database: $DBI::errstr";

	# Get the list of tests (messages table)
	#
	$sql = "SELECT id,name FROM apps ORDER BY id";
	$sth = $dbh->prepare($sql);
	$sth->execute();
	while (my ($id, $name) = $sth->fetchrow_array()) {
		$apps{$id} = $name;
	}

	# Get the list of tests (messages table)
	#
	$sql = "SELECT id,name FROM browsers WHERE name != 'unknown' ORDER BY id";
	$sth = $dbh->prepare($sql);
	$sth->execute();
	while (my ($id, $name) = $sth->fetchrow_array()) {
		$browsers{$id} = $name;
	}

	# Get the list of tests (messages table)
	#
	$sql = "SELECT id,build FROM builds ORDER BY id";
	$sth = $dbh->prepare($sql);
	$sth->execute();
	while (my ($id, $build) = $sth->fetchrow_array()) {
		$builds{$id} = $build;
	}

	# Get the list of tests (messages table)
	#
	$sql = "SELECT id,name FROM messages ORDER BY id";
	$sth = $dbh->prepare($sql);
	$sth->execute();
	while (my ($id, $name) = $sth->fetchrow_array()) {
		$messages{$id} = $name;
	}

	# Get the list of tests (messages table)
	#
	$sql = "SELECT id,milestone FROM milestones ORDER BY id";
	$sth = $dbh->prepare($sql);
	$sth->execute();
	while (my ($id, $milestone) = $sth->fetchrow_array()) {
		$milestones{$id} = $milestone;
	}

	# Build the page
	&page;
}

&main;

exit;




#!/usr/bin/env perl
#===================================================================================
#
#         FILE:  jess-vm
#
#        USAGE:  ./jess-vm ../../binary/bin1.obf
#        			... ooo OOO Then your controller comm's via std in/out
#        			ask for directions from me, phone if im sleeping
#
#     SYNOPSIS:  (disregard this:)to control this program
#     				change initial input port values:
#     					search for READPORT
#     				change number of iterations run:
#     					search for 5023
#
#  DESCRIPTION:  hacky (but tested on 1001, results in perfect circular orbit) sim vm
#      OPTIONS:  ---
# REQUIREMENTS:  ---
#         BUGS:  ---
#        NOTES:  ---
#       AUTHOR:  Jess Balint (), jbalint@improvedideas.com
#      COMPANY:  ImprovedIdeas
#      VERSION:  1.0
#      CREATED:  06/26/2009 10:03:34 PM CDT
#     REVISION:  $Id: perl-file-header,v 1.1 2004/06/14 00:24:05 jbalint Exp $
#===================================================================================

#use strict;
use warnings;

use IO::Handle;
use FileHandle;
use Data::Dumper;
use Math::Complex;

my @ins;
my @d = 0.0 x 16384;
my @output;
my @thrusts;

# if log should print ports
my $print_ports = 1;

$inport0=0;
$inport1=0;
$inportConfig=0;

my $in = new FileHandle($ARGV[0]) or die($!);
my $stdin = new IO::Handle;
$stdin->fdopen(fileno(STDIN), "r") or die($!);

my $logfile = new FileHandle("vm_logfile.$$", "w") or die($!);

my $even = 1;

my $status =0;


my $i = 0;
	$op =0 ;
	my $t;

sub print_trace_and_quit
{
	print STDERR "Printing trace and quit\n";
	my $subfile = new FileHandle("vm_subfile.$$", "w");
	if (!$subfile)
	{
		print STDERR "Failed to open sub file $!, dumping to log\n";
		print $logfile Dumper(\@thrusts);
		$logfile->flush();
		$logfile->close();
		exit;
	}
	print $logfile Dumper(\@thrusts);
	my $px = '0'; my $py = '0';
	$subfile->write(pack("L", 0xcafebabe));
	$teamid = 156;
	$subfile->write(pack("L", $teamid));
	$subfile->write(pack("L", $inportConfig));
	#assumption,never velo on zero
	$subfile->write(pack("L", 0));
	$subfile->write(pack("L", 1));
	$subfile->write(pack("L", 16000));
	$subfile->write(pack("d", $inportConfig));
	for($i=0; $i <= $#thrusts; ++$i)
	{
#FORDEBUG$subfile->write(pack("L", 0xcccccccc));
		$a = $thrusts[$i];
		$x = $a->[1];
		$y = $a->[2];
		$subfile->write(pack("L", $a->[0]));
		if ($x ne $px)
		{
			push(@GAC, [2, $x]);
		}
		if ($y ne $py)
		{
			push(@GAC, [3, $y]);
		}
		$subfile->write(pack("L", $#GAC + 1));
		$subfile->write(pack("Ld", @{$_})) for (@GAC);
		$px = $x;
		$py = $y;
		@GAC=();
	}
	$subfile->write(pack("L", $t));
	$subfile->write(pack("L", 0));
	$subfile->close();

	exit;
}

sub print_msg
{
	print $logfile shift;
}

sub getint
{
	my $i = shift;
	return unpack("L", $i);
}
sub getdub
{
	my $d = shift;
	return unpack("d", $d);
}
while ($c=$in->read($x, 12,))
{
	if ($c != 12)
	{
		die($!);
	}
	#print unpack("H*", $x), "\n";
	if ($even)
	{
		$even = 0;
		$int = substr($x, 8, 4);
		$dubya = substr($x, 0, 8);
	}
	else
	{
		$int = substr($x, 0, 4);
		$dubya = substr($x, 4, 8);
		$even = 1;
	}
	$int = getint($int);
	$dubya = getdub($dubya);
	if ($int > 0)
	{
		$op = $int >> 28;
		if ($op > 0)
		{
			 # D
            $ins[$i] = {'t','D','op',$op,'r1',($int>>14)&0x3fff,'r2',$int&0x3fff};
		}
		else
		{
 				# S
 			$ins[$i] = {'t','S','op',$int>>24,'imm',($int>>21)&0x7,'r1',$int&0x3fff};
		}
	}
	#print "p=($i, $int, $dubya)\n";
	$d[$i]=$dubya;
	$i++;
	$op =0 ;
}
print_msg "read $i ins/d pairs\n";

#only once at beginning
$inportConfig=$stdin->getline();
chomp($inportConfig);
for ($t=0;$t < 5023;++$t) {
	$inport0=$stdin->getline();
	if(!$inport0)
	{
		print_trace_and_quit();
	}
	chomp($inport0);
	if($inport0 =~m/X/)
	{
		$inport0='0';
		$inport1='0';
	}
	else
	{
		$inport1=$stdin->getline();
		chomp($inport1);
		push(@thrusts, [$t, $inport0, $inport1]);
	}
	print_msg "*****************************************t=$t\n";
for ($j=0;$j<$i;++$j)
{
	next unless ($op=$ins[$j]);

	$logfile->flush;
	if (0)
	{
		print_msg "Op pc = $j ". $op->{'t'}. " ". $op->{'op'};
		if ($op->{'t'} eq 'S')
		{
			print_msg " IMM=". $op->{'imm'} if ($op->{'op'} == 1);
		}
		print_msg "\n";
	}

	if ($op->{'t'} eq 'D')
	{
		$o = $op->{'op'};
		if ($o == 1)
		{
			$d[$j]=$d[$op->{'r1'}]+$d[$op->{'r2'}];
		}
		elsif ($o == 2)
		{
			$d[$j]=$d[$op->{'r1'}]-$d[$op->{'r2'}];
		}
		elsif ($o == 3)
		{
			$d[$j]=$d[$op->{'r1'}]*$d[$op->{'r2'}];
		}
		elsif ($o == 4)
		{
			if ($d[$op->{'r2'}] == 0.0)
			{
				$d[$j]=0.0;
			}
			else
			{
				$d[$j]=$d[$op->{'r1'}]/$d[$op->{'r2'}];
			}
		}
		elsif ($o == 5)
		{
			$port = $op->{'r1'};
			$val = $d[$op->{'r2'}];
			# WRITEPORT
			print_msg "write port (pc=$j), port=$port, val=$val\n"
				if ($print_ports);
			$output[$port]=$val;
		}
		elsif ($o == 6)
		{
			if ($status == 1)
			{
				$m = $op->{'r1'};
			}
			else
			{
				$m = $op->{'r2'};
			}
			$d[$j] = $d[$m];
		}
		else
		{
			print_msg "Out of D range op=$o at pc=$j\n";
		}
	}
	elsif ($op->{'t'} eq 'S')
	{
		$o = $op->{'op'};
		if ($o == 0)
		{
           	## farside comics?j
		}
		elsif ($o == 1)
		{
			$m = $d[$op->{'r1'}];
			$imm = $op->{'imm'};
			if ($imm == 0)
			{
				$status = $m < 0.0;
			}
			elsif ($imm == 1)
			{
				$status = $m <= 0.0;
			}
			elsif ($imm == 2)
			{
				$status = $m == 0.0;
			}
			elsif ($imm == 3)
			{
				$status = $m >= 0.0;
			}
			elsif ($imm == 4)
			{
				$status = $m > 0.0;
			}
			else
			{
				print_msg "IMM $imm out of range at pc=$j\n";
			}
			if (!$status)
			{
				$status = 0;
			}
		}
		elsif ($o == 2)
		{
			$d[$j] = abs(sqrt($d[$op->{'r1'}]));
		}
		elsif ($o == 3)
		{
			$d[$j] = $d[$op->{'r1'}];
		}
		elsif ($o == 4)
		{
			$port = $op->{'r1'} || -1;
			# READPORT
			if ($port == 2)
			{
				$d[$j] = $inport0;
			}
			elsif ($port == 3)
			{
				$d[$j] = $inport1;
			}
			elsif ($port == 0x3e80)
			{
				$d[$j] = $inportConfig;
			}
			else
			{
				print_msg "UNKNOWN port $port pc=$j\n";
			}
			print_msg "read port (pc=$j), port=$port, val=$d[$j]\n"
				if ($print_ports);
		}
		else
		{
			print_msg "Out of S range op=$o at pc=$j\n";
		}
	}
}
if ($inportConfig < 1005)
{
print_msg "Hohmann Output:";
print_msg "\nScore: ". $output[0];
print_msg "\nFuel: ". $output[1];
print_msg "\nsx: ". $output[2];
print_msg "\nsy: ". $output[3];
print_msg "\ntarget orb rad: ". $output[4]."\n";
print "t=$t,0=".$output[0]."\n";
print "t=$t,1=".$output[1]."\n";
print "t=$t,2=".$output[2]."\n";
print "t=$t,3=".$output[3]."\n";
print "t=$t,4=".$output[4]."\n";
}
elsif ($inportConfig < 4000)
{
print_msg "Meet and Greet Output:";
print_msg "\nScore: ". $output[0];
print_msg "\nFuel: ". $output[1];
print_msg "\nsx: ". $output[2];
print_msg "\nsy: ". $output[3];
print_msg "\nsx rel target: ". $output[4]."\n";
print_msg "\nsy rel target: ". $output[5]."\n";
print "t=$t,0=".$output[0]."\n";
print "t=$t,1=".$output[1]."\n";
print "t=$t,2=".$output[2]."\n";
print "t=$t,3=".$output[3]."\n";
print "t=$t,4=".$output[4]."\n";
print "t=$t,5=".$output[5]."\n";
}
else
{
print_msg "Clear skies:";
print_msg "\nScore: ". $output[0];
print_msg "\nFuel: ". $output[1];
print_msg "\nsx: ". $output[2];
print_msg "\nsy: ". $output[3];
print_msg "\nsx fuel: ". $output[4]."\n";
print_msg "\nsy fuel: ". $output[5]."\n";
print_msg "\nfuel in station: ". $output[6]."\n";
print "t=$t,0=".$output[0]."\n";
print "t=$t,1=".$output[1]."\n";
print "t=$t,2=".$output[2]."\n";
print "t=$t,3=".$output[3]."\n";
print "t=$t,4=".$output[4]."\n";
print "t=$t,5=".$output[5]."\n";
print "t=$t,6=".$output[6]."\n";
$on=6;
for($tgt=0;$tgt<12;++$tgt)
{
	$on++;
	print_msg "\n tgt $tgt sx = ".$output[$on];
	print "t=$t,0_$tgt".$output[$on]."\n";
	$on++;
	print_msg "\n tgt $tgt sy = ".$output[$on];
	print "t=$t,1_$tgt".$output[$on]."\n";
	$on++;
	print_msg "\n tgt $tgt found = ".$output[$on];
	print "t=$t,2_$tgt".$output[$on]."\n";
}
}
}

exit;

$l =~ s/.*: (.+?) (.+?) .*/$1$2/g;

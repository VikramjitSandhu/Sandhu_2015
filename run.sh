#!/bin/sh

#sudo apt-get install -y openjdk-7-jre-headless

# Go to program source dir
cd src

# Name of main class to run
MAIN_CLASS=MyWordCount

# compile
javac *.java

# Run
java $MAIN_CLASS


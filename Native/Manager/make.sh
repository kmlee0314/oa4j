#!/bin/bash
source ./make.env
make clean
make
mv WCCOAjava libWCCOAjava.so $API_ROOT/../bin

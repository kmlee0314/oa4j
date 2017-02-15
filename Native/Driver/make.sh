#!/bin/bash
source ./make.env
make clean
make
mv WCCOAjavadrv libWCCOAjavadrv.so $API_ROOT/../bin

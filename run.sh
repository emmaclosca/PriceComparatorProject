#!/bin/sh

mvn install
mvn clean package
mvn exec:java -Dexec.mainClass=com.pricecomparator.Server
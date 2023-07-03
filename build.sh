#!/bin/sh

./mvnw -DskipTests clean package -U assembly:single
#
rm -rf release/dataopen-sdk-java.zip
zip -j release/dataopen-sdk-java.zip target/dataopen-sdk-java-1.0.0.jar target/dataopen-sdk-java-1.0.0-jar-with-dependencies.jar README.md

#!/bin/bash

mvn clean compile assembly:single

java -cp target/project-1.0-SNAPSHOT-jar-with-dependencies.jar com.project.app.App
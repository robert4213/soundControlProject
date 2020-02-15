#!/bin/bash
mvn clean compile
mvn exec:java -D exec.mainClass=com.project.app.App
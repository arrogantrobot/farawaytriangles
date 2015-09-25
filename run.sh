#!/bin/bash

sbt package
scala target/scala-2.10/farawaytriangles_2.10-0.1-SNAPSHOT.jar com.farawaylabs.scala.logo.SVG

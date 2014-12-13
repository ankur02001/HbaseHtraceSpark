#!/bin/bash
  // ****************************************************************************
  // Group 4: Ankur Pandey , Tushar Gupta , Nupur Bhonge , Tushar Bhatia , Nisha Choudhary 
  //  
  // ****************************************************************************
echo " ================================== "
echo " Running tracing And Analysis Script"
echo " ================================== "
echo " ================================== "
echo "  Generating htrace.out"
echo " ================================== "

cd /teaching/14f-cis655/proj-dtracing/htrace-client/1_getTraces/editable

./tt_sh/runMain_woAnt.sh

echo " ================================== "
echo "  Combining client and server htrace.out" 
echo " ================================== "
cat /teaching/14f-cis655/tmp/server-htrace.out >> /teaching/14f-cis655/proj-dtracing/htrace-client/1_getTraces/editable/htrace.out
cd /teaching/14f-cis655/proj-dtracing/htrace-client   
cp 1_getTraces/editable/htrace.out 2_analyseTraces/
cd 2_analyseTraces/
echo "Removing previous graph"
rm graphs/* 

echo " ================================== "
echo "  Generating vishual graph"
echo " ================================== "
./tt_sh/visual.sh

echo " ================================== "
echo " Generating Analysis output File"
echo " ================================== "
#./analysis/DataAnalysis.java
cp ../1_getTraces/editable/htrace.out ./
cd /teaching/14f-cis655/proj-dtracing/DataAnalysis
javac DataAnalysis.java 
java DataAnalysis

echo " ================================== "
echo " Generating Graph"
echo " =================================== "

echo " Compiling LineGraph"
javac -cp ".:/teaching/14f-cis655/proj-dtracing/DataAnalysis/jcommon-1.0.23/jcommon-1.0.23.jar:/teaching/14f-cis655/proj-dtracing/DataAnalysis/jfreechart-1.0.19/jfreechart-1.0.19-demo.jar:/teaching/14f-cis655/proj-dtracing/DataAnalysis/Htrace_out.txt" XYLineChartExample.java

echo " Generating LineGraph"
java -cp ".:/teaching/14f-cis655/proj-dtracing/DataAnalysis/jcommon-1.0.23/jcommon-1.0.23.jar:/teaching/14f-cis655/proj-dtracing/DataAnalysis/jfreechart-1.0.19/jfreechart-1.0.19-demo.jar:/teaching/14f-cis655/proj-dtracing/DataAnalysis/Htrace_out.txt"  XYLineChartExample &
echo "done"

echo " ================================== "
echo " Running spark"
echo " ================================== "
cd /teaching/14f-cis655/proj-dtracing/spark/spark-1.1.0/
./bin/run-example JavaTrace /teaching/14f-cis655/proj-dtracing/DataAnalysis/Htrace_out.txt


cd /teaching/14f-cis655/proj-dtracing/DataAnalysis
echo " Compiling Bargraph"
javac -cp ".:/teaching/14f-cis655/proj-dtracing/DataAnalysis/jcommon-1.0.23/jcommon-1.0.23.jar:/teaching/14f-cis655/proj-dtracing/DataAnalysis/jfreechart-1.0.19/jfreechart-1.0.19-demo.jar:/teaching/14f-cis655/proj-dtracing/DataAnalysis/Data_Analysis.txt" BarChartDemo.java 

echo " Generating Bargraph"
java -cp ".:/teaching/14f-cis655/proj-dtracing/DataAnalysis/jcommon-1.0.23/jcommon-1.0.23.jar:/teaching/14f-cis655/proj-dtracing/DataAnalysis/jfreechart-1.0.19/jfreechart-1.0.19-demo.jar:/teaching/14f-cis655/proj-dtracing/DataAnalysis/Data_Analysis.txt"  BarChartDemo &
echo "done"


echo " ================================== "
echo "Enjoyed working in this project. Thank you Prof. Tang."
echo "================================== "



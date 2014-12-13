  // ****************************************************************************
  // Group 4: Ankur Pandey , Tushar Gupta , Nupur Bhonge , Tushar Bhatia , Nisha Choudhary 
  //  Reference :  http://www.apache.org/licenses/LICENSE-2.0
  // ****************************************************************************
package org.apache.spark.examples;

import scala.Tuple2;
import org.apache.spark.api.java.*;
import org.apache.spark.api.java.function.Function;
import org.apache.spark.SparkConf;
import org.apache.spark.api.java.JavaPairRDD;
import org.apache.spark.api.java.JavaRDD;
import org.apache.spark.api.java.JavaSparkContext;
import org.apache.spark.api.java.function.FlatMapFunction;
import org.apache.spark.api.java.function.Function2;
import org.apache.spark.api.java.function.PairFunction;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Pattern;
import java.io.*;

public final class JavaTrace {
  private static final Pattern SPACE = Pattern.compile(" ");
 private static final Pattern COLON = Pattern.compile(":");

static FileWriter out=null;
static String flag="";
static int sum_gets=0;
static int max_gets=0;
static int sum_RZ=0;
static int max_RZ=0;
static int sum_RZ_exist=0;
static int max_RZ_exist=0;
static int sum_client=0;
static int max_client=0;
static int max_block=0;
static int sum_block=0;

  public static void main(String[] args) throws Exception {

    if (args.length < 1) {
      System.err.println("Usage: JavaTrace <file>");
      System.exit(1);
    }
//String logFile = "/teaching/14f-cis655/proj-dtracing/spark-1.1.0-bin-hadoop2.4/README.md";
//	JavaSparkContext ctx = new JavaSparkContext("local", "JavaTrace",
//      "/teaching/14f-cis655/proj-dtracing/spark-1.1.0-bin-hadoop2.4", new String[]{"target/simple-project-1.0.jar"});
    SparkConf sparkConf = new SparkConf().setAppName("JavaTrace");
    out=new FileWriter("/teaching/14f-cis655/proj-dtracing/DataAnalysis/Data_Analysis.txt",false);
    JavaSparkContext ctx = new JavaSparkContext(sparkConf);
    JavaRDD<String> lines = ctx.textFile(args[0], 1);
    
    
   Function<String,Boolean> filters=
new Function<String,Boolean>() {
      public Boolean call(String s) {
	
if(flag=="Gets")        
	return s.contains("Gets");
else if(flag=="ClientService.Get")        
	return s.contains("ClientService.Get");
else if(flag=="RecoverableZookeeper.exists")        
	return s.contains("RecoverableZookeeper.exists");
else if(flag=="HFileReaderV2.readBlock")        
	return s.contains("HFileReaderV2.readBlock");
else 
      return s.contains("RecoverableZookeeper.getData");

      }
    };

PairFunction<String,String,String> keyData=
 new PairFunction<String,String,String>(){
public Tuple2<String,String> call(String s) {
        return new Tuple2(COLON.split(s)[0],COLON.split(s)[1]);
      }
};

/** RDD for Gets */
flag="Gets";
JavaRDD<String> gets = lines.filter(filters);
JavaPairRDD<String,String> gets_rdd = gets.mapToPair(keyData);
long count_gets=gets.count();

sum_gets=0;
max_gets=0;

List<Tuple2<String, String>> gets_output = gets_rdd.collect();
    for (Tuple2<String,String> tuple : gets_output) {
        int val=Integer.parseInt(tuple._2());
        if(val > max_gets)
         max_gets=val;
	sum_gets=sum_gets + val;
     System.out.println(tuple._1() + ": " + tuple._2());
    }


int mean_gets=sum_gets/(int)count_gets;
System.out.println(sum_gets + " :sum_gets , " + max_gets + " :max_gets , " + count_gets + " :count , " + mean_gets + " :mean " );
double deviation= ((double)(max_gets - mean_gets)/(double)mean_gets);
System.out.println(deviation + " : times deviating from mean" );
StringBuffer st=new StringBuffer();
st.append("Gets:").append(deviation).append("\n");
out.write(st.toString());


/** RDD for RZ_getdata */
flag="RecoverableZookeeper.getData";
JavaRDD<String> RZ_getdata = lines.filter(filters);
JavaPairRDD<String,String> rz_gd_rdd = RZ_getdata.mapToPair(keyData);
long count_RZ_getdata=RZ_getdata.count();

sum_RZ=0;
max_RZ=0;

List<Tuple2<String, String>> RZ_output = rz_gd_rdd.collect();
    for (Tuple2<String,String> tuple : RZ_output) {
        int val=Integer.parseInt(tuple._2());
        if(val > max_RZ)
         max_RZ=val;
	sum_RZ=sum_RZ + val;
     System.out.println(tuple._1() + ": " + tuple._2());
    }


int mean_RZ=sum_RZ/(int)count_RZ_getdata;
System.out.println(sum_RZ + " :sum_RZ_gd , " + max_RZ + " :max_gets , " + count_RZ_getdata + " :count , " + mean_RZ + " :mean" );
double deviation_RZ= ((double)(max_RZ - mean_RZ)/(double)mean_RZ);
System.out.println(deviation_RZ + " : times deviating_RZ from mean" );
StringBuffer st1=new StringBuffer();
st1.append("RecoverableZookeeper.getData:").append(deviation_RZ).append("\n");
out.write(st1.toString());


/** RDD for RZ_exists **/
flag="RecoverableZookeeper.exists";
JavaRDD<String> RZ_exists = lines.filter(filters);
JavaPairRDD<String,String> rz_exist_rdd = RZ_exists.mapToPair(keyData);
long count_RZ_exists=RZ_exists.count();


sum_RZ_exist=0;
max_RZ_exist=0;

List<Tuple2<String, String>> RZ_exist_output = rz_exist_rdd.collect();
    for (Tuple2<String,String> tuple : RZ_exist_output) {
        int val=Integer.parseInt(tuple._2());
        if(val > max_RZ_exist)
         max_RZ_exist=val;
	sum_RZ_exist=sum_RZ_exist + val;
     System.out.println(tuple._1() + ": " + tuple._2());
    }


int mean_RZ_exist=sum_RZ_exist/(int)count_RZ_exists;
System.out.println(sum_RZ_exist + " :sum_RZ_exist , " + max_RZ_exist + " :max_RZ_exist , " + count_RZ_exists + " :count ," + mean_RZ_exist + " :mean" );
double deviation1= ((double)(max_RZ_exist - mean_RZ_exist)/(double)mean_RZ_exist);
System.out.println(deviation1 + " : times deviating from mean" );
StringBuffer st2=new StringBuffer();
st2.append("RecoverableZookeeper.exists:").append(deviation1).append("\n");
out.write(st2.toString());


/** RDD for Hfilereader_readBlock **/
flag="HFileReaderV2.readBlock";
JavaRDD<String> Hfile = lines.filter(filters);
JavaPairRDD<String,String> hfile_rdd = Hfile.mapToPair(keyData);
long count_Hfile=Hfile.count();


sum_block=0;
max_block=0;

List<Tuple2<String, String>> hfile_output = hfile_rdd.collect();
    for (Tuple2<String,String> tuple : hfile_output) {
        int val=Integer.parseInt(tuple._2());
        if(val > max_block)
         max_block=val;
	sum_block=sum_block + val;
     System.out.println(tuple._1() + ": " + tuple._2());
    }


int mean_block=sum_block/(int)count_Hfile;
System.out.println(sum_block + " :sum_HfileRead , " + max_block + " :max_Hfile , " + count_Hfile + " :count , " + mean_block + " :mean" );
double deviation2= ((double)(max_block - mean_block)/(double)mean_block);
System.out.println(deviation2 + " : times deviating from mean" );
StringBuffer st3=new StringBuffer();
st3.append("HFileReaderV2.readBlock:").append(deviation2).append("\n");
out.write(st3.toString());

/** RDD for Client_get **/
flag="ClientService.Get";
JavaRDD<String> Client_get = lines.filter(filters);
JavaPairRDD<String,String> client_rdd = Client_get.mapToPair(keyData);
long count_Client_get=Client_get.count();
System.out.println(count_Client_get + " : Client count" );

sum_client=0;
max_client=0;

List<Tuple2<String, String>> client_rdd_output = client_rdd .collect();
    for (Tuple2<String,String> tuple : client_rdd_output) {
        int val=Integer.parseInt(tuple._2());
        if(val > max_client)
         max_client=val;
	sum_client=sum_client + val;
     System.out.println(tuple._1() + ": " + tuple._2());
    }


int mean_client=sum_client/(int)count_Client_get;
System.out.println(sum_client + " :sum_client , " + max_client + " :max_client , " + count_gets + " :count , " + mean_client + " :mean" );
double deviation3= ((double)(max_client - mean_client)/(double)mean_client);
System.out.println(deviation3 + " : times deviating from mean" );
StringBuffer st4=new StringBuffer();
st4.append("ClientService.Get:").append(deviation3).append("\n");
out.write(st4.toString());

if(out != null)
out.close();


ctx.stop();


  }
}

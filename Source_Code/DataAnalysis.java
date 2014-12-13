  // ****************************************************************************
  // Group 4: Ankur Pandey , Tushar Gupta , Nupur Bhonge , Tushar Bhatia , Nisha Choudhary 
  //                     *
  // ****************************************************************************

import java.io.*;
import java.util.StringTokenizer;

public class DataAnalysis {

	/**
	 * @param args
	 * @throws IOException
	 */
	public FileWriter out=null;
         public static String filepath="/teaching/14f-cis655/proj-dtracing/DataAnalysis/";
	public DataAnalysis()
	{
		try {
			out = new FileWriter(filepath+"Htrace_out.txt",false);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void DescriptionSearch() throws Exception{
		// String
		// search[]={"Gets","HFileReaderV2.readBlock","ClientService.Get","RecoverableZookeeper.exists"};
		//out.write(0);
		//The constraint is that the below sequence should not be changed
		String search[] = { "Gets" ,"RecoverableZookeeper.exists","RecoverableZookeeper.getData","ClientService.Get","HFileReaderV2.readBlock"};
		for (int i = 0; i < search.length; i++) {
			try {
				func_search(search[i]);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
	}

	public void func_search(String func_name) throws Exception {

		String DELIMITER = ",";
		String TOKEN = ":";
		FileReader in = null;
		//FileWriter out = null;
		BufferedReader br = null;
		String s;
		try {
			in = new FileReader(filepath+"htrace.out");
			
			//out.write(0);
			br = new BufferedReader(in);
			String line;

			while ((line = br.readLine()) != null) {
				StringBuffer result = new StringBuffer();
				String spanID = "";
				long start = 0, stop = 0;
				if (line.contains(func_name)) {
					StringTokenizer str = new StringTokenizer(line, DELIMITER);
					while (str.countTokens() > 0) {
						s = str.nextToken();
						// System.out.println(s);

						if (s.contains(func_name) || s.contains("Start")
								|| s.contains("Stop") || s.contains("SpanID")) {
							StringTokenizer str1 = new StringTokenizer(s, TOKEN);
							String ss = str1.nextToken();
							String ss1 = str1.nextToken();

							if (ss != null) {
								if (ss.contains("Description"))
									result.append(func_name);
								else if (ss.contains("Start"))
									start = Long.parseLong(ss1);
								else if (ss.contains("Stop"))
									stop = Long.parseLong(ss1);
								else if (ss.contains("SpanID"))
									spanID = ss1;

							}
						}
					}
					long processing = stop - start;
					// long Val=0;
					long processingVal = getLower(spanID);
					processing = processing - processingVal;
					//System.out.println(processing);
					//System.out.println("result:" + result);
					result.append(':').append(processing).append("\n");
					out.write(result.toString());
				}
			}
		} finally {
			if (in != null) {
				in.close();
			}
		}

	}

	public long getLower(String spanID) throws Exception {

			//System.out.println("calling with spanID = " + spanID + " ");
			String DELIMITER = ",";
			String TOKEN = ":";
			FileReader in = null;
		//	FileWriter out = null;

			BufferedReader br1 = null;
			 BufferedReader br=null;
			long processing=0;
			String s;


			try {
				in = new FileReader(filepath+"htrace.out");
				br1 = new BufferedReader(in);
				String line,line1;
				boolean hasChildNode = false;
				while ((line1 = br1.readLine()) != null) {
	                    StringBuffer result = new StringBuffer();
					String parentID = "";
					long start , stop ;
					long lowerprocessing = 0;
					StringTokenizer str = new StringTokenizer(line1, DELIMITER);
					if (line1.contains("\"ParentID\":" + spanID)){
	                        hasChildNode = true;
	                       // System.out.println("child node yes/no:"+hasChildNode);
					}
				}

	            if(!hasChildNode){
	            	//System.out.println("I dont have any child nodes i am returning");
	                return 0;
	            }
	            in = new FileReader(filepath+"htrace.out");
	            br = new BufferedReader(in);
	           while ((line = br.readLine()) != null) {
					//System.out.println("the line is:"+line);
					StringBuffer result = new StringBuffer();
					String parentID = "";
					long start , stop ;
					long lowerprocessing = 0;
					StringTokenizer str = new StringTokenizer(line, DELIMITER);
					if (line.contains("\"ParentID\":" + spanID)){
					while (str.countTokens() > 0) {
						s = str.nextToken();
						// System.out.println(s);
						if (s.contains("SpanID")) {
							StringTokenizer str1 = new StringTokenizer(s, TOKEN);
							String ss = str1.nextToken();
							String ss1 = str1.nextToken();
//System.out.println("data we are getting from line: ":+ ss + " : value is " + ss1);
	                        start = 0; stop = 0;
	                //  System.out.println("calling lower with parentID here = "+ ss1 + " ");
							lowerprocessing = getLower(ss1);
			//System.out.println("lowerprocessing returned= "+ lowerprocessing+ " ");
		//System.out.println("the line I am working on:"+line);
	                        StringTokenizer str_pid_forstartstop = new StringTokenizer(line, DELIMITER);
	                        while (str_pid_forstartstop.countTokens() > 0) {
	                              String spanstartstop = str_pid_forstartstop.nextToken();
	                                    if (spanstartstop.contains("Start")) {
												//System.out.println("this is my start span field: "+ spanstartstop);
												StringTokenizer spanstartstoptoken = new StringTokenizer(spanstartstop, TOKEN);
												String sp1 = spanstartstoptoken.nextToken();
												String spanvalue1 = spanstartstoptoken.nextToken();
												//System.out.println("start time is = "+ spanvalue1 + " ");
	                                            start = Long.parseLong(spanvalue1);
											}

											if (spanstartstop.contains("Stop")) {
												//System.out.println("this is my stop span field: "+ spanstartstop);
												StringTokenizer spanstartstoptoken = new StringTokenizer(spanstartstop, TOKEN);
												String sp1 = spanstartstoptoken.nextToken();
												String spanvalue1 = spanstartstoptoken.nextToken();
												//System.out.println("stop time = "+ spanvalue1 + " ");
	                                            stop = Long.parseLong(spanvalue1);
											}
	                                   }
	                                processing += stop - start + lowerprocessing;
	                               // System.out.println("the processing time inside is:"+ processing);
									}
								}
							}
						}
					}finally {
				if (in != null) {
					in.close();
				}
			}
			//System.out.println("processing = " + processing + " ");

			return processing;


	}

	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		DataAnalysis da = new DataAnalysis();
		try {
			da.out = new FileWriter(filepath+"Htrace_out.txt");
			da.DescriptionSearch();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		finally{
			if (da.out != null) {
				da.out.close();
			}
		}
		
		

	}

}

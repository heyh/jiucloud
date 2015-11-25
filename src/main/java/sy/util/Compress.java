package sy.util;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSONObject;

public class Compress {

	private static final int BUFFER_LENGTH = 400;
	// 压缩字节最小长度，小于这个长度的字节数组不适合压缩，压缩完会更大
	public static final int BYTE_MIN_LENGTH = 50;

	// 字节数组是否压缩标志位
	public static final byte FLAG_GBK_STRING_UNCOMPRESSED_BYTEARRAY = 0;
	public static final byte FLAG_GBK_STRING_COMPRESSED_BYTEARRAY = 1;
	public static final byte FLAG_UTF8_STRING_COMPRESSED_BYTEARRAY = 2;
	public static final byte FLAG_NO_UPDATE_INFO = 3;

	/**
	 * 数据压缩
	 * 
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void compress(InputStream is, OutputStream os)
			throws Exception {
		GZIPOutputStream gos = new GZIPOutputStream(os);
		int count;
		byte data[] = new byte[BUFFER_LENGTH];
		while ((count = is.read(data, 0, BUFFER_LENGTH)) != -1) {
			gos.write(data, 0, count);
		}
		gos.finish();
		gos.flush();
		gos.close();
	}

	
	  /** 
	    * 数据压缩 
	    *  
	    * @param data 
	    * @return 
	    * @throws Exception 
	    */  
	    public static byte[] byteCompress(byte[] data) throws Exception {  
	        ByteArrayInputStream bais = new ByteArrayInputStream(data);  
	       ByteArrayOutputStream baos = new ByteArrayOutputStream();  
	        // 压缩  
	       compress(bais, baos);  
	       byte[] output = baos.toByteArray();  
	       baos.flush();  
	       baos.close();  
	       bais.close();  
	       return output;  
    } 

	
	/**
	 * 数据解压缩
	 * 
	 * @param is
	 * @param os
	 * @throws Exception
	 */
	public static void decompress(InputStream is, OutputStream os)
			throws Exception {
		GZIPInputStream gis = new GZIPInputStream(is);
		int count;
		byte data[] = new byte[BUFFER_LENGTH];
		while ((count = gis.read(data, 0, BUFFER_LENGTH)) != -1) {
			os.write(data, 0, count);
		}
		gis.close();
	}

	/**
	 * 数据解压缩 
	 * @param data
	 * @return
	 * @throws Exception
	 */
	public static byte[] byteDecompress(byte[] data) throws Exception {
		ByteArrayInputStream bais = new ByteArrayInputStream(data);
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		// 解压缩
		decompress(bais, baos);
		data = baos.toByteArray();
		baos.flush();
		baos.close();
		bais.close();
		return data;
	}

	/**
	 * 输出
	 * @param res
	 */
	public void writerByte(HttpServletResponse res,String jsonStr) {
		 byte[] resultOriginalByte = jsonStr.getBytes();
		//组织最后返回数据的缓冲字节数组
        ByteArrayOutputStream resultBuffer = new ByteArrayOutputStream();
        OutputStream os = null;
		try{
			os = res.getOutputStream();
            //如果要返回的结果字节数组小于50位，不将压缩
            if(resultOriginalByte.length < Compress.BYTE_MIN_LENGTH){
                byte flagByte = Compress.FLAG_GBK_STRING_UNCOMPRESSED_BYTEARRAY;
                resultBuffer.write(flagByte);
                resultBuffer.write(resultOriginalByte);
            }
            else{
                byte flagByte = Compress.FLAG_GBK_STRING_COMPRESSED_BYTEARRAY;
                resultBuffer.write(flagByte);
                resultBuffer.write(Compress.byteCompress(resultOriginalByte));
            }
            resultBuffer.flush();
            resultBuffer.close();
            //将最后组织后的字节数组发送给客户端
            os.write(resultBuffer.toByteArray());
        } catch (IOException e) {
			e.printStackTrace();
		}catch(Exception e){
			e.printStackTrace();
		}finally{
            try {
                os.close();
            } catch (IOException e) {
            	e.printStackTrace();
                // TODO Auto-generated catch block
              //  e.printStackTrace();
            }
        }
		
	}
	
	
	
	private static void urlConnectionPost() {
	    StringBuilder responseBuilder = null;
	    BufferedReader reader = null;
	    OutputStreamWriter wr = null;

	    URL url;
	    try {
	    	
	    	
	    	String sign = "?sign=eyJkZXNjIjoi5Yqf6J2H5belIOW3pSIsImxvZ2lubmFtZSI6ImNrYSIsIm9mZmVyIjoiMSIsIm9yZGVyIjoiZGVzYyIsInB3ZCI6IjExMTExMSIsInNvcnQiOiJzYWxlcyIsInRpdGxlIjoi5omL5py65pyJ6Zeu6aKYIn0=";
	        url = new URL("http://localhost:8080/appk/interfaceFacebackController/add_fbi"+sign);
	        URLConnection conn = url.openConnection();
	        conn.setDoOutput(true);
	        conn.setConnectTimeout(1000 * 5);
	        wr = new OutputStreamWriter(conn.getOutputStream());
	        wr.write("");
	        wr.flush();

	        // Get the response
	        reader = new BufferedReader(new InputStreamReader(conn
	                .getInputStream()));
	        responseBuilder = new StringBuilder();
	        String line = null;
	        while ((line = reader.readLine()) != null) {
	            responseBuilder.append(line);
	        }
	        wr.close();
	        reader.close();
	        System.out.println(responseBuilder.toString());
	        System.out.println("====================================");
	    } catch (IOException e) {
	        // TODO Auto-generated catch block
	        e.printStackTrace();
	    }

	}

	
	public static void main(String[] args) {
		
		
		Compress.urlConnectionPost();
		
		
		
		
		/*//基金净值历史走势信息
        ArrayList<Jjjz> jjjzs = null;
        
        //得到基金净值历史走势的方法省略了
        
        Gson gson = new Gson();
        String jsonStr = gson.toJson(jjjzs, jjjzs.getClass());
        
        byte[] resultOriginalByte = jsonStr.getBytes();
                
        //组织最后返回数据的缓冲字节数组
        ByteArrayOutputStream resultBuffer = new ByteArrayOutputStream();
        OutputStream os = null;
        
        
        try {
            
            os = response.getOutputStream();
            //如果要返回的结果字节数组小于50位，不将压缩
            if(resultOriginalByte.length < Compress.BYTE_MIN_LENGTH){
                byte flagByte = Compress.FLAG_GBK_STRING_UNCOMPRESSED_BYTEARRAY;
                resultBuffer.write(flagByte);
                resultBuffer.write(resultOriginalByte);
            }
            else{
                byte flagByte = Compress.FLAG_GBK_STRING_COMPRESSED_BYTEARRAY;
                resultBuffer.write(flagByte);
                resultBuffer.write(Compress.byteCompress(resultOriginalByte));
            }
            resultBuffer.flush();
            resultBuffer.close();
                       
            //将最后组织后的字节数组发送给客户端
            os.write(resultBuffer.toByteArray());
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        finally{
            try {
                os.close();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return null;*/
		
		
		/*--------------------------接收
		byte[] receivedByte = EntityUtils.toByteArray(httpResponse.getEntity());

        String result = null;
        
        //判断接收到的字节数组是否是压缩过的
        if (receivedByte[0] == Compress.FLAG_GBK_STRING_UNCOMPRESSED_BYTEARRAY) {
            result = new String(receivedByte, 1, receivedByte.length - 1, EXCHANGE_ENCODING);
        } 
        
        else if (receivedByte[0] == Compress.FLAG_GBK_STRING_COMPRESSED_BYTEARRAY) {

            byte[] compressedByte = new byte[receivedByte.length - 1];

            for (int i = 0; i < compressedByte.length; i++) {
                compressedByte[i] = receivedByte[i + 1];
            }
            byte[] resultByte = Compress.byteDecompress(compressedByte);
            result = new String(resultByte, EXCHANGE_ENCODING);
        }
*/
		
		
	}
	
	public void a(){
		Map<String, String> obj = new HashMap<String,String>();
		for(int i=0;i<4;i++){
			obj.put("a"+i, "csd");
		}
		String str = JSONObject.toJSONString(obj);
		System.out.println(str);
		byte [] resultOriginalByte = str.getBytes();
		//压缩
		ByteArrayOutputStream resultBuffer = new ByteArrayOutputStream();
	    OutputStream os = null;
	    //如果要返回的结果字节数组小于50位，不将压缩
	    try {
	    	   System.out.println(resultOriginalByte.length+" length");
			    if(resultOriginalByte.length < Compress.BYTE_MIN_LENGTH){
			    	System.out.println("===========");
				    byte flagByte = Compress.FLAG_GBK_STRING_UNCOMPRESSED_BYTEARRAY;
				    resultBuffer.write(flagByte);
					resultBuffer.write(resultOriginalByte);
			    }else{
			    	byte flagByte = Compress.FLAG_GBK_STRING_COMPRESSED_BYTEARRAY;
	                resultBuffer.write(flagByte);
	                resultBuffer.write(Compress.byteCompress(resultOriginalByte));
			    	
			    }
			   
		                   
		        //将最后组织后的字节数组发送给客户端
		       // os.write(resultBuffer.toByteArray());
		        byte [] receivedByte  = resultBuffer.toByteArray();
		        System.out.println(resultBuffer.size());
		        
		        
		        
		        //解压
		        
		        
		        
		        
		        String result = null;
		      //判断接收到的字节数组是否是压缩过的
		        if (receivedByte[0] == Compress.FLAG_GBK_STRING_UNCOMPRESSED_BYTEARRAY) {
		            result = new String(receivedByte, 1, receivedByte.length - 1, "GBK");
		        } 
		        
		        else if (receivedByte[0] == Compress.FLAG_GBK_STRING_COMPRESSED_BYTEARRAY) {

		            byte[] compressedByte = new byte[receivedByte.length - 1];

		            for (int i = 0; i < compressedByte.length; i++) {
		                compressedByte[i] = receivedByte[i + 1];
		            }
		            byte[] resultByte = Compress.byteDecompress(compressedByte);
		            result = new String(resultByte, "GBK");
		        }
		        
		        System.out.println(result+"   ================");
		        
		        
		        
			    } catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	}
	
	public String castbyte(byte [] receivedByte){
		String result = null;
		try{
		
	      //判断接收到的字节数组是否是压缩过的
	        if (receivedByte[0] == Compress.FLAG_GBK_STRING_UNCOMPRESSED_BYTEARRAY) {
	            result = new String(receivedByte, 1, receivedByte.length - 1, "GBK");
	        } 
	        
	        else if (receivedByte[0] == Compress.FLAG_GBK_STRING_COMPRESSED_BYTEARRAY) {

	            byte[] compressedByte = new byte[receivedByte.length - 1];

	            for (int i = 0; i < compressedByte.length; i++) {
	                compressedByte[i] = receivedByte[i + 1];
	            }
	            byte[] resultByte = Compress.byteDecompress(compressedByte);
	            result = new String(resultByte, "GBK");
	        }
	        
	        System.out.println(result+"   ================");
	        
		    } catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return result;
	}
}

package hehe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class testServlet extends HttpServlet {
	
	private static final long serialVersionUID = 1L;
	
	public static String loadJson (String url){
		String result=null;
		try{
			HttpGet request = new HttpGet(url);//这里发送get请求
	        // 获取当前客户端对象
	        HttpClient httpClient = new DefaultHttpClient();
	        // 通过请求对象获取响应对象
	        HttpResponse response = httpClient.execute(request);
	        
	        if (response.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                result= EntityUtils.toString(response.getEntity(),"utf-8");
            } 
		}catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
		
		System.out.println(result);
		return result;
	}
	
//	public static String loadJson (String url) {  
//	//	System.out.println(url);
//        StringBuilder json = new StringBuilder(); 
//   //     window.open(url);
//        try {  
//            URL urlObject = new URL(url);  
//            
//            URLConnection uc=urlObject.openConnection();
//            System.out.println(uc.getURL());
//            uc.setConnectTimeout(8000);
//           
//          //  if(uc.getContent())
//            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));  
//            String inputLine = null;  
//            System.out.println(inputLine=in.readLine());
////            while((inputLine=in.readLine())==null);
//          	json.append(inputLine);
//            while ( (inputLine = in.readLine()) != null) {  
//            //	System.out.println(inputLine);
//                json.append(inputLine);  
//            }  
//            in.close();  
//        } catch (MalformedURLException e) {  
//            e.printStackTrace();  
//        } catch (IOException e) {  
//            e.printStackTrace();  
//        }  
//        return json.toString();  
//    }  
	
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		doPost(request,response);
		request.setAttribute("entity", "中国");
	}

	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
//		/** 设置响应头允许ajax跨域访问 **/  
//        response.setHeader("Access-Control-Allow-Origin", "*");  
//        /* 星号表示所有的异域请求都可以接受， */  
//        response.setHeader("Access-Control-Allow-Methods", "GET,POST");
        
	//	System.out.println("hehe");
		request.setCharacterEncoding("UTF-8");    //设定客户端提交给servlet的内容按UTF-8编码
        response.setCharacterEncoding("UTF-8");    //设定servlet传回给客户端的内容按UTF-8编码
        response.setContentType("text/html;charset=UTF-8");    //告知浏览器用UTF-8格式解析内容     
        
        String entity= request.getParameter("entity");//获取实体名参数
        System.out.println(entity);
       String url="http://knowledgeworks.cn:20314/probaseplus/pbapi/getconcepts?kw="+entity;
   //     String url="http://knowledgeworks.cn:20314/probaseplus/pbapi/getconcepts?kw=南京";    
        try{
        	String json=loadJson(url);	
//        	PrintWriter pw = response.getWriter();
//            pw.println(json);
        	JsonParser parser= new JsonParser();
        	JsonObject object=(JsonObject) parser.parse(json);
        	
        	JsonArray array=object.get("concept").getAsJsonArray();
        	
        	List<Record> records = new ArrayList<Record>();
        	
        	for(int i=0;i<array.size();i++){
             //   System.out.println("---------------");
                JsonElement subObject=array.get(i);
                String hehe= subObject.toString();
                int a=hehe.indexOf("\"");
                int b=hehe.lastIndexOf("\"");
                String first=hehe.substring(a+1,b);
                String second=hehe.substring(b+2,hehe.length()-1);
             //   System.out.println("concept="+first+"value="+second);
                
                Record temp = new Record();
                temp.setName(first);
                temp.setValue(Double.parseDouble(second));
                records.add(temp);
                
            }
        	
        	Gson gson = new Gson();
        	String result = gson.toJson(records);
        	
        //    response.setContentType("text/html;chartset=utf-8");
            response.getWriter().write(result);
        	
        }catch (JsonIOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
	}

}

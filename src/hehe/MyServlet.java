package hehe;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

import java.io.BufferedReader;
 
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;


//import com.google.gson.Gson;
public class MyServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	public static String loadJson (String url) {  
        StringBuilder json = new StringBuilder();  
        try {  
            URL urlObject = new URL(url);  
            URLConnection uc = urlObject.openConnection();  
            BufferedReader in = new BufferedReader(new InputStreamReader(uc.getInputStream()));  
            String inputLine = null;  
            while ( (inputLine = in.readLine()) != null) {  
                json.append(inputLine);  
            }  
            in.close();  
        } catch (MalformedURLException e) {  
            e.printStackTrace();  
        } catch (IOException e) {  
            e.printStackTrace();  
        }  
        return json.toString();  
    } 
	/**
	 * Constructor of the object.
	 */
	public MyServlet() {
		super();
	}

	/**
	 * Destruction of the servlet. <br>
	 */
	public void destroy() {
		super.destroy(); // Just puts "destroy" string in log
		// Put your code here
	}

	/**
	 * The doGet method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to get.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<!DOCTYPE HTML PUBLIC \"-//W3C//DTD HTML 4.01 Transitional//EN\">");
		out.println("<HTML>");
		out.println("  <HEAD><TITLE>A Servlet</TITLE></HEAD>");
		out.println("  <BODY>");
		out.print("    This is ");
		out.print(this.getClass());
		out.println(", using the GET method");
		out.println("  </BODY>");
		out.println("</HTML>");
		out.flush();
		out.close();
	}

	/**
	 * The doPost method of the servlet. <br>
	 *
	 * This method is called when a form has its tag value method equals to post.
	 * 
	 * @param request the request send by the client to the server
	 * @param response the response send by the server to the client
	 * @throws ServletException if an error occurred
	 * @throws IOException if an error occurred
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		request.setCharacterEncoding("UTF-8");    //设定客户端提交给servlet的内容按UTF-8编码
        response.setCharacterEncoding("UTF-8");    //设定servlet传回给客户端的内容按UTF-8编码
        response.setContentType("text/html;charset=UTF-8");    //告知浏览器用UTF-8格式解析内容     
        
        String name= request.getParameter("entity");//获取实体名参数
        
        String url="http://knowledgeworks.cn:20314/probaseplus/pbapi/getconcepts?kw="+name;
             
        
        try{
        	String json=loadJson(url);
        	JsonParser parser= new JsonParser();
        	JsonObject object=(JsonObject) parser.parse(json);
        	
        	JsonArray array=object.get("concept").getAsJsonArray();
        	
        	List<Record> records = new ArrayList<Record>();
        	
        	for(int i=0;i<array.size();i++){
                System.out.println("---------------");
                JsonElement subObject=array.get(i);
                String hehe= subObject.toString();
                int a=hehe.indexOf("\"");
                int b=hehe.lastIndexOf("\"");
           //     System.out.println("a="+a+"b="+b);
                String first=hehe.substring(a+1,b);
                String second=hehe.substring(b+2,hehe.length()-1);
                System.out.println("concept="+first+"value="+second);
                
                Record temp = new Record();
                temp.setName(first);
                temp.setValue(Double.parseDouble(second));
                
                records.add(temp);
                
            }
        	
        	Gson gson = new Gson();
        	String result = gson.toJson(records);
        	
            response.setContentType("text/html;chartset=utf-8");
            response.getWriter().write(result);
        	
        }catch (JsonIOException e) {
            e.printStackTrace();
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
	}

	/**
	 * Initialization of the servlet. <br>
	 *
	 * @throws ServletException if an error occurs
	 */
	public void init() throws ServletException {
		// Put your code here
	}

}

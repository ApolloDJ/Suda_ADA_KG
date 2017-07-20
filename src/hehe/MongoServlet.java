package hehe;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

//import com.Aliez.echart.DB;
//import com.Aliez.echart.DBCollection;
//import com.Aliez.echart.DBCursor;
//import com.Aliez.echart.DBObject;
//import com.Aliez.echart.EventVo;
//import com.Aliez.echart.Gson;
//import com.Aliez.echart.MongoClient;


public class MongoServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req,resp);
    }
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       request.setCharacterEncoding("UTF-8");    //设定客户端提交给servlet的内容按UTF-8编码
       response.setCharacterEncoding("UTF-8");    //设定servlet传回给客户端的内容按UTF-8编码
       response.setContentType("text/html;charset=UTF-8");    //告知浏览器用UTF-8格式解析内容
       String entity = request.getParameter("entity");
	
	   List<TripleFromMongo> triple = new ArrayList<TripleFromMongo>();    //用一个ArrayList来盛装封装了各气象数据的对象
	   MongoClient mongoClient = new MongoClient("192.168.131.192",27017);
//	   DB db=mongoClient.getDB("ment2ent");
//	   DBCollection colle
//	   
	   
	   DB db = mongoClient.getDB("cndbpedia");
	   DBCollection collection = db.getCollection("triples");
	   System.out.println("Collection userInfo selected successfully");
	   BasicDBObject query = new BasicDBObject("s",entity );
	   System.out.println(entity);
	   DBCursor cursor = collection.find(query);
	 //  DBCursor cursor = collection.find();
	   int i = 0;
	   List<String> cstr =new ArrayList<String>();
	   cstr.add(entity);
	   
	   while(cursor.hasNext()/*&&i<=10*/)
	   {
		   DBObject e = cursor.next();
		   Map t = e.toMap();
		   TripleFromMongo temp = new TripleFromMongo();
		   temp.setId(t.get("_id").toString());
		   temp.setTimestamp(t.get("timestamp").toString());
		   String str=t.get("o").toString();
		   int a,b;
		   if((a=str.indexOf("<a>"))!=-1){
			   b=str.indexOf("</a>");
			   str=str.substring(a+3,b);
		   }
		   temp.setO(str);
		   
		   str=t.get("p").toString();
		   if(str.equals("CATEGORY_ZH")){
			   str="是";
		   }
		   
		   temp.setP(str);
		   temp.setS(t.get("s").toString());
		   
		   int flag=0;
		   for(int cnt=0;cnt<cstr.size();cnt++){
			   if(temp.getO().equals(cstr.get(cnt)))
				   flag=1;
		   }
		   if(flag==1)
			   continue;
		   cstr.add(temp.getO());
//		   if(temp.getO().equals(temp.getS()))
//			   continue;
		   triple.add(temp);
		   
		   i++;
	   }
	  
	   Gson gson = new Gson();
	   String json = gson.toJson(triple);
	   
	   response.setContentType("text/html;chartset=UTF-8");
	   response.getWriter().write(json);
	   
	   System.out.println(json);
     }

}

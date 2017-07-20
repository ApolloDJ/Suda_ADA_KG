package hehe;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.logging.Logger;

//import org.bson.types.ObjectId;

import com.mongodb.*;
//import com.mongodb.MongoClient;
//import com.mongodb.client.MongoCollection;
//import com.mongodb.client.MongoDatabase;


public class MongoDBUtil {
	 
    private static final MongoDBUtil instance = new MongoDBUtil();
 
    private static MongoClient mongo = null;
     
     
    /**
     * 私有化
     */
    private MongoDBUtil() {
    }
     
    /**
     * 单例
     * @return
     */
    public static MongoDBUtil getInstance() {
         
        return instance;
         
    }
     
    /**
     * 初始化MongoDB
     */
    public void init() {
 
        try {
 
            mongo = new MongoClient("localhost", 27017);
 
        } catch (Exception e) {
            e.printStackTrace();
        }
         
    }
 
    /**
     * 获取DB对象
     * @return
     */
    public DB getDB() {
         
        try {
             
            if (mongo == null) {  
                init(); 
            }     
             
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        return null;
 
    }
 
    /**
     * 获取集合对象
     * @param name
     * @return
     */
    private DBCollection getCollection(String name) {
         
        try {
             
            return getDB().getCollection(name);
             
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return null;
 
    }
 
    /**
     * 插入MongoDB
     * @param name
     * @param obj
     */
    public void insert(String name, DBObject obj) {
         
        try {
             
            long begin = System.currentTimeMillis();
            getCollection(name).insert(obj);
            long end = System.currentTimeMillis();
             
        } catch (Exception e) {
            e.printStackTrace();
        }
 
         
    }
 
    /**
     * 删除指定条件的数据
     * @param name
     * @param obj
     */
    public void delete(String name, DBObject obj) {
         
        try {
             
            getCollection(name).remove(obj);
             
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
 
    /**
     * 清空集合
     * @param collection
     * @throws Exception
     */
    public void deleteAll(String collection) {
 
        try {
             
            List<DBObject> rs = findAll(collection);
         
            if (rs != null && !rs.isEmpty()) {
                 
                for (int i = 0; i < rs.size(); i++) {
                    getCollection(collection).remove(rs.get(i));
                }
                 
            }
         
        } catch (Exception e) {
            e.printStackTrace();
        }
         
    }
 
    /**
     * 如果更新的数据 不存在 插入一条数据
     * @param collection
     * @param setFields
     * @param whereFields
     */
    public void updateOrInsert(String name, DBObject set, DBObject where) {
 
        try {
             
            getCollection(name).update(where, set, true, false);
             
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
 
    /**
     * 只更新存在的数据,不会新增. 批量更新.
     * @param name
     * @param setFields
     * @param whereFields
     */
    public void updateExistDataWithBatch(String name, DBObject set, DBObject where) {
 
        try {
             
            getCollection(name).update(where, new BasicDBObject("$set", set), false, true);
             
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
 
    /**
     * 按照ObjectId,批量更新.<br>
     * 因massageToObjectId()删除,所以MongoDB驱动包版本最高为2.10.1(含)<br>
     * 2.10.1以上版本无此方法,故需要自行确定ObjectId.<br>
     * 待有时间找到该方法的替代方法.
     * @param name
     * @param ids
     * @param set
     */
    public void updateBatchByObjectId(String name, String ids, DBObject set) {
//         
//        try {
//             
//            if (ids == null || ids == "")
//                return;
//             
//            String[] id = ids.split(",");
//             
//            for (int i = 0; i < id.length; i++) {
//                 
//                BasicDBObject dest = new BasicDBObject();
//                BasicDBObject doc = new BasicDBObject();
//                dest.put("_id", ObjectId.massageToObjectId(id[i]));
//                doc.put("$set", set);
//                getCollection(name).update(dest, doc, false, true);
//                 
//            }
//             
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
// 
// 
    }
 
    /**
     * 查询全部
     * @param name
     * @return
     */
    public List<DBObject> findAll(String name) {
 
        try {
             
            return getCollection(name).find().toArray();
             
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return null;
         
    }
 
    /**
     * 查询1条记录
     * @param name
     * @param obj
     * @return
     */
    public DBObject findOne(String name, DBObject obj) {
 
        try {
             
            DBCollection coll = getCollection(name);
            return coll.findOne(obj);
             
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        return null;
 
    }
 
    /**
     * 查询指定条数的记录
     * @param name
     * @param obj
     * @param limit
     * @return
     */
    public List<DBObject> find(String name, DBObject obj, int limit) {
         
        try {
             
            DBCollection coll = getCollection(name);
            DBCursor c = coll.find(obj).limit(limit);
             
            if (c != null){
                 
                List<DBObject> list = new ArrayList<DBObject>();
                list = c.toArray();
                 
                return list;
                 
            }
             
        } catch (Exception e) {
            e.printStackTrace();
        }
         
        return null;
         
    }
 
    /**
     * 查询符合的全部数据
     * @param name
     * @param where
     * @return
     */
    public List<DBObject> find(String name, DBObject where) {
         
        try {
             
            DBCursor c = getCollection(name).find(where);
             
            if (c != null) {
                 
                List<DBObject> list = new ArrayList<DBObject>();
                list = c.toArray();
                 
                return list;
                 
            }
             
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return null;
 
    }
     
    /**
     * 返回Queue的查询
     * @param name
     * @param where
     * @return
     * @throws Exception
     */
    public LinkedBlockingQueue<DBObject> findQueue(String name, DBObject where) {
         
        try {
             
            LinkedBlockingQueue<DBObject> queue = new LinkedBlockingQueue<DBObject>();
             
            DBCursor c = getCollection(name).find(where);
             
            if (c != null) {
                 
                for (DBObject obj : c) {
                    obj = c.next();
                    queue.offer(obj);
                }
                 
                return queue;
                 
            }
             
        } catch (Exception e) {
            e.printStackTrace();
        }
 
        return null;
 
    }
 
    /**
     * 关闭Mongo链接
     */
    public void close() {
         
        try {
             
            if (mongo != null) {
                 
                mongo.close();
                 
            }
             
        } catch (Exception e) {
            e.printStackTrace();
        }
 
    }
 
}
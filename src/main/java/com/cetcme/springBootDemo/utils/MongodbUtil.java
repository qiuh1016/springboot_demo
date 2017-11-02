package com.cetcme.springBootDemo.utils;

import java.util.ArrayList;
import java.util.List;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;

public class MongodbUtil {

	MongoClient mongoClient = null;
	private static final MongodbUtil mongoDBDaoImpl = new MongodbUtil();
	
	private MongodbUtil()
	{
		if (mongoClient == null)
        {
            MongoClientOptions.Builder buide = new MongoClientOptions.Builder();
            buide.connectionsPerHost(100);// 与目标数据库可以建立的最大链接数
            buide.connectTimeout(1000 * 60 * 20);// 与数据库建立链接的超时时间
            buide.maxWaitTime(100 * 60 * 5);// 一个线程成功获取到一个可用数据库之前的最大等待时间
            buide.threadsAllowedToBlockForConnectionMultiplier(1000);
            buide.maxConnectionIdleTime(0);
            buide.maxConnectionLifeTime(0);
            buide.socketTimeout(0);
            buide.socketKeepAlive(true);
            MongoClientOptions myOptions = buide.build();
            

            MongoCredential credential = MongoCredential.createScramSha1Credential("admin", "db_rcld_zj", "123456".toCharArray());  
            List<MongoCredential> credentials = new ArrayList<MongoCredential>();  
            credentials.add(credential);  
            List<ServerAddress> seeds = new ArrayList<>();
            seeds.add(new ServerAddress("dds-bp1bc85d401e3d841.mongodb.rds.aliyuncs.com:3717"));
            seeds.add(new ServerAddress("dds-bp1bc85d401e3d842.mongodb.rds.aliyuncs.com:3717"));
            try
            {
                mongoClient = new MongoClient(seeds,credentials,myOptions);//("localhost", myOptions,credentials);
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
	}
	
	public static MongodbUtil getMongoDBDaoImpl()
    {
        return mongoDBDaoImpl;
    }
	
	public MongoDatabase getDB()
	{
		return mongoClient.getDatabase("db_rcld_zj");
	}
	
	
	public MongoCollection<Document> GetCollection()
	{	 
		MongoDatabase mongoDatabase = mongoClient.getDatabase("db_rcld_zj");
	 	MongoCollection<Document> collection = mongoDatabase.getCollection("t_acq_data_histroy");
		return collection;
	}
}

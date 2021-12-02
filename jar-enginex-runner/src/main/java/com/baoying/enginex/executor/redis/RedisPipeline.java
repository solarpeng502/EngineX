//package com.baoying.enginex.executor.redis;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.dao.DataAccessException;
//import org.springframework.data.redis.connection.RedisConnection;
//import org.springframework.data.redis.core.RedisCallback;
//import org.springframework.data.redis.core.RedisOperations;
//import org.springframework.data.redis.core.RedisTemplate;
//import org.springframework.data.redis.core.SessionCallback;
//import org.springframework.stereotype.Component;
//
//import java.util.List;
//
//@Component
//public class RedisPipeline {
//
//    @Autowired
//    private RedisTemplate redisTemplate;
//
//    public List<Object> hGetAllBatch(List<String> keys) {
//        List list = redisTemplate.executePipelined(new RedisCallback<Object>() {
//            @Override
//            public Object doInRedis(RedisConnection redisConnection) throws DataAccessException {
//                for(String key : keys){
//                    redisConnection.hGetAll(key.getBytes());
//                }
//                return null;
//            }
//        });
//        return list;
//    }
//
//    public List<Object> usePipeline(List<String> keys) {
//        List list = redisTemplate.executePipelined(new SessionCallback<Object>() {
//            @Override
//            public <K, V> Object execute(RedisOperations<K, V> redisOperations) throws DataAccessException {
//                for(String key : keys){
//                    redisOperations.opsForHash().entries(null);
//                }
//                return null;
//            }
//        });
//        return list;
//    }
//
//}

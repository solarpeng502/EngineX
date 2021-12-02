package com.baoying.enginex.executor.sql;

import redis.clients.jedis.*;

import java.util.List;

public class RedisTest {
    public static void main(String[] args) {
//        JedisPoolConfig config = new JedisPoolConfig();
//        config.setMaxTotal(1);
//        config.setMaxIdle(1);
//        config.setMaxWaitMillis(3000);
//        config.setTestOnBorrow(true);
//        config.setTestOnReturn(true);
//
//        JedisPool pool = new JedisPool(config,
//                "127.0.0.1",
//                6379,
//                3000,
//                "root",
//                1);
//        Jedis jedis = pool.getResource();
        JedisShardInfo jedisShardInfo = new JedisShardInfo("47.102.125.25",6379,3000,"root");
        jedisShardInfo.setPassword("enginex123");
        Jedis jedis = new Jedis(jedisShardInfo);
        jedis.select(1);
        if (jedis.isConnected()){

            Object eval = jedis.eval("local result = 0;\n" +
                    "local flag = true;\n" +
                    "local cursor  = '0';\n" +
                    "while(flag)\n" +
                    "do\n" +
                    "local  scanResult = redis.call('hscan','THRESHOLD_MOBILE_DAY:20211116:手机号1',cursor ,'match','12*');\n" +
                    "cursor  = scanResult[1];\n" +
                    "  for k, v in pairs(scanResult[2]) do\n" +
                    "   if k%2 == 0 then\n" +
                    "     result = result+v;\n" +
                    "     end\n" +
                    "  end\n" +
                    " if scanResult == nil or cursor == '0'then\n" +
                    "   flag = false;\n" +
                    " end\n" +
                    "end\n" +
                    "return result;");

//        System.out.println(jedis.eval("return redis.call('get', 'test1')"));
//            Object eval = jedis.eval("return redis.call('LRANGE', 'list1' , 0 , -1)");
            long value = 0;
            if (eval instanceof List){
                System.out.println(eval);
               List result =  (List) eval;

                if (result.get(1) instanceof List){
                    System.out.println(result.get(1));
                    List<String> kv = (List)result.get(1);
                    for (int i = 0; i < kv.size(); i++) {
                        if (i%2 == 0){
                            continue;
                        }
                        value += Long.valueOf(kv.get(i));
                    }

                }

            }

            System.out.println(value);
//            String aaa = jedis.get("test1");
//            System.out.println(aaa);
        }


    }
}

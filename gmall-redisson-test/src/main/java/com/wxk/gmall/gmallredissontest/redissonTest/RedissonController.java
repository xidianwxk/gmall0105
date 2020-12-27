package com.wxk.gmall.gmallredissontest.redissonTest;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.wxk.gmall.util.RedisUtil;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import redis.clients.jedis.Jedis;


/**
 * @author wxk
 * @creat 2020-11-12 17:11
 */

@Controller
public class RedissonController {

    @Autowired
    RedisUtil redisUtil;

    @Autowired
    RedissonClient redissonClient;

    @RequestMapping("testRedisson")
    @ResponseBody
    public String testRedisson(){

//        Jedis jedis = redisUtil.getJedis();
//
//        String v = jedis.get("k");
//        if (StringUtils.isBlank(v)) {
//            v = "1";
//        }
//        System.out.println("->" + v);
//        jedis.set("k", (Integer.parseInt(v) + 1) + "");
//        jedis.close();



        //压力测试   上锁  防止并发
        Jedis jedis = redisUtil.getJedis();

        RLock lock = redissonClient.getLock("lock");// 声明锁
        lock.lock();//上锁
        try {
            String v = jedis.get("k");
            if (StringUtils.isBlank(v)) {
                v = "1";
            }
            System.out.println("->" + v);
            jedis.set("k", (Integer.parseInt(v) + 1) + "");
        }finally {
            jedis.close();
            lock.unlock();// 解锁
        }

        return "success";
    }
}

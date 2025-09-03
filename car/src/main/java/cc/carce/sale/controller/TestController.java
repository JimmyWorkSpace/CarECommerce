package cc.carce.sale.controller;

import cc.carce.sale.common.RedisUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 测试控制器
 * 用于测试Redis配置
 */
@Api(tags = "测试接口")
@RestController
@RequestMapping("/api/test")
public class TestController {

    @Autowired(required = false)
    private RedisUtil redisUtil;

    /**
     * 测试Redis连接
     */
    @ApiOperation(value = "测试Redis连接", notes = "测试Redis是否正常工作")
    @GetMapping("/redis")
    public Map<String, Object> testRedis() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (redisUtil != null) {
                // 测试Redis写入
                redisUtil.set("test:key", "Hello Redis!");
                // 测试Redis读取
                Object value = redisUtil.get("test:key");
                
                result.put("success", true);
                result.put("message", "Redis连接正常");
                result.put("data", value);
            } else {
                result.put("success", false);
                result.put("message", "Redis未配置或不可用");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Redis连接失败: " + e.getMessage());
            result.put("error", e.toString());
        }
        
        return result;
    }

    /**
     * 测试Redis基本操作
     */
    @ApiOperation(value = "测试Redis基本操作", notes = "测试Redis的增删改查操作")
    @GetMapping("/redis/operations")
    public Map<String, Object> testRedisOperations() {
        Map<String, Object> result = new HashMap<>();
        
        try {
            if (redisUtil != null) {
                // 测试字符串操作
                redisUtil.set("test:string", "测试字符串", 60);
                Object stringValue = redisUtil.get("test:string");
                
                // 测试Hash操作
                redisUtil.hset("test:hash", "field1", "value1");
                Object hashValue = redisUtil.hget("test:hash", "field1");
                
                // 测试过期时间
                long expire = redisUtil.getExpire("test:string");
                
                result.put("success", true);
                result.put("message", "Redis操作测试成功");
                result.put("data", new HashMap<String, Object>() {{
                    put("string", stringValue);
                    put("hash", hashValue);
                    put("expire", expire);
                }});
            } else {
                result.put("success", false);
                result.put("message", "Redis未配置或不可用");
            }
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Redis操作测试失败: " + e.getMessage());
            result.put("error", e.toString());
        }
        
        return result;
    }
} 
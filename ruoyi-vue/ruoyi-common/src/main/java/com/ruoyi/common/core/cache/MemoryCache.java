package com.ruoyi.common.core.cache;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

/**
 * 基于ConcurrentHashMap的内存缓存工具类
 * 用于替换Redis缓存
 *
 * @author ruoyi
 */
@Component
@Slf4j
public class MemoryCache {
    
    /**
     * 主缓存存储
     */
    private final ConcurrentHashMap<String, CacheItem> cache = new ConcurrentHashMap<>();
    
    /**
     * 定时清理过期数据的调度器
     */
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    
    /**
     * 缓存项
     */
    private static class CacheItem {
        private final Object value;
        private final long expireTime;
        
        public CacheItem(Object value, long expireTime) {
            this.value = value;
            this.expireTime = expireTime;
        }
        
        public Object getValue() {
            return value;
        }
        
        public boolean isExpired() {
            return expireTime > 0 && System.currentTimeMillis() > expireTime;
        }
    }
    
    public MemoryCache() {
        // 每分钟清理一次过期数据
        scheduler.scheduleAtFixedRate(this::cleanExpiredItems, 1, 1, TimeUnit.MINUTES);
    }
    
    /**
     * 清理过期数据
     */
    private void cleanExpiredItems() {
        cache.entrySet().removeIf(entry -> entry.getValue().isExpired());
    }
    
    /**
     * 缓存基本的对象
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     */
    public <T> void setCacheObject(final String key, final T value) {
        cache.put(key, new CacheItem(value, -1));
    }
    
    /**
     * 缓存基本的对象，带过期時間
     *
     * @param key 缓存的键值
     * @param value 缓存的值
     * @param timeout 時間
     * @param timeUnit 時間颗粒度
     */
    public <T> void setCacheObject(final String key, final T value, final Integer timeout, final TimeUnit timeUnit) {
        long expireTime = System.currentTimeMillis() + timeUnit.toMillis(timeout);
        cache.put(key, new CacheItem(value, expireTime));
    }
    
    /**
     * 设置有效時間
     *
     * @param key 缓存键
     * @param timeout 超时時間
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout) {
        return expire(key, timeout, TimeUnit.SECONDS);
    }
    
    /**
     * 设置有效時間
     *
     * @param key 缓存键
     * @param timeout 超时時間
     * @param unit 時間单位
     * @return true=设置成功；false=设置失败
     */
    public boolean expire(final String key, final long timeout, final TimeUnit unit) {
        CacheItem item = cache.get(key);
        if (item != null) {
            long expireTime = System.currentTimeMillis() + unit.toMillis(timeout);
            cache.put(key, new CacheItem(item.getValue(), expireTime));
            return true;
        }
        return false;
    }
    
    /**
     * 获得缓存的基本对象
     *
     * @param key 缓存键值
     * @return 缓存键值对应的数据
     */
    @SuppressWarnings("unchecked")
    public <T> T getCacheObject(final String key) {
        CacheItem item = cache.get(key);
        if (item != null) {
            if (item.isExpired()) {
                cache.remove(key);
                return null;
            }
            return (T) item.getValue();
        }
        return null;
    }
    
    /**
     * 刪除单个对象
     *
     * @param key 缓存键
     */
    public boolean deleteObject(final String key) {
        return cache.remove(key) != null;
    }
    
    /**
     * 刪除集合对象
     *
     * @param collection 多个对象
     * @return 刪除的数量
     */
    public long deleteObject(final Collection<String> collection) {
        AtomicLong count = new AtomicLong(0);
        collection.forEach(key -> {
            if (cache.remove(key) != null) {
                count.incrementAndGet();
            }
        });
        return count.get();
    }
    
    /**
     * 缓存List数据
     *
     * @param key 缓存的键值
     * @param dataList 待缓存的List数据
     * @return 缓存的对象数量
     */
    public <T> long setCacheList(final String key, final List<T> dataList) {
        cache.put(key, new CacheItem(new ArrayList<>(dataList), -1));
        return dataList.size();
    }
    
    /**
     * 获得缓存的list对象
     *
     * @param key 缓存的键值
     * @return 缓存键值对应的数据
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getCacheList(final String key) {
        CacheItem item = cache.get(key);
        if (item != null && !item.isExpired()) {
            return (List<T>) item.getValue();
        }
        return new ArrayList<>();
    }
    
    /**
     * 缓存Set
     *
     * @param key 缓存键值
     * @param dataSet 缓存的数据
     * @return 缓存数据的对象
     */
    public <T> void setCacheSet(final String key, final Set<T> dataSet) {
        cache.put(key, new CacheItem(new HashSet<>(dataSet), -1));
    }
    
    /**
     * 获得缓存的set
     *
     * @param key 缓存键
     * @return 缓存的Set数据
     */
    @SuppressWarnings("unchecked")
    public <T> Set<T> getCacheSet(final String key) {
        CacheItem item = cache.get(key);
        if (item != null && !item.isExpired()) {
            return (Set<T>) item.getValue();
        }
        return new HashSet<>();
    }
    
    /**
     * 缓存Map
     *
     * @param key 缓存键
     * @param dataMap 缓存的Map数据
     */
    public <T> void setCacheMap(final String key, final Map<String, T> dataMap) {
        if (dataMap != null) {
            cache.put(key, new CacheItem(new ConcurrentHashMap<>(dataMap), -1));
        }
    }
    
    /**
     * 获得缓存的Map
     *
     * @param key 缓存键
     * @return 缓存的Map数据
     */
    @SuppressWarnings("unchecked")
    public <T> Map<String, T> getCacheMap(final String key) {
        CacheItem item = cache.get(key);
        if (item != null && !item.isExpired()) {
            return (Map<String, T>) item.getValue();
        }
        return new ConcurrentHashMap<>();
    }
    
    /**
     * 往Hash中存入数据
     *
     * @param key 缓存键
     * @param hKey Hash键
     * @param value 值
     */
    @SuppressWarnings("unchecked")
    public <T> void setCacheMapValue(final String key, final String hKey, final T value) {
        CacheItem item = cache.get(key);
        Map<String, T> map;
        if (item != null && !item.isExpired()) {
            map = (Map<String, T>) item.getValue();
        } else {
            map = new ConcurrentHashMap<>();
        }
        map.put(hKey, value);
        cache.put(key, new CacheItem(map, item != null ? item.expireTime : -1));
    }
    
    /**
     * 获取Hash中的数据
     *
     * @param key 缓存键
     * @param hKey Hash键
     * @return Hash中的对象
     */
    @SuppressWarnings("unchecked")
    public <T> T getCacheMapValue(final String key, final String hKey) {
        CacheItem item = cache.get(key);
        if (item != null && !item.isExpired()) {
            Map<String, T> map = (Map<String, T>) item.getValue();
            return map.get(hKey);
        }
        return null;
    }
    
    /**
     * 刪除Hash中的数据
     * 
     * @param key 缓存键
     * @param hkey Hash键
     */
    @SuppressWarnings("unchecked")
    public void delCacheMapValue(final String key, final String hkey) {
        CacheItem item = cache.get(key);
        if (item != null && !item.isExpired()) {
            Map<String, Object> map = (Map<String, Object>) item.getValue();
            map.remove(hkey);
        }
    }
    
    /**
     * 获取多个Hash中的数据
     *
     * @param key 缓存键
     * @param hKeys Hash键集合
     * @return Hash对象集合
     */
    @SuppressWarnings("unchecked")
    public <T> List<T> getMultiCacheMapValue(final String key, final Collection<Object> hKeys) {
        CacheItem item = cache.get(key);
        if (item != null && !item.isExpired()) {
            Map<String, T> map = (Map<String, T>) item.getValue();
            return hKeys.stream()
                    .map(hKey -> map.get(hKey.toString()))
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }
    
    /**
     * 获得缓存的基本对象列表（支持通配符匹配）
     *
     * @param pattern 字符串前缀模式
     * @return 匹配的键列表
     */
    public Collection<String> keys(final String pattern) {
        String regex = pattern.replace("*", ".*");
        return cache.keySet().stream()
                .filter(key -> key.matches(regex))
                .collect(Collectors.toList());
    }
    
    /**
     * 清空所有缓存
     */
    public void clear() {
        cache.clear();
    }
    
    /**
     * 获取缓存大小
     */
    public int size() {
        return cache.size();
    }
} 
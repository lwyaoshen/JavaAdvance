package RedisUtil;
/**
 * 
 * @ClassName:       JedisUtil
 * @Description:    ����Redis�İ�����
 * @author:         yangsheng
 */
package cn.com.pioneercloud.infra.common.cache;


import cn.com.pioneercloud.infra.common.util.ClassLoaderUtil;
import cn.com.pioneercloud.infra.common.util.IPUtil;
import cn.com.pioneercloud.infra.common.util.StringUtil;
import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisShardInfo;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;
import redis.clients.jedis.ShardedJedis;
import redis.clients.jedis.ShardedJedisPool;
import redis.clients.jedis.exceptions.JedisException;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * ��spring��ϰ汾 Created by qct on 2015/7/7.
 */
public class JedisUtil {
    private static Logger logger = LoggerFactory.getLogger(JedisUtil.class);
    private ShardedJedisPool shardedJedisPool;

    private static class SingletonHolder {
        private static final JedisUtil JEDIS_UTIL = new JedisUtil();
    }

    private JedisUtil() {
        this.init();
    }

    /**
     * Instance jedis util.
     *
     * @return the jedis util
     */
    public static JedisUtil instance() {
        return SingletonHolder.JEDIS_UTIL;
    }

    /**
     * Init.
     */
    private void init() {
        Properties p = new Properties();
        InputStreamReader reader;
        InputStream is = ClassLoaderUtil.getResourceAsStream("redis.properties", JedisUtil.class);
        if (null != is) {
            try {
                reader = new InputStreamReader(is, "UTF-8");
                p.load(reader);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxTotal(Integer.parseInt(p.getProperty("redis.pool.max.total")));
        config.setMaxIdle(Integer.parseInt(p.getProperty("redis.pool.max.idle")));
        config.setMaxWaitMillis(Integer.parseInt(p.getProperty("redis.pool.max.wait.millis")));
        config.setTestOnBorrow(Boolean.parseBoolean(p.getProperty("redis.pool.test.on.borrow")));
        config.setTestOnReturn(Boolean.parseBoolean(p.getProperty("redis.pool.test.on.return")));
        config.setTestWhileIdle(Boolean.parseBoolean(p.getProperty("redis.pool.test.while.idle")));
        config.setNumTestsPerEvictionRun(Integer.parseInt(p.getProperty("redis.pool.num.tests.per.eviction.run")));
        config.setTimeBetweenEvictionRunsMillis(Integer.parseInt(p.getProperty(
                "redis.pool.time.between.eviction.runs.millis")));
        config.setMinEvictableIdleTimeMillis(Integer.parseInt(p.getProperty("redis.pool.min.evictable.idle.time.millis")));
        config.setSoftMinEvictableIdleTimeMillis(Integer.parseInt(p.getProperty(
                "redis.pool.soft.min.evictable.idle.time.millis")));
        config.setJmxEnabled(Boolean.parseBoolean(p.getProperty("redis.pool.jmx.enabled")));
        config.setJmxNamePrefix(p.getProperty("redis.pool.jmx.name.prefix"));
        config.setBlockWhenExhausted(Boolean.parseBoolean(p.getProperty("redis.pool.block.when.exhausted")));
        // Node
        List<JedisShardInfo> shardInfos = new ArrayList<>();
        Enumeration<?> names = p.propertyNames();
        while (names.hasMoreElements()) {
            String key = names.nextElement().toString();
            if (key.startsWith("redis.node.")) {
                String[] host = p.getProperty(key).split(":");
                shardInfos.add(new JedisShardInfo(host[0], Integer.parseInt(host[1])));
            }
        }

        shardedJedisPool = new ShardedJedisPool(config, shardInfos);
    }


    /**
     * ����һ��key�Ĺ���ʱ�䣨��λ���룩
     *
     * @param key     keyֵ
     * @param seconds ����������
     * @return 1�������˹���ʱ�� 0��û�����ù���ʱ��/�������ù���ʱ��
     */
    public long expire(String key, int seconds) {
        if (key == null || key.equals("")) {
            return 0;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.expire(key, seconds);
        } catch (Exception ex) {
            logger.error("EXPIRE error[key=" + key + " seconds=" + seconds + "]" + ex.getMessage(), ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return 0;
    }

    /**
     * ����һ��key��ĳ��ʱ������
     *
     * @param key           keyֵ
     * @param unixTimestamp unixʱ�������1970-01-01 00:00:00��ʼ�����ڵ�����
     * @return 1�������˹���ʱ�� 0��û�����ù���ʱ��/�������ù���ʱ��
     */
    public long expireAt(String key, int unixTimestamp) {
        if (key == null || key.equals("")) {
            return 0;
        }

        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.expireAt(key, unixTimestamp);
        } catch (Exception ex) {
            logger.error("EXPIRE error[key=" + key + " unixTimestamp=" + unixTimestamp + "]" + ex.getMessage(), ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return 0;
    }

    /**
     * �ض�һ��List
     *
     * @param key   �б�key
     * @param start ��ʼλ�� ��0��ʼ
     * @param end   ����λ��
     * @return ״̬��
     */
    public String trimList(String key, long start, long end) {
        if (key == null || key.equals("")) {
            return "-";
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.ltrim(key, start, end);
        } catch (Exception ex) {
            logger.error("LTRIM ����[key=" + key + " start=" + start + " end=" + end + "]" + ex.getMessage(), ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return "-";
    }

    /**
     * ���Set����
     */
    public long countSet(String key) {
        if (key == null) {
            return 0;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.scard(key);
        } catch (Exception ex) {
            logger.error("countSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return 0;
    }

    /**
     * ��ӵ�Set�У�ͬʱ���ù���ʱ�䣩
     *
     * @param key     keyֵ
     * @param seconds ����ʱ�� ��λs
     */
    public boolean addSet(String key, int seconds, String... value) {
        boolean result = addSet(key, value);
        if (result) {
            long i = expire(key, seconds);
            return i == 1;
        }
        return false;
    }

    /**
     * ��ӵ�Set��
     */
    public boolean addSet(String key, String... value) {
        if (key == null || value == null) {
            return false;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.sadd(key, value);
            return true;
        } catch (Exception ex) {
            logger.error("setList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * @return �ж�ֵ�Ƿ������set��
     */
    public boolean containsInSet(String key, String value) {
        if (key == null || value == null) {
            return false;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.sismember(key, value);
        } catch (Exception ex) {
            logger.error("setList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * ��ȡSet
     */
    public Set<String> getSet(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.smembers(key);
        } catch (Exception ex) {
            logger.error("getList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * ��set��ɾ��value
     */
    public boolean removeSetValue(String key, String... value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.srem(key, value);
            return true;
        } catch (Exception ex) {
            logger.error("getList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * ��list��ɾ��value Ĭ��count 1
     *
     * @param values ֵlist
     */
    public int removeListValue(String key, List<String> values) {
        return removeListValue(key, 1, values);
    }

    /**
     * ��list��ɾ��value
     *
     * @param values ֵlist
     */
    public int removeListValue(String key, long count, List<String> values) {
        int result = 0;
        if (values != null && values.size() > 0) {
            for (String value : values) {
                if (removeListValue(key, count, value)) {
                    result++;
                }
            }
        }
        return result;
    }

    /**
     * ��list��ɾ��value
     *
     * @param count Ҫɾ������
     */
    public boolean removeListValue(String key, long count, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.lrem(key, count, value);
            return true;
        } catch (Exception ex) {
            logger.error("getList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * ��ȡList
     *
     * @param start ��ʼλ��
     * @param end   ����λ��
     */
    public List<String> rangeList(String key, long start, long end) {
        if (key == null || key.equals("")) {
            return null;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.lrange(key, start, end);
        } catch (Exception ex) {
            logger.error("rangeList ����[key=" + key + " start=" + start + " end=" + end + "]" + ex.getMessage(), ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * ���List����
     */
    public long countList(String key) {
        if (key == null) {
            return 0;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.llen(key);
        } catch (Exception ex) {
            logger.error("countList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return 0;
    }

    /**
     * ��ӵ�List�У�ͬʱ���ù���ʱ�䣩
     *
     * @param key     keyֵ
     * @param seconds ����ʱ�� ��λs
     */
    public boolean addList(String key, int seconds, String... value) {
        boolean result = addList(key, value);
        if (result) {
            long i = expire(key, seconds);
            return i == 1;
        }
        return false;
    }

    public boolean addList(byte[] key,  byte[] value) {
        if (key == null || value == null) {
            return false;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.rpush(key, value);
            return true;
        } catch (Exception ex) {
            logger.error("setList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * ��ӵ�List
     */
    public boolean addList(String key, String... value) {
        if (key == null || value == null) {
            return false;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.rpush(key, value);
            return true;
        } catch (Exception ex) {
            logger.error("setList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }


    /**
     * ��ӵ�List
     */
    public boolean addList(String key, String value,int expire) {
        if (key == null || value == null) {
            return false;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.rpush(key, value);
            if(expire != 0)
                shardedJedis.expire(key,expire);
            return true;
        } catch (Exception ex) {
            logger.error("setList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * ��ӵ�List(ֻ����)
     */
    public boolean addList(String key, List<String> list) {
        if (key == null || list == null || list.size() == 0) {
            return false;
        }
        for (String value : list) {
            addList(key, value);
        }
        return true;
    }

    /**
     * ��ȡList
     */
    public List<String> getList(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.lrange(key, 0, -1);
        } catch (Exception ex) {
            logger.error("getList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    public List<byte[]> getList(byte[] key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.lrange(key,0,-1);
        } catch (Exception ex) {
            logger.error("getList error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * ����HashSet����
     *
     * @param domain ����
     * @param key    ��ֵ
     * @param value  Json String or String value
     */
    public boolean setHSet(String domain, String key, String value) {
        if (value == null) {
            return false;
        }
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.hset(domain, key, value);
            return true;
        } catch (Exception ex) {
            logger.error("setHSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * ���HashSet����
     *
     * @param domain ����
     * @param key    ��ֵ
     * @return Json String or String value
     */
    public String getHSet(String domain, String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.hget(domain, key);
        } catch (Exception ex) {
            logger.error("getHSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * ɾ��HashSet����
     *
     * @param domain ����
     * @param key    ��ֵ
     * @return ɾ���ļ�¼��
     */
    public long delHSet(String domain, String key) {
        ShardedJedis shardedJedis = null;
        long count = 0;
        try {
            shardedJedis = shardedJedisPool.getResource();
            count = shardedJedis.hdel(domain, key);
        } catch (Exception ex) {
            logger.error("delHSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return count;
    }

    /**
     * ɾ��HashSet����
     *
     * @param domain ����
     * @param key    ��ֵ
     * @return ɾ���ļ�¼��
     */
    public long delHSet(String domain, String... key) {
        ShardedJedis shardedJedis = null;
        long count = 0;
        try {
            shardedJedis = shardedJedisPool.getResource();
            count = shardedJedis.hdel(domain, key);
        } catch (Exception ex) {
            logger.error("delHSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return count;
    }

    /**
     * �ж�key�Ƿ����
     *
     * @param domain ����
     * @param key    ��ֵ
     */
    public boolean existsHSet(String domain, String key) {
        ShardedJedis shardedJedis = null;
        boolean isExist = false;
        try {
            shardedJedis = shardedJedisPool.getResource();
            isExist = shardedJedis.hexists(domain, key);
        } catch (Exception ex) {
            logger.error("existsHSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return isExist;
    }

    /**
     * ȫ��ɨ��hset
     *
     * @param match fieldƥ��ģʽ
     */
    public List<Map.Entry<String, String>> scanHSet(String domain, String match) {
        ShardedJedis shardedJedis = null;
        try {
            int cursor = 0;
            shardedJedis = shardedJedisPool.getResource();
            ScanParams scanParams = new ScanParams();
            scanParams.match(match);
            Jedis jedis = shardedJedis.getShard(domain);
            ScanResult<Map.Entry<String, String>> scanResult;
            List<Map.Entry<String, String>> list = new ArrayList<Map.Entry<String, String>>();
            do {
                scanResult = jedis.hscan(domain, String.valueOf(cursor), scanParams);
                list.addAll(scanResult.getResult());
                cursor = Integer.parseInt(scanResult.getStringCursor());
            } while (cursor > 0);
            return list;
        } catch (Exception ex) {
            logger.error("scanHSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * ���� domain ָ���Ĺ�ϣ���������ֶε�valueֵ
     */
    public List<String> hvals(String domain) {
        ShardedJedis shardedJedis = null;
        List<String> retList = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            retList = shardedJedis.hvals(domain);
        } catch (Exception ex) {
            logger.error("hvals error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return retList;
    }

    /**
     * ���ض�Ӧkey������hash�ֶε�ֵ
     *
     * @param key ָ����key
     */
    public Map<String, String> hgetall(String key) {
        ShardedJedis shardedJedis = null;
        Map<String, String> resultMap = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            resultMap = shardedJedis.hgetAll(key);
        } catch (Exception ex) {
            logger.error("hgetall error. {}", ex.getMessage());
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return resultMap;
    }

    /**
     * ���ض�Ӧkey���ֶ�ֵ
     *
     * @param key   ��
     * @param field �ֶ�
     * @return ��Ӧkey���ֶ�ֵ
     */
    public String hget(String key, String field) {
        ShardedJedis shardedJedis = null;
        String result = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            result = shardedJedis.hget(key, field);
        } catch (Exception ex) {
            logger.error("hget error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return result;
    }

    /**
     * ���ض�Ӧkey���ֶ�ֵ
     *
     * @param key   ��
     * @param field �ֶ�
     * @return ��Ӧkey���ֶ�ֵ
     */
    public byte[] hget(byte[] key, byte[] field) {
        ShardedJedis shardedJedis = null;
        byte[] result = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            //result = shardedJedis.hget(key, field);
            result = shardedJedis.hget(key, field);
        } catch (Exception ex) {
            logger.error("hget error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return result;
    }

    /**
     * ����hash�ж�Ӧ�ֶε�ֵ
     *
     * @param key   ��
     * @param field �ֶ�
     * @param value ֵ
     * @return <p>-1:����ʧ�� <p>0:���fieldԭ����map�����Ѿ����� <p>1:���field��һ���µ��ֶ�
     */
    public long hset(String key, String field, String value, int expire) {
        ShardedJedis shardedJedis = null;
        long result = -1;
        try {
            shardedJedis = shardedJedisPool.getResource();
            result = shardedJedis.hset(key, field, value);
            if (expire > 0) {
                shardedJedis.expire(key, expire);
            }
        } catch (Exception ex) {
            logger.error("hget error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return result;
    }

    /**
     * ����hash�ж�Ӧ�ֶε�ֵ
     *
     * @param key   ��
     * @param field �ֶ�
     * @return <p>-1:����ʧ�� <p>0:���fieldԭ����map�����Ѿ����� <p>1:���field��һ���µ��ֶ�
     */
    public long hdel(String key, String field) {
        ShardedJedis shardedJedis = null;
        long result = -1;
        try {
            shardedJedis = shardedJedisPool.getResource();
            result = shardedJedis.hdel(key, field);
        } catch (Exception ex) {
            logger.error("hdel error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return result;
    }

    /**
     * ����hash�ж�Ӧ�ֶε�ֵ
     *
     * @param key   ��
     * @param field �ֶ�
     * @param value ֵ
     * @return <p>-1:����ʧ�� <p>0:���fieldԭ����map�����Ѿ����� <p>1:���field��һ���µ��ֶ�
     */
    public long hset( byte[] key, byte[] field, byte[] value, int expire) {
        ShardedJedis shardedJedis = null;
        long result = -1;
        try {
            shardedJedis = shardedJedisPool.getResource();
            result = shardedJedis.hset(key,field,value);
            if (expire > 0) {
                shardedJedis.expire(key, expire);
            }
        } catch (Exception ex) {
            logger.error("hget error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return result;
    }

    /**
     * ����hash�ж�Ӧ�ֶε�ֵ
     *
     * @param key   ��
     * @param field �ֶ�
     * @param value ֵ
     * @return <p>-1:����ʧ�� <p>0:���fieldԭ����map�����Ѿ����� <p>1:���field��һ���µ��ֶ�
     */
    public long hset(String key, String field, String value) {
        return hset(key, field, value, -1);
    }

    public String hmset(String key, Map<String, String> value, int expire) {
        ShardedJedis shardedJedis = null;
        String result = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            result = shardedJedis.hmset(key, value);
            if (expire > 0) {
                shardedJedis.expire(key, expire);
            }
        } catch (Exception ex) {
            logger.error("hmset error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return result;
    }

    public String hmset(String key, Map<String, String> value) {
        return hmset(key, value, -1);
    }

    public List<String> hmget(String key, String field) {
        ShardedJedis shardedJedis = null;
        List<String> result = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            result = shardedJedis.hmget(key, field);
        } catch (Exception ex) {
            logger.error("hmget error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return result;
    }

    /**
     * ���� domain ָ���Ĺ�ϣ���������ֶε�keyֵ
     */
    public Set<String> hkeys(String domain) {
        ShardedJedis shardedJedis = null;
        Set<String> retList = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            retList = shardedJedis.hkeys(domain);
        } catch (Exception ex) {
            logger.error("hkeys error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return retList;
    }

    /**
     * ���� domain ָ���Ĺ�ϣkeyֵ����
     */
    public long lenHset(String domain) {
        ShardedJedis shardedJedis = null;
        long retList = 0;
        try {
            shardedJedis = shardedJedisPool.getResource();
            retList = shardedJedis.hlen(domain);
        } catch (Exception ex) {
            logger.error("hkeys error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return retList;
    }

    /**
     * �������򼯺�
     */
    public boolean setSortedSet(String key, long score, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.zadd(key, score, value);
            return true;
        } catch (Exception ex) {
            logger.error("setSortedSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * ������򼯺�
     */
    public Set<String> getSoredSet(String key, long startScore, long endScore, boolean orderByDesc) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            if (orderByDesc) {
                return shardedJedis.zrevrangeByScore(key, endScore, startScore);
            } else {
                return shardedJedis.zrangeByScore(key, startScore, endScore);
            }
        } catch (Exception ex) {
            logger.error("getSoredSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * �������򳤶�
     */
    public long countSoredSet(String key, long startScore, long endScore) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            Long count = shardedJedis.zcount(key, startScore, endScore);
            return count == null ? 0L : count;
        } catch (Exception ex) {
            logger.error("countSoredSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return 0L;
    }

    /**
     * ɾ�����򼯺�
     */
    public boolean delSortedSet(String key, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            long count = shardedJedis.zrem(key, value);
            return count > 0;
        } catch (Exception ex) {
            logger.error("delSortedSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    /**
     * ������򼯺�
     */
    public Set<String> getSoredSetByRange(String key, int startRange, int endRange, boolean orderByDesc) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            if (orderByDesc) {
                return shardedJedis.zrevrange(key, startRange, endRange);
            } else {
                return shardedJedis.zrange(key, startRange, endRange);
            }
        } catch (Exception ex) {
            logger.error("getSoredSetByRange error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    /**
     * ���������
     */
    public Double getScore(String key, String member) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.zscore(key, member);
        } catch (Exception ex) {
            logger.error("getSoredSet error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    public boolean set(String key, String value, int second) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.setex(key, second, value);
            return true;
        } catch (Exception ex) {
            logger.error("set error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    public boolean set(String key, String value) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.set(key, value);
            return true;
        } catch (Exception ex) {
            logger.error("set error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    public boolean set(byte[] key, byte[] value,int expire) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.set(key,value);
            if (expire > 0) {
                shardedJedis.expire(key, expire);
            }
            return true;
        } catch (Exception ex) {
            logger.error("set error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    public byte[] get(byte[] key){
        ShardedJedis shardedJedis = null;
        byte[] result = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            result = shardedJedis.get(key);
        } catch (Exception ex) {
            logger.error("get error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return result;
    }

    public String get(String key) {
        return get(key, null);
    }

    public String get(String key, String defaultValue) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.get(key) == null ? defaultValue : shardedJedis.get(key);
        } catch (Exception ex) {
            logger.error("get error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return defaultValue;
    }

    public boolean del(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            shardedJedis.del(key);
            return true;
        } catch (Exception ex) {
            logger.error("del error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return false;
    }

    public long incr(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.incr(key);
        } catch (Exception ex) {
            logger.error("incr error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return 0;
    }

    public long decr(String key) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.decr(key);
        } catch (Exception ex) {
            logger.error("incr error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return 0;
    }

    public List<String> blpop(int timeout, String queue) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.blpop(timeout, queue);
        } catch (Exception ex) {
            logger.error("blpop error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    public List<String> brpop(int timeout, String queue) {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
            return shardedJedis.brpop(timeout, queue);
        } catch (Exception ex) {
            logger.error("blpop error.", ex);
            returnBrokenResource(shardedJedis);
        } finally {
            returnResource(shardedJedis);
        }
        return null;
    }

    private void returnBrokenResource(ShardedJedis shardedJedis) {
        try {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        } catch (Exception e) {
            logger.error("returnBrokenResource error.", e);
        }
    }

    private void returnResource(ShardedJedis shardedJedis) {
        try {
            if (shardedJedis != null) {
                shardedJedis.close();
            }
        } catch (Exception e) {
            logger.error("returnResource error.", e);
        }
    }

    public static Map<String, String> transMap4Jedis(Map<String, Object> param) {
        Map<String, String> result = Maps.newHashMap();
        param.forEach((s, o) -> {
            if (o == null) {
                result.put(s, null);
            } else {
                if (o instanceof Date) {
                    result.put(s, StringUtil.dateFormat((Date) o, StringUtil.DF_YMD_24));
                } else {
                    result.put(s, o.toString());
                }
            }
        });
        return result;
    }

    public boolean isConnected() {
        ShardedJedis shardedJedis = null;
        try {
            shardedJedis = shardedJedisPool.getResource();
        } catch (JedisException je) {
            returnBrokenResource(shardedJedis);
            return false;
        } finally {
            returnResource(shardedJedis);
        }
        return true;
    }

    public static void main(String[] args) {
        System.out.println(IPUtil.internalIp("114.114.141.144"));
//        ExecutorService executorService = Executors.newCachedThreadPool();
//        executorService.submit(() -> {
//            while (true) {
//                List<String> blpop = instance().blpop(2, "deploy:host:channel");
//                if (blpop != null) {
//                    System.out.println("thread1 : " + blpop);
//                }
//            }
//        });
//        executorService.submit(() -> {
//            while (true) {
//                List<String> blpop = instance().blpop(2, "deploy:host:channel");
//                if (blpop != null) {
//                    System.out.println("thread2 : " + blpop);
//                }
//            }
//        });

    }
}
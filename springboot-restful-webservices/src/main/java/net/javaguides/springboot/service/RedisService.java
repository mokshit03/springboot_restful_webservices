package net.javaguides.springboot.service;
import java.util.concurrent.TimeUnit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor
@Service
public class RedisService {

    @Autowired
    private RedisTemplate redisTemplate;

    public <T> T get(String key, Class<T> entityClass){
    try
    {
        Object o = redisTemplate.opsForValue().get(redisTemplate);
        ObjectMapper objectMapper = new ObjectMapper();
        return objectMapper.readValue(o.toString(), entityClass);
    }
    catch (Exception e)
    {
            log.error("Exception", e);
            return null;
    }
    }


    public void set(String key, Object o, Long ttl){
        try
        {
            ObjectMapper objectMapper = new ObjectMapper();
            String jsonvalue = objectMapper.writeValueAsString(o);
            redisTemplate.opsForValue().set(key, jsonvalue, ttl, TimeUnit.SECONDS);
        }
        catch (Exception e)
        {
                log.error("Exception", e);
        }
        }

}

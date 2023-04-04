package shop.zip.travel.global.util;

import java.time.Duration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;
import shop.zip.travel.global.error.ErrorCode;
import shop.zip.travel.global.error.exception.NotMatchingValueForKey;

@Service
public class RedisUtil {

  private final RedisTemplate<String, String> redisTemplate;

  public RedisUtil(RedisTemplate<String, String> redisTemplate) {
    this.redisTemplate = redisTemplate;
  }

  public String getData(String key) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    if (valueOperations.get(key) == null) {
      throw new NotMatchingValueForKey(ErrorCode.NOT_MATCHING_VALUE_FOR_KEY);
    }
    return valueOperations.get(key);
  }

  public void setDataWithExpire(String key, String value, long duration) {
    ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
    Duration expireDuration = Duration.ofMinutes(duration);
    valueOperations.set(key, value, expireDuration);
  }

  public void deleteData(String key) {
    redisTemplate.delete(key);
  }
}

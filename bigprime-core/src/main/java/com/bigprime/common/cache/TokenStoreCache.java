package com.bigprime.common.cache;

import com.bigprime.common.base.UserDetail;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;

/**
 * @author lyw
 * @version 1.0
 */
@Component
@AllArgsConstructor
public class TokenStoreCache {
    private final RedisCache redisCache;

    public boolean containsKey(String key) {
        return redisCache.hasKey(key);
    }

    public void saveUser(String accessToken, UserDetail user) {
        String key = RedisKeys.getAccessTokenKey(accessToken);
        redisCache.set(key, user);
        redisCache.set(RedisKeys.getAccessTokenKey(user.getId()), accessToken);
    }

    public UserDetail getUser(String accessToken) {
        String key = RedisKeys.getAccessTokenKey(accessToken);
        return (UserDetail)redisCache.get(key);
    }

    public void deleteUser(String accessToken, Long userId) {
        String key = RedisKeys.getAccessTokenKey(accessToken);
        redisCache.delete(key);
        redisCache.delete(RedisKeys.getAccessTokenKey(userId));
    }
}

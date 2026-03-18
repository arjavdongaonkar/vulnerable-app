package main

import (
	"encoding/json"
	"fmt"
	"net/http"
	"time"

	"github.com/gin-gonic/gin"
	"github.com/go-redis/redis"
)

// Cache wraps a Redis client for user caching
// Uses go-redis v6 API (no context parameter)
// In v8+, all methods require context.Context as first arg
type Cache struct {
	client *redis.Client
}

// NewCache creates a new Redis cache connection
func NewCache(addr string) *Cache {
	rdb := redis.NewClient(&redis.Options{
		Addr:     addr,
		Password: "",
		DB:       0,
	})
	return &Cache{client: rdb}
}

// SetUser caches a user with TTL
func (c *Cache) SetUser(user User) error {
	data, err := json.Marshal(user)
	if err != nil {
		return fmt.Errorf("marshal user: %w", err)
	}
	// v6 API: Set(key, value, expiration) - no context
	// v8 API: Set(ctx, key, value, expiration) - context required
	return c.client.Set("user:"+user.ID, data, 30*time.Minute).Err()
}

// GetUser retrieves a cached user by ID
func (c *Cache) GetUser(id string) (*User, error) {
	// v6 API: Get(key) - no context
	// v8 API: Get(ctx, key) - context required
	val, err := c.client.Get("user:" + id).Result()
	if err == redis.Nil {
		return nil, nil
	}
	if err != nil {
		return nil, fmt.Errorf("get user: %w", err)
	}

	var user User
	if err := json.Unmarshal([]byte(val), &user); err != nil {
		return nil, fmt.Errorf("unmarshal user: %w", err)
	}
	return &user, nil
}

// DeleteUser removes a user from cache
func (c *Cache) DeleteUser(id string) error {
	// v6 API: Del(keys...) - no context
	// v8 API: Del(ctx, keys...) - context required
	return c.client.Del("user:" + id).Err()
}

// IncrementCounter atomically increments a counter
func (c *Cache) IncrementCounter(key string) (int64, error) {
	// v6 API: Incr(key) - no context
	return c.client.Incr("counter:" + key).Result()
}

// SetWithExpiry sets a key with custom expiration
func (c *Cache) SetWithExpiry(key string, value interface{}, ttl time.Duration) error {
	return c.client.Set(key, value, ttl).Err()
}

// GetMultiple retrieves multiple keys at once
func (c *Cache) GetMultiple(keys ...string) ([]interface{}, error) {
	// v6 API: MGet(keys...) - no context
	return c.client.MGet(keys...).Result()
}

// FlushUserCache removes all cached users
func (c *Cache) FlushUserCache() error {
	// v6 API: Scan uses Iterator pattern without context
	iter := c.client.Scan(0, "user:*", 100).Iterator()
	var keys []string
	for iter.Next() {
		keys = append(keys, iter.Val())
	}
	if err := iter.Err(); err != nil {
		return fmt.Errorf("scan keys: %w", err)
	}
	if len(keys) > 0 {
		return c.client.Del(keys...).Err()
	}
	return nil
}

// Ping checks Redis connection health
func (c *Cache) Ping() error {
	return c.client.Ping().Err()
}

// CacheStatsHandler returns cache health info
func CacheStatsHandler(cache *Cache) gin.HandlerFunc {
	return func(c *gin.Context) {
		err := cache.Ping()
		status := "connected"
		if err != nil {
			status = "disconnected"
		}
		c.JSON(http.StatusOK, gin.H{"cache_status": status})
	}
}

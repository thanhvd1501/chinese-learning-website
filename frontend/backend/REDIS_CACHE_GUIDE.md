# 🚀 Redis Cache Implementation Guide

## Enterprise-Level Redis Caching Architecture

### 📋 Overview

Implementation này sử dụng Redis làm distributed cache với các enterprise features:

- ✅ **Connection Pooling** với Lettuce
- ✅ **Custom Serialization** với Jackson
- ✅ **Multiple Cache Configurations** với different TTLs
- ✅ **Health Monitoring** và statistics
- ✅ **Admin Management Endpoints**
- ✅ **Graceful Error Handling**
- ✅ **Redis Commander** UI for visualization
- ✅ **Production-ready** với Sentinel và Cluster support

---

## 🏗️ Architecture

```
┌─────────────────────────────────────────────────────────┐
│                    Application Layer                     │
├─────────────────────────────────────────────────────────┤
│  @Cacheable Annotation → Spring Cache Abstraction       │
├─────────────────────────────────────────────────────────┤
│              RedisCacheManager (Custom Config)          │
│  - Multiple cache configs với different TTLs             │
│  - Custom key generator                                  │
│  - Error handler (graceful degradation)                 │
├─────────────────────────────────────────────────────────┤
│           RedisTemplate (Custom Serialization)          │
│  - Jackson JSON serializer                               │
│  - String key serializer                                 │
│  - Transaction support                                   │
├─────────────────────────────────────────────────────────┤
│        Lettuce Connection Pool (20 connections)         │
│  - max-active: 20                                        │
│  - max-idle: 10                                          │
│  - min-idle: 5                                           │
├─────────────────────────────────────────────────────────┤
│                    Redis Server                          │
│  - 512MB max memory                                      │
│  - LRU eviction policy                                   │
│  - AOF persistence                                       │
└─────────────────────────────────────────────────────────┘
```

---

## 🚀 Quick Start

### 1. Start Redis

```bash
cd backend
docker-compose up -d redis redis-commander
```

Services:
- **Redis**: http://localhost:6379
- **Redis Commander** (UI): http://localhost:8081

### 2. Verify Redis Connection

```bash
# Using redis-cli
docker exec -it chinese-learning-redis redis-cli ping
# Should return: PONG

# Check info
docker exec -it chinese-learning-redis redis-cli info
```

### 3. Start Application

```bash
mvn spring-boot:run
```

### 4. Verify Cache is Working

```bash
# Get vocabularies (first call - cache miss)
curl http://localhost:8080/api/vocab?page=0&size=20

# Get vocabularies again (cache hit - faster!)
curl http://localhost:8080/api/vocab?page=0&size=20

# Check cache statistics (requires ADMIN token)
curl -H "Authorization: Bearer $ADMIN_TOKEN" \
  http://localhost:8080/api/admin/cache/stats
```

---

## 📊 Cache Configuration

### Cache Hierarchy

| Cache Name | TTL | Use Case |
|-----------|-----|----------|
| `radicals` | 2 hours | Very static data, rarely changes |
| `textbooks` | 1 hour | Static catalog data |
| `courses` | 1 hour | Course listings |
| `grammar-topics` | 1 hour | Grammar reference data |
| `ai-responses` | 1 hour | AI chat responses for repeated queries |
| `vocabularies` | 30 min | Frequently accessed, may update |
| `user-progress` | 10 min | User learning progress |
| `users` | 5 min | User profile data (frequent updates) |
| **default** | 10 min | Fallback for unconfigured caches |

### Why Different TTLs?

1. **Static data** (radicals, courses) → **Long TTL** (1-2 hours)
   - Rarely changes
   - Safe to cache long
   - Reduces database load significantly

2. **Semi-static data** (vocabularies) → **Medium TTL** (30 min)
   - May be updated occasionally
   - Balance between freshness and performance

3. **User data** (profiles, progress) → **Short TTL** (5-10 min)
   - Frequently updated
   - Need fresher data
   - Still benefit from caching

---

## 🔧 Configuration Details

### application.yml

```yaml
spring:
  cache:
    type: redis
    redis:
      time-to-live: 600000 # 10 minutes default
      cache-null-values: false # Don't cache null
      key-prefix: "chinese-learning::"
      use-key-prefix: true

  data:
    redis:
      host: localhost
      port: 6379
      database: 0
      timeout: 2000ms
      lettuce:
        pool:
          max-active: 20    # Max connections
          max-idle: 10      # Max idle
          min-idle: 5       # Min idle
          max-wait: 2000ms  # Wait timeout
```

### Redis Server Configuration

```bash
# In docker-compose.yml
redis-server \
  --appendonly yes \                    # AOF persistence
  --requirepass "" \                     # No password (dev)
  --maxmemory 512mb \                   # Memory limit
  --maxmemory-policy allkeys-lru        # Eviction policy
```

**Eviction Policies:**
- `allkeys-lru`: Evict least recently used keys
- `volatile-lru`: Evict LRU keys with TTL
- `allkeys-random`: Evict random keys
- `volatile-random`: Evict random keys with TTL

---

## 💻 Usage Examples

### Basic Caching

```java
@Service
public class VocabularyService {
    
    @Cacheable(value = "vocabularies", 
               key = "#page + '-' + #size + '-' + #bienThe + '-' + #search")
    public PageResponse<VocabularyResponse> getVocabularies(
            int page, int size, BienTheType bienThe, String search) {
        // Database query - only executed on cache miss
        return repository.findAll(...);
    }
}
```

**Key Format:** `VocabularyService.getVocabularies:0:20:GIAN:你好`

**Redis Key:** `chinese-learning::vocabularies::VocabularyService.getVocabularies:0:20:GIAN:你好`

### Cache Eviction

```java
@Service
public class CourseService {
    
    @CacheEvict(value = "courses", allEntries = true)
    public CourseResponse createCourse(CreateCourseRequest request) {
        // Invalidate ALL course cache entries
        return repository.save(...);
    }
    
    @CacheEvict(value = "courses", key = "#id")
    public void deleteCourse(Long id) {
        // Invalidate specific course cache
        repository.deleteById(id);
    }
}
```

### Cache Put

```java
@CachePut(value = "courses", key = "#result.id")
public CourseResponse updateCourse(Long id, UpdateCourseRequest request) {
    // Update cache with new data
    return repository.save(...);
}
```

### Manual Cache Operations

```java
@Service
@RequiredArgsConstructor
public class ManualCacheService {
    
    private final RedisTemplate<String, Object> redisTemplate;
    
    public void cacheData(String key, Object value) {
        redisTemplate.opsForValue().set(
            "custom::" + key, 
            value, 
            Duration.ofMinutes(30)
        );
    }
    
    public Object getData(String key) {
        return redisTemplate.opsForValue().get("custom::" + key);
    }
}
```

---

## 📈 Monitoring & Management

### Health Check

```bash
# Via Actuator
curl http://localhost:8080/actuator/health

# Response
{
  "status": "UP",
  "components": {
    "redis": {
      "status": "UP",
      "details": {
        "connected": true,
        "uptime_in_seconds": "3600",
        "used_memory_human": "1.2M",
        "connected_clients": "5",
        "total_cached_keys": 150
      }
    }
  }
}
```

### Cache Statistics

```bash
# Get cache stats (ADMIN only)
curl -H "Authorization: Bearer $ADMIN_TOKEN" \
  http://localhost:8080/api/admin/cache/stats

# Response
{
  "connected": true,
  "uptime_in_seconds": "3600",
  "used_memory_human": "1.2M",
  "connected_clients": "5",
  "total_commands_processed": "15234",
  "total_cached_keys": 150,
  "cache_breakdown": {
    "vocabularies": 80,
    "courses": 30,
    "textbooks": 10,
    "users": 30
  }
}
```

### Clear Cache

```bash
# Clear specific cache
curl -X DELETE \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  http://localhost:8080/api/admin/cache/vocabularies

# Clear all caches
curl -X DELETE \
  -H "Authorization: Bearer $ADMIN_TOKEN" \
  http://localhost:8080/api/admin/cache/all
```

### Redis Commander UI

Access: http://localhost:8081

Features:
- Browse all keys
- View key values
- Delete keys
- Monitor memory usage
- Real-time statistics

---

## 🔍 Debugging

### Enable Cache Logging

```yaml
logging:
  level:
    org.springframework.cache: DEBUG
    org.springframework.data.redis: DEBUG
    io.lettuce.core: DEBUG
```

### View Cache Operations

```bash
# Monitor Redis commands in real-time
docker exec -it chinese-learning-redis redis-cli monitor

# Output
1698537000.123456 [0 172.18.0.1:54321] "GET" "chinese-learning::vocabularies::..."
1698537000.234567 [0 172.18.0.1:54322] "SET" "chinese-learning::courses::..." "..."
```

### Check Cache Keys

```bash
# List all keys
docker exec -it chinese-learning-redis redis-cli KEYS "chinese-learning::*"

# Count keys
docker exec -it chinese-learning-redis redis-cli DBSIZE

# Get key TTL
docker exec -it chinese-learning-redis redis-cli TTL "chinese-learning::vocabularies::..."
```

---

## 🛡️ Error Handling

### Graceful Degradation

```java
@Override
public void handleCacheGetError(RuntimeException exception, 
                               Cache cache, Object key) {
    log.error("Cache GET error - falling back to database", exception);
    // Application continues without cache
    // Performance impact but no downtime
}
```

**Behavior:**
- Cache failure → Log error
- Continue without cache
- Database query executes normally
- Application remains available

### Connection Pool Exhaustion

```yaml
lettuce:
  pool:
    max-wait: 2000ms  # Timeout after 2 seconds
```

If all connections busy:
1. Wait up to 2 seconds
2. If still no connection → throw exception
3. Error handler catches it
4. Fall back to database

---

## 🚀 Performance Optimization

### 1. Connection Pooling

```yaml
max-active: 20  # 20 concurrent connections
min-idle: 5     # Always keep 5 ready
max-idle: 10    # Don't keep more than 10 idle
```

**Impact:**
- Reuse connections (no handshake overhead)
- Handle 20 concurrent requests
- Fast response time

### 2. Serialization

```java
// JSON serialization vs Java serialization
// JSON: Human readable, language agnostic, slower
// Java: Binary, faster, not readable

// We use JSON for:
// - Debugging ease
// - Cross-language compatibility
// - Acceptable performance
```

### 3. Key Design

```
Good: chinese-learning::vocabularies::0:20:GIAN
Bad:  vocab_0_20_GIAN (collision risk)
```

**Best Practices:**
- Include namespace prefix
- Include cache name
- Use meaningful separators
- Include all relevant parameters

### 4. TTL Strategy

```
Frequently updated data → Short TTL (5-10 min)
Static data → Long TTL (1-2 hours)
Critical data → No cache (always fresh)
```

---

## 📊 Metrics & Monitoring

### Spring Boot Actuator Metrics

```bash
# Cache metrics
curl http://localhost:8080/actuator/metrics/cache.gets

# Response
{
  "name": "cache.gets",
  "measurements": [
    {"statistic": "COUNT", "value": 1523.0},
    {"statistic": "HIT", "value": 1200.0},
    {"statistic": "MISS", "value": 323.0}
  ]
}

# Hit ratio: 1200 / 1523 = 78.8%
```

### Redis INFO Command

```bash
docker exec -it chinese-learning-redis redis-cli INFO stats

# Key metrics:
# - total_connections_received
# - total_commands_processed
# - keyspace_hits
# - keyspace_misses
# - used_memory
# - evicted_keys
```

---

## 🏭 Production Deployment

### High Availability với Redis Sentinel

```yaml
spring:
  data:
    redis:
      sentinel:
        master: mymaster
        nodes:
          - redis-sentinel-1:26379
          - redis-sentinel-2:26379
          - redis-sentinel-3:26379
```

**Benefits:**
- Automatic failover
- Master election
- No single point of failure

### Horizontal Scaling với Redis Cluster

```yaml
spring:
  data:
    redis:
      cluster:
        nodes:
          - redis-1:7000
          - redis-2:7001
          - redis-3:7002
        max-redirects: 3
```

**Benefits:**
- Data sharding
- Scale storage capacity
- Distribute load

### Security

```yaml
spring:
  data:
    redis:
      password: ${REDIS_PASSWORD}  # From environment
      ssl: true                     # Enable SSL/TLS
```

### Backup Strategy

```bash
# RDB snapshots
save 900 1      # Save if 1 key changed in 15 min
save 300 10     # Save if 10 keys changed in 5 min
save 60 10000   # Save if 10000 keys changed in 1 min

# AOF (Append Only File)
appendonly yes
appendfsync everysec  # Sync every second
```

---

## 📚 Best Practices

### ✅ DO

1. **Use appropriate TTLs** based on data volatility
2. **Set memory limits** to prevent OOM
3. **Monitor cache hit ratio** (aim for >70%)
4. **Implement graceful degradation**
5. **Use connection pooling**
6. **Namespace your keys** to avoid collisions
7. **Clear stale cache** after updates

### ❌ DON'T

1. **Cache everything** - only cache what's needed
2. **Use default TTL** for all caches
3. **Cache null values** - waste of memory
4. **Ignore cache errors** - implement error handling
5. **Hardcode cache keys** - use dynamic key generation
6. **Cache sensitive data** without encryption
7. **Forget to clear cache** on updates

---

## 🎯 Performance Benchmarks

### Cache Hit vs Miss

```
Cache HIT:  ~1-5ms   (99% faster)
Cache MISS: ~50-100ms (database query)

With 80% hit ratio:
- 80% requests: 1-5ms
- 20% requests: 50-100ms
- Average: ~15-25ms (vs 50-100ms without cache)
```

### Connection Pool Impact

```
Without pool: ~10-20ms connection overhead per request
With pool:    ~0ms (reuse existing connection)
```

---

## 🆘 Troubleshooting

### Issue: High Memory Usage

**Solution:**
1. Check `used_memory` in Redis INFO
2. Verify eviction policy is working
3. Reduce TTLs if needed
4. Increase `maxmemory` limit

### Issue: Cache Miss Rate High

**Solution:**
1. Check if keys are properly generated
2. Verify TTL isn't too short
3. Monitor eviction count
4. Increase memory if evictions are high

### Issue: Connection Timeouts

**Solution:**
1. Increase pool size
2. Increase `max-wait` timeout
3. Check Redis server load
4. Verify network connectivity

---

## 📖 Summary

**Enterprise Redis Caching** provides:
- ✅ 10-100x performance improvement
- ✅ Reduced database load
- ✅ Better user experience
- ✅ Scalability for high traffic
- ✅ Production-ready architecture

**Production Checklist:**
- [x] Connection pooling configured
- [x] Appropriate TTLs set
- [x] Error handling implemented
- [x] Monitoring enabled
- [x] Backup strategy defined
- [x] Security configured
- [x] Documentation complete

🚀 **Redis cache is production-ready!**


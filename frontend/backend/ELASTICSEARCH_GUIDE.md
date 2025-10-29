# Elasticsearch Integration Guide

## 📋 Tổng quan

Hệ thống đã tích hợp **Elasticsearch 8.11.1** để cung cấp tính năng tìm kiếm nâng cao với:

- **Full-text search** với fuzzy matching
- **Multi-field search** (tìm kiếm đồng thời nhiều trường)
- **Autocomplete suggestions**
- **Advanced filtering** và **aggregations**
- **Real-time indexing** với scheduled sync

---

## 🏗️ Kiến trúc

```
PostgreSQL (Source of Truth)
      ↓
Data Sync Service (Scheduled)
      ↓
Elasticsearch (Search Engine)
      ↓
Search API Endpoints
```

### Components

1. **Document Models** (`search/document/`)
   - `VocabularyDocument` - Từ vựng
   - `GrammarTopicDocument` - Ngữ pháp
   - `CourseDocument` - Khóa học

2. **Repositories** (`search/repository/`)
   - Elasticsearch repositories với custom queries

3. **Services** (`search/service/`)
   - `VocabularySearchService`
   - `GrammarSearchService`
   - `CourseSearchService`
   - `DataSyncService` - Đồng bộ dữ liệu

4. **Controllers**
   - `SearchController` - Public search endpoints
   - `DataSyncController` - Admin sync endpoints

---

## 🚀 Setup Local Development

### 1. Start Elasticsearch & Kibana

```bash
cd backend
docker-compose up -d elasticsearch kibana
```

Kiểm tra health:
```bash
# Elasticsearch
curl http://localhost:9200/_cluster/health

# Kibana UI
open http://localhost:5601
```

### 2. Start Application

```bash
mvn spring-boot:run
```

Application sẽ tự động:
- Connect tới Elasticsearch
- Tạo indexes
- Sync toàn bộ dữ liệu từ PostgreSQL

---

## 📊 Elasticsearch Indices

### vocabularies

```json
{
  "mappings": {
    "properties": {
      "hanzi": { "type": "text", "analyzer": "ik_max_word" },
      "pinyin": { "type": "text", "analyzer": "pinyin_analyzer" },
      "nghia": { "type": "text", "analyzer": "vietnamese_analyzer" },
      "viDu": { "type": "text" },
      "bienThe": { "type": "keyword" },
      "tags": { "type": "keyword" },
      "searchText": { "type": "text", "analyzer": "standard" }
    }
  }
}
```

### grammar_topics

```json
{
  "mappings": {
    "properties": {
      "title": { "type": "text" },
      "structure": { "type": "text", "analyzer": "ik_max_word" },
      "explanation": { "type": "text", "analyzer": "vietnamese_analyzer" },
      "tags": { "type": "keyword" }
    }
  }
}
```

### courses

```json
{
  "mappings": {
    "properties": {
      "title": { "type": "text" },
      "description": { "type": "text" },
      "level": { "type": "integer" },
      "difficulty": { "type": "keyword" },
      "textbookId": { "type": "long" }
    }
  }
}
```

---

## 🔍 Search API Examples

### 1. Simple Vocabulary Search

```bash
GET /api/search/vocabulary?q=你好&page=0&size=20
```

Response:
```json
{
  "results": [
    {
      "id": 1,
      "hanzi": "你好",
      "pinyin": "nǐ hǎo",
      "nghia": "Xin chào",
      "bienThe": "BOTH"
    }
  ],
  "totalHits": 1,
  "page": 0,
  "size": 20,
  "totalPages": 1,
  "searchTimeMs": 45,
  "maxScore": 0.95
}
```

### 2. Advanced Vocabulary Search

```bash
POST /api/search/vocabulary
Content-Type: application/json

{
  "query": "xin chào",
  "page": 0,
  "size": 20,
  "bienThe": "GIAN",
  "fuzzySearch": true,
  "sortBy": "createdAt",
  "sortDirection": "DESC"
}
```

### 3. Search by Hanzi

```bash
GET /api/search/vocabulary/hanzi?hanzi=你&page=0&size=20
```

### 4. Search by Pinyin

```bash
GET /api/search/vocabulary/pinyin?pinyin=ni&page=0&size=20
```

### 5. Autocomplete Suggestions

```bash
GET /api/search/vocabulary/suggest?prefix=你&limit=10
```

Response:
```json
["你好", "你们", "你的", "你们好"]
```

### 6. Grammar Search

```bash
GET /api/search/grammar?q=比较句&page=0&size=20
```

### 7. Course Search with Filters

```bash
POST /api/search/courses
Content-Type: application/json

{
  "query": "HSK",
  "difficulty": "MEDIUM",
  "minLevel": 1,
  "maxLevel": 3,
  "page": 0,
  "size": 10
}
```

---

## 🔄 Data Synchronization

### Auto Sync

Data tự động sync:
- **On startup**: Toàn bộ dữ liệu
- **Scheduled**: Mỗi 30 phút (cấu hình trong `application.yml`)

### Manual Sync (Admin Only)

```bash
# Sync all data
POST /api/admin/sync/all
Authorization: Bearer {admin_token}

# Sync vocabulary only
POST /api/admin/sync/vocabulary
Authorization: Bearer {admin_token}

# Sync grammar
POST /api/admin/sync/grammar
Authorization: Bearer {admin_token}

# Sync courses
POST /api/admin/sync/courses
Authorization: Bearer {admin_token}
```

---

## 🎯 Search Features

### 1. Fuzzy Search
Tự động sửa lỗi chính tả:
```
Query: "xinhchao" → Matches: "xin chào"
Query: "nǐhǎo" → Matches: "你好"
```

### 2. Multi-field Search
Tìm kiếm đồng thời nhiều trường với trọng số:
- `hanzi^3` - priority cao nhất
- `pinyin^2` - priority trung bình
- `nghia^2` - priority trung bình
- `viDu` - priority thấp

### 3. Wildcard Search
```
Query: "你*" → Matches: "你好", "你们", "你的"
```

### 4. Range Queries (Courses)
```json
{
  "minLevel": 1,
  "maxLevel": 3
}
```

---

## 📈 Performance Tuning

### Index Settings

```json
{
  "settings": {
    "number_of_shards": 1,
    "number_of_replicas": 0,
    "refresh_interval": "30s",
    "max_result_window": 10000
  }
}
```

### Query Optimization

1. **Use filters for exact matches** (faster than queries)
2. **Limit result size** (default: 20, max: 100)
3. **Use pagination** instead of large result sets
4. **Cache frequent queries** with Redis

---

## 🔧 Monitoring

### Kibana Dev Tools

```
# Check cluster health
GET /_cluster/health

# Check indices
GET /_cat/indices?v

# View vocabulary mapping
GET /vocabularies/_mapping

# Count documents
GET /vocabularies/_count

# Sample search
GET /vocabularies/_search
{
  "query": {
    "multi_match": {
      "query": "你好",
      "fields": ["hanzi^3", "pinyin^2", "nghia^2"]
    }
  }
}
```

### Application Logs

```
2024-01-01 12:00:00 INFO  DataSyncService - Syncing vocabulary data...
2024-01-01 12:00:05 INFO  DataSyncService - Vocabulary sync completed: 5000 items in 5234 ms
2024-01-01 12:00:10 INFO  VocabularySearchService - Vocabulary search completed: 42 results in 45 ms
```

---

## 🐛 Troubleshooting

### Issue: Connection refused to Elasticsearch

**Solution:**
```bash
# Check if Elasticsearch is running
docker ps | grep elasticsearch

# Check logs
docker logs chinese-learning-elasticsearch

# Restart
docker-compose restart elasticsearch
```

### Issue: Sync không hoạt động

**Giải pháp:**
1. Kiểm tra logs: `docker logs chinese-learning-app`
2. Manual trigger: `POST /api/admin/sync/all`
3. Xóa và rebuild indices:
   ```bash
   DELETE /vocabularies
   DELETE /grammar_topics
   DELETE /courses
   POST /api/admin/sync/all
   ```

### Issue: Search results không chính xác

**Giải pháp:**
1. Rebuild search text: Re-sync data
2. Update analyzers trong document mappings
3. Adjust field boosting trong queries

---

## 📚 References

- [Elasticsearch Documentation](https://www.elastic.co/guide/en/elasticsearch/reference/current/index.html)
- [Spring Data Elasticsearch](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/)
- [IK Analyzer (Chinese)](https://github.com/medcl/elasticsearch-analysis-ik)

---

## 🎓 Best Practices

1. **Always use PostgreSQL as source of truth**
2. **Schedule regular syncs** (30 minutes recommended)
3. **Monitor Elasticsearch health** via Kibana
4. **Use appropriate analyzers** cho từng ngôn ngữ
5. **Cache search results** với Redis cho queries phổ biến
6. **Limit result size** để tối ưu performance
7. **Use filters over queries** khi có thể (faster)
8. **Implement circuit breaker** cho Elasticsearch failures

---

**Author**: Senior Backend Architect  
**Last Updated**: 2024-10-30


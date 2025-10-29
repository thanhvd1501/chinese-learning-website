# 🔍 Elasticsearch Integration - Quick Start

## Tổng quan

Hệ thống Chinese Learning Backend đã được tích hợp **Elasticsearch 8.11.1** để cung cấp khả năng tìm kiếm nâng cao.

## ✨ Tính năng

- **Full-text search** với fuzzy matching
- **Multi-language support** (Chinese, Pinyin, Vietnamese)
- **Autocomplete suggestions**
- **Advanced filtering** (variant type, difficulty, level)
- **Real-time sync** từ PostgreSQL
- **High performance** search engine

## 🚀 Quick Start

### 1. Start Elasticsearch & Kibana

```bash
cd backend
docker-compose up -d elasticsearch kibana
```

Kiểm tra:
- Elasticsearch: http://localhost:9200
- Kibana: http://localhost:5601

### 2. Start Application

```bash
mvn spring-boot:run
```

Application tự động sync dữ liệu từ PostgreSQL khi khởi động.

### 3. Test Search API

```bash
# Search vocabulary
curl "http://localhost:8080/api/search/vocabulary?q=你好"

# Autocomplete
curl "http://localhost:8080/api/search/vocabulary/suggest?prefix=你&limit=5"

# Search grammar
curl "http://localhost:8080/api/search/grammar?q=比较句"

# Search courses
curl "http://localhost:8080/api/search/courses?q=HSK"
```

## 📡 API Endpoints

### Search (Public)

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/search/vocabulary?q={query}` | Tìm từ vựng |
| GET | `/api/search/vocabulary/hanzi?hanzi={char}` | Tìm theo Hanzi |
| GET | `/api/search/vocabulary/pinyin?pinyin={text}` | Tìm theo Pinyin |
| GET | `/api/search/vocabulary/meaning?meaning={text}` | Tìm theo nghĩa |
| GET | `/api/search/vocabulary/suggest?prefix={text}` | Autocomplete |
| POST | `/api/search/vocabulary` | Advanced search |
| GET | `/api/search/grammar?q={query}` | Tìm ngữ pháp |
| GET | `/api/search/courses?q={query}` | Tìm khóa học |

### Data Sync (Admin Only)

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/admin/sync/all` | Sync toàn bộ |
| POST | `/api/admin/sync/vocabulary` | Sync từ vựng |
| POST | `/api/admin/sync/grammar` | Sync ngữ pháp |
| POST | `/api/admin/sync/courses` | Sync khóa học |

## 📊 Architecture

```
PostgreSQL (Source) 
    ↓ Sync Service
Elasticsearch (Search)
    ↓ Search Service
REST API (Controller)
```

## 📁 Project Structure

```
backend/
├── src/main/java/com/chineselearning/
│   ├── search/
│   │   ├── document/           # Elasticsearch documents
│   │   │   ├── VocabularyDocument.java
│   │   │   ├── GrammarTopicDocument.java
│   │   │   └── CourseDocument.java
│   │   ├── repository/         # Elasticsearch repositories
│   │   │   ├── VocabularySearchRepository.java
│   │   │   ├── GrammarSearchRepository.java
│   │   │   └── CourseSearchRepository.java
│   │   ├── service/            # Search services
│   │   │   ├── VocabularySearchService.java
│   │   │   ├── GrammarSearchService.java
│   │   │   ├── CourseSearchService.java
│   │   │   └── DataSyncService.java
│   │   └── dto/                # Search DTOs
│   │       ├── SearchRequest.java
│   │       └── SearchResponse.java
│   ├── controller/
│   │   ├── SearchController.java
│   │   └── DataSyncController.java
│   └── config/
│       ├── ElasticsearchConfig.java
│       └── AsyncConfig.java
├── ELASTICSEARCH_GUIDE.md                     # Chi tiết guide
├── ELASTICSEARCH_IMPLEMENTATION_SUMMARY.md    # Implementation details
└── docker-compose.yml                         # Elasticsearch + Kibana
```

## 🔄 Data Synchronization

### Tự động
- **On Startup**: Sync toàn bộ dữ liệu
- **Scheduled**: Mỗi 30 phút (configurable)

### Thủ công (Admin)
```bash
# Sync all data
curl -X POST http://localhost:8080/api/admin/sync/all \
  -H "Authorization: Bearer {admin_token}"
```

## 📚 Documentation

- **[ELASTICSEARCH_GUIDE.md](ELASTICSEARCH_GUIDE.md)** - Complete guide
  - API examples
  - Search features
  - Monitoring
  - Troubleshooting

- **[ELASTICSEARCH_IMPLEMENTATION_SUMMARY.md](ELASTICSEARCH_IMPLEMENTATION_SUMMARY.md)** - Implementation details
  - Architecture
  - Components
  - Best practices

## 🛠️ Configuration

### application.yml
```yaml
spring:
  elasticsearch:
    uris: http://localhost:9200
    
app:
  elasticsearch:
    sync:
      cron: "0 */30 * * * *"
      enabled: true
```

## 🔧 Troubleshooting

### Elasticsearch không start
```bash
docker-compose restart elasticsearch
docker logs chinese-learning-elasticsearch
```

### Manual sync
```bash
POST /api/admin/sync/all
```

### Check index health
```bash
curl http://localhost:9200/_cat/indices?v
```

## 🎯 Performance

- Average search time: **< 50ms**
- Fuzzy matching enabled
- Auto pagination
- Result caching ready

## 🔒 Security

- Public search endpoints (appropriate for content discovery)
- Admin-only sync endpoints
- JWT authentication via Keycloak
- Role-based access control

## 📈 Monitoring

Access Kibana: http://localhost:5601

Dev Tools examples:
```
GET /vocabularies/_search
GET /grammar_topics/_search
GET /courses/_search
```

---

**For detailed information, see [ELASTICSEARCH_GUIDE.md](ELASTICSEARCH_GUIDE.md)**


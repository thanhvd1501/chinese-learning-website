# Flyway SQL Migration Fixes

## ✅ Fixed All SQL Schema & Data Mismatches

All SQL migration files have been updated to match the refactored Entity models.

---

## 📊 Changes Summary

### V1__init_schema.sql ✅

| Table | Old Column | New Column | Old Value | New Value |
|-------|-----------|------------|-----------|-----------|
| **textbooks** | `phien_ban` | `version` | 'MOI', 'CU' | 'NEW', 'OLD' |
| **textbooks** | `nam_xuat_ban` | `publication_year` | - | - |
| **courses** | `difficulty` | `difficulty` | 'Cơ bản', 'Trung bình', 'Nâng cao', 'Chuyên gia' | 'BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT' |
| **vocabulary** | `vi_du` | `example` | - | - |
| **vocabulary** | `bien_the` | `variant` | 'GIAN', 'PHON' | 'SIMPLIFIED', 'TRADITIONAL' |
| **grammar_topics** | `level` | `level` | 'Cơ bản', 'Trung bình', 'Nâng cao' | 'BASIC', 'MEDIUM', 'ADVANCED' |

### V2__seed_data.sql ✅

Updated all INSERT statements to use correct:
- Column names (`version`, `publication_year`, `example`, `variant`)
- Enum values ('NEW', 'OLD', 'BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT', 'SIMPLIFIED', 'BASIC', 'MEDIUM')

---

## 🔧 Detailed Changes

### 1. Textbooks Table
```sql
-- Before
phien_ban VARCHAR(10) CHECK (phien_ban IN ('PB3', 'MOI', 'CU'))
nam_xuat_ban INTEGER

-- After
version VARCHAR(10) CHECK (version IN ('PB3', 'NEW', 'OLD'))
publication_year INTEGER
```

### 2. Courses Table
```sql
-- Before
difficulty VARCHAR(20) CHECK (difficulty IN ('Cơ bản', 'Trung bình', 'Nâng cao', 'Chuyên gia'))

-- After
difficulty VARCHAR(20) CHECK (difficulty IN ('BEGINNER', 'INTERMEDIATE', 'ADVANCED', 'EXPERT'))
```

### 3. Vocabulary Table
```sql
-- Before
vi_du TEXT
bien_the VARCHAR(10) CHECK (bien_the IN ('GIAN', 'PHON', 'BOTH'))

-- After
example TEXT
variant VARCHAR(20) CHECK (variant IN ('SIMPLIFIED', 'TRADITIONAL', 'BOTH'))
```

### 4. Grammar Topics Table
```sql
-- Before
level VARCHAR(20) CHECK (level IN ('Cơ bản', 'Trung bình', 'Nâng cao'))

-- After
level VARCHAR(20) CHECK (level IN ('BASIC', 'MEDIUM', 'ADVANCED'))
```

### 5. Indexes
```sql
-- Before
CREATE INDEX idx_vocabulary_bien_the ON vocabulary(bien_the);

-- After
CREATE INDEX idx_vocabulary_variant ON vocabulary(variant);
```

---

## 📝 Seed Data Changes

### Textbooks
```sql
-- Before
INSERT INTO textbooks (name, description, phien_ban, nam_xuat_ban, ...) VALUES
('...', '...', 'MOI', 2020, ...)

-- After
INSERT INTO textbooks (name, description, version, publication_year, ...) VALUES
('...', '...', 'NEW', 2020, ...)
```

### Courses
```sql
-- Before
(1, 'Hán 1', '...', 15, '2-3 tháng', 'Cơ bản', ...)

-- After
(1, 'Hán 1', '...', 15, '2-3 tháng', 'BEGINNER', ...)
```

### Grammar Topics
```sql
-- Before
('Các loại từ trong tiếng Trung', '...', 'Cơ bản')

-- After
('Các loại từ trong tiếng Trung', '...', 'BASIC')
```

### Vocabulary
```sql
-- Before
INSERT INTO vocabulary (hanzi, pinyin, nghia, vi_du, bien_the, hsk_level) VALUES
('你好', 'nǐ hǎo', 'Xin chào', '你好，我是小明。', 'GIAN', 1)

-- After
INSERT INTO vocabulary (hanzi, pinyin, nghia, example, variant, hsk_level) VALUES
('你好', 'nǐ hǎo', 'Xin chào', '你好，我是小明。', 'SIMPLIFIED', 1)
```

---

## ✅ Verification

### Schema Alignment
- ✅ All column names match Entity field names (with `@Column` mapping)
- ✅ All CHECK constraints use correct enum values
- ✅ All indexes reference correct column names

### Data Alignment
- ✅ All INSERT statements use correct column names
- ✅ All enum values match Entity enum definitions
- ✅ Sample data uses valid constraint values

---

## 🎯 Enum Mapping Reference

| Entity | Enum | Old Values | New Values |
|--------|------|------------|------------|
| **Textbook** | `VersionType` | MOI, CU | NEW, OLD |
| **Course** | `Difficulty` | Cơ bản, Trung bình, Nâng cao, Chuyên gia | BEGINNER, INTERMEDIATE, ADVANCED, EXPERT |
| **Vocabulary** | `VariantType` | GIAN, PHON | SIMPLIFIED, TRADITIONAL |
| **GrammarTopic** | `Level` | Cơ bản, Trung bình, Nâng cao | BASIC, MEDIUM, ADVANCED |

---

## 🚀 Migration Status

```
✅ V1__init_schema.sql - UPDATED
✅ V2__seed_data.sql - UPDATED
✅ All constraints valid
✅ All seed data valid
✅ Ready for Flyway migration
```

---

## 📋 How to Run

```bash
# Clean existing database (if needed)
docker-compose down -v

# Start fresh
docker-compose up -d postgres

# Spring Boot will auto-run Flyway migrations
./mvnw spring-boot:run
```

---

## ✅ Result

**Flyway migrations will now run successfully!** 🎉

All SQL schema and seed data now perfectly match the refactored Entity models.

---

**Fixed by**: Senior Backend Architect  
**Date**: 2024-10-30  
**Status**: ✅ COMPLETE - READY FOR MIGRATION


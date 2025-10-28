-- Seed data for textbooks
INSERT INTO textbooks (name, description, phien_ban, nam_xuat_ban, pdf_url, cover_image_url) VALUES
('Hán ngữ 6 Quyển - Phiên bản 3', 'Xuất bản năm 2022, tái bản lần 1 năm 2024', 'PB3', 2022, '/han_ngu_q1.pdf', '/han-1.png'),
('Hán ngữ 6 Quyển - Phiên bản Mới', 'Xuất bản năm 2020, tái bản lần 8 năm 2024', 'MOI', 2020, '/han_ngu_q1.pdf', '/han-2.png'),
('Hán ngữ 6 Quyển - Phiên bản Cũ', 'Xuất bản năm 2015, tái bản năm 2024', 'CU', 2015, '/han_ngu_q1.pdf', '/han-3.png');

-- Seed data for courses (assuming we have a textbook with id=1)
INSERT INTO courses (textbook_id, level, title, description, lessons, duration, difficulty, cover_image_url) VALUES
(1, 'Hán 1', 'Hán 1', 'Cấp độ cơ bản nhất, học 150 từ vựng thiết yếu', 15, '2-3 tháng', 'Cơ bản', '/han-1.png'),
(1, 'Hán 2', 'Hán 2', 'Mở rộng từ vựng lên 300 từ, học ngữ pháp cơ bản', 15, '3-4 tháng', 'Cơ bản', '/han-2.png'),
(1, 'Hán 3', 'Hán 3', 'Nâng cao với 600 từ vựng và ngữ pháp phức tạp hơn', 10, '4-5 tháng', 'Trung bình', '/han-3.png'),
(1, 'Hán 4', 'Hán 4', 'Thành thạo 1200 từ vựng, có thể giao tiếp tự nhiên', 10, '6-8 tháng', 'Trung bình', '/han-4.png'),
(1, 'Hán 5', 'Hán 5', 'Trình độ cao với 2500 từ vựng và văn bản phức tạp', 13, '8-12 tháng', 'Nâng cao', '/han-5.png'),
(1, 'Hán 6', 'Hán 6', 'Cấp độ cao nhất với 5000+ từ vựng và văn học', 13, '12+ tháng', 'Chuyên gia', '/han-6.png');

-- Seed data for grammar topics
INSERT INTO grammar_topics (title, description, level) VALUES
('Các loại từ trong tiếng Trung', 'Phân loại và cách sử dụng các từ loại trong tiếng Trung', 'Cơ bản'),
('Kết cấu cú pháp câu', 'Cấu trúc câu và thành phần câu', 'Cơ bản'),
('Thành phần ngữ pháp Hán Ngữ', 'Các thành phần ngữ pháp chính', 'Trung bình'),
('Các kiểu câu trong tiếng trung', 'Phân loại các loại câu', 'Trung bình'),
('Cấu trúc ngữ pháp sơ cấp', 'Ngữ pháp cơ bản cho người mới bắt đầu', 'Cơ bản'),
('Mẫu cấu trúc ngữ pháp Hán ngữ cố định', 'Các mẫu câu cố định thường dùng', 'Trung bình'),
('Lượng từ trong tiếng Trung', 'Cách sử dụng các lượng từ', 'Trung bình'),
('Sách ngữ pháp tiếng Trung cơ bản', 'Tài liệu học ngữ pháp cơ bản', 'Cơ bản');

-- Seed data for vocabulary (sample data)
INSERT INTO vocabulary (hanzi, pinyin, nghia, vi_du, bien_the, hsk_level) VALUES
('你好', 'nǐ hǎo', 'Xin chào', '你好，我是小明。', 'GIAN', 1),
('您', 'nín', 'Ông/Bà (kính ngữ)', '您好！', 'GIAN', 1),
('我', 'wǒ', 'Tôi', '我是学生。', 'GIAN', 1),
('你', 'nǐ', 'Bạn', '你好吗？', 'GIAN', 1),
('他', 'tā', 'Anh ấy', '他是老师。', 'GIAN', 1),
('她', 'tā', 'Cô ấy', '她是我朋友。', 'GIAN', 1),
('我们', 'wǒmen', 'Chúng tôi', '我们是学生。', 'GIAN', 1),
('你们', 'nǐmen', 'Các bạn', '你们好！', 'GIAN', 1),
('他们', 'tāmen', 'Họ', '他们是老师。', 'GIAN', 1),
('的', 'de', 'Sở hữu, liên kết', '我的书', 'GIAN', 1),
('是', 'shì', 'Là', '我是学生。', 'GIAN', 1),
('在', 'zài', 'Ở', '我在家。', 'GIAN', 1),
('有', 'yǒu', 'Có', '我有书。', 'GIAN', 1),
('了', 'le', 'Đã', '我吃了饭。', 'GIAN', 1),
('不', 'bù', 'Không', '不是。', 'GIAN', 1),
('很', 'hěn', 'Rất', '很好。', 'GIAN', 1),
('吗', 'ma', 'Câu hỏi', '你好吗？', 'GIAN', 1),
('呢', 'ne', 'Thì, nhỉ', '你呢？', 'GIAN', 1),
('也', 'yě', 'Cũng', '我也是。', 'GIAN', 1),
('都', 'dōu', 'Đều', '我们都好。', 'GIAN', 1);

-- Seed some common radicals
INSERT INTO radicals (hanzi, strokes, meaning, pronunciation, frequency_rank) VALUES
('口', 3, 'Mouth', 'kǒu', 1),
('亻', 2, 'Person', 'rén', 2),
('木', 4, 'Tree', 'mù', 3),
('水', 4, 'Water', 'shuǐ', 4),
('火', 4, 'Fire', 'huǒ', 5),
('女', 3, 'Woman', 'nǚ', 6),
('子', 3, 'Child', 'zǐ', 7),
('心', 4, 'Heart', 'xīn', 8),
('日', 4, 'Sun/Day', 'rì', 9),
('月', 4, 'Moon/Month', 'yuè', 10),
('人', 2, 'Person', 'rén', 11),
('手', 4, 'Hand', 'shǒu', 12),
('目', 5, 'Eye', 'mù', 13),
('耳', 6, 'Ear', 'ěr', 14),
('言', 7, 'Speech', 'yán', 15);

-- Add some vocabulary tags
INSERT INTO vocab_tags (vocab_id, tag) VALUES
(1, 'greeting'),
(1, 'common'),
(2, 'polite'),
(3, 'pronoun'),
(4, 'pronoun'),
(5, 'pronoun'),
(10, 'particle'),
(10, 'common'),
(11, 'verb'),
(11, 'common');


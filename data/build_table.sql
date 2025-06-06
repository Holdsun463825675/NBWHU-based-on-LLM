-- 用户表
CREATE TABLE user (
    id VARCHAR(256) PRIMARY KEY,
    email VARCHAR(256),
    telephone VARCHAR(256),
    username VARCHAR(256) NOT NULL UNIQUE,
    password VARCHAR(256) NOT NULL,
    identity ENUM('未选择', '学生', '职场人士', '儿童') DEFAULT '未选择',
    word_book VARCHAR(255)
);

-- 单词
CREATE TABLE word (
    id VARCHAR(256) PRIMARY KEY,
    word VARCHAR(256) NOT NULL,
    phonetic VARCHAR(256),
    difficulty_level ENUM('L1', 'L2', 'L3', 'L4'),
    tag_1 VARCHAR(256),
    tag_2 VARCHAR(256),
    tag_3 VARCHAR(256),
    chinese_definition_1 VARCHAR(256),
    chinese_definition_2 VARCHAR(256),
    chinese_definition_3 VARCHAR(256),
    chinese_definition_4 VARCHAR(256),
    chinese_definition_5 VARCHAR(256),
    example_sentence_1 VARCHAR(256),
    example_sentence_2 VARCHAR(256),
    example_sentence_3 VARCHAR(256),
    example_sentence_4 VARCHAR(256),
    example_sentence_5 VARCHAR(256),
    synonym_1 VARCHAR(256),
    synonym_2 VARCHAR(256),
    synonym_3 VARCHAR(256),
    confusion_1 VARCHAR(256),
    confusion_2 VARCHAR(256),
    confusion_3 VARCHAR(256)
);

-- 单词书
CREATE TABLE word_book (
    id VARCHAR(256) PRIMARY KEY,
    tag VARCHAR(256),
    name VARCHAR(256),
    word_count INT DEFAULT 0
);

-- 单词分配
CREATE TABLE word_alllocation (
    id VARCHAR(256) PRIMARY KEY,
    word_id VARCHAR(256),
    word_book_id VARCHAR(256),
    FOREIGN KEY (word_id) REFERENCES word(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (word_book_id) REFERENCES word_book(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- 近义词与易混淆词表
CREATE TABLE synonym_and_confusion (
    id VARCHAR(256) PRIMARY KEY,
    word VARCHAR(256) NOT NULL,
    chinese_definition_1 VARCHAR(256),
    chinese_definition_2 VARCHAR(256),
    chinese_definition_3 VARCHAR(256),
    chinese_definition_4 VARCHAR(256),
    chinese_definition_5 VARCHAR(256)
);

-- 每日学习记录
CREATE TABLE daily_study_record (
    id VARCHAR(256) PRIMARY KEY,
    user_id VARCHAR(256) NOT NULL,
    plan_number INT NOT NULL,
    learned_number INT DEFAULT 0,
    date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 每日单词状态
CREATE TABLE daily_word_statu (
    id VARCHAR(256) PRIMARY KEY,
    user_id VARCHAR(256) NOT NULL,
    word_id VARCHAR(256) NOT NULL,
    status TINYINT NOT NULL DEFAULT 3,
    prompt_times INT DEFAULT 0,
    date DATE NOT NULL,
    skip_count INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (word_id) REFERENCES word(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 用户单词状态
CREATE TABLE user_word_statu (
    id VARCHAR(256) PRIMARY KEY,
    user_id VARCHAR(256) NOT NULL,
    word_id VARCHAR(256) NOT NULL,
    status ENUM('已掌握', '正在学习') DEFAULT '正在学习',
    important_index INT DEFAULT 0,
    FOREIGN KEY (user_id) REFERENCES user(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (word_id) REFERENCES word(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 用户测试记录
CREATE TABLE user_test_result (
    id VARCHAR(256) PRIMARY KEY,
    user_id VARCHAR(256) NOT NULL,
    test_type ENUM('看英选中', '看中选英', '拼写', '释义判断') NOT NULL,
    score INT NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 单词测试状态
CREATE TABLE word_test_statu (
    id VARCHAR(256) PRIMARY KEY,
    user_id VARCHAR(256) NOT NULL,
    word_id VARCHAR(256) NOT NULL,
    test_type ENUM('看英选中', '看中选英', '拼写', '释义判断') NOT NULL,
    status ENUM('正确', '错误') NOT NULL,
    date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
        ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (word_id) REFERENCES word(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 打卡记录
CREATE TABLE attendance_record (
    id VARCHAR(256) PRIMARY KEY,
    user_id VARCHAR(256) NOT NULL,
    status ENUM('打卡', '未打卡') DEFAULT '未打卡',
    date DATE NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 对话表
CREATE TABLE dialogue (
    id VARCHAR(256) PRIMARY KEY,
    user_id VARCHAR(256) NOT NULL,
    name VARCHAR(256),
    theme VARCHAR(256),
    build_time DATETIME DEFAULT CURRENT_TIMESTAMP,
    update_time DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES user(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- 对话记录表
CREATE TABLE dialogue_record (
    id VARCHAR(256) PRIMARY KEY,
    dialogue_id VARCHAR(256) NOT NULL,
    content TEXT NOT NULL,
    timestamp DATETIME DEFAULT CURRENT_TIMESTAMP,
    role ENUM('user', 'LLM') NOT NULL,
    FOREIGN KEY (dialogue_id) REFERENCES dialogue(id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE verification_code (
    email VARCHAR(255),
    code VARCHAR(10) 
);

CREATE TABLE user_word_record (
    id VARCHAR(256) PRIMARY KEY,
    user_id VARCHAR(256) NOT NULL,
    word_id VARCHAR(256) NOT NULL
);
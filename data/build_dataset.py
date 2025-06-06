from datetime import date
import random
import pandas as pd
import mysql.connector
import uuid

# === Excel 文件路径与读取 ===
file_path = r"数据集\dataset.xlsx"
df1 = pd.read_excel(file_path, sheet_name="Sheet1", skiprows=1)
df2 = pd.read_excel(file_path, sheet_name="Sheet2", skiprows=1)

# 数据库连接配置
conn = mysql.connector.connect(
    host="localhost",
    user="root",
    password="your_password",
    database="your_dbname",
    charset="utf8mb4"
)
cursor = conn.cursor()

def gen_id():
    return str(uuid.uuid4())

# === 插入单词书 ===
book_tags = ["L1", "L2", "L3", "L4", "学前", "小学", "中考", "高考", "四级", "六级", "雅思", "托福"]
book_ids = {}
for tag in book_tags:
    book_id = gen_id()
    book_ids[tag] = book_id
    cursor.execute("INSERT INTO word_book (id, tag, name, word_count) VALUES (%s, %s, %s, %s)",
                   (book_id, tag, f"词书-{tag}", 0))

# === 插入单词与分配 ===
for _, row in df1.iterrows():
    word_id = gen_id()

    word = str(row["单词"]).strip() if pd.notna(row["单词"]) else None
    phonetic = str(row["音标"]).strip() if pd.notna(row["音标"]) else None
    level = str(row["难度"]).strip() if pd.notna(row["难度"]) else None

    tags = [str(row.get(col)).strip() for col in ["标签1", "标签2", "标签3"] if pd.notna(row.get(col))]

    defs = [str(row.get(col)).strip() if pd.notna(row.get(col)) else None
            for col in ["中文释义1", "中文释义2", "中文释义3", "中文释义4", "中文释义5"]]
    exs = [str(row.get(col)).strip() if pd.notna(row.get(col)) else None
           for col in ["例句1", "例句2", "例句3", "例句4", "例句5"]]
    syns = [str(row.get(col)).strip() if pd.notna(row.get(col)) else None
            for col in ["近义词1", "近义词2", "近义词3"]]
    confs = [str(row.get(col)).strip() if pd.notna(row.get(col)) else None
             for col in ["易混淆词1", "易混淆词2", "易混淆词3"]]

    cursor.execute("""
        INSERT INTO word (
            id, word, phonetic, difficulty_level,
            tag_1, tag_2, tag_3,
            chinese_definition_1, chinese_definition_2, chinese_definition_3, chinese_definition_4, chinese_definition_5,
            example_sentence_1, example_sentence_2, example_sentence_3, example_sentence_4, example_sentence_5,
            synonym_1, synonym_2, synonym_3,
            confusion_1, confusion_2, confusion_3
        ) VALUES (%s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s, %s)
    """, (
        word_id, word, phonetic, level,
        *(tags + [None] * (3 - len(tags))),
        *(defs + [None] * (5 - len(defs))),
        *(exs + [None] * (5 - len(exs))),
        *(syns + [None] * (3 - len(syns))),
        *(confs + [None] * (3 - len(confs))),
    ))

    allocation_tags = set(tags + [level] if level else tags)
    for tag in allocation_tags:
        if tag in book_ids:
            alloc_id = gen_id()
            cursor.execute(
                "INSERT INTO word_alllocation (id, word_id, word_book_id) VALUES (%s, %s, %s)",
                (alloc_id, word_id, book_ids[tag])
            )


# === 插入 synonym_and_confusion 表 ===
for _, row in df2.iterrows():
    word = str(row[1]).strip() if pd.notna(row[1]) else None
    definitions = [str(row[i]).strip() if pd.notna(row[i]) else None for i in range(2, 7)]
    entry_id = gen_id()
    cursor.execute("""
        INSERT INTO synonym_and_confusion (
            id, word,
            chinese_definition_1, chinese_definition_2, chinese_definition_3,
            chinese_definition_4, chinese_definition_5
        ) VALUES (%s, %s, %s, %s, %s, %s, %s)
    """, (entry_id, word, *definitions))


# 更新 word_count
for tag, book_id in book_ids.items():
    cursor.execute("SELECT COUNT(*) FROM word_alllocation WHERE word_book_id = %s", (book_id,))
    count = cursor.fetchone()[0]
    cursor.execute("UPDATE word_book SET word_count = %s WHERE id = %s", (count, book_id))

# 提交更改
conn.commit()

print("dataset 已存入数据库！")

# 重新获取所有 word_id
cursor = conn.cursor()
cursor.execute("SELECT id FROM word")
all_word_ids = [r[0] for r in cursor.fetchall()]

# 插入用户
user_ids = []
for i in range(100):
    user_id = gen_id()
    user_ids.append(user_id)

    username = f"user{i}"
    password = "pass123"
    email = f"{username}@example.com"
    telephone = f"1380000{str(i).zfill(4)}"
    identity = random.choice(["学生", "职场人士", "儿童"])
    word_book = random.choice(["L1", "L2", "L3", "L4", "学前", "小学", "中考", "高考", "四级", "六级", "雅思", "托福"])

    cursor.execute(
        "INSERT INTO user (id, email, telephone, username, password, identity, word_book) VALUES (%s, %s, %s, %s, %s, %s, %s)",
        (user_id, email, telephone, username, password, identity, word_book)
    )

# 每个用户插入学习记录、单词状态、测试记录、对话等
today = date.today()

for user_id in user_ids:
    # daily_study_records
    record_id = gen_id()
    cursor.execute("""
        INSERT INTO daily_study_record (id, user_id, plan_number, learned_number, date)
        VALUES (%s, %s, %s, %s, %s)
    """, (record_id, user_id, 20, random.randint(10, 20), today))

    # daily_word_status
    for _ in range(5):
        wid = random.choice(all_word_ids)
        dw_id = gen_id()
        cursor.execute("""
            INSERT INTO daily_word_statu (id, user_id, word_id, status, prompt_times, date, skip_count)
            VALUES (%s, %s, %s, %s, %s, %s, %s)
        """, (dw_id, user_id, wid, random.randint(0, 3), random.randint(0, 5), today, 0))

    # user_word_status
    for _ in range(10):
        wid = random.choice(all_word_ids)
        uw_id = gen_id()
        cursor.execute("""
            INSERT INTO user_word_statu (id, user_id, word_id, status, important_index)
            VALUES (%s, %s, %s, %s, %s)
        """, (uw_id, user_id, wid, random.choice(["已掌握", "正在学习"]), random.randint(0, 10)))

    # user_test_results
    test_id = gen_id()
    cursor.execute("""
        INSERT INTO user_test_result (id, user_id, test_type, score, date)
        VALUES (%s, %s, %s, %s, %s)
    """, (test_id, user_id, random.choice(["看英选中", "看中选英", "拼写", "释义判断"]), random.randint(60, 100), today))

    # word_test_status
    for _ in range(5):
        wid = random.choice(all_word_ids)
        wts_id = gen_id()
        cursor.execute("""
            INSERT INTO word_test_statu (id, user_id, word_id, test_type, status, date)
            VALUES (%s, %s, %s, %s, %s, %s)
        """, (wts_id, user_id, wid,
              random.choice(["看英选中", "看中选英", "拼写", "释义判断"]),
              random.choice(["正确", "错误"]), today))

    # attendance_records
    attend_id = gen_id()
    cursor.execute("""
        INSERT INTO attendance_record (id, user_id, status, date)
        VALUES (%s, %s, %s, %s)
    """, (attend_id, user_id, random.choice(["打卡", "未打卡"]), today))

    # dialogues + dialogue_records
    dlg_id = gen_id()
    cursor.execute("""
        INSERT INTO dialogue (id, user_id, name, theme, build_time, update_time)
        VALUES (%s, %s, %s, %s, NOW(), NOW())
    """, (dlg_id, user_id, "Daily Chat", "Vocabulary"))

    for role in ["user", "LLM"]:
        dr_id = gen_id()
        cursor.execute("""
            INSERT INTO dialogue_record (id, dialogue_id, content, timestamp, role)
            VALUES (%s, %s, %s, NOW(), %s)
        """, (dr_id, dlg_id, f"Example {role} message", role))

# 提交
conn.commit()
cursor.close()
conn.close()
print("剩余模拟数据已插入完毕。")


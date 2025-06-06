package whu.english_learn.service.impl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whu.english_learn.dto.WordDTO;
import whu.english_learn.entity.*;
import whu.english_learn.mapper.*;
import whu.english_learn.service.WordStudyService;

import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@Service
public class WordStudyServiceImpl implements WordStudyService {
    @Autowired
    private WordMapper wordMapper;
    @Autowired
    private DailyStudyRecordMapper dailyStudyRecordMapper;
    @Autowired
    private UserMapper  userMapper;
    @Autowired
    private DailyWordStatusMapper  dailyWordStatusMapper;
    @Autowired
    private UserWordStatusMapper  userWordStatusMapper;

    @Override
    public List<WordDTO> getStudyWords(String username) {
        //获取用户相关信息
        User user = userMapper.getUserByUsername(username);
        //获取用户选择词书
        String wordBook = user.getWordBook();
        //获取相关单词书的全部单词
        List<Word> words = wordMapper.getWordsByTags(wordBook);
        List<WordDTO> wordDTOs = words.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        //获取pLannedCount的值
        int plannedCount = dailyStudyRecordMapper.getplanNumberByUserId(user.getId());
        //获取pLannedCount个单词
        List<WordDTO> copy = new ArrayList<>(wordDTOs); // 创建副本避免修改原集合
        Collections.shuffle(copy); // O(n)时间复杂度
        List<WordDTO> result = copy.subList(0, plannedCount); // 取前plannedCount个

        //将单词列表插入到daily_word_statu表中
        for (WordDTO wordDTO : result) {
            DailyWordStatus dailyWordStatus = new DailyWordStatus();
            dailyWordStatus.setId(UUID.randomUUID().toString());  // 为每个单词生成唯一 ID
            dailyWordStatus.setUserId(user.getId());  // 设置用户 ID
            dailyWordStatus.setWordId(wordDTO.getId());  // 设置单词 ID
            dailyWordStatus.setStatus(3);  // 设置默认状态为 3
            dailyWordStatus.setPromptTimes(0);  // 设置默认提示次数为 0
            dailyWordStatus.setDate(java.sql.Date.valueOf(LocalDate.now()));  // 设置日期为当前日期

            dailyWordStatusMapper.insert(dailyWordStatus);  // 插入到 daily_word_statu 表中
        }

        //将单词列表插入到user_word_statu表中
        for (WordDTO wordDTO : result) {
            // 新增用户单词状态记录（user_word_status）
            UserWordStatus userWordStatus = new UserWordStatus();
            userWordStatus.setId(UUID.randomUUID().toString()); //生成唯一ID
            userWordStatus.setUserId(user.getId());  // 设置用户ID
            userWordStatus.setWordId(wordDTO.getId());  // 设置单词ID
            userWordStatus.setStatus(UserWordStatus.Status.正在学习);
            userWordStatus.setImportantIndex(0);  // 默认重要指数

            // 检查是否已存在记录
            if (userWordStatusMapper.exists(user.getId(), wordDTO.getId()) == null) {
                userWordStatusMapper.insert(userWordStatus);
            } else {
                userWordStatusMapper.updateStatus(
                        user.getId(),
                        wordDTO.getId(),
                        UserWordStatus.Status.正在学习
                );
            }
        }
        return result;
    }


    @Override
    public List<WordDTO> getStudyWordsFirst(String wordBook, int plannedCount, String username) {
        //获取相关单词书的全部单词
        List<Word> words = wordMapper.getWordsByTags(wordBook);
        List<WordDTO> wordDTOs = words.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        //获取pLannedCount个单词
        List<WordDTO> copy = new ArrayList<>(wordDTOs); // 创建副本避免修改原集合
        Collections.shuffle(copy); // O(n)时间复杂度
        List<WordDTO> result = copy.subList(0, plannedCount); // 取前50个

        //将wordBook存到user表中
        userMapper.addWordBook(username, wordBook);

        //将plannedCount插入到daily_study_record表中
        //首先获取当前用户信息
        User user = userMapper.getUserByUsername(username);

        //设置插入daily_study_record表的相关信息
        DailyStudyRecord dailyStudyRecord = new DailyStudyRecord();
        dailyStudyRecord.setId(UUID.randomUUID().toString());  // 设置daily_study_record的ID
        dailyStudyRecord.setUser(user);
        dailyStudyRecord.setPlanNumber(plannedCount);
        dailyStudyRecord.setDate(LocalDate.now());  // 设置日期为当前日期
        dailyStudyRecordMapper.insert(dailyStudyRecord);

        //将单词列表插入到daily_word_statu表中
        for (WordDTO wordDTO : result) {
            DailyWordStatus dailyWordStatus = new DailyWordStatus();
            dailyWordStatus.setId(UUID.randomUUID().toString());  // 为每个单词生成唯一 ID
            dailyWordStatus.setUserId(user.getId());  // 设置用户 ID
            dailyWordStatus.setWordId(wordDTO.getId());  // 设置单词 ID
            dailyWordStatus.setStatus(3);  // 设置默认状态为 3
            dailyWordStatus.setPromptTimes(0);  // 设置默认提示次数为 0
            dailyWordStatus.setDate(java.sql.Date.valueOf(LocalDate.now()));  // 设置日期为当前日期

            dailyWordStatusMapper.insert(dailyWordStatus);  // 插入到 daily_word_statu 表中
        }

        //将单词列表插入到user_word_statu表中
        for (WordDTO wordDTO : result) {
            // 新增用户单词状态记录（user_word_status）
            UserWordStatus userWordStatus = new UserWordStatus();
            userWordStatus.setId(UUID.randomUUID().toString()); //生成唯一ID
            userWordStatus.setUserId(user.getId());  // 设置用户ID
            userWordStatus.setWordId(wordDTO.getId());  // 设置单词ID
            userWordStatus.setStatus(UserWordStatus.Status.正在学习);
            userWordStatus.setImportantIndex(0);  // 默认重要指数

            // 检查是否已存在记录
            if (userWordStatusMapper.exists(user.getId(), wordDTO.getId()) == null) {
                userWordStatusMapper.insert(userWordStatus);
            } else {
                userWordStatusMapper.updateStatus(
                        user.getId(),
                        wordDTO.getId(),
                        UserWordStatus.Status.正在学习
                );
            }
        }

        return result;
    }

    @Override
    public void knownWord(String username, String word, Date date) {
        //更新相关单词Status为Status-1
        dailyWordStatusMapper.updateKnownStatus(username, word, date);
    }

    @Override
    public void unKnownWord(String username, String word, Date date) {
        // 更新时设置跳过次数为5
        //更新相关单词Status为3，且promptTimes加1
        dailyWordStatusMapper.updateUnknownStatus(username, word, date);
    }

    @Override
    public void tooSimpleWord(String username, String word, Date date) {
        //更新相关单词Status为0
        dailyWordStatusMapper.updateEasyStatus(username, word, date);
        //将用户单词状态设置为已掌握
        userWordStatusMapper.updateWordStatus(username, word, UserWordStatus.Status.已掌握);
    }

    @Override
    public WordDTO getNextWord(String username, String word, Date date) {
        // 1. 获取用户信息
        User user = userMapper.getUserByUsername(username);

        // 2. 获取今日学习记录
        DailyStudyRecord record = dailyStudyRecordMapper.getTodayRecord(user.getId(), date);
        if (record == null) {
            throw new IllegalStateException("未找到今日学习计划");
        }
        // 3. 检查当前单词状态
        String wordId = wordMapper.getWordIdByWord(word);
        DailyWordStatus currentStatus = dailyWordStatusMapper.getCurrentStatus(
                user.getId(),
                wordId,
                new java.sql.Date(date.getTime()) // 转换为SQL日期
        );

        // 新增逻辑：当status=0时增加已学数量
        if (currentStatus != null && currentStatus.getStatus() <= 0) {
            dailyStudyRecordMapper.incrementLearnedNumber(
                    record.getId(),
                    record.getLearnedNumber() + 1
            );
            record.setLearnedNumber(record.getLearnedNumber() + 1); // 更新本地对象状态
        }

        // 4.获取今日单词学习列表
        List<DailyWordStatus> candidates = dailyWordStatusMapper.getLearningWords(
                user.getId(),
                date,
                record.getPlanNumber(),
                wordId
        );


        // 5. 智能推荐算法
        Optional<DailyWordStatus> nextWord = candidates.stream()
                .filter(w -> !w.getWordId().equals(wordId))
                // 新增过滤条件：只选择skipCount=0的单词
                .filter(w -> w.getSkipCount() == 0)
                .sorted(Comparator
                        .comparingDouble((DailyWordStatus w) ->
                                Math.sqrt(w.getPromptTimes()) + w.getStatus() * 0.5
                        ).reversed()
                        .thenComparingDouble(w -> ThreadLocalRandom.current().nextDouble() * 2)
                )
                .findFirst();

        if (!nextWord.isEmpty()) {
            // 递减所有单词的跳过次数
            dailyWordStatusMapper.decrementSkipCounts(user.getId(), date);
        }
        // 6. 判断是否完成学习
        if (nextWord.isEmpty()) {
            // 更新学习完成数
            record.setLearnedNumber(record.getPlanNumber());
            dailyStudyRecordMapper.updateLearnedNumber(record);
            return null;
        }

        // 7. 获取单词详情
        Word wordEntity = wordMapper.getWordById(nextWord.get().getWordId());
        return convertToDTO(wordEntity);
    }

    @Override
    public void changeBook(String username, String wordBook,Date date) {
        //更新用户选书信息
        userMapper.addWordBook(username, wordBook);
        //删除原有记录
        User user = userMapper.getUserByUsername(username);
        dailyWordStatusMapper.deleteByUserId(user.getId(),date);
    }

    //方法函数 作用为将单词转换为DTO形式
    private WordDTO convertToDTO(Word word) {
        WordDTO dto = new WordDTO();
        dto.setId(word.getId());
        dto.setWord(word.getWord());
        dto.setPhonetic(word.getPhonetic());
        dto.setDifficultyLevel(word.getDifficultyLevel());

        // 转换标签
        List<String> tags = new ArrayList<>();
        if (word.getTag1() != null) tags.add(word.getTag1());
        if (word.getTag2() != null) tags.add(word.getTag2());
        if (word.getTag3() != null) tags.add(word.getTag3());
        dto.setTags(tags);

        // 转换中文释义
        List<String> definitions = Arrays.asList(
                        word.getChineseDefinition1(),
                        word.getChineseDefinition2(),
                        word.getChineseDefinition3(),
                        word.getChineseDefinition4(),
                        word.getChineseDefinition5()
                ).stream()
                .filter(def -> def != null && !def.trim().isEmpty())
                .collect(Collectors.toList());
        dto.setChineseDefinitions(definitions);

        // 转换例句
        List<String> examples = Arrays.asList(
                        word.getExampleSentence1(),
                        word.getExampleSentence2(),
                        word.getExampleSentence3(),
                        word.getExampleSentence4(),
                        word.getExampleSentence5()
                ).stream()
                .filter(ex -> ex != null && !ex.trim().isEmpty())
                .collect(Collectors.toList());
        dto.setExampleSentences(examples);

        // 转换近义词
        List<String> synonyms = new ArrayList<>();
        if (word.getSynonym1() != null) synonyms.add(word.getSynonym1());
        if (word.getSynonym2() != null) synonyms.add(word.getSynonym2());
        if (word.getSynonym3() != null) synonyms.add(word.getSynonym3());
        dto.setSynonyms(synonyms);

        // 转换易混淆词
        List<String> confusions = new ArrayList<>();
        if (word.getConfusion1() != null) confusions.add(word.getConfusion1());
        if (word.getConfusion2() != null) confusions.add(word.getConfusion2());
        if (word.getConfusion3() != null) confusions.add(word.getConfusion3());
        dto.setConfusions(confusions);

        return dto;
    }
}

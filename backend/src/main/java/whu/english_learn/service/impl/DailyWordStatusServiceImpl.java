package whu.english_learn.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import whu.english_learn.entity.DailyWordStatus;
import whu.english_learn.mapper.DailyWordStatusMapper;
import whu.english_learn.service.DailyWordStatusService;

import java.util.Date;
import java.util.List;

@Service
public class DailyWordStatusServiceImpl implements DailyWordStatusService {

    @Autowired
    private DailyWordStatusMapper dailyWordStatusMapper;

    @Override
    public List<DailyWordStatus> getDailyWordsByUserIdAndDate(String userId, Date date) {
        return dailyWordStatusMapper.getDailyWordsByUserIdAndDate(userId, date);
    }
} 
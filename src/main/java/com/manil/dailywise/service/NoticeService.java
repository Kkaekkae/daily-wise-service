package com.manil.dailywise.service;

import com.manil.dailywise.exception.KkeaException;
import com.manil.dailywise.repository.NoticeRepository;
import com.manil.dailywise.entity.Notice;
import com.manil.dailywise.dto.common.RCode;
import com.manil.dailywise.dto.notice.NoticeRespDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class NoticeService {
    private NoticeRepository noticeRepository;

    @Autowired
    public void setNoticeRepository(NoticeRepository noticeRepository) {
        this.noticeRepository = noticeRepository;
    }

    public NoticeRespDTO getNotice() throws KkeaException {
        try{
            long currentTimeMill = System.currentTimeMillis();
            Notice notice = noticeRepository.findFirstByEndAtGreaterThanEqualAndStartAtLessThanEqualOrderByCreatedAtDesc(
                    currentTimeMill,
                    currentTimeMill
            );
            return NoticeRespDTO.from(notice);
        } catch (KkeaException e) {
            throw e;
        } catch (Exception e) {
            throw new KkeaException(RCode.INTERNAL_DATABASE_ERROR, e);
        }
    }
}

package org.sopt.artoo.api;

import lombok.extern.slf4j.Slf4j;
import org.sopt.artoo.service.NoticeService;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
public class NoticeController {
    private NoticeService noticeService;

}

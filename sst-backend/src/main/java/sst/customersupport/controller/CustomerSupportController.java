package sst.customersupport.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import lombok.RequiredArgsConstructor;
import sst.customersupport.domain.CustomerSupport;
import sst.customersupport.service.CustomerSupportService;

@RestController
@RequiredArgsConstructor
public class CustomerSupportController {

    private final CustomerSupportService customerSupportService;

    // 공지사항 조회
    @GetMapping("/api/customersupport/notice")
    public List<CustomerSupport> noticeList() {
        return customerSupportService.getNoticeList();
    }

    // FAQ 조회
    @GetMapping("/api/customersupport/faq")
    public List<CustomerSupport> faqList() {
        return customerSupportService.getFaqList();
    }
}
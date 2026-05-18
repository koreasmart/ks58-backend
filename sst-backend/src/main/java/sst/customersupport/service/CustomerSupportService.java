package sst.customersupport.service;

import java.util.List;
import sst.customersupport.domain.CustomerSupport;

public interface CustomerSupportService {

    // 공지사항 조회
    List<CustomerSupport> getNoticeList();

    // FAQ 조회
    List<CustomerSupport> getFaqList();
}
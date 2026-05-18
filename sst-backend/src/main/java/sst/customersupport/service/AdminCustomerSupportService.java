package sst.customersupport.service;

import sst.customersupport.domain.CustomerSupport;

public interface AdminCustomerSupportService {

    // 공지사항 작성
    int createNotice(CustomerSupport customerSupport);

    // 공지사항 수정
    int modifyNotice(CustomerSupport customerSupport);

    // 공지사항 삭제
    int removeNotice(Long csNo);

    // FAQ 작성
    int createFaq(CustomerSupport customerSupport);

    // FAQ 수정
    int modifyFaq(CustomerSupport customerSupport);

    // FAQ 삭제
    int removeFaq(Long csNo);
}
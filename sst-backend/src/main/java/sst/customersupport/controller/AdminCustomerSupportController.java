package sst.customersupport.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import sst.customersupport.domain.CustomerSupport;
import sst.customersupport.service.AdminCustomerSupportService;


@PreAuthorize("hasRole('ADMIN')")
@RestController
@RequiredArgsConstructor
public class AdminCustomerSupportController {

    private final AdminCustomerSupportService adminCustomerSupportService;

    // 공지사항 작성
    @PostMapping("/api/admin/customersupport/notice")
    public int createNotice(@RequestBody CustomerSupport customerSupport) {
        return adminCustomerSupportService.createNotice(customerSupport);
    }

    // 공지사항 수정
    @PutMapping("/api/admin/customersupport/notice/{csNo}")
    public int modifyNotice(
            @PathVariable("csNo") Long csNo,
            @RequestBody CustomerSupport customerSupport) {

        customerSupport.setCsNo(csNo);
        return adminCustomerSupportService.modifyNotice(customerSupport);
    }

    // 공지사항 삭제
    @DeleteMapping("/api/admin/customersupport/notice/{csNo}")
    public int removeNotice(@PathVariable("csNo") Long csNo) {
        return adminCustomerSupportService.removeNotice(csNo);
    }

    // FAQ 작성
    @PostMapping("/api/admin/customersupport/faq")
    public int createFaq(@RequestBody CustomerSupport customerSupport) {
        return adminCustomerSupportService.createFaq(customerSupport);
    }

    // FAQ 수정
    @PutMapping("/api/admin/customersupport/faq/{csNo}")
    public int modifyFaq(
            @PathVariable("csNo") Long csNo,
            @RequestBody CustomerSupport customerSupport) {

        customerSupport.setCsNo(csNo);
        return adminCustomerSupportService.modifyFaq(customerSupport);
    }

    // FAQ 삭제
    @DeleteMapping("/api/admin/customersupport/faq/{csNo}")
    public int removeFaq(@PathVariable("csNo") Long csNo) {
        return adminCustomerSupportService.removeFaq(csNo);
    }
}
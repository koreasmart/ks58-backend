package sst.customersupport.service;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sst.customersupport.domain.CustomerSupport;
import sst.customersupport.mapper.CustomerSupportMapper;

@Service
@RequiredArgsConstructor
public class AdminCustomerSupportServiceImpl implements AdminCustomerSupportService {

    private final CustomerSupportMapper customerSupportMapper;

    @Override
    public int createNotice(CustomerSupport customerSupport) {
        return customerSupportMapper.insertNotice(customerSupport);
    }

    @Override
    public int modifyNotice(CustomerSupport customerSupport) {
        return customerSupportMapper.updateNotice(customerSupport);
    }

    @Override
    public int removeNotice(Long csNo) {
        return customerSupportMapper.deleteNotice(csNo);
    }

    @Override
    public int createFaq(CustomerSupport customerSupport) {
        return customerSupportMapper.insertFaq(customerSupport);
    }

    @Override
    public int modifyFaq(CustomerSupport customerSupport) {
        return customerSupportMapper.updateFaq(customerSupport);
    }

    @Override
    public int removeFaq(Long csNo) {
        return customerSupportMapper.deleteFaq(csNo);
    }
}
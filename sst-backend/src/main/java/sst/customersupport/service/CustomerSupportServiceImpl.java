package sst.customersupport.service;

import java.util.List;

import org.springframework.stereotype.Service;

import lombok.RequiredArgsConstructor;
import sst.customersupport.domain.CustomerSupport;
import sst.customersupport.mapper.CustomerSupportMapper;

@Service
@RequiredArgsConstructor
public class CustomerSupportServiceImpl implements CustomerSupportService {

    private final CustomerSupportMapper customerSupportMapper;

    @Override
    public List<CustomerSupport> getNoticeList() {
        return customerSupportMapper.selectNoticeList();
    }

    @Override
    public List<CustomerSupport> getFaqList() {
        return customerSupportMapper.selectFaqList();
    }
}
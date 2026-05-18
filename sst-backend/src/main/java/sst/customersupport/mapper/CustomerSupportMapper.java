package sst.customersupport.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import sst.customersupport.domain.CustomerSupport;

@Mapper
public interface CustomerSupportMapper {

    // 공지사항 조회
    List<CustomerSupport> selectNoticeList();

    // FAQ 조회
    List<CustomerSupport> selectFaqList();

    // 공지사항 작성
    int insertNotice(CustomerSupport customerSupport);

    // 공지사항 수정
    int updateNotice(CustomerSupport customerSupport);

    // 공지사항 삭제
    int deleteNotice(Long csNo);
    
    // 자주묻는 질문 작성
    int insertFaq(CustomerSupport customerSupport);

    // 자주묻는 질문 수정
    int updateFaq(CustomerSupport customerSupport);

    // 자주묻는 질문 삭제
    int deleteFaq(Long csNo);
}
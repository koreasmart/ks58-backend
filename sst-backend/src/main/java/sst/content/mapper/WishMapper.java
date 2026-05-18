package sst.content.mapper;

import org.apache.ibatis.annotations.Mapper;
import sst.content.dto.WishDto;
import sst.content.dto.WishResponseDto;
import java.util.List;

@Mapper
public interface WishMapper {

    /** 찜 추가 */
    int insertWish(WishDto dto);

    /** 찜 삭제 */
    int deleteWish(WishDto dto);

    /** 찜 여부 확인 (0: 미찜, 1: 찜) */
    int checkWish(WishDto dto);

    /** 내 찜 목록 조회 */
    List<WishResponseDto> selectMyWishlist(Long mbrId);
}
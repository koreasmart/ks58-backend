package sst.content.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sst.content.dto.WishDto;
import sst.content.dto.WishResponseDto;
import sst.content.mapper.WishMapper;
import java.util.List;

@Service
@RequiredArgsConstructor
public class WishService {

    private final WishMapper wishMapper;

    // ─────────────────────────────────────────
    // 찜 토글 (찜이면 삭제, 아니면 추가)
    // ─────────────────────────────────────────
    @Transactional
    public boolean toggleWish(WishDto dto) {
        int count = wishMapper.checkWish(dto);
        if (count > 0) {
            wishMapper.deleteWish(dto);
            return false; // 찜 해제
        } else {
            wishMapper.insertWish(dto);
            return true;  // 찜 추가
        }
    }

    // ─────────────────────────────────────────
    // 찜 여부 확인
    // ─────────────────────────────────────────
    @Transactional(readOnly = true)
    public boolean checkWish(WishDto dto) {
        return wishMapper.checkWish(dto) > 0;
    }

    // ─────────────────────────────────────────
    // 내 찜 목록 조회
    // ─────────────────────────────────────────
    @Transactional(readOnly = true)
    public List<WishResponseDto> getMyWishlist(Long mbrId) {
        return wishMapper.selectMyWishlist(mbrId);
    }
}
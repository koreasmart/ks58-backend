package sst.global.dto;

import java.util.List;
import lombok.Getter;

/**
 * 🚀 공통 페이징 응답 DTO (제네릭 적용)
 * 어떤 데이터(Member, Post, Review)든 이 객체 하나로 감싸서 프론트로 보냅니다.
 */
@Getter
public class PageResponse<T> {
    private List<T> list;        // 실제 데이터 목록
    private int totalCount;      // 전체 데이터 개수
    private int totalPages;      // 전체 페이지 수
    private int currentPage;     // 현재 페이지
    private int pageSize;        // 페이지당 개수

    public PageResponse(List<T> list, int totalCount, PageRequest pageRequest) {
        this.list = list;
        this.totalCount = totalCount;
        this.currentPage = pageRequest.getPage();
        this.pageSize = pageRequest.getSize();
        // 🚀 전체 페이지 수 자동 계산 로직 (ex: 총 101개 / 10 = 11페이지)
        this.totalPages = (int) Math.ceil((double) totalCount / this.pageSize);
    }
}
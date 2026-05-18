package sst.global.code.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonCodeSearchRequest {

    private String groupCode;

    private int page = 1;

    private int size = 10;

    private String sortBy = "groupCode";

    private String sortDir = "asc";

    public int getOffset() {
        return (page - 1) * size;
    }
}
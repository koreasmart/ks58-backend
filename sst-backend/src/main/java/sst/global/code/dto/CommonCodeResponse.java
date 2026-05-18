package sst.global.code.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CommonCodeResponse {

    private String code;
    private String codeName;
    private String codeEngName;
    private String groupCode;
    private Integer sortOrder;
    private String useYn;
}
package sst.customersupport.domain;

import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerSupport {

    private Long csNo;
    private String csCatCd;
    private Long csWrterNo;

    private String csTitle;
    private String csContent;

    private int csInqireCnt;

    private String csPinYn;

    private LocalDateTime csRegDate;
    private LocalDateTime csUpDate;

    private String csUseYn;
}
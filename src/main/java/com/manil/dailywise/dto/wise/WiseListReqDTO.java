package com.manil.dailywise.dto.wise;

import com.manil.dailywise.enums.common.SortValue;
import com.manil.dailywise.enums.wise.WiseListSortType;
import lombok.Data;

@Data
public class WiseListReqDTO {
    String search;
    WiseListSortType sortType;
    SortValue sort;
}

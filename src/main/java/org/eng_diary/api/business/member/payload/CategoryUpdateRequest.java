package org.eng_diary.api.business.member.payload;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
public class CategoryUpdateRequest {

    private List<CategoryData> categories;
    private List<Long> deletedCategoryIds;

    @Setter
    private Long memberId;

}

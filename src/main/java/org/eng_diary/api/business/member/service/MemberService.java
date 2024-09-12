package org.eng_diary.api.business.member.service;

import lombok.RequiredArgsConstructor;
import org.eng_diary.api.business.auth.model.User;
import org.eng_diary.api.business.member.payload.CategoryData;
import org.eng_diary.api.business.member.payload.CategoryUpdateRequest;
import org.eng_diary.api.business.member.repository.MemberRepository;
import org.eng_diary.api.domain.MemberWordCategory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional
    public void updateWordCategory(CategoryUpdateRequest request) {

        List<CategoryData> categories = request.getCategories();

        for (CategoryData category : categories) {
            Long categoryId = category.getId();
            if (categoryId == null) {   // 신규 카테고리 추가
                User member = new User();
                member.setId(request.getMemberId());

                MemberWordCategory newCategory = new MemberWordCategory();
                newCategory.setCategoryName(category.getName());
                newCategory.setMember(member);

                memberRepository.addWordNewCategory(newCategory);
                continue;
            }

            memberRepository.updateWordCategory(category, request.getMemberId());
        }

        // 기존 카테고리 삭제
        List<Long> deletedCategoryIds = request.getDeletedCategoryIds();
        for (Long categoryId : deletedCategoryIds) {
            memberRepository.deleteWordCategory(categoryId, request.getMemberId());
        }

    }
}

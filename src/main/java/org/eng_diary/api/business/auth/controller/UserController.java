package org.eng_diary.api.business.auth.controller;

import org.eng_diary.api.business.auth.exception.ResourceNotFoundException;
import org.eng_diary.api.business.auth.model.User;
import org.eng_diary.api.business.auth.payload.UserDTO;
import org.eng_diary.api.business.auth.repository.UserRepository;
import org.eng_diary.api.security.CurrentUser;
import org.eng_diary.api.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class UserController {

    @Autowired
    private UserRepository userRepository;

    @GetMapping("/user/me")
    @PreAuthorize("hasRole('USER')")
    public UserDTO getCurrentUser(@CurrentUser UserPrincipal userPrincipal) {
        User user = userRepository.findById(userPrincipal.getId())
            .orElseThrow(() -> new ResourceNotFoundException("User", "id", userPrincipal.getId()));

        UserDTO userDTO = new UserDTO();
        userDTO.setId(user.getId());
        userDTO.setName(user.getName());
        userDTO.setRole("USER");
        userDTO.setProfileImgUrl(user.getImageUrl());

        return userDTO;
    }
}

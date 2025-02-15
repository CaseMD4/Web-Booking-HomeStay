package com.example.case_team_3.service.chat;

import org.springframework.security.core.userdetails.UserDetails;

public interface UserIdAwareUserDetails extends UserDetails {
    Integer getUserId();
}

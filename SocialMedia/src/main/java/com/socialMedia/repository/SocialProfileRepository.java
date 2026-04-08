package com.socialMedia.repository;

import com.socialMedia.model.SocialProfile;
import com.socialMedia.model.SocialUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialProfileRepository extends JpaRepository<SocialProfile, Long> {
}

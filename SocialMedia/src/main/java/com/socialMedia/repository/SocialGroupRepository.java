package com.socialMedia.repository;

import com.socialMedia.model.SocialGroup;
import com.socialMedia.model.SocialUser;
import org.hibernate.boot.jaxb.mapping.spi.JaxbPersistentAttribute;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SocialGroupRepository extends JpaRepository<SocialGroup, Long> {
}

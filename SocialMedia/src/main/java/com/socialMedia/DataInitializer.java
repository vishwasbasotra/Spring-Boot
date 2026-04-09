package com.socialMedia;

import com.socialMedia.model.Post;
import com.socialMedia.model.SocialGroup;
import com.socialMedia.model.SocialProfile;
import com.socialMedia.model.SocialUser;
import com.socialMedia.repository.PostRepository;
import com.socialMedia.repository.SocialGroupRepository;
import com.socialMedia.repository.SocialProfileRepository;
import com.socialMedia.repository.SocialUserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DataInitializer {

    public final SocialUserRepository socialUserRepository;
    public final SocialProfileRepository socialProfileRepository;
    public final SocialGroupRepository socialGroupRepository;
    public final PostRepository postRepository;

    public DataInitializer(SocialUserRepository socialUserRepository, SocialProfileRepository socialProfileRepository, SocialGroupRepository socialGroupRepository, PostRepository postRepository) {
        this.socialUserRepository = socialUserRepository;
        this.socialProfileRepository = socialProfileRepository;
        this.socialGroupRepository = socialGroupRepository;
        this.postRepository = postRepository;
    }

    @Bean
    public CommandLineRunner initializeData(){
        return (args -> {
            // creating users
            SocialUser user1 = new SocialUser();
            SocialUser user2 = new SocialUser();
            SocialUser user3 = new SocialUser();

            // saving users to the db
            socialUserRepository.save(user1);
            socialUserRepository.save(user2);
            socialUserRepository.save(user3);

            // creating groups
            SocialGroup group1 = new SocialGroup();
            SocialGroup group2 = new SocialGroup();

            // add users to the groups
            group1.getSocialUsers().add(user1);
            group1.getSocialUsers().add(user2);

            group2.getSocialUsers().add(user2);
            group2.getSocialUsers().add(user3);

            // saving groups to the db
            socialGroupRepository.save(group1);
            socialGroupRepository.save(group2);

            // associating users with groups
            user1.getSocialGroups().add(group1);
            user2.getSocialGroups().add(group1);

            user2.getSocialGroups().add(group2);
            user3.getSocialGroups().add(group2);

            // save users back to db to update association
            socialUserRepository.save(user1);
            socialUserRepository.save(user2);
            socialUserRepository.save(user3);

            // creating some posts
            Post post1 = new Post();
            Post post2 = new Post();
            Post post3 = new Post();

            // associating posts with users
            post1.setSocialUser(user1);
            post2.setSocialUser(user2);
            post3.setSocialUser(user3);

            // saving posts to the db
            postRepository.save(post1);
            postRepository.save(post2);
            postRepository.save(post3);

            // creating some user profiles
            SocialProfile socialProfile1 = new SocialProfile();
            SocialProfile socialProfile2 = new SocialProfile();
            SocialProfile socialProfile3 = new SocialProfile();

            // associating profiles with the users
            socialProfile1.setUser(user1);
            socialProfile2.setUser(user2);
            socialProfile3.setUser(user3);

            // saving social profile to the db
            socialProfileRepository.save(socialProfile1);
            socialProfileRepository.save(socialProfile2);
            socialProfileRepository.save(socialProfile3);
        });
    }
}

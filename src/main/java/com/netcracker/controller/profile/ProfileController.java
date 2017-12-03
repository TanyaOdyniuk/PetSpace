package com.netcracker.controller.profile;

import com.netcracker.model.user.Profile;
import com.netcracker.service.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    ProfileService profileService;
    @Value("${server.address}")
    private String serverAddress;
    @Value("${server.port}")
    private String serverPort;

/*    @GetMapping
    public List<Advertisement> getProfileAds() {
        return profileService.viewProfile();
    }*/

    @GetMapping("/{id}")
    public Profile getMyProfile(@PathVariable("id") BigInteger profileId) {
        return profileService.viewProfile(profileId);
    }
}

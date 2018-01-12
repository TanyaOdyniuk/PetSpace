package com.netcracker.controller.profile;

import com.netcracker.model.user.Profile;
import com.netcracker.service.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @GetMapping("/{id}")
    public Profile getProfile(@PathVariable("id") BigInteger profileId) {
        return profileService.viewProfile(profileId);
    }
}

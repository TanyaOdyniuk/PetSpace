package com.netcracker.controller.profile;

import com.netcracker.model.user.Profile;
import com.netcracker.service.profile.ProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;

@RestController
@RequestMapping("/profile")
public class ProfileController {
    @Autowired
    private ProfileService profileService;

    @GetMapping("/{id}")
    public Profile getProfile(@PathVariable("id") BigInteger profileId) {
        return profileService.viewProfile(profileId);
    }


    @PostMapping("/update")
    public void updatePet(@RequestBody Profile profile){
        profileService.updateProfile(profile);
    }
}

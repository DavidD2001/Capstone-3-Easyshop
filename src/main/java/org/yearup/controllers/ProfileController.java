package org.yearup.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.yearup.data.ProfileDao;
import org.yearup.data.UserDao;
import org.yearup.models.Profile;

@RestController
@RequestMapping("/profile")
@CrossOrigin
@PreAuthorize("isAuthenticated()")
public class ProfileController
{
    private final ProfileDao profileDao;
    private final UserDao userDao;

    @Autowired
    public ProfileController(ProfileDao profileDao, UserDao userDao)
    {
        this.profileDao = profileDao;
        this.userDao = userDao;
    }

    @GetMapping
    public Profile getProfile(Authentication auth)
    {
        String username = auth.getName();
        int userId = userDao.getIdByUsername(username);

        return profileDao.getByUserId(userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Profile createProfile(Authentication auth,
                                 @RequestBody Profile profile)
    {
        String username = auth.getName();
        int userId = userDao.getIdByUsername(username);

        profile.setUserId(userId);
        return profileDao.create(profile);
    }

    @PutMapping
    public void updateProfile(@RequestBody Profile profile, Authentication auth)
    {
        String username = auth.getName();
        int userId = userDao.getIdByUsername(username);

        profileDao.update(userId, profile);
    }

    @DeleteMapping
    public void deleteProfile(Authentication auth)
    {
        String username = auth.getName();
        int userId = userDao.getIdByUsername(username);

        profileDao.delete(userId);
    }
}

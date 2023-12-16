package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.model.Preference;
import com.daiken.workoutprogress.model.User;
import com.daiken.workoutprogress.repository.PreferenceRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@Component
public class PreferenceQueryResolver implements GraphQLQueryResolver {

    private final PreferenceRepository preferenceRepository;
    private final UserService userService;

    @Autowired
    public PreferenceQueryResolver(PreferenceRepository preferenceRepository,
                                   UserService userService) {
        this.preferenceRepository = preferenceRepository;
        this.userService = userService;
    }

    @PreAuthorize("isAuthenticated()")
    public Preference myPreference() {
        User me = userService.getContextUser();
        Preference preference = preferenceRepository.findByUserId(me.getId()).orElse(null);
        if (preference == null) {
            return preferenceRepository.save(new Preference(me));
        }
        return preference;
    }
}

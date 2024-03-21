package com.daiken.workoutprogress.api.graphql;

import com.daiken.workoutprogress.api.graphql.input.PreferenceInput;
import com.daiken.workoutprogress.models.Preference;
import com.daiken.workoutprogress.models.User;
import com.daiken.workoutprogress.repositories.PreferenceRepository;
import com.daiken.workoutprogress.services.UserService;
import graphql.kickstart.tools.GraphQLMutationResolver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Component;

@PreAuthorize("isAuthenticated()")
@Component
public class PreferenceMutationResolver implements GraphQLMutationResolver {

    private final PreferenceRepository preferenceRepository;
    private final UserService userService;

    @Autowired
    public PreferenceMutationResolver(PreferenceRepository preferenceRepository,
                                      UserService userService) {
        this.preferenceRepository = preferenceRepository;
        this.userService = userService;
    }

    public Preference updateMyPreference(PreferenceInput input) {
        User me = userService.getContextUser();
        Preference preference = preferenceRepository.findByUserId(me.getId()).orElse(null);
        if (preference == null) {
            return preferenceRepository.save(new Preference(me, input));
        } else {
            preference.update(input);
            return preferenceRepository.save(preference);
        }
    }
}

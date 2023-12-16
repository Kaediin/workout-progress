package com.daiken.workoutprogress.migrations;

import com.daiken.workoutprogress.annotation.Migratable;

@Migratable
public class PreferenceMigrations {

//    @Migration(key = "updatePreferenceWithDistance", order = 2, author = "Kaedin")
//    public void updatePreferenceWithDistance(List<String> messages, PreferenceRepository preferenceRepository) {
//        preferenceRepository.saveAll(
//                preferenceRepository
//                        .findAllByUnitExists(true)
//                        .peek(it -> {
//                            it.weightUnit = it.unit;
//                            it.distanceUnit = LogUnit.KM;
//                            it.unit = null;
//                        })
//                        .collect(Collectors.toList())
//        );
//    }
}

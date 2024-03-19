## [v0.1.5]

### Added

- #28: Timer completion sound is now conditioned to the preferences
- #30: Endpoint to restart workout
- #31: Introduced endpoint that returns the logs of an exercise in the latest workout excluding the current active one

### Changed

- #30: Auto adjust workout muscle groups is now enabled by default and cannot be disabled

## [v0.1.4]

### Added

- #5: Added unit tests for model objects
- #5: Added unit tests for services
- #5: Added integration tests for repositories
- #27: Endpoint to relog specific log

## [v0.1.3]

### Added

- #26: Timer duration to preference
- #26: Timer auto start after logging to preference

## [v0.1.2]

### Added

- #15: Integrated SentryIO for issue tracking
- #16: Readme
- #23: Endpoint to re-log last logged exercise of a workout

### Changed

- #23: For a logged secondary muscle group to be listed on the workout, that group must be logged at least once in 2
  separate exercises.

## [v0.1.1]

### Added

- #18: Support for logging distance
- #19: Editing support for workouts

### Changed

- #17: Last logged exercise is not last logged set

## [v0.1.0]

- #6: Warmup toggle
- #7: Preference option for hiding unit selector
- #8: Remarks for creating a workout and logging an exercise
- #9: Option to add a default applied weight to an exercises. Useful for some logging certain machines
- #10: Migrated number value of logged weight to WeightValue object
- #11: Notes to exercise
- #13: Auto adjust workout muscle groups based on logged exercises
- #14: Added query to get last logged exercise log by exercise id and user id

## [v0.0.1]

### Added

- #0: GraphQL
- #0: Exercises
- #0: Workouts
- #0: Users
- #0: Cognito
- #1: Moved db name to environment variables
- #2: Added preferences
- #3: Workout ends with latest logged exercise
- #4: Delete option to Workouts
- #4: Delete option to Exercises
- #4: Edit option to Exercises

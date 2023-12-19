package com.daiken.workoutprogress.models;

import com.devskiller.friendly_id.FriendlyId;
import software.amazon.awssdk.services.cognitoidentityprovider.model.AttributeType;
import software.amazon.awssdk.services.cognitoidentityprovider.model.UserType;

import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class CognitoUser {

    public String name;
    public String userName;
    public String fid;
    public String given_name;
    public String family_name;
    public String middle_name;
    public String nickname;
    public String email;
    public String gender;
    public String zoneinfo;
    public String locale;
    public Date createdDate;

    public CognitoUser() {
    }

    public CognitoUser(UserType userType) {
        createdDate = Date.from(userType.userCreateDate());
        userName = userType.username();

        parseUserTypeAttributes(userType.attributes());
        if (userName == null) userName = getFullName().replace(" ", "");
    }

    public String getFullName() {
        if (name != null && !name.isEmpty()) {
            return name;
        }

        return Stream.of(given_name, middle_name, family_name)
                .filter(Objects::nonNull)
                .filter(string -> !string.isEmpty())
                .collect(Collectors.joining(" "));
    }

    public void parseUserTypeAttributes(List<AttributeType> attributeDataTypes) {
        for (AttributeType type : attributeDataTypes) {
            String value = type.value();

            switch (type.name()) {
                case "sub":
                    fid = FriendlyId.toFriendlyId(UUID.fromString(value));
                    break;
                case "email":
                    email = value;
                    break;
                case "name":
                    name = value;
                    break;
                case "given_name":
                    given_name = value;
                    break;
                case "family_name":
                    family_name = value;
                    break;
                case "middle_name":
                    middle_name = value;
                    break;
                case "gender":
                    gender = value;
                    break;
                case "locale":
                    locale = value;
                    break;
            }
        }
    }
}

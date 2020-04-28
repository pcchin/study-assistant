/*
 * Copyright 2020 PC Chin. All rights reserved.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.pcchin.studyassistant.fragment.project.create;

import androidx.annotation.NonNull;

import com.google.android.material.textfield.TextInputLayout;
import com.pcchin.studyassistant.database.project.ProjectDatabase;
import com.pcchin.studyassistant.database.project.data.MemberData;
import com.pcchin.studyassistant.database.project.data.RoleData;
import com.pcchin.studyassistant.functions.GeneralFunctions;
import com.pcchin.studyassistant.functions.SecurityFunctions;
import com.pcchin.studyassistant.utils.misc.RandomString;

/** Functions used when creating a project in ProjectCreateFragment. **/
final class ProjectCreateFragmentCreate {

    /** Creates the admin role based on the given info. **/
    @NonNull
    static RoleData createAdminRole(boolean customAdmin, RandomString idRand,
                                     RandomString saltRand, String projectID,
                                     TextInputLayout customAdminName,
                                     TextInputLayout customAdminPass1,
                                     ProjectDatabase projectDatabase) {
        RoleData adminRole;
        if (customAdmin && customAdminName.getEditText() != null
                && customAdminPass1.getEditText() != null) {
            if (customAdminPass1.getEditText().getText().length() > 0) {
                // Admin with password
                String adminSalt = saltRand.nextString();
                adminRole = new RoleData(GeneralFunctions.generateValidProjectString(idRand,
                        ProjectCreateFragment.TYPE_ROLE, projectDatabase), projectID,
                        customAdminName.getEditText().getText().toString(),
                        adminSalt, SecurityFunctions.roleHash(customAdminPass1
                        .getEditText().getText().toString(), adminSalt));
            } else {
                adminRole = new RoleData(GeneralFunctions.generateValidProjectString(idRand,
                        ProjectCreateFragment.TYPE_ROLE, projectDatabase), projectID,
                        customAdminName.getEditText().getText().toString(),
                        saltRand.nextString(), "");
            }
        } else {
            adminRole = new RoleData(GeneralFunctions.generateValidProjectString(idRand,
                    ProjectCreateFragment.TYPE_ROLE, projectDatabase), projectID,
                    "Admin", saltRand.nextString(), "");
        }
        adminRole.canDeleteProject = true;
        adminRole.canModifyInfo = true;
        adminRole.canModifyOtherTask = true;
        adminRole.canModifyOtherUser = true;
        adminRole.canModifyOwnTask = true;
        adminRole.canModifyRole = true;
        adminRole.canModifyOtherStatus = true;
        adminRole.canPostStatus = true;
        adminRole.canSetPassword = true;
        adminRole.canViewOtherTask = true;
        adminRole.canViewOtherUser = true;
        adminRole.canViewRole = true;
        adminRole.canViewTask = true;
        adminRole.canViewStatus = true;
        adminRole.canViewMedia = true;
        return adminRole;
    }

    /** Creates the member role based on the given info. **/
    @NonNull
    static RoleData createMemberRole(boolean customMember, RandomString idRand,
                                      RandomString saltRand, String projectID,
                                      TextInputLayout customMemberName,
                                      TextInputLayout customMemberPass1,
                                      ProjectDatabase projectDatabase) {
        RoleData memberRole;
        // Creates member role
        if (customMember && customMemberName.getEditText() != null
                && customMemberPass1.getEditText() != null) {
            if (customMemberPass1.getEditText().getText().length() > 0) {
                // Admin with password
                String memberSalt = saltRand.nextString();
                memberRole = new RoleData(GeneralFunctions.generateValidProjectString(idRand,
                        ProjectCreateFragment.TYPE_ROLE, projectDatabase), projectID,
                        customMemberName.getEditText().getText().toString(),
                        memberSalt, SecurityFunctions.roleHash(customMemberPass1
                        .getEditText().getText().toString(), memberSalt));
            } else {
                memberRole = new RoleData(GeneralFunctions.generateValidProjectString(idRand,
                        ProjectCreateFragment.TYPE_ROLE, projectDatabase), projectID,
                        customMemberName.getEditText().getText().toString(),
                        saltRand.nextString(), "");
            }
        } else {
            memberRole = new RoleData(GeneralFunctions.generateValidProjectString(idRand,
                    ProjectCreateFragment.TYPE_ROLE, projectDatabase), projectID,
                    "Member", saltRand.nextString(), "");
        }
        return memberRole;
    }

    /** Creates the initial member based on the given info. **/
    static MemberData createInitialMember(RandomString idRand, @NonNull RandomString saltRand,
                                          String projectID, String projectSalt, String adminRoleID,
                                          @NonNull TextInputLayout memberName,
                                          TextInputLayout memberPass1,
                                          ProjectDatabase projectDatabase) {
        MemberData initialMember = null;
        String memberSalt = saltRand.nextString();
        if (memberName.getEditText() != null && memberPass1.getEditText() != null) {
            if (memberPass1.getEditText().getText().length() > 0) {
                initialMember = new MemberData(GeneralFunctions
                        .generateValidProjectString(idRand, ProjectCreateFragment.TYPE_MEMBER, projectDatabase),
                        projectID, memberName.getEditText().getText().toString(), "",
                        memberSalt,
                        SecurityFunctions.memberHash(memberPass1.getEditText()
                                .getText().toString(), memberSalt, projectSalt),
                        adminRoleID);
            } else {
                initialMember = new MemberData(GeneralFunctions
                        .generateValidProjectString(idRand, ProjectCreateFragment.TYPE_MEMBER, projectDatabase),
                        projectID, memberName.getEditText().getText().toString(), "",
                        memberSalt, "", adminRoleID);
            }
        }
        return initialMember;
    }
}

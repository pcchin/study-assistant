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

package com.pcchin.studyassistant.fragment.project.verify;

import android.annotation.SuppressLint;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.room.Room;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.pcchin.studyassistant.R;
import com.pcchin.studyassistant.database.project.ProjectDatabase;
import com.pcchin.studyassistant.database.project.data.MemberData;
import com.pcchin.studyassistant.database.project.data.ProjectData;
import com.pcchin.studyassistant.database.project.data.RoleData;
import com.pcchin.studyassistant.functions.SecurityFunctions;
import com.pcchin.studyassistant.ui.MainActivity;
import com.pcchin.studyassistant.ui.AutoDismissDialog;
import com.pcchin.studyassistant.ui.ExtendedFragment;
import com.pcchin.studyassistant.fragment.project.ProjectInfoFragment;
import com.pcchin.studyassistant.fragment.project.ProjectSelectFragment;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class ProjectLoginFragment extends Fragment implements ExtendedFragment {
    private static final String ARG_ID = "projectID";
    private ProjectDatabase projectDatabase;
    private ProjectData project;

    /** Default constructor. **/
    public ProjectLoginFragment() {
    }

    /** Constructor used when attempting to access the project. **/
    public static ProjectLoginFragment newInstance(String projectID) {
        ProjectLoginFragment fragment = new ProjectLoginFragment();
        Bundle args = new Bundle();
        args.putString(ARG_ID, projectID);
        fragment.setArguments(args);
        return fragment;
    }

    /** Initializes the fragment and the project info. **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getActivity() != null && getFragmentManager() != null) {
            projectDatabase = Room.databaseBuilder(getActivity(),
                    ProjectDatabase.class, MainActivity.DATABASE_PROJECT)
                    .fallbackToDestructiveMigrationFrom(1, 2, 3, 4, 5)
                    .allowMainThreadQueries().build();
            if (getArguments() != null) {
                project = projectDatabase.ProjectDao().searchByID(getArguments().getString(ARG_ID));
            }
            if (project == null) {
                // Go back to project selection
                Toast.makeText(getActivity(), R.string.p_error_project_not_found,
                        Toast.LENGTH_SHORT).show();
                projectDatabase.close();
                if (getActivity() != null) {
                    // If statement added to prevent NullPointerException
                    ((MainActivity) getActivity()).displayFragment(new ProjectSelectFragment());
                }
            } else {
                if (project.projectProtected || project.membersEnabled || project.rolesEnabled) {
                    getActivity().setTitle(R.string.v2_project_login);
                    if (project.projectProtected) {
                        // Set up password input layout
                        @SuppressLint("InflateParams") TextInputLayout passwordLayout =
                                (TextInputLayout) getLayoutInflater()
                                .inflate(R.layout.popup_edittext, null);
                        if (passwordLayout.getEditText() != null) {
                            passwordLayout.getEditText().setInputType(InputType.TYPE_CLASS_TEXT |
                                    InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        }
                        passwordLayout.setEndIconMode(TextInputLayout.END_ICON_PASSWORD_TOGGLE);
                        passwordLayout.setHint(getString(R.string.v1_project_protected_password));

                        // Set up OnShowListener
                        DialogInterface.OnShowListener passwordDialogListener = dialogInterface -> {
                            ((AlertDialog) dialogInterface).getButton(
                                    DialogInterface.BUTTON_POSITIVE).setOnClickListener(view -> {
                                        // Check if password is correct
                                        if (passwordLayout.getEditText().getText()
                                                .toString().length() >= 8) {
                                            if (Objects.equals(SecurityFunctions.projectHash(
                                                    passwordLayout.getEditText().getText()
                                                            .toString(), project.salt),
                                                    project.projectPass)) {
                                                // Password is correct
                                                dialogInterface.dismiss();
                                                if (!project.membersEnabled && !project.rolesEnabled) {
                                                    // No secondary check
                                                    projectDatabase.close();
                                                    ((MainActivity) getActivity()).displayFragment
                                                            (ProjectInfoFragment.newInstance(
                                                                    project.projectID, "admin",
                                                                    false, true));
                                                }
                                            } else {
                                                passwordLayout.setError(getString(
                                                        R.string.error_password_incorrect));
                                            }
                                        } else {
                                            // Display error message
                                            passwordLayout.setError(getString(
                                                    R.string.error_password_short));
                                        }
                            });
                            ((AlertDialog) dialogInterface).getButton(
                                    DialogInterface.BUTTON_NEGATIVE).setOnClickListener(view -> {
                                        // Returns to ProjectSelectFragment
                                        dialogInterface.dismiss();
                                        projectDatabase.close();
                                        ((MainActivity) getActivity())
                                                .displayFragment(new ProjectSelectFragment());
                                    });
                        };

                        AutoDismissDialog passwordDialog = new AutoDismissDialog(
                                getString(R.string.v1_project_protected), passwordLayout,
                                new String[]{getString(android.R.string.ok),
                                        getString(android.R.string.cancel), ""},
                                passwordDialogListener);
                        passwordDialog.setCancellable(false);
                        passwordDialog.show(getFragmentManager(), "ProjectLoginFragment.1");
                    }
                } else {
                    // All users will be logged in as admin if roles & members are not enabled
                    // and the project is not password-protected
                    projectDatabase.close();
                    if (getActivity() != null) {
                        // If statement added to prevent NullPointerException
                        ((MainActivity) getActivity()).displayFragment(ProjectInfoFragment
                                .newInstance(project.projectID, "admin", false, true));
                    }
                }
            }
        }
    }

    /** Sets up the layout for the login fragment. **/
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnScroll = inflater.inflate(R.layout.fragment_project_login, container, false);
        ((TextView) returnScroll.findViewById(R.id.v2_title)).setText(project.projectTitle);
        // Set up icon (only for portrait)
        if (getActivity() != null) {
            String iconPath = getActivity().getFilesDir().getAbsolutePath() + "/icons/project/"
                    + project.projectID + ".jpg";
            ImageView projectIcon = returnScroll.findViewById(R.id.v2_icon);
            if (project.hasIcon && new File(iconPath).exists()) {
                projectIcon.setImageURI(Uri.fromFile(new File(iconPath)));
            }
        }

        // Set up TextInputLayouts
        TextInputLayout passwordInputLayout = returnScroll.findViewById(R.id.v2_password_input);
        TextInputLayout userInputLayout = returnScroll.findViewById(R.id.v2_username_input);

        // Set up whether to display members or roles
        if (getActivity() != null) {
            if (project.membersEnabled) {
                returnScroll.findViewById(R.id.v2_role).setVisibility(View.GONE);
                returnScroll.findViewById(R.id.v2_role_input).setVisibility(View.GONE);
                returnScroll.findViewById(R.id.v2_button_signup).setOnClickListener(view -> {
                    // Display signup page
                    ((MainActivity) getActivity()).displayFragment(ProjectSignupFragment
                            .newInstance(project.projectID));
                });
                // Login through username
                returnScroll.findViewById(R.id.v2_button_login).setOnClickListener(view -> {
                    Toast.makeText(getActivity(), R.string.v2_logging_in, Toast.LENGTH_SHORT).show();
                    userInputLayout.setErrorEnabled(false);
                    passwordInputLayout.setErrorEnabled(false);

                    // Check if username exists
                    if (userInputLayout.getEditText() != null && passwordInputLayout.getEditText() != null) {
                        if (userInputLayout.getEditText().getText().length() == 0) {
                            userInputLayout.setErrorEnabled(true);
                            userInputLayout.setError(getString(R.string.v_error_username_blank));
                        } else {
                            MemberData targetMember = projectDatabase.MemberDao()
                                    .searchInProjectByUsername(project.projectID,
                                            userInputLayout.getEditText().getText().toString());
                            if (targetMember == null) {
                                userInputLayout.setErrorEnabled(true);
                                userInputLayout.setError(getString(R.string.v_error_username_password_incorrect));
                            } else {
                                // Check if password entered is correct
                                String hashedPassword = SecurityFunctions.memberHash(
                                        passwordInputLayout.getEditText().getText().toString(),
                                        targetMember.salt, project.salt);
                                if (Objects.equals(hashedPassword, targetMember.memberPass)) {
                                    projectDatabase.close();
                                    ((MainActivity) getActivity()).displayFragment(ProjectInfoFragment
                                            .newInstance(project.projectID,
                                                    targetMember.memberID, true, true));
                                } else {
                                    passwordInputLayout.setErrorEnabled(true);
                                    passwordInputLayout.setError(
                                            getString(R.string.v_error_username_password_incorrect));
                                }
                            }
                        }
                    }
                });
                // Sign up if conditions met
                if (project.membersEnabled && project.memberSignupEnabled) {
                    returnScroll.findViewById(R.id.v2_button_signup).setOnClickListener(view -> {
                        projectDatabase.close();
                        ((MainActivity) getActivity()).displayFragment(ProjectSignupFragment
                                .newInstance(project.projectID));
                    });
                }
            } else if (project.rolesEnabled) {
                returnScroll.findViewById(R.id.v2_username).setVisibility(View.GONE);
                returnScroll.findViewById(R.id.v2_username_input).setVisibility(View.GONE);
                returnScroll.findViewById(R.id.v2_button_signup).setVisibility(View.GONE);

                // Populate spinner
                Spinner roleSpinner = returnScroll.findViewById(R.id.v2_role_input);
                List<RoleData> roleDataList = projectDatabase.RoleDao().searchByProject(project.projectID);
                ArrayList<String> roleList = new ArrayList<>();
                for (RoleData role : roleDataList) {
                    roleList.add(role.roleName);
                }
                ArrayAdapter<String> roleAdapter = new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_spinner_dropdown_item, roleList);
                roleSpinner.setAdapter(roleAdapter);

                // Login through role
                returnScroll.findViewById(R.id.v2_button_login).setOnClickListener(view -> {
                    Toast.makeText(getActivity(), R.string.v2_logging_in, Toast.LENGTH_SHORT).show();
                    passwordInputLayout.setErrorEnabled(false);
                    if (passwordInputLayout.getEditText() != null) {
                        String inputPassword = passwordInputLayout.getEditText().getText().toString();
                        RoleData roleSelected = roleDataList.get(roleSpinner.getSelectedItemPosition());
                        if (inputPassword.length() == 0 && roleSelected.rolePass.length() == 0) {
                            // Role doesn't have a password
                            projectDatabase.close();
                            ((MainActivity) getActivity()).displayFragment(ProjectInfoFragment
                                    .newInstance(project.projectID, roleSelected.roleID,
                                            false, true));
                        } else if (inputPassword.length() >= 8) {
                            // Check whether the password for the role is correct.
                            String hashedPassword = SecurityFunctions.roleHash(
                                    inputPassword, roleSelected.salt);
                            if (Objects.equals(hashedPassword, roleSelected.rolePass)) {
                                projectDatabase.close();
                                ((MainActivity) getActivity()).displayFragment(ProjectInfoFragment
                                        .newInstance(project.projectID, roleSelected.roleID,
                                                false, true));
                            } else {
                                passwordInputLayout.setErrorEnabled(true);
                                passwordInputLayout.setError(getString(R.string.error_password_incorrect));
                            }
                        } else {
                            passwordInputLayout.setErrorEnabled(true);
                            passwordInputLayout.setError(getString(R.string.error_password_short));
                        }
                    }
                });
                // Sign up if conditions met
                if (project.membersEnabled && project.memberSignupEnabled) {
                    returnScroll.findViewById(R.id.v2_button_signup).setOnClickListener(view -> {
                        projectDatabase.close();
                        ((MainActivity) getActivity()).displayFragment(ProjectSignupFragment
                                .newInstance(project.projectID));
                    });
                }
            }
        }
        return returnScroll;
    }

    /** Return to
     * @see ProjectSelectFragment **/
    @Override
    public boolean onBackPressed() {
        projectDatabase.close();
        if (getActivity() != null) {
            ((MainActivity) getActivity()).displayFragment(new ProjectSelectFragment());
            return true;
        }
        return false;
    }
}
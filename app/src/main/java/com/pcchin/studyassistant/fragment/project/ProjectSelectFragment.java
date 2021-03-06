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

package com.pcchin.studyassistant.fragment.project;

import android.annotation.SuppressLint;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

import com.pcchin.studyassistant.R;
import com.pcchin.studyassistant.database.project.ProjectDatabase;
import com.pcchin.studyassistant.database.project.data.ProjectData;
import com.pcchin.studyassistant.fragment.project.create.ProjectCreateFragment;
import com.pcchin.studyassistant.functions.DatabaseFunctions;
import com.pcchin.studyassistant.ui.ExtendedFragment;
import com.pcchin.studyassistant.activity.MainActivity;
import com.pcchin.studyassistant.fragment.main.MainFragment;
import com.pcchin.studyassistant.fragment.project.verify.ProjectLoginFragment;

import java.util.List;

public class ProjectSelectFragment extends Fragment implements ExtendedFragment {
    private ProjectDatabase projectDatabase;

    /** Default constructor. **/
    public ProjectSelectFragment() {
        // Default constructor.
    }

    /** Initializes the fragments and the projects. **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        projectDatabase = DatabaseFunctions.getProjectDatabase(requireActivity());
        requireActivity().setTitle(R.string.projects);
        setHasOptionsMenu(true);
    }

    /** Sets up the layout and add projects to it. **/
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View returnView = inflater.inflate(R.layout.blank_list, container, false);
        LinearLayout linearView = returnView.findViewById(R.id.blank_linear);
        List<ProjectData> displayList = projectDatabase.ProjectDao().getAllProjects();
        for (int i = 0; i < displayList.size(); i++) {
            @SuppressLint("InflateParams") Button subjectBtn = (Button) getLayoutInflater()
                    .inflate(R.layout.hyperlink_btn, null);
            subjectBtn.setText(displayList.get(i).projectTitle);
            int finalI = i;
            subjectBtn.setOnClickListener(view ->
                    ((MainActivity) requireActivity()).displayFragment(ProjectLoginFragment
                    .newInstance(displayList.get(finalI).projectID)));
            linearView.addView(subjectBtn);
        }
        return returnView;
    }

    /** Sets up the menu for the fragment. **/
    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.menu_selector, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    /** Creates a new project. **/
    public void onNewProjectPressed() {
        projectDatabase.close();
        ((MainActivity) requireActivity()).displayFragment(new ProjectCreateFragment());
    }

    /** Imports an existing project file. **/
    public void onImportPressed() {
        // TODO: Import project
    }

    /** Returns to
     * @see MainFragment **/
    @Override
    public boolean onBackPressed() {
        projectDatabase.close();
        ((MainActivity) requireActivity()).displayFragment(new MainFragment());
        return true;
    }

    /** Closes the database if the fragment is paused. **/
    @Override
    public void onPause() {
        super.onPause();
        projectDatabase.close();
    }

    /** Reopens the database when the fragment is resumed. **/
    @Override
    public void onResume() {
        super.onResume();
        if (!projectDatabase.isOpen()) {
            projectDatabase = DatabaseFunctions.getProjectDatabase(requireActivity());
        }
    }
}

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

package com.pcchin.studyassistant.fragment.main;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.pcchin.studyassistant.R;
import com.pcchin.studyassistant.functions.GeneralFunctions;
import com.pcchin.studyassistant.fragment.about.AboutFragment;
import com.pcchin.studyassistant.ui.ExtendedFragment;
import com.pcchin.studyassistant.fragment.notes.SubjectSelectFragment;
import com.pcchin.studyassistant.fragment.project.ProjectSelectFragment;
import com.pcchin.studyassistant.activity.MainActivity;

public class MainFragment extends Fragment implements ExtendedFragment {
    private boolean doubleBackToExitPressedOnce;

    /** Default constructor. **/
    public MainFragment() {
        // Default constructor.
    }

    /** Initialize fragment. Nothing to see here. **/
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requireActivity().setTitle(R.string.app_name);
    }

    /** Creates the fragment and sets up the listener for the buttons. **/
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment and set up listeners
        View returnView = inflater.inflate(R.layout.fragment_main, container, false);
        returnView.findViewById(R.id.m1_notes).setOnClickListener(v ->
                ((MainActivity) requireActivity()).displayFragment(new SubjectSelectFragment()));
        returnView.findViewById(R.id.m1_projects).setOnClickListener(v ->
                ((MainActivity) requireActivity()).displayFragment(new ProjectSelectFragment()));
        returnView.findViewById(R.id.m1_about).setOnClickListener(v ->
                ((MainActivity) requireActivity()).displayFragment(new AboutFragment()));
        return returnView;
    }

    /** Display the exit dialog when the 'Back' button is pressed. **/
    @Override
    public boolean onBackPressed() {
        // Press back to exit
        if (doubleBackToExitPressedOnce) {
            GeneralFunctions.exitApp(requireActivity());
        } else {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(requireContext(), "Press back again to exit", Toast.LENGTH_SHORT).show();
            new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 1500);
        }
        return true;
    }
}

/*
 * Copyright 2019 PC Chin. All rights reserved.
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

package com.pcchin.studyassistant.database.project;

import androidx.room.Database;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.pcchin.studyassistant.functions.ConverterFunctions;
import com.pcchin.studyassistant.database.project.dao.MemberDao;
import com.pcchin.studyassistant.database.project.dao.ProjectDao;
import com.pcchin.studyassistant.database.project.dao.RoleDao;
import com.pcchin.studyassistant.database.project.dao.StatusDao;
import com.pcchin.studyassistant.database.project.dao.TaskDao;
import com.pcchin.studyassistant.database.project.data.MemberData;
import com.pcchin.studyassistant.database.project.data.ProjectData;
import com.pcchin.studyassistant.database.project.data.RoleData;
import com.pcchin.studyassistant.database.project.data.StatusData;
import com.pcchin.studyassistant.database.project.data.TaskData;

/** The notes layer for the projects. **/
@Database(entities={ProjectData.class, MemberData.class, TaskData.class, RoleData.class,
        StatusData.class}, version=1)
@TypeConverters(ConverterFunctions.class)
public abstract class ProjectDatabase extends RoomDatabase {
    public abstract ProjectDao ProjectDao();
    public abstract MemberDao MemberDao();
    public abstract TaskDao TaskDao();
    public abstract RoleDao RoleDao();
    public abstract StatusDao StatusDao();
}
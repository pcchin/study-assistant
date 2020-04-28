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

package com.pcchin.studyassistant.activity;

/** Constants used in MainActivity. **/
public final class ActivityConstants {
    private ActivityConstants() {
        throw new IllegalStateException("Utility class");
    }

    // Shared preference constants
    public static final String SHAREDPREF_APP_UPDATE_PATH = "AppUpdatePath";
    public static final String SHAREDPREF_LAST_UPDATE_CHECK = "lastUpdateCheck";

    // General intent constants
    public static final String INTENT_VALUE_DISPLAY_UPDATE = "displayUpdate";
    public static final String INTENT_VALUE_START_FRAGMENT = "startFragment";
    public static final String INTENT_VALUE_REQUEST_CODE = "requestCode";

    // Intent constants for notes
    public static final String INTENT_VALUE_SUBJECT = "subject";
    public static final String INTENT_VALUE_MESSAGE = "message";
    public static final String INTENT_VALUE_TITLE = "title";

    // Intent constants for projects
    public static final String INTENT_PROJECT_ID = "projectID";
    public static final String INTENT_ID2 = "id2";
    public static final String INTENT_IS_MEMBER = "isMember";

    // Intent codes
    public static final int SELECT_ZIP_FILE = 300;
    public static final int SELECT_SUBJECT_FILE = 301;
    public static final int SELECT_PROJECT_ICON = 302;
    public static final int EXTERNAL_STORAGE_READ_PERMISSION = 201;

    // Other constants
    public static final String DATABASE_NOTES = "notesSubject";
    public static final String DATABASE_PROJECT = "projectDatabase";
    public static final String LOG_APP_NAME = "StudyAssistant";

    // Permission codes
    static final int EXTERNAL_STORAGE_PERMISSION = 200;
}

package com.android.myapplication.model;

import io.realm.RealmList;
import io.realm.RealmObject;

public class AllUserRealm extends RealmObject {
    public RealmList<AllUserModel> getAllUserRealmList() {
        return allUserModelRealmList;
    }

    public void setAllUserRealmList(RealmList<AllUserModel> allUserModelRealmList) {
        this.allUserModelRealmList = allUserModelRealmList;
    }

    public RealmList<AllUserModel> allUserModelRealmList;
}

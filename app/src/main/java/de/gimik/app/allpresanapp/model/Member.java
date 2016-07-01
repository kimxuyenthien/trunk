package de.gimik.app.allpresanapp.model;

import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

/**
 * Created by Dang on 22.09.2015.
 */
@DatabaseTable(tableName = "Member")
public class Member extends AuditableEntity {

    @SerializedName("memberNumber")
    @DatabaseField
    private String memberNumber;

    @SerializedName("postCode")
    @DatabaseField
    private String postCode;

    @SerializedName("savedLogin")
    @DatabaseField
    private boolean savedLogin;


    public String getMemberNumber() {
        return memberNumber;
    }

    public void setMemberNumber(String memberNumber) {
        this.memberNumber = memberNumber;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public boolean isSavedLogin() {
        return savedLogin;
    }

    public void setSavedLogin(boolean savedLogin) {
        this.savedLogin = savedLogin;
    }
}

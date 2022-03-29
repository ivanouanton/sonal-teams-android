package com.waveneuro.data.model.response.user;

import com.asif.abase.data.model.BaseModel;
import com.google.gson.annotations.SerializedName;

public class UserInfoResponse extends BaseModel {

	@SerializedName("msg")
	private String message;

	@SerializedName("error")
	private String error;

	@SerializedName("custom:goal")
	private String customGoal;

	@SerializedName("sub")
	private int sub;

	@SerializedName("birthdate")
	private String birthdate;

	@SerializedName("custom:tos_signed")
	private boolean customTosSigned;

	@SerializedName("gender")
	private String gender;

	@SerializedName("image_thumbnail_url")
	private String imageThumbnailUrl;

	@SerializedName("created_at")
	private String createdAt;

	@SerializedName("given_name")
	private String givenName;

	@SerializedName("name")
	private String name;

	@SerializedName("location")
	private String location;

	@SerializedName("modified_at")
	private String modifiedAt;

	@SerializedName("family_name")
	private String familyName;

	@SerializedName("email")
	private String email;

	@SerializedName("username")
	private String username;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getError() {
		return error;
	}

	public void setError(String error) {
		this.error = error;
	}

	public void setCustomGoal(String customGoal){
		this.customGoal = customGoal;
	}

	public String getCustomGoal(){
		return customGoal;
	}

	public void setSub(int sub){
		this.sub = sub;
	}

	public int getSub(){
		return sub;
	}

	public void setBirthdate(String birthdate){
		this.birthdate = birthdate;
	}

	public String getBirthdate(){
		return birthdate;
	}

	public void setCustomTosSigned(boolean customTosSigned){
		this.customTosSigned = customTosSigned;
	}

	public boolean isCustomTosSigned(){
		return customTosSigned;
	}

	public void setGender(String gender){
		this.gender = gender;
	}

	public String getGender(){
		return gender;
	}

	public void setImageThumbnailUrl(String imageThumbnailUrl){
		this.imageThumbnailUrl = imageThumbnailUrl;
	}

	public String getImageThumbnailUrl(){
		return imageThumbnailUrl;
	}

	public void setCreatedAt(String createdAt){
		this.createdAt = createdAt;
	}

	public String getCreatedAt(){
		return createdAt;
	}

	public void setGivenName(String givenName){
		this.givenName = givenName;
	}

	public String getGivenName(){
		return givenName;
	}

	public void setName(String name){
		this.name = name;
	}

	public String getName(){
		return name;
	}

	public void setLocation(String location){
		this.location = location;
	}

	public String getLocation(){
		return location;
	}

	public void setModifiedAt(String modifiedAt){
		this.modifiedAt = modifiedAt;
	}

	public String getModifiedAt(){
		return modifiedAt;
	}

	public void setFamilyName(String familyName){
		this.familyName = familyName;
	}

	public String getFamilyName(){
		return familyName;
	}

	public void setEmail(String email){
		this.email = email;
	}

	public String getEmail(){
		return email;
	}

	public void setUsername(String username){
		this.username = username;
	}

	public String getUsername(){
		return username;
	}

	@Override
 	public String toString(){
		return 
			"UserInfoResponse{" + 
			"custom:goal = '" + customGoal + '\'' + 
			",sub = '" + sub + '\'' + 
			",birthdate = '" + birthdate + '\'' + 
			",custom:tos_signed = '" + customTosSigned + '\'' + 
			",gender = '" + gender + '\'' + 
			",image_thumbnail_url = '" + imageThumbnailUrl + '\'' + 
			",created_at = '" + createdAt + '\'' + 
			",given_name = '" + givenName + '\'' + 
			",name = '" + name + '\'' + 
			",location = '" + location + '\'' + 
			",modified_at = '" + modifiedAt + '\'' + 
			",family_name = '" + familyName + '\'' + 
			",email = '" + email + '\'' + 
			",username = '" + username + '\'' + 
			"}";
		}
}
package com.waveneuro.data.model.response.patient;

import com.asif.abase.data.model.BaseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PatientListResponse extends BaseModel {


    @SerializedName("patients")
    @Expose
    private List<Patient> patients;

    public List<Patient> getPatients() {
        return patients;
    }

    public void setPatients(List<Patient> patients) {
        this.patients = patients;
    }

    public class Patient {


        @SerializedName("first_name")
        private String firstName;

        @SerializedName("last_name")
        private String lastName;

        @SerializedName("birthday")
        private String birthday;

        @SerializedName("tos_signed")
        private boolean tosSigned;

        @SerializedName("sex")
        private boolean isMale;

        @SerializedName("email")
        private String email;

        @SerializedName("username")
        private String username;

        @SerializedName("id")
        private int id;

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public boolean isMale() {
            return isMale;
        }

        public void setMale(boolean male) {
            isMale = male;
        }

        public boolean isTosSigned() {
            return tosSigned;
        }

        public void setTosSigned(boolean tosSigned) {
            this.tosSigned = tosSigned;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getBirthday() {
            return birthday;
        }

        public void setBirthday(String birthday) {
            this.birthday = birthday;
        }

        @SerializedName("organization")
        @Expose
        private Organization organization;

        public Organization getOrganization() {
            return organization;
        }

        public String getOrganizationName() {
            return organization.getName();
        }

        public void setOrganization(Organization organization) {
            this.organization = organization;
        }

        public class Organization {

            @SerializedName("name")
            private String name;

            @SerializedName("id")
            private int id;

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public Organization(String name) {
                this.name = name;
            }
        }
    }

}

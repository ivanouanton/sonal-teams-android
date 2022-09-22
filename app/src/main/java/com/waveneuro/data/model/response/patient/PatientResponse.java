package com.waveneuro.data.model.response.patient;

import com.asif.abase.data.model.BaseModel;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PatientResponse extends BaseModel {


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

        class Organization {

            @SerializedName("name")
            private String name;

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

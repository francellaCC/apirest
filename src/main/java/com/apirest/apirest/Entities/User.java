package com.apirest.apirest.Entities;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "users")
public class User {

   @Id
   @GeneratedValue(strategy =  GenerationType.IDENTITY)
   private Long id;

   @Column(nullable = false)
   private String nameUser;

   @Column(nullable = false, unique = true)
   private String email;

   @Column(nullable = false )
   private  Integer roleUser;

   @OneToMany(mappedBy = "createdBy")
   @JsonManagedReference(value = "user-modules")
   private List<Module> createdModules;

   @OneToMany(mappedBy = "createdBy", cascade = CascadeType.ALL, orphanRemoval = true)
   @JsonManagedReference(value = "user-languages")
   private List<Language> createdLanguages;


   public Long getId() {
      return id;
   }

   public void setId(Long id) {
      this.id = id;
   }

   public String getNameUser() {
      return nameUser;
   }

   public void setNameUser(String nameUser) {
      this.nameUser = nameUser;
   }

   public String getEmail() {
      return email;
   }

   public void setEmail(String email) {
      this.email = email;
   }

   public Integer getRoleUser() {
      return roleUser;
   }

   public void setRoleUser(Integer roleUser) {
      this.roleUser = roleUser;
   }

   public List<Module> getCreatedModules() {
      return createdModules;
   }

   public void setCreatedModules(List<Module> createdModules) {
      this.createdModules = createdModules;
   }

   public List<Language> getCreatedLanguages() {
      return createdLanguages;
   }

   public void setCreatedLanguages(List<Language> createdLanguages) {
      this.createdLanguages = createdLanguages;
   }
}

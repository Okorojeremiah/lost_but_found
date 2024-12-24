//package com.winners.lostbutfound.models;
//
//import jakarta.validation.constraints.Email;
//import jakarta.validation.constraints.NotBlank;
//import lombok.AllArgsConstructor;
//import lombok.Builder;
//import lombok.Getter;
//import lombok.NoArgsConstructor;
//import org.springframework.data.annotation.Id;
//import org.springframework.data.mongodb.core.mapping.DBRef;
//import org.springframework.data.mongodb.core.mapping.Document;
//
//import java.time.LocalDate;
//
//@Document
//@AllArgsConstructor
//@NoArgsConstructor
//@Getter
//@Builder
//public class ItemOwner {
//
//    @Id
//    private String id;
//
//    @NotBlank(message = "First name cannot be blank")
//    private String firstName;
//
//    @NotBlank(message = "Last name cannot be blank")
//    private String lastName;
//
//    @Email(message = "Please provide a valid email")
//    @NotBlank(message = "Email cannot be blank")
//    private String email;
//
//    @NotBlank(message = "Phone number cannot be blank")
//    private String phoneNumber;
//
//    private String imageUrl;
//
//    private String itemId;
//}

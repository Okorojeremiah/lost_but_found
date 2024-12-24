package com.winners.lostbutfound.models;

import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.Objects;

import static com.winners.lostbutfound.models.ItemStatus.NOT_CLAIMED;

@Document
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class Item {

    @Id
    private String id;

    @NotBlank(message = "Item name cannot be blank")
    private String name;

    private Integer itemNumber;

    @NotBlank(message = "location field cannot be blank")
    private String locationFound;

    @NotBlank(message = "description cannot be blank")
    private String description;

    @Setter
    private ItemStatus itemStatus = NOT_CLAIMED;

    private LocalDate dateFound;

    @NotBlank(message = "Finder name cannot be blank")
    private String finderName;

    @NotBlank(message = "Finder email cannot be blank")
    private String finderEmail;

    @NotBlank(message = "Finder phone number cannot be blank")
    private String finderPhoneNumber;

    @Setter
    @NotBlank(message = "Owner first name cannot be blank")
    private String ownerFirstName;

    @Setter
    @NotBlank(message = "Owner last name cannot be blank")
    private String ownerLastName;

    @Setter
    @NotBlank(message = "Owner email cannot be blank")
    private String ownerEmail;

    @Setter
    @NotBlank(message = "Owner phone number cannot be blank")
    private String ownerPhoneNumber;

    @NotBlank(message = "Image url cannot be blank")
    private String image;

    @Setter
    @NotBlank(message = "Image url cannot be blank")
    private String ownerImage;

    @Setter
    @NotBlank(message = "Admin name cannot be blank")
    private String adminName;

    @Setter
    private LocalDate claimDate;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Item item = (Item) o;
        return Objects.equals(id, item.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}

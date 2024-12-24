package com.winners.lostbutfound.controllers;

import com.winners.lostbutfound.dtos.*;
import com.winners.lostbutfound.exceptions.EmptyFileException;
import com.winners.lostbutfound.models.Item;
import com.winners.lostbutfound.services.ItemService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("api/v1/item")
public class ItemController {

    private final ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> registerItem(
            @RequestPart("itemRegistrationRequest") ItemRegistrationRequest itemRegistrationRequest,
            @RequestPart("imageFile") MultipartFile imageFile) throws IOException {
        return ResponseEntity.ok(itemService.registerItem(itemRegistrationRequest, imageFile));
    }
    @Operation(summary = "Get an Item by number", description = "Return a single Item")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Item found"),
            @ApiResponse(responseCode = "404", description = "Item not found")
    })

    @GetMapping("/{itemNumber}")
    public ResponseEntity<Item> getItem(@PathVariable("itemNumber") Integer itemNumber){
        return ResponseEntity.ok(itemService.findByItemNumber(itemNumber));
    }

    @GetMapping("/items")
    public Page<Item> getItems(@RequestParam(value = "page", defaultValue = "0") int page,
                               @RequestParam(value = "size", defaultValue = "10") int size){
        Pageable pageable = PageRequest.of(page, size);
        return itemService.findAllItem(pageable);
    }

    @GetMapping("/all")
    public ResponseEntity<List<Item>> getAllItem(){
        return ResponseEntity.ok(itemService.findAllItem());
    }

    @GetMapping("/search")
    public ResponseEntity<List<ItemDTO>> search(@RequestParam String searchQuery){
        return ResponseEntity.ok(itemService.searchItem(searchQuery));
    }

    @PostMapping("/claim")
    public ResponseEntity<ClaimItemResponse> claim(
            @RequestPart("claimRequest") ClaimRequest claimRequest,
            @RequestPart("imageFile") MultipartFile imageFile) throws IOException {
        if (imageFile.isEmpty()){
            throw new EmptyFileException("image file is empty, please choose another file");
        }
        return ResponseEntity.ok(itemService.claimItem(claimRequest, imageFile));
    }
}

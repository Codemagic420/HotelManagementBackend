package com.kea.hotel.hotelbackend.controller;

import com.kea.hotel.hotelbackend.model.Guest;
import com.kea.hotel.hotelbackend.service.GuestService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/guests")
public class GuestController {

    private final GuestService service;

    public GuestController(GuestService service) {
        this.service = service;
    }

    @GetMapping
    public List<Guest> getAllGuests() {
        return service.findAll();
    }

    @PostMapping
    public Guest createGuest(@RequestBody Guest guest) {
        return service.save(guest);
    }

    @DeleteMapping("/{id}")
    public void deleteGuest(@PathVariable Long id) {
        service.delete(id);
    }
}

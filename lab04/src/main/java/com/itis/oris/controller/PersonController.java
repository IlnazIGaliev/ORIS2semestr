package com.itis.oris.controller;

import com.itis.oris.model.Person;
import com.itis.oris.model.Phone;
import com.itis.oris.service.PersonService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/persons")
public class PersonController {

    private final PersonService personService;

    public PersonController(PersonService personService) {
        this.personService = personService;
    }

    @GetMapping("/new")
    public String createPage(Model model) {
        model.addAttribute("person", new Person());
        return "person_create";
    }

    @PostMapping
    public String createPerson(
            @RequestParam("name") String name,
            @RequestParam("phoneNumber") String phoneNumber
    ) {

        Person person = new Person();
        person.setName(name);

        Phone phone = new Phone();
        phone.setNumber(phoneNumber);

        person.setPhone(phone);

        personService.save(person);

        return "redirect:/persons";
    }

    @GetMapping
    public String personsPage(Model model) {
        model.addAttribute("persons", personService.findAll());
        return "persons";
    }
}

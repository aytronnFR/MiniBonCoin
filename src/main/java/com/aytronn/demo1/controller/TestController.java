package com.aytronn.demo1.controller;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/test")
public class TestController {

  @GetMapping
  public String test() { //USER, EDITOR, ADMIN
    return "test::getAll";
  }

  @GetMapping("/{id}")
  public String test2(@PathVariable String id) { //USER, EDITOR, ADMIN
    return "test::getById : " + id;
  }

  @PostMapping
  public String create() { //EDITOR, ADMIN
    return "test::create";
  }

  @PutMapping("/{id}")
  public String update(@PathVariable String id) { //EDITOR, ADMIN
    return "test::update : " + id;
  }

  @DeleteMapping("/{id}")
  public String delete(@PathVariable String id) { //ADMIN
    return "test::delete : " + id;
  }
}

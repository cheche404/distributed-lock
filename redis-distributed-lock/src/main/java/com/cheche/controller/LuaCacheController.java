package com.cheche.controller;

import com.cheche.lru.LRUService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author cheche
 * @date 2023/2/8
 */
@RestController
@RequestMapping("/lua")
public class LuaCacheController {

  private final LRUService luaService;

  public LuaCacheController(LRUService luaService) {
    this.luaService = luaService;
  }

  @RequestMapping("put")
  private String put(@RequestParam Integer key, @RequestParam Integer value) {
    luaService.put(key, value);
    return "ok";
  }

  @RequestMapping("get")
  private Integer get(@RequestParam Integer key) {
    return luaService.get(key);
  }

  @RequestMapping("print")
  private String print() {
    return luaService.print();
  }


}

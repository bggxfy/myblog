package com.bgg;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;

@SpringBootTest
class MyblogApplicationTests {

    @Test
    void contextLoads() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name","bgg");
        map.put("age","21");
        map.put("school","cqut");
        map.put("major","computer");
        //new
        String age = map.get("age");
        System.out.println("age = "+age);

        map.remove("major");
        System.out.println(map);
    }

}

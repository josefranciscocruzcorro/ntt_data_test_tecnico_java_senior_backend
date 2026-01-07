package com.example.tests;

import com.intuit.karate.junit5.Karate;

class IntegrationTest {
    
    @Karate.Test
    Karate testAll() {
        return Karate.run("classpath:com/example/tests/e2e.feature");
    }
}

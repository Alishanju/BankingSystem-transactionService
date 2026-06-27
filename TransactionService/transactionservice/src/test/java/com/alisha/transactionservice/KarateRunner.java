package com.alisha.transactionservice;

import com.intuit.karate.junit5.Karate;

//mvn test -Dtest=KarateRunner
class KarateRunner {

    @Karate.Test
    Karate runAllTests() {

        return Karate.run(
                "classpath:karate/login.feature",
                "classpath:karate/create-transaction.feature");

    }
}
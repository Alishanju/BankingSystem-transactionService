function fn() {

    var config = {

        customerUrl: 'http://localhost:8081',

        transactionUrl: 'http://localhost:8083'

    };

    karate.log(config);

    return config;

}
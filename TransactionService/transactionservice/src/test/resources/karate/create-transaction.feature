Feature: Create Transaction

Background:

* url customerUrl

Given path '/auth/login'

And request

"""
{
    "username":"Nemalu",
    "password":"Nemalu@786"
}
"""

When method POST

Then status 200

* def token = response.token

Scenario: Create Transaction

Given url transactionUrl + '/api/transactions'

And header Authorization = 'Bearer ' + token

And request

"""
{
    "customerId":2,
    "amount":5000,
    "transactionType":"DEPOSIT",
    "description":"Salary"
}
"""

When method POST

Then status 200

And match response.customerId == 2

And match response.status == "SUCCESS"

And print response
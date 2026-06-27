Feature: Login API

Scenario: Login successfully

Given url customerUrl + '/auth/login'

And request

"""
{
    "username":"Nemalu",
    "password":"Nemalu@786"
}
"""

When method POST

Then status 200

And print response

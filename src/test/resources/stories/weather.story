
Feature: As a user I'd like to check weather in the any sity in the world

Scenario: 4160 verify weather by GET request for cities
Meta:
@Winter
@testrail:4160
When WS-User send GET request to endpoint '<city>'
Then WS-User receive HTTP response with status code '<statusCode>'
And in Response body attribute city should be equal '<value>'
Examples:
| city                   |  value                                                 | statusCode  |
| London                 |  London                                                | 200         |
| Kyiv                   |  Kyiv                                                  | 200         |
| NonExistingCity        |  An internal error occured while servicing the request | 400         |


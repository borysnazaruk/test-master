Meta:
@MSVS-168

Narrative:
 As a MFC manager I'd like to review info about current batches
 When I request info about batches for today
 I should receive list of batches with detailed information

Scenario: MSVC-168 verify GET request for batches
Meta:
@tangerine
When WS-User send GET request to endpoint '/batch'
Then WS-User receive HTTP response with status code '200'
And Response body should not be NULL

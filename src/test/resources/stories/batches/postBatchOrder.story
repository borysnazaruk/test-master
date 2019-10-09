Meta:
@MSVS-151

Narrative:
 As a MFC manager I'd like to have preliminary batch order
 When new orders for a given cutoff are posted
 I should see them in correspondent preliminary batch order

Scenario: MSVC-151 verify POST order request for batches
Meta:
@tangerine
When WS-User send POST request to endpoint 'customer-order'
Then WS-User receive HTTP response with status code '200'
And Response body should contains status 'processing' and order id


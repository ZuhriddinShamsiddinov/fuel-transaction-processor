# Portfolio screenshots checklist

Capture these after `docker compose up --build` is healthy:

1. **Swagger UI** — open http://localhost:8080/swagger-ui.html  
   Show the endpoint list (webhooks, transactions, drivers).

2. **Successful webhook** — terminal with the README curl returning `HTTP/1.1 202 Accepted`.

3. **Transaction list** — browser or curl:
   `GET http://localhost:8080/api/v1/transactions` with a processed row.

4. **Optional WebSocket** — connect to `ws://localhost:8080/ws/transactions` and show a live event after a webhook.

Save PNGs into this folder as:
- `01-swagger-ui.png`
- `02-webhook-202.png`
- `03-transactions-api.png`

Use them as Upwork portfolio cover / gallery images.

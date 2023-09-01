## Cards Service
API to Manage Task Cards

## Tech Stack
- Java 17
- Spring boot 3.1.3
- MySQL
- Libraries: Lombok, MapStruct
- Actuator for Health Checks
- Swagger for API Documentation

## Running the service
- Clone the repo
- Update Database properties(host, port, password & DB name) from application.properties file
- Run the service directly from Intellij or from the project root, run: ``mvn spring-boot:run`` requires maven
- Execute insert statements below to add users to the DB:
    ```
    INSERT INTO `users` VALUES (1, 'member@test.com', 'Member', 'Member', '$2a$10$1aALnut/l1sG53TQ3wRN.usZDzeyVybzuRowkM.CA60.vdleS7mYa', 'MEMBER');
    INSERT INTO `users` VALUES (2, 'admin@test.com', 'Admin', 'Admin', '$2a$10$1aALnut/l1sG53TQ3wRN.usZDzeyVybzuRowkM.CA60.vdleS7mYa', 'ADMIN');
    INSERT INTO `users` VALUES (3, 'test@test.com', 'Member', 'Member', '$2a$10$1aALnut/l1sG53TQ3wRN.usZDzeyVybzuRowkM.CA60.vdleS7mYa', 'MEMBER');
    ```
- Import the postman collection file ``cards-logicea.postman_collection.json`` and run the login request
  Alternatively: execute curl below 
  ```curl --location 'http://localhost:8080/api/1.0/login' \
  --header 'Content-Type: application/json' \
  --data-raw '{
  "email": "member@test.com",
  "password": "Test!123"
  }'```
- To add card, from the postman collection, update the authorization token for add-card request and click on Send button
  or Use curl command below:
  ```curl --location 'http://localhost:8080/api/1.0/cards' \
    --header 'Content-Type: application/json' \
    --header 'Authorization: Bearer eyJhbGciOiJIUzUxMiJ9.eyJpZCI6MSwicm9sZSI6WyJNRU1CRVIiXSwic3ViIjoibWVtYmVyQHRlc3QuY29tIiwiaWF0IjoxNjkzNTAwMzM5LCJleHAiOjE2OTM1MDM5Mzl9.PWZIbPPna-ztMwV-mn8zTHHAMTSx25rViWVTBt45mZyLg6ugfs2AqipRr1FvzrLwW95DJX63amdA6cNlH5FYBQ' \
    --data '{
    "name": "Team building",
    "color": "#FFFFFF",
    "description": "Plan for team building"
    }' ```
**Note:** Authorization should be updated

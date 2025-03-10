
# Ecommerce Microservice

#### Microservices
- User service
- Inventory service
- Order service
- Payment service
- Notification service

#### Technologies
- Spring Boot
- Event-Driven Architecture with Kafka
- Caching with Redis
- Security with Spring Security
- Database (PostgreSQL)
- Identity and Access Management with Keycloak

#### Diagram
![Ecommerce Microservice Diagram](/doc/diagram.jpg)

#### Setup
Open terminal and run 
```docker-compose up -d```  
All the dependent services (PostgreSQL, Zookeeper, Kafka, Keycloak, Redis) will start running in Docker.  

#### Setup Keycloak
- Open a browser and go to "http://localhost:9090/admin/master/console/"
- Login with username and password defined in docker-compose file (here, username: admin, password: admin)
- Create a new Realm "ecommerce-springboot-realm"
- Add Realm role "ADMIN" and "USER"
- Create a Client "user-service"
- Enable "Client authentication", "Authorization", "Direct access grants", "Service account roles"
- Go to "Service account roles" and assign role "realm-admin", "manage-users" from realm-management
- Create another Client "order-service" in the same way
- Assign role "realm-admin" from realm-management and "ADMIN" from Realm roles.
- Add a new user and assign role "ADMIN"

#### API Documentation
After running all services, go to http://localhost:8080/swagger-ui.html to get all API documentation.
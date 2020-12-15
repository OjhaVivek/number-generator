Description: Project generated numbers in decreasing order starting from the goal and decreasing by step size each time until it is greated than or equal to 0. Input is provided to the project via APIs. APIs are used to query task completion status and numbers list generated.
Technologies: Spring Boot, Kafka, Cassandra and Docker

Testing in local

1. Checkout the code
2. Run sudo docker-compose up -d or docker-compose up -d to bring up the application (This app uses zookeeper, kafka and cassandra ports. Make sure they're available)
 ![Settings Window](https://github.com/OjhaVivek/number-generator/blob/master/assets/Screenshot%20from%202020-12-15%2020-28-07.png)
 ![Settings Window](https://github.com/OjhaVivek/number-generator/blob/master/assets/Screenshot%20from%202020-12-15%2020-29-12.png)
3. Open postman or any other rest client. Follow screenshots (in sequence) to learn how to hit APIS and get responses.
 
 Welcome Message API
 ![Settings Window](https://github.com/OjhaVivek/number-generator/blob/master/assets/Screenshot%20from%202020-12-15%2020-24-29.png)
 
 Single Task Generation API
 ![Settings Window](https://github.com/OjhaVivek/number-generator/blob/master/assets/Screenshot%20from%202020-12-15%2020-24-58.png)
 
 Task status API for the single task generated
 ![Settings Window](https://github.com/OjhaVivek/number-generator/blob/master/assets/Screenshot%20from%202020-12-15%2020-26-03.png)
 
 Number List API for the single task generated
 ![Settings Window](https://github.com/OjhaVivek/number-generator/blob/master/assets/Screenshot%20from%202020-12-15%2020-27-02.png)
 
 Bulk Task Generation API
 ![Settings Window](https://github.com/OjhaVivek/number-generator/blob/master/assets/Screenshot%20from%202020-12-15%2020-42-48.png)
 
 Task Status for Bulk tasks generated
 ![Settings Window](https://github.com/OjhaVivek/number-generator/blob/master/assets/Screenshot%20from%202020-12-15%2020-43-05.png)
 
 Numbers list for the bulk tasks generated
 ![Settings Window](https://github.com/OjhaVivek/number-generator/blob/master/assets/Screenshot%20from%202020-12-15%2020-43-46.png)

My current project is not working well - has some issues parsing and tokenizing the user's input. I guess the problem lies in the parser file and the service file that has the main logic of taking the data calling parser function and then after tokanizing return the AST node. I will have to work with the logics and make updates.
So currently this project doesn't works as intended due to some logical error that needs instant fixes. This project was an assignment from a company, so after their evaluation process ends, I will commit changes asap.


Rule Engine with Abstract Syntax Tree (AST)
--
* Overview

This project is a rule-based engine designed to evaluate user eligibility based on configurable rules using an Abstract Syntax Tree (AST) structure. The rule engine processes eligibility conditions for users stored in a MySQL database, allowing flexible rule configurations for user attributes. The solution is structured with scalability and production-readiness in mind, leveraging the Model-View-Controller (MVC) architecture.


---
* Table of Contents

-> Project Structure

-> Features

-> Technologies Used

-> Setup and Installation

-> Testing

-> Future Improvements


---

* Project Structure :

This project uses the MVC (Model-View-Controller) pattern to enhance scalability, maintainability, and testability. The MVC structure includes:


-> Model: Defines data models and the database schema for users and eligibility rules.

-> View: Minimal UI to demonstrate evaluation results.

-> Controller: Manages rule evaluations, API calls, and processes AST-based rule parsing.


---


* Features :

-> Dynamic Rule Creation: Enables the creation of rules to evaluate user attributes in real time using AST.

-> User Eligibility Evaluation: Fetches and evaluates data from the users table in MySQL based on provided rules.

-> Data Persistence: Rules and configurations are stored persistently, allowing rule reusability across sessions.

-> User Evaluation: Evaluates all users in the database based on the specified rule, with no direct UI for adding user data in this version.

---
* Technologies Used :

-> Java 21: Core language for application logic.

-> Spring Boot: Framework for application development.

-> MySQL: Database for storing user data and rule configurations.

-> Thymeleaf: Template engine for basic frontend displays.

-> Maven 3.9.9: For project build and dependency management.

---
* Setup and Installation :

-> Prerequisites -

* Java 17 or higher


* Maven (for managing dependencies)


* MySQL (for persistent data storage)


* Git Version Control

-> Instructions -

Clone the repository : bash command -
git clone https://github.com/Choudhary-jii/rule-engine


-> Configure MySQL:

* Update MySQL credentials(username & password) in application.properties and ensure a database named 'rule_engine_db' exists.


* The application.properties file path is: src/main/resources/application.properties.


-> Run the Application:

* bash code -
mvn clean install

mvn spring-boot:run


*  Access the Application:


-> Open your browser and go to http://localhost:8080 to test rule evaluation.

---



*  Testing : 


-> Unit Testing -  Includes tests for rule evaluation and parsing logic to verify AST-based processing.


-> Integration Testing: Validates interactions between different project components, including MySQL, rule evaluation, and API endpoints.

---
* Future Improvements :

->  Frontend for User Data Input: Plan to add a UI for user data management to enhance rule evaluation and database interaction.


---
License
This project is open-source and available under the MIT License.


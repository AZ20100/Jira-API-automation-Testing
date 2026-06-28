# Jira REST API Automation Framework

## 📌 Overview
A REST API automation framework for Jira Cloud using 
RestAssured, TestNG, and Allure Reports.

## 🛠️ Tech Stack
- Java 17
- REST Assured 5.4.0
- TestNG 7.9.0
- Allure Reports 2.25.0
- Maven
- Jackson Databind

## ✅ Test Coverage

### Issues Management
- Create Issue
- Get Issue by Key
- Update Issue
- Delete Issue

### Comments Lifecycle
- Add comment
- Verify comment exists
- Edit comment
- Delete comment

### Assignee Management
- Assign issue to user
- Set assignee to null
- Assign to invalid user (negative)

### Transitions
- Fetch available transitions
- Transition to In Progress
- Verify status is In Progress
- Transition to Done

### Negative Scenarios
- Add comment to non-existent issue
- Force invalid transition
- Assign to invalid user

## 📂 Project Structure
src/
├── main/java/
└── test/java/
    └── com/testing/base/
        ├── JiraBaseTest.java
        └── JiraIssuesTest.java

## ▶️ How to Run

### Run Tests
mvn clean test

### Generate Allure Report
allure generate target/allure-results --clean -o target/allure-report

### Open Report
allure open target/allure-report

## ⚙️ Configuration
Create src/test/resources/config.properties:
BASE_URL=https://yoursite.atlassian.net/
EMAIL=your@email.com
API_TOKEN=your_api_token

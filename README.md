# Selenium BDD Restful Booker Framework

A test automation framework built with Selenium, Cucumber BDD, and REST Assured targeting the [Restful Booker Platform](https://automationintesting.online).

## Tech Stack

- **Java 17**
- **Selenium 4** - UI automation
- **Cucumber 7** - BDD with Gherkin feature files
- **REST Assured 5** - API testing
- **JUnit 5** - Test runner
- **WebDriverManager** - Automatic driver management
- **Allure** - Test reporting
- **Maven** - Build and dependency management
- **GitHub Actions** - CI/CD pipeline

## Project Structure

    src
    ├── main/java/com/markatreats
    │   ├── page_objects/        # Page Object classes
    │   └── utils/               # Custom Selenium wrappers
    └── test
        ├── java
        │   ├── runner/          # Cucumber test runner
        │   └── steps/           # Step definitions and hooks
        └── resources
            ├── features/        # Gherkin feature files
            └── junit-platform.properties

## Test Coverage

### API Tests (REST Assured)
- ✅ Authentication
- ✅ GET all bookings
- ✅ POST create booking
- ✅ GET booking by ID
- ✅ PUT update booking
- ✅ DELETE booking

### UI Tests (Selenium) - Coming Soon
- Room booking flow
- Admin panel management

## Running Tests

### API tests locally
    mvn test -P on-local

### Full suite locally
    mvn test -P on-local-suite

### Parallel execution
    mvn test -P parallel

### Headless CI
    mvn test -P ci

## Browser Support

Specify browser via system property (defaults to Chrome):

    mvn test -Dbrowser=chrome
    mvn test -Dbrowser=firefox
    mvn test -Dbrowser=edge

## CI/CD

GitHub Actions workflow runs on every push and pull request to main. Allure results are uploaded as artifacts on every run.

## Custom Selenium Wrappers

The bddFunctions utility class provides production-grade wrappers around core Selenium interactions with built-in explicit waits, stale element handling, and detailed pass/fail logging.

## Author

Mark Canning | [github.com/marcusatreats](https://github.com/marcusatreats)
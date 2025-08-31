# Jenkins Pipeline Test Project

This project contains API tests using TestNG, Rest Assured, and Allure reporting.

## Running Tests

### Using Dynamic Package Selection (Recommended)

The project uses a single `testng.xml` file with dynamic package selection. You can specify which package of tests to run using the `packageToRun` parameter.

**Default (runs all tests):**
```bash
mvn test
```

**Run tests from specific package:**
```bash
# Run tests from t1 package (includes subpackages)
mvn test -DpackageToRun="tests.t1.*"

# Run tests from t2 package (includes subpackages)
mvn test -DpackageToRun="tests.t2.*"

# Run tests from t1.t11 subpackage only
mvn test -DpackageToRun="tests.t1.t11.*"

# Run all tests explicitly
mvn test -DpackageToRun="tests.*"
```

### Test Results Summary

- **Default**: Runs all tests (`tests.*`) - 36 tests
- **t1 package**: Runs tests from `tests.t1.*` (includes t1.t11 subpackage) - 18 tests  
- **t2 package**: Runs tests from `tests.t2.*` - 9 tests
- **t1.t11 subpackage**: Runs tests from `tests.t1.t11.*` only - 6 tests

## Project Structure

```
src/test/java/
├── base/
│   ├── TestBase.java
│   ├── DynamicSuiteFactory.java
│   └── TestRunner.java
├── dataproviders/
│   └── ObjectDataProvider.java
├── listeners/
│   └── AllureListener.java
└── tests/
    ├── GetApiTests.java
    ├── PostApiTests.java
    ├── DeleteApiTests.java
    ├── t1/
    │   ├── GetApiTests.java
    │   ├── PostApiTests.java
    │   ├── DeleteApiTests.java
    │   └── t11/
    │       ├── GetApiTests.java
    │       ├── PostApiTests.java
    │       └── DeleteApiTests.java
    └── t2/
        ├── GetApiTests.java
        ├── PostApiTests.java
        └── DeleteApiTests.java
```

## Test Configuration

- **TestNG suite**: `src/test/resources/testng.xml` (single file with dynamic package selection)
- **Dynamic runner**: `base.TestRunner` (handles package parameter)
- **Suite factory**: `base.DynamicSuiteFactory` (creates TestNG suites dynamically)
- **Test framework**: TestNG
- **HTTP client**: Rest Assured
- **Reporting**: Allure
- **Java version**: 24

## How It Works

1. The `testng.xml` file defines a parameter `packageToRun` with default value `tests.*`
2. Maven passes the `-DpackageToRun` value as a system property
3. The `TestRunner` class reads the system property and creates a dynamic TestNG suite
4. The `DynamicSuiteFactory` creates the TestNG XML suite with the specified package
5. TestNG executes only the tests from the specified package

## Troubleshooting

If no tests are running, check:
1. That test classes are in the correct package structure
2. That test methods are properly annotated with `@Test`
3. Use `mvn test -X` for debug output to see what's happening
4. Check that the package name in `packageToRun` parameter is correct

## Note

The API being tested has a daily rate limit of 100 requests. If you see test failures with status 405 and rate limit messages, the API limit has been reached for the day.

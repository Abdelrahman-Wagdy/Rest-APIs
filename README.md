# REST API Test Automation

A Java 24, REST Assured, TestNG, and Allure demonstration against the Objects API. The repository illustrates JSON-driven test data, a reusable HTTP client, package selection, and Jenkins execution.

## Requirements and setup

- JDK 24 and Maven 3.9 recommended; the project compiles with release 24.
- Network access to the configured API and Maven dependencies.
- Allure CLI for local report viewing.

```bash
git clone https://github.com/Abdelrahman-Wagdy/Rest-APIs.git
cd Rest-APIs
mvn clean test
```

Review `src/test/resources/config/endpoints.properties` before running. Tests create and delete demo objects. Public API availability and request quotas can affect results; inspect the response body instead of assuming every error is a product defect.

## Select tests

The supported property is **testPackage**:

```bash
mvn clean test -DtestPackage="tests.*"
mvn clean test -DtestPackage="tests.t1.*"
mvn clean test -DtestPackage="tests.t1.t11.*"
mvn clean test -DtestPackage="tests.t2.*"
mvn clean test -DtestPackage="tests.t2.*" -Dgroups=smoke
```

Without a group filter, grouped and ungrouped tests are eligible to run. With `-Dgroups=smoke`, TestNG applies group selection and dependency rules.

Maven filters `${testPackage}` in `src/test/resources/testng.xml` into `target/test-classes/testng.xml`. Surefire then reads that generated suite. Do not run the unfiltered source XML directly expecting Maven substitution.

There is no dynamic suite factory or custom runner in this repository. The similar classes in `tests`, `tests.t1`, `tests.t1.t11`, and `tests.t2` demonstrate package selection; they are not four different API feature sets. Exact invocation counts depend on selection and data-provider expansion.

## Code map

| Location | Responsibility |
| --- | --- |
| `src/main/java/config/ConfigManager.java` | API configuration |
| `src/main/java/http/RestClient.java` | HTTP request helpers |
| `src/main/java/models/ObjectPayload.java` | Request model |
| `src/main/java/utils/JsonUtils.java` | JSON utilities |
| `src/test/java/base/TestBase.java` | Shared setup and status assertion |
| `src/test/java/dataproviders/ObjectDataProvider.java` | JSON-backed input data |
| `src/test/java/listeners/AllureListener.java` | TestNG reporting listener |
| `src/test/java/tests/` | GET, POST, and DELETE test examples and package variants |
| `src/test/resources/testdata/create_objects.json` | Input fixtures |

## Reports

```bash
allure serve target/allure-results
```

Surefire reports are written to `target/surefire-reports`. Inspect both the assertion and API response when diagnosing failures. An Allure annotation alone is not proof that an attachment was recorded; verify generated report content.

## Jenkins

Configure a Pipeline from SCM using this repository's `Jenkinsfile` and select the branch in the job's SCM settings. The agent needs tools named `Maven 3.9.11` and `temurin-24`, plus the Allure plugin and a configured Allure installation. Email Extension and SMTP are needed only when email is enabled.

Parameters:

- `FILES`: suite filenames under `src/test/resources`, comma-separated; blank uses `testng.xml`.
- `PACKAGES`: package selector; blank uses `tests.*`.
- `GROUPS`: optional group filter; blank leaves groups unfiltered.
- `EMAIL_TO`: optional recipients; blank disables email.

Selected suites run in one Maven invocation so `clean` does not erase an earlier suite's reports. The pipeline uses the filtered XML files, archives reports even after test failure, and publishes Allure results when available. Test failures retain Maven's nonzero exit status.

## Next improvements

- Consolidate duplicated test classes once package-selection examples are no longer needed.
- Clean up every object created by POST tests, including after assertion failures.
- Replace permissive status assertions with checks tied to an agreed API contract.
- Add schema checks and a controlled local API fixture for deterministic execution.
- Add a documented asynchronous job example separately; this repository currently demonstrates synchronous HTTP requests.

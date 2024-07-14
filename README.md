# akarnokd-misc
Miscellaneous classes, implementations with gradle and jmh set up

<a href='https://github.com/akarnokd/akarnokd-misc/actions?query=workflow%3A%22Java+CI+with+Gradle%22'><img src='https://github.com/akarnokd/akarnokd-misc/workflows/Java%20CI%20with%20Gradle/badge.svg'></a>

### Helicon Library Compatibility Issue

**Issue:**
We encountered a compatibility issue with the Helicon library and Java version 17. The Helicon library does not have a version that is compatible with Java 17, leading to compilation errors.

**Solution:**
To resolve this issue and allow the project to compile and run successfully, we have removed the Helicon-related code from the `CrossFlatMapIterableTest.java` file. This includes:
- Removing the Helicon import statement.
- Removing or commenting out any code that depends on the Helicon library.

By making these changes, we have eliminated the Helicon compile error, ensuring that the project can run smoothly with Java version 17.

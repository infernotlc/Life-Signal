GyroApp
GyroApp is a mobile application developed for capturing and storing gyroscope data on Android devices. It leverages the Android Sensor API to monitor gyroscope sensor readings and persists the data using Room, a local SQLite database.

Features
Gyroscope Data Capture: The app continuously captures gyroscope sensor data, including X, Y, and Z-axis values, providing insights into device orientation and movement.

Real-time Display: View real-time gyroscope sensor readings on the user interface, allowing users to observe changes as they occur.

Technologies Used
Kotlin: The app is written in Kotlin, a modern programming language for Android development.

Room Database: Room is employed for local data storage, ensuring efficient and persistent gyroscope data management.

Coroutines: Leveraging Kotlin Coroutines for asynchronous and responsive data processing.

Android Architecture Components: GyroApp leverages various Android Architecture Components, including ViewModel and LiveData, for robust and lifecycle-aware data management.

Sensor API: The Android Sensor API is used for accessing device sensor data, providing insights into gyroscope readings and other sensor information.

RecyclerView: GyroApp utilizes RecyclerView to efficiently display and manage lists of gyroscope data in the user interface.

Material Design Components: The app follows the principles of Material Design, incorporating Material Design Components for a cohesive and visually appealing user experience.

Getting Started
To run the GyroApp project locally, follow these steps:

Clone the repository: git clone https://github.com/infernotlc/GyroApp.git
Open the project in Android Studio.
Build and run the application on an Android device or emulator.
Feel free to explore the codebase, contribute, and provide feedback!

Future Enhancements
GyroApp is an evolving project with plans for future enhancements, including:

Integration of additional sensors (e.g., accelometer + gyroscope) for a more comprehensive data collection.
We welcome contributions and collaboration to make GyroApp even better! If you encounter issues or have ideas for improvements, please open an issue or submit a pull request.


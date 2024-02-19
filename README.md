# Local Explorer
Local Explorer is a web application built with Spring Boot and React. It provides a platform for exploring local places and attractions.


## Features

- Search for local places and attractions nearby.
- View details about each place, including location, and photos.
- Integration with external APIs for additional data, such as weather forecasts.

## Prerequisites

Before running the application, ensure you have the following installed:

- Java Development Kit (JDK)
- Node.js and npm (Node Package Manager)
- Maven

## Getting Started

To run the Local Explorer Application, follow these steps:

1. Clone the repository to your local machine:

    ```
    git clone https://github.com/your-username/local-explorer.git
    ```

2. Navigate to the project directory:

    ```
    cd local-explorer
    ```

3. Set up secrets:
   
    - Create a file named `secrets.txt` in the root directory of the project.
    - Add the API keys for the OpenAO, OpenWeatherMap and Google APIs in the following format:

        ```
        APIKEY_OPENAI: YOUR_OPENAI_API_KEY
        APIKEY_OPENWEATHER: YOUR_OPENWEATHER_API_KEY
        APIKEY_GOOGLE: YOUR_GOOGLE_API_KEY
        ```

4. Run the `run-local-exporter` script to set up secrets and start the application:

    - On Windows: Double-click the `run-local-exporter.bat` file.

5. Access the application in your web browser:

    ```
    http://localhost:3000
    ```
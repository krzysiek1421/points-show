# Spring-Based Quiz Points Display Application

## Project Description

This application is designed as a companion to my quiz program, aiming to simplify the visualization and management of scores during quiz events. It is a straightforward yet effective Spring-based application whose primary function is to display and update scores in real-time.

## Key Features

- **Real-Time Score Updates**: The application receives and displays score updates through a Spring server, ensuring up-to-date information about the game status.
- **Integration with Quiz Program**: Seamlessly integrated with a dedicated quiz program, this application is perfect for use during various events such as knowledge competitions or interactive games.
- **Server-Sent Events (SSE) Technology**: Utilizing SSE, the application can continuously receive updates from the server, allowing dynamic refresh of scores without manual page reloads.

## Motivation

This project serves as a showcase of my skills in developing web applications using the Spring Framework. It is designed to be simple in use yet efficient and reliable in event environments.

## Behind the Scenes

A client (e.g., a score display) maintains an open SSE connection with the Spring server, which sends current score updates. An additional client (admin panel) can send score update requests to the server, which then relays this information to the score display in real-time.

## Technologies

- **Spring Framework**: The backbone of the application, providing the backend and SSE logic.
- **JavaScript**: Used on the client-side to handle SSE and dynamically display data.
- **HTML/CSS**: A clean and straightforward user interface for displaying scores.

## Launch Instructions

### 1. Run the command below to build the docker image:
docker build -t points-show .

### 2. Run the Docker Container:
docker run -p 8080:8080 points-show

### 3. Access the app via your localhost at port 8080 with endpoint /get-sample

## Additional Note

While it was possible to integrate this application directly with my quiz program project, I chose not to showcase that integration here. My aim is to keep my innovative ideas for the quiz program confidential. However, this application offers an insight into my approach and methodology in handling such functionalities.

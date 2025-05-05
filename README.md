#  Assigment 1 - 2048 AI
To be able to run the program, all that is needed is python with the pygame packange installed.
You can download python from python.org and pygame can be installed with the command:
```
pip install pygame
```
To run the program, execute the game.py file:
```
python game.py
```

Word of advice:
Run game at depth 8 or more at your own risk. Computer might crash unless quantum computing is used

# Assignment 2 – Belief Base

This project implements a belief base with support for logical entailment and belief revision using resolution.

## How to Run the Project

### Option 1: Run with Maven

Make sure you have **Java** and **Maven** installed.

Navigate to the following directory from the root of this project:

```bash
cd Assignment_2/agent
```

Then run:

```bash
mvn exec:java -Dexec.mainClass="com.logic.App"
```

### Option 2: Run Manually with Java

Navigate to the following directory from the root of this project:

```
cd Assignment_2/agent/src/main/java/com/logic
```

Then compile all Java source files and run the application:

```bash
javac model/*.java view/*.java controller/*.java *.java && java App.java
```


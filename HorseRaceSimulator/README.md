# Horse Race Simulator

A horse-racing simulation with built-in betting mechanics, offering both a text-based console version and a Swing-powered GUI version.

## Features
- **Two Modes**  
  - **Console Mode**: Fully text-based, run in your terminal.  
  - **GUI Mode**: Interactive Swing interface with animated horses and visual betting.
- **Betting System**  
  - Place bets on any horse before the race starts.  
  - Automatic payout and balance updates based on the race outcome.
- **Customizable Race Settings**  
  - Set the number of horses, track length, and horse speed ranges.  
  - Real-time progress updates in the console or GUI.

## Table of Contents
1. [Prerequisites](#prerequisites)  
2. [Installation](#installation)  
3. [Usage](#usage)  
4. [Project Structure](#project-structure)

## Prerequisites
- **Java Development Kit (JDK) 8** or later  
- **Git** (if cloning from GitHub)

> The GUI uses Java Swing, which is bundled with the JDK—no extra dependencies required.

## Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/<YOUR_USERNAME>/HorseRaceSimulator.git
   cd HorseRaceSimulator
   
2. **Compile the project**
while on cmd - directory of HorseSimulator run

javac -d . Part1/*.java Part2/Part2/*.java

3. **Run the application**

To start the Console Mode:
    **java Part1.Race**

This version allows you to enter race settings and place bets via text prompts.
It displays real-time progress in the terminal and automatically settles bets once a winner is declared.

To start the GUI Mode:
    **java Part2.RaceGUI**
    
This version launches an interactive Swing-powered GUI.
You can visually configure race parameters, place bets, and watch the animated race progress with pop-up results.


## Project Structure
HorseRaceSimulator/
├── Part1/
│   ├── Horse.java
│   └── Race.java           // Console version of the race simulation
├── Part2/Part2/
│   ├── ExtendedHorse.java  // Extended horse class with race statistics and performance
│   ├── RacePanel.java      // GUI race simulation panel
│   ├── BettingPanel.java   // GUI betting system panel
│   └── RaceGUI.java        // Main class for the GUI version
└── README.md               // This file
# User Guide

EZ-Schedule is a **desktop application for managing and scheduling of events, optimized for use via a Command Line
Interface** (CLI) while still providing an easy way to visualize all events through a Graphical User Interface (GUI).

## Index

* [Quick start](#quick-start)
* [Features](#features)
    * [Viewing help: `help`](#help)
    * [Adding a event: `add`](#add)
    * [Deleting a event: `delete`](#delete)
    * [Listing all events: `list`](#list)
    * [Retrieving next event: `next`](#next)
    * [Locating all events by keyword: `find`](#find)
    * [Exits the application: `exit`](#exit)
* [FAQ](#faq)
* [Command summary](#command-summary)

## <div id="quick-start"> Quick start </div>

1. Ensure you have Java `11` or above installed in your Computer.
2. Download the latest `EzSchedule.jar` from [here](https://github.com/AY2223S2-CS2103-W17-3/tp/releases).
3. Copy the file to the folder you want to use as the *home folder* for your EzSchedule.
4. Open a command terminal, `cd` into the folder you put the jar file in, and use the `java -jar EzSchedule.jar` command
   to run the application.

## <div id="features"> Features </div>

### <div id="help"> Viewing help: `help` (coming soon) </div>

Shows the user a list of instructions available.

Format: `help`

Example: `help`<br><br>

### <div id="add"> Adding a event: `add` (coming soon)</div>

Adds an event into the Scheduler.

Format: `add <event> /<start time> /<end time>`

Example:

* `add booked tennis court /2023-02-01 /2023-02-02`<br><br>

### <div id="delete"> Deleting a event: `delete` (coming soon)</div>

Deletes an event in the Scheduler.

Format: `delete <event>`

Example:

* `delete booked tennis court `<br><br>

### <div id="list"> Listing all events: `list` (coming soon)</div>

Displays all events currently saved in the Scheduler.

Format: `list`

Example: `list`<br><br>

### <div id="next"> Retrieving next event: `next` (coming soon)</div>

Searches the Scheduler and returns the next upcoming event start time.

Format: `next event`

Example: `next event`<br><br>

### <div id="find"> Locating all events by keyword: `find` (coming soon)</div>

Finds the Scheduler for the task, returns all events containing the relevant keyword.

Format: `find <event>`

Example: `find booked tennis court`<br><br>

### <div id="exit"> Exits the application `exit` (coming soon)</div>

Exits the application.

Format: `exit`

Example: `exit`<br><br>

## <div id="faq"> FAQ </div>

**Q:** On a scale of 1 to 10, how awesome is this product?

**A:** 10<br><br>

## <div id="command-summary"> Command summary </div>

Action |      Format      |                                              Example |
:----- |:----------------:|-----------------------------------------------------:|
Help   |      `help`      |                                               `help` |
Add    |  `add <event>`   |     `add booked tennis court /2023-02-01 /2023-02-0` |
Delete | `delete <event>` |                         `delete booked tennis court` |
List   |      `list`      |                                               `list` |
Next   |   `next event`   |                                         `next event` |
Find   |  `find <event>`  |                           `find booked tennis court` |
Exit   |      `exit`      |                                               `exit` |

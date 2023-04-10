---
layout: page
title: Developer Guide
---
* Table of Contents
{:toc}

## **Introduction**

-------------------------------------------------------------------------------------------------------------------
_Ez-Schedule_ is a **desktop application for managing and scheduling of events, optimized for use via a CLI**
while still providing an easy way to visualize all events through a GUI.

_Ez-Schedule_ will benefit a fast typist who needs to plan and track upcoming events.

### **Acknowledgements**

* This project is based on the AddressBook Level 3 (AB3) project created by the SE-EDU initiative.
* Libraries used: JavaFX, JUnit5

### **Setting Up, Getting Started**

Refer to the guide [_Setting Up and Getting Started_](SettingUp.md).

<div style="page-break-after: always;"></div>

## **Design**

--------------------------------------------------------------------------------------------------------------------
<div markdown="span" class="alert alert-success">
:bulb: **Tip:** 
The `.puml` files used to create diagrams in this document can be found in the 
[diagrams](https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/docs/diagrams/) folder. 
Refer to the [_PlantUML Tutorial_ at se-edu/guides](https://se-education.org/guides/tutorials/plantUml.html) 
to learn how to create and edit diagrams.
</div>

[MainClass]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/Main.java
[MainAppClass]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/MainApp.java
[UiClass]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/ui/Ui.java
[MainWindowClass]:https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/ui/MainWindow.java
[MainWindowView]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/resources/view/MainWindow.fxml
[LogicClass]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/logic/Logic.java
[ModelClass]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/model/Model.java
[StorageClass]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/storage/Storage.java

### **Architecture**

<img src="images/ArchitectureDiagram.png" width="280" />

The **_Architecture Diagram_** given above explains the high-level design of the App.

Given below is a quick overview of main components and how they interact with each other.

**Main components of the architecture**

**`Main`** has two classes called [`Main`][MainClass] and [`MainApp`][MainAppClass].
It is responsible for,
* At app launch: Initializes the components in the correct sequence, and connects them up with each other.
* At shut down: Shuts down the components and invokes cleanup methods where necessary.

[**`Commons`**](#common-classes) represents a collection of classes used by multiple other components.

The rest of the App consists of four components.

* [**`UI`**](#ui-component): The UI of the App.
* [**`Logic`**](#logic-component): The command executor.
* [**`Model`**](#model-component): Holds the data of the App in memory.
* [**`Storage`**](#storage-component): Reads data from, and writes data to, the hard disk.

<div style="page-break-after: always;"></div>

### **How the Architecture Components Interact With Each**

The _Sequence Diagram_ below shows how the components interact with each other for the scenario where the user issues 
the command `delete 1`.

<img src="images/ArchitectureSequenceDiagram.png" width="574" />

Each of the four main components (also shown in the diagram above),

* defines its _API_ in an `interface` with the same name as the Component.
* implements its functionality using a concrete `{Component Name}Manager` class (which follows the corresponding 
  API `interface` mentioned in the previous point).

For example, the `Logic` component defines its API in the `Logic.java` interface and implements its functionality using 
the `LogicManager.java` class which follows the `Logic` interface. Other components interact with a given component 
through its interface rather than the concrete class(reason: to prevent outside component's being coupled to the 
implementation of a component), as illustrated in the (partial) class diagram below.

<img src="images/ComponentManagers.png" width="300" />

<div style="page-break-after: always;"></div>

### **UI Component**

**API** : [`Ui.java`][UiClass]

<img src="images/UiClassDiagram.png" width="1000"/>

The UI consists of a `MainWindow` that is made up of parts e.g.`CommandBox`, `ResultDisplay`, `EventListPanel`, 
`StatusBarFooter` etc. All these, including the `MainWindow`, inherit from the abstract `UiPart` class which captures 
the commonalities between classes that represent parts of the visible GUI.

The `UI` component uses the JavaFx UI framework. 
The layout of these UI parts are defined in matching `.fxml` files that are in the `src/main/resources/view` folder. 
For example, the layout of the [`MainWindow`][MainWindowClass] is specified in [`MainWindow.fxml`][MainWindowView].

The `UI` component,

* executes user commands using the `Logic` component.
* listens for changes to `Model` data so that the UI can be updated with the modified data.
* keeps a reference to the `Logic` component, because the `UI` relies on the `Logic` to execute commands.
* depends on some classes in the `Model` component, as it displays `Event` object residing in the `Model`.

<div style="page-break-after: always;"></div>

### **Logic Component**

**API** : [`Logic.java`][LogicClass]

Here's a (partial) class diagram of the `Logic` component:

<img src="images/LogicClassDiagram.png" width="550"/>

How the `Logic` component works:
1. When `Logic` is called upon to execute a command, it uses the `SchedulerParser` class to parse the user command.
1. This results in a `Command` object (more precisely, an object of one of its subclasses e.g., `AddCommand`) which is executed by the `LogicManager`.
1. The command can communicate with the `Model` when it is executed (e.g. to add a event).
1. The result of the command execution is encapsulated as a `CommandResult` object which is returned back from `Logic`.

The Sequence Diagram below illustrates the interactions within the `Logic` component for the `execute("delete 1")` API call.

![Interactions Inside the Logic Component for the `delete 1` Command](images/DeleteSequenceDiagram-new.png)

<div markdown="span" class="alert alert-info">
:information_source: **Note:** 
The lifeline for `DeleteCommandParser` should end at the destroy marker (X) but due to a limitation of PlantUML,
the lifeline reaches the end of diagram.
</div>

Here are the other classes in `Logic` (omitted from the class diagram above) that are used for parsing a user command:

<img src="images/ParserClasses.png" width="600" />

How the parsing works:
* When called upon to parse a user command, the `SchedulerParser` class creates an `XYZCommandParser` (`XYZ` is a placeholder for the specific command name e.g., `AddCommandParser`) which uses the other classes shown above to parse the user command and create a `XYZCommand` object (e.g., `AddCommand`) which the `AddressBookParser` returns back as a `Command` object.
* All `XYZCommandParser` classes (e.g., `AddCommandParser`, `DeleteCommandParser`, ...) inherit from the `Parser` interface so that they can be treated similarly where possible e.g, during testing.

<div style="page-break-after: always;"></div>

### **Model Component**

**API** : [`Model.java`][ModelClass]

<img src="images/ModelClassDiagram.png" width="450" />

The `Model` component,

* stores the scheduler data i.e., all `Event` objects (which are contained in a `uniqueEventList` object).
* stores the currently 'selected' `Event` objects (e.g., results of a search query) as a separate _filtered_ list 
  which is exposed to outsiders as an unmodifiable `ObservableList<Event>` that can be 'observed' e.g. the UI can be 
  bound to this list so that the UI automatically updates when the data in the list change.
* stores a `UserPref` object that represents the user’s preferences. 
  This is exposed to the outside as a `ReadOnlyUserPref` objects.
* does not depend on any of the other three components (as the `Model` represents data entities of the domain, 
  they should make sense on their own without depending on other components)

<div style="page-break-after: always;"></div>

### **Storage Component**

**API** : [`Storage.java`][StorageClass]

<img src="images/StorageClassDiagram.png" width="550" />

The `Storage` component,
* can save both scheduler data and user preference data in json format, and read them back into corresponding objects.
* inherits from both `SchedulerStorage` and `UserPrefStorage`, which means it can be treated as either one (if only the 
  functionality of only one is needed).
* depends on some classes in the `Model` component (because the `Storage` component's job is to save/retrieve objects that belong to the `Model`)

### **Common Classes**

Classes used by multiple components are in the `ezschedule.commons` package.

<div style="page-break-after: always;"></div>

## **Implementation**

--------------------------------------------------------------------------------------------------------------------
EZ-Schedule mainly follows the Command design pattern. Elaboration on how the application parses commands is given in the
_Architecture_ section above.

This section describes some noteworthy details on how certain features are implemented.

### **Add Command**

[AddCommand.java]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/logic/commands/AddCommand.java
[AddCommandParser.java]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/logic/parser/AddCommandParser.java

For _Add_ command, the noteworthy classes are:
- [`AddCommandParser.java`][AddCommandParser.java] - For parsing the arguments to `AddCommand`.
- [`AddCommand.java`][AddCommand.java] - For execution.

The following exceptions may be thrown during this process, namely:
- ParseException for missing arguments
- ParseException for invalid arguments
- InvalidDateException for correct syntax but invalid (do not exist) dates
- CommandException for identical events
- CommandException for events with clashing time

Given below is an example usage scenario of how the _Add_ command executes.

-- user input --  
Step 1. User executes add command with correct and valid arguments.  

-- `SchedulerParser` --  
Step 2. Returns new `AddCommandParser`.  

-- `AddCommandParser` --   
Step 3. Verify that all argument prefixes are present.  
Step 4. Verify that all argument format is valid.  
Step 5. Verify that the given event start time is before end time.  
Step 6. Returns new `AddCommand`.  

-- `AddCommand` --   
Step 7. Verify that the same event has not already been added.  
Step 8. Verify that the new event to be added does not have time conflict with another event on the same day.  
Step 9. Event is added.  

The execution can be seen in the activity diagram given below.

_Activity Diagram for a typical `add` command_  
![AddCommandActivityDiagram.png](images/AddCommandActivityDiagram.png)

<div style="page-break-after: always;"></div>

### **Recur Command**

[RecurCommand.java]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/logic/commands/RecurCommand.java
[RecurCommandParser.java]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/logic/parser/RecurCommandParser.java

For _Recur_ command, the noteworthy classes are:
- [`RecurCommandParser.java`][RecurCommandParser.java] - For parsing the arguments to `RecurCommand`.
- [`RecurCommand.java`][RecurCommand.java] - For execution.

The following exceptions may be thrown during this process, namely:
- ParseException for missing arguments
- ParseException for invalid arguments
- ParseException for index out of range
- InvalidDateException for correct syntax but invalid (do not exist) dates
- CommandException for end dates in the past
- CommandException for recur factor exceeding max allowable

Given below is an example usage scenario of how the _Recur_ command executes.

-- user input --   
Step 1. User executes Recur command with correct and valid arguments.  

-- `SchedulerParser` --  
Step 2. Returns new `RecurCommandParser`.  

-- `RecurCommandParser` --  
Step 3. Verify that all argument prefixes are present.  
Step 4. Verify that all argument format is valid.  
Step 5. Returns new `RecurCommand`.  

-- `RecurCommand` --  
Step 6. Verify that the given index exist in Ez-Schedule.  
Step 7. Verify that the given recurring end time is not in the past.  
Step 8. For the given recur factor, verify that it is valid.  
Step 9. Check all dates for event to be recurred on for any event clash.  
Step 10. Add event into Ez-Schedule on all dates to be recurred.  

The execution, with Step 9 in further detail, can be seen in the activity diagrams given below.

_Activity Diagram for a typical `recur` command_  
![RecurCommandActivityDiagram.png](images/RecurCommandActivityDiagram.png)

_Activity: Check for time clash for all recurring dates._  
![RecurCommandRecurringAddActivityDiagram.png](images/RecurCommandRecurringAddActivityDiagram.png)

<div style="page-break-after: always;"></div>

### **Edit Command**

[EditCommand.java]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/logic/commands/EditCommand.java
[EditCommandParser.java]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/logic/parser/EditCommandParser.java

For _Edit_ command, the noteworthy classes are:
- [`EditCommandParser.java`][EditCommandParser.java] - For parsing the arguments to `EditCommand`.
- [`EditCommand.java`][EditCommand.java] - For execution.

The following exceptions may be thrown during this process, namely:
- ParseException for missing arguments
- ParseException for invalid arguments
- InvalidDateException for correct syntax but invalid (do not exist) dates
- CommandException for index out of range
- CommandException for identical events
- CommandException for events with clashing time

Given below is an example usage scenario of how the _Edit_ command executes.

-- user input --  
Step 1. User executes edit command with correct and valid arguments.

-- `SchedulerParser` --  
Step 2. Returns new `EditCommandParser`.

-- `EditCommandParser` --   
Step 3. Verify that at least one of the argument prefixes is present.  
Step 4. Verify that provided arguments are valid.     
Step 5. Returns new `EditCommand`.

-- `EditCommand` --   
Step 6. Verify that the given index exist in Ez-Schedule.  
Step 7. Verify that the same event has not already been added.  
Step 8. Verify that the new event to be added does not have time conflict with another event on the same day.  
Step 9. Event is edited.

The execution can be seen in the activity diagram given below.

_Activity Diagram for a typical `edit` command_  
![EditCommandActivityDiagram.png](images/EditCommandActivityDiagram.png)

<div style="page-break-after: always;"></div>

### **Delete Command**

[DeleteCommandParserClass]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/logic/parser/DeleteCommandParser.java
[DeleteCommandClass]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/logic/commands/DeleteCommand.java

For _Delete_ command, the noteworthy classes are:
- [`DeleteCommandParser.java`][DeleteCommandParserClass] - Parse the arguments for `DeleteCommand`
- [`DeleteCommand.java`][DeleteCommandClass] - Execute command

The following exceptions may be thrown during this process, namely:
- `ParseException` for missing arguments
- `ParseException` for invalid arguments
- `CommandException` for index exceeding list size

Given below is an example usage scenario of how the _Delete_ command executes.

-- user input --  
Step 1. User executes `delete` command with multiple valid arguments.  

-- `SchedulerParser` --  
Step 2. Returns new `DeleteCommandParser`.  

-- `DeleteCommandParser` --  
Step 3. Verifies that provided arguments is valid.  
Step 4. Parses provided arguments into `List<Index>`.  
Step 5. Returns new `DeleteCommand`.  

-- `DeleteCommand` -- <br>
Step 6: Sorts and Reverses provided `List<Index>`.  
Step 7: Verifies that none of the `Index` exceeds size of list of `Event`.  
Step 8: Delete the `Event`(s) according the `List<Index>`.  

Other alternative path of execution can be traced in the activity diagram below.

_Activity Diagram for a typical `delete` command_  
![DeleteCommandActivityDiagram.png](images/DeleteCommandActivityDiagram.png)  

<div style="page-break-after: always;"></div>

### **Find Command**

[FindCommand.java]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/logic/commands/FindCommand.java
[FindCommandParser.java]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/logic/parser/FindCommandParser.java

For _Find_ command, the noteworthy classes are:
- [`FindCommandParser.java`][FindCommandParser.java] - For parsing the arguments to `FindCommand`.
- [`FindCommand.java`][FindCommand.java] - For execution.

The following exceptions may be thrown during this process, namely:
- ParseException for missing arguments
- ParseException for invalid arguments
- InvalidDateException for correct syntax but invalid (do not exist) dates

Given below is an example usage scenario of how the _Find_ command executes.

-- user input --  
Step 1. User executes find command with correct and valid arguments.

-- `SchedulerParser` --  
Step 2. Returns new `FindCommandParser`.

-- `FindCommandParser` --  
Step 3. Verify that at least one of the argument prefixes is present.  
Step 4. Verify that provided arguments are valid.  
Step 5. Creates a `FindEventDescriptor`  
Step 6. Returns new `FindCommand.java` using `FindEventDescriptor`.

-- `FindCommand` --  
Step 7. Determine if `Name`, `Date` or both are present from `FindEventDescriptor`.  
Step 8. Create one of the following predicates, depending on the arguments provided: 
`EventMatchesKeywordsAndDatePredicate`, `EventContainsKeywordsPredicate` or`EventMatchesDatePredicate`.  
Step 9. Updates the `ObservableList` using the predicate.

The execution can be seen in the activity diagram given below.

_Activity Diagram for a typical `find` command_  
![FindCommandActivityDiagram.png](images/FindCommandActivityDiagram.png)  

<div style="page-break-after: always;"></div>

### **Next Command**

[ShowNextCommandParserClass]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/logic/parser/ShowNextCommandParser.java
[ShowNextCommandClass]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/logic/commands/ShowNextCommand.java
[UpcomingEventPredicateClass]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/model/event/UpcomingEventPredicate.java

To keep track of the next upcoming event, we have opted to keep `Event`s sorted in chronological order.

`Event`s are kept chronologically sorted by:
1. `Event` was made comparable via a `Comparable<Event>` interface.
   The comparison criteria are `Event#Date` and `Event#StartTime`.
2. In `Scheduler`, attached a [`javafx.collections.ListChangeListener`][ListChangeListener] to `UniqueEventList` 
   (the underlying run-time storage of the list of `Event`s) to know whenever `UniqueEventList` is changed
   (possibly as a result of `add`, `edit`, `delete`, or even a sorting action).
3. When a change is detected, we would sort the `Event`s via `FXCollections#sort`.

Sequence Diagram for how `UniqueEventList` maintains a chronological order of `Event`
![SortSequenceDiagram.png](images/SortSequenceDiagram.png)

For _Next_ command, the noteworthy classes are:
- [`ShowNextCommandParser.java`][ShowNextCommandParserClass] - Parse the arguments for `ShowNextCommand`
- [`ShowNextCommand.java`][ShowNextCommandClass] - Execute command
- [`UpcomingEventPredicate.java`][UpcomingEventPredicateClass] - Check if `Event` should be displayed as ongoing/upcoming

During this process, `ParseException` may be thrown for invalid arguments.
No exception is thrown for no arguments, as there is a default behaviour.

Given below is an example usage scenario of how the `Next` command executes.

-- user input --  
Step 1. User executes `next` command with valid arguments.  

-- `SchedulerParser` --  
Step 2. Returns new `ShowNextCommandParser`.  

-- `ShowNextCommandParser` --  
Step 3. Verifies that provided argument is valid.  
Step 4. Returns new `ShowNextCommand`.  

-- `ShowNextCommand` --  
Step 5. Creates an `UpcomingEventPredicate` using the provided argument.  
Step 6. Uses the created `UpcomingEventPredicate` to filter for ongoing/upcoming `Event`(s).  
Step 7. Updates `EventListPanel` with filtered `Event`(s).  

Other alternative path of execution can be traced in the activity diagram below.

_Activity Diagram for a typical `next` command_  
![NextCommandActivityDiagram.png](images/NextCommandActivityDiagram.png)  

[ListChangeListener]: https://docs.oracle.com/javase/8/javafx/api/javafx/collections/ListChangeListener.html
[`ListChangeListener.Change`]: https://docs.oracle.com/javase/8/javafx/api/javafx/collections/ListChangeListener.Change.html

<div style="page-break-after: always;"></div>

### **Undo Command**

[UndoCommand.java]: https://github.com/AY2223S2-CS2103-W17-3/tp/blob/master/src/main/java/ezschedule/logic/commands/UndoCommand.java

For _Undo_ command, the noteworthy classes are:
- [`UndoCommand.java`][UndoCommand.java] - For execution.

The following exceptions may be thrown during this process, namely:
- CommandException for attempting to execute undo when recent command is empty

Given below is an example usage scenario of how the _Undo_ command executes.

-- user input --  
Step 1. User executes a valid `add` command.

-- `AddCommand` --  
Step 2. Adds the `add` command as recent command.  
Step 3. Adds the added `Event` as recent event.

-- user input --  
Step 4. User executes `undo` command.

-- `UndoCommand` --  
Step 5. Verify that recent command is not empty.  
Step 6. Undo the recent command (deletes the recent event).

The execution can be seen in the activity diagram given below.

_Activity Diagram for a typical `undo` command_  
![UndoCommandActivityDiagram.png](images/UndoCommandActivityDiagram.png)  

<div style="page-break-after: always;"></div>

## **Documentation, Logging, Testing, Configuration, Dev-Ops**

--------------------------------------------------------------------------------------------------------------------
* [Documentation guide](Documentation.md)
* [Testing guide](Testing.md)
* [Logging guide](Logging.md)
* [Configuration guide](Configuration.md)
* [DevOps guide](DevOps.md)


## **Glossary**

--------------------------------------------------------------------------------------------------------------------
* **Clashing Events**: One or more events where any duration of the event overlaps with the another event
* **CLI**: Command Line Interface
* **Event**: A task with a starting time and an ending time
* **Mainstream OS**: Windows, Linux, Unix, OS-X
* **GUI**: Graphical User Interface
* **Ongoing Event**: An event that has started, but not ended
* **Upcoming Event**: An event that has not started

<div style="page-break-after: always;"></div>

## **Appendices**

--------------------------------------------------------------------------------------------------------------------

### **Appendix A: Project Requirements**

#### **Product Scope**

**Target user profile**:
* has a need to manage a significant number of events
* prefer desktop apps over other types
* prefers visual representation of output
* is reasonably comfortable using CLI apps
* can type fast

**Value proposition**: manage events at high-level, provides faster event analysis with graphical outputs.


#### **User Stories**

Priorities: High (must have) - `* * *`, Medium (nice to have) - `* *`, Low (unlikely to have) - `*`

| Priority | As a …         | I want to …                                   | So that I can…                                                          |
|----------|----------------|-----------------------------------------------|-------------------------------------------------------------------------|
| `* * *`  | new user       | see usage instructions                        | refer to instructions when I forget how to use the App                  |
| `* * *`  | user           | add a event's schedule into the app           | see their schedules                                                     |
| `* * *`  | user           | delete an event from the app                  | remove events that are no longer scheduled                              |
| `* * *`  | user           | list all events                               | have a high-level overview of all events that I currently have          |
| `* * *`  | regular user   | save my scheduler on exit                     | reload them again next time                                             |
| `* * *`  | regular user   | load my events in the scheduler on start      | use the scheduler immediately without having to reenter all events      |
| `* *`    | clumsy user    | undo a command                                | reset the scheduler to the previous state before I made a mistake       |
| `* *`    | forgetful user | see my next event clearly                     | readily see what is upcoming for me without having to type any commands |
| `* *`    | user           | sort my events based on date and time         | readily see my upcoming events chronologically                          |
| `* *`    | regular user   | repeat an event over a certain period of time | easily repeated events in my scheduler for the same type of events      |
| `* *`    | long term user | see my monthly schedule easily                | readily see which days I am more available, or which are my busier days |
| `* *`    | forgetful user | find my next event                            | check on the next event in the scheduler                                |
| `* *`    | clumsy user    | undo accidental deletions                     | easily restore accidents in scheduling                                  |
| `* *`    | user           | find an event by name                         | locate details of events without having to go through the entire list   |
| `* *`    | user           | find an event by date                         | locate details of events without having to go through the entire list   |
| `* *`    | user           | edit my schedule                              | make changes to events                                                  |
| `* *`    | busy user      | be able to schedule many events               | schedule as many events as I want                                       |

#### **Use Cases**

(For all use cases below, the **System** is the `EZ-Schedule` and the **Actor** is the `user`, unless specified otherwise)

<details open markdown="block">
<summary markdown="span">**Use case: Add an event**</summary>

**MSS**
1. User requests to add an event
2. System adds the event<br> 
   Use case ends.

**Extensions**
* 1a. The event already exist.
    * 1a1. System shows an error message.<br>
      Use case ends.

* 1b. The given event format invalid.
    * 1b1. System shows an error message.<br>
      Use case ends.

* 1c. The given end time of the event is before start time.
    * 1c1. System shows an error message.<br>
      Use case ends.

</details>

<details open markdown="block">
<summary markdown="span">**Use case: Recur an event**</summary>

**MSS**

1. User requests to recur an event over a period of time
2. System repeatedly adds the event<br>
   Use case ends.

**Extensions**

* 1a. The length of time to recur is not appropriate.
    * 1a1. System shows an error message.<br>
      Use case ends.

* 1b. The given event format invalid.
    * 1b1. System shows an error message.<br>
      Use case ends.

* 1c. The given end date of recur is in the past.
    * 1c1. System shows an error message.<br>
      Use case ends.

* 1d. There is a clash of events in an upcoming day.
    * 1d1. System shows an error message.<br>
      Use case ends.

</details>

<details open markdown="block">
<summary markdown="span">**Use case: Edit an event**</summary>

**MSS**

1. User requests to edit an event
2. System edits the event<br>
   Use case ends.

**Extensions**

* 1a. The event already exist.
    * 1a1. System shows an error message.<br>
      Use case ends.

* 1b. The given format invalid.
    * 1b1. System shows an error message.<br>
      Use case ends.

</details>

<details open markdown="block">
<summary markdown="span">**Use case: Delete an event**</summary>

**MSS**

1. User requests to delete an event/ events
2. System deletes the event(s)<br>
   Use case ends.

**Extensions**

* 1a. The given index/ indices is invalid.
    * 1a1. System shows an error message.<br>
      Use case ends.

* 1b. The given format invalid.
    * 1b1. System shows an error message.<br>
      Use case ends.

</details>

<details open markdown="block">
<summary markdown="span">**Use case: Find an event by name**</summary>

**MSS**

1. User requests to find an event by name
2. System finds events with matching name
3. System displays events with matching name<br>
   Use case ends.

**Extensions**

* 1a. The given event format invalid.
    * 1a1. System shows an error message.<br>
      Use case ends.

</details>

<details open markdown="block">
<summary markdown="span">**Use case: Show next event(s) by name**</summary>

**MSS**

1. User requests to show next x number of event(s)
2. System displays the next x number of events in chronological order<br>
   Use case ends.

**Extensions**

* 1a. The given event format invalid.
    * 1a1. System shows an error message.<br>
      Use case ends.

</details>

<details open markdown="block">
<summary markdown="span">**Use case: Undo a delete**</summary>

**MSS**
1. User requests to undo the recent delete
2. System restores the most recent delete<br>
   Use case ends.

**Extensions**

* 1a. Undo is the first command given to the scheduler.
    * 1a1. System shows an error message.<br>
      Use case ends.

</details>

<details open markdown="block">
<summary markdown="span">**Use case: List all events**</summary>

**MSS**

1. User requests to list all events
2. System shows all events in the scheduler<br>
   Use case ends.

</details>

<details open markdown="block">
<summary markdown="span">**Use case: Get help instructions**</summary>

**MSS**

1. User requests for help instructions
2. System shows help instructions<br>
   Use case ends.

</details>

<details open markdown="block">
<summary markdown="span">**Use case: Exit the application**</summary>

**MSS**

1. User requests to exit the application
2. System saves all events
3. System exits the application<br>
   Use case ends.

</details>

#### **Non-Functional Requirements**

1. Should work on any _mainstream OS_ as long as it has Java `11` or above installed.
2. Should be able to hold up to 1000 events without a noticeable sluggishness in performance for typical usage.
3. A user with above average typing speed for regular English text (i.e. not code, not system admin commands) should be able to accomplish most of the tasks faster using commands than using the mouse.
4. A user with colour blindness may require a high-contrast setting for graphical outputs.

<div style="page-break-after: always;"></div>

### **Appendix B: Planned Enhancements**

To resolve certain known feature flaws, we have planned to add some enhancements in the near future:

**Data Verification when loading save files**

Currently, the program reads and loads the data from the save files directly.
As long as the data is in the correct format, the data can be loaded successfully.
We plan to enhance this by verifying the validity of the data, 
that is making sure the saved events does not violate constraints such as overlapping time.

This will help to prevent errors made by advanced user who decided to modify the save files directly.
While the advanced user may be aware of the structure of the file,
knowing if the event added has conflict with any existing events can be hard,
especially if there are many existing events.

**Increasing the minimum size of the windowed screen**

Currently, if a user sets their windowed screen to the minimum size,
it can potentially cut off access to the local storage path where all event data is stored.

![windowedScreenCutOff.png](images/windowedScreenCutOff.png)

We plan to increase the minimum size of the windowed screen to ensure that the path to the local storage is always visible.
This will provide convenience to users who wish to modify the event data from the local storage

**Reformatting the display in events panel and upcoming events panel**

Currently, if the event names are too long, they may overflow onto the date portion,
which can make it difficult to view both the full event name and date.

![eventsPanelNotFullyDisplayingName.png](images/eventsPanelNotFullyDisplayingName.png)

We plan to improve the display of event names by wrapping the text and continuing it on the next line.
This will prevent the issue of long event names overflowing onto the date portion

**Changing the display of events in calendar date boxes**

Currently, if event names exceed 5 characters in length, 
the characters beyond the fifth are replaced with "..." in the display.

![calendarBoxesNotFullyDisplayingName.png](images/calendarBoxesNotFullyDisplayingName.png)

The original purpose was to give users a quick overview and inform them of the presence of events on certain dates. 
However, this truncated display does not provide much value to users as it fails to show the full event name.

We plan to change the way calendar date boxes display events.
Rather than showing event names, we will use dots to represent events. 
Each dot will have a different color within the same day to represent different events. 
This approach will allow users to quickly see an overview of the number of events on a given day.
An example is given below.

![calendarBoxesPlannedEnhancement.png](images/calendarBoxesPlannedEnhancement.png)

If users are interested in knowing more about the events, 
they can use the `find` command or click on the calendar date box to view the event details.

**Increase flexibility in event names**

Currently, event names only support alphanumeric characters and spaces. 
This may cause inconvenience to users as we anticipate that event names may include 
special characters such as brackets, colons, and dashes.

We plan to increase the number of characters supported in event names. 
However, certain characters such as `/` and `\` will not be supported as they may potentially cause conflicts

<div style="page-break-after: always;"></div>

### **Appendix C: Instructions for Manual Testing**

Presented below are a series of instructions, organized in **Context, Action, Result (CAR)** format, 
that can be followed to perform manual testing of the application.

<div markdown="span" class="alert alert-info">
:information_source: **Note:** These instructions only provide a starting point for testers to work on;
testers are expected to do more *exploratory* testing.
</div>


#### **Launch and Shutdown**

#### Scenario 1  
{: .no_toc}  
**Context:** Initial launch

**Action:**
1. Download the jar file and copy into an empty folder
2. Double-click the jar file
3. Ensure that it is launched in `Java 11`

**Result:**  Displays the GUI with the application opened to full-screen

#### Scenario 2
{: .no_toc}  
**Context:** Normal launch

**Action:** Launch the application again (follow from scenario 1 step 2)

**Result:**  Displays the GUI with the application opened to full-screen

#### Scenario 3
{: .no_toc}  
**Context:**  Restarting the application with existing data

**Action:**
1. Click on the 'X' button located in the top-right corner of the screen
2. Double-click the jar file

**Result:**  The existing data should remain unchanged


#### **Adding an Event**

#### Scenario 1
{: .no_toc}  
**Context:** The `Event` has not yet been added to _Ez-Schedule_

**Action:** Execute the command `add n/Tennis d/2023-05-01 s/10:00 e/12:00`

**Result:** 
* The `Event` has been successfully added to _Ez-Schedule_
* Details of the `Event` is also added to the Events Panel and the Calendar

#### Scenario 2
{: .no_toc}  
**Context:** An identical `Event` to the one being added, already exists in _Ez-Schedule_

**Action:**  Execute the command `add n/Tennis d/2023-05-01 s/10:00 e/12:00`

**Result:** The Response Box will display the message "This event already exists in the scheduler"

#### Scenario 3
{: .no_toc}  
**Context:** The selected time slot is already occupied by another `Event` in _Ez-Schedule_

**Action:**  Execute the command `add n/Tennis d/2023-05-01 s/10:00 e/12:00`

**Result:** The Response Box will display the message "Another event already exists at the chosen time"


#### **Recurring an Event**

#### Scenario 1
{: .no_toc}  
**Context:** No conflicting `Event` scheduled during the recurring time frame specified

**Action:** Execute the command: `recur 1 d/2023-05-10 every/day`

**Result:**
* The `Event` will be added repeatedly until the specified end date
* Details of all the `Event` is also added to the Events Panel and the Calendar

#### Scenario 2
{: .no_toc}  
**Context:** Another `Event` already exist in the recurring time frame specified by the `Event` being added

**Action:** Execute the command: `recur 1 d/2023-05-10 every/day`

**Result:** The Response Box will display the message "Unable to recur. 10 May has a clashing event."


#### **Editing an Event**

#### Scenario 1
{: .no_toc}  
**Context:** There exists an `Event` with the index 1 in _Ez-Schedule_

**Action:** Execute the command: `edit 1 n/Basketball`

**Result:** The name of the `Event` has been updated to Basketball

#### Scenario 2
{: .no_toc}  
**Context:** Modify an `Event` to make it identical to another `Event` already present in _Ez-Schedule_

**Action:** Execute `edit` command with arguments identical to another existing `Event`

**Result:** The Response Box will display the message "This event already exists in the scheduler"


#### **Deleting Events**

#### Scenario 1
{: .no_toc}  
**Context:** There exists an `Event` with the index 1 in _Ez-Schedule_

**Action:** Execute the command: `delete 1`

**Result:** 
* The `Event` is removed from _Ez-Schedule_
* Details of the `Event` is also removed from the Events Panel, Upcoming Events Panel, and the Calendar

#### Scenario 2
{: .no_toc}  
**Context:** There exists multiple `Event` with the indexes 1, 2 and 3 in _Ez-Schedule_

**Action:** Execute the command: `delete 1 2 3`

**Result:** 
* All the `Event` is removed from _Ez-Schedule_
* Details of the `Event` is also removed from the Events Panel, Upcoming Events Panel, and the Calendar

#### Scenario 3
{: .no_toc}  
**Context:** One of the specified indexes, index 10, does not exist in _Ez-Schedule_

**Action:** Execute the command: `delete 1 2 3 10`

**Result:** 
* The Response Box will display the message "The event index 10 provided is invalid"
* None of the `Event` with valid index will be removed from _Ez-Schedule_


#### **Finding Events**

#### Scenario 1
{: .no_toc}  
**Context:** There exists at least one `Event` whose `Name` includes the word Tennis

**Action:** Execute the command: `find n/Tennis`

**Result:** 
* The Events Panel will be updated to display only those `Event` whose `Name` includes the word Tennis
* The Calendar will highlight all the date boxes that correspond to days on which the found `Event` are scheduled

#### Scenario 2
{: .no_toc}  
**Context:** There exists at least one `Event` whose `Name` partially matches the word 'Ten'

**Action:** Execute the command: `find n/Ten`

**Result:**
* The Events Panel will be updated to display only `Event` whose `Name` includes Ten, which may include `Name` such as Tennis
* The Calendar will highlight all the date boxes that correspond to days on which the found `Event` are scheduled

#### Scenario 3
{: .no_toc}  
**Context:** There exists at least 1 `Event` whose `Date` correspond to May 1, 2023

**Action:** Execute the command: `find d/2023-05-01`

**Result:**
* The Events Panel will be updated to display only those `Event` whose `Date` are May 1, 2023
* The date box for May 1, 2023 will be highlighted on the Calendar

#### Scenario 4
{: .no_toc}  
**Context:** There exists at least one `Event` whose `Name` includes the word Tennis and `Date` corresponds to May 1, 2023

**Action:** Execute the command: `find n/Tennis d/2023-05-01`

**Result:**
* The Events Panel will be updated to display only those `Event` whose `Name` includes the word Tennis and `Date` are May 1, 2023
* The date box for May 1, 2023 will be highlighted on the Calendar


#### **Show Next Events**

#### Scenario 1
{: .no_toc}  
**Context:** None needed

**Action:** Execute the command: `next`

**Result:**
* The Response Box will display the message "1 events listed!"
* The Upcoming Events Panel will display the next upcoming or ongoing event that you have

#### Scenario 2
{: .no_toc}  
**Context:** There exists at least two upcoming or ongoing `Event`

**Action:** Execute the command: `next 2`

**Result:**
* The Response Box will display the message "2 events listed!"
* The Upcoming Events Panel will display the next 2 upcoming or ongoing event that you have


#### **Undo a Command**

#### Scenario 1
{: .no_toc}  
**Context:** None needed

**Action:**
1. Execute the command: `add n/Tennis d/2023-05-01 s/10:00 e/12:00`
2. Execute the command: `undo`  

**Result:** 
* The Response Box will display the message "Action undone: add"
* The Tennis `Event` that was added will be removed from _Ez-Schedule_

#### Scenario 2
{: .no_toc}  
**Context:** No conflicting `Event` scheduled during the recurring time frame specified

**Action:**
1. Execute the command: `recur 1 d/2023-05-10 every/recur`
2. Execute the command: `undo`

**Result:**
* The Response Box will display the message "Action undone: recur"
* All the `Event` that was added will be removed from _Ez-Schedule_

#### Scenario 3
{: .no_toc}  
**Context:** There exists an `Event` with an index 1 and a `Name` of Tennis in _Ez-Schedule_

**Action:**
1. Execute the command: `edit 1 n/Basketball`
2. Execute the command: `undo`

**Result:**
* The Response Box will display the message "Action undone: edit"
* The Tennis `Event` that was edited to Basketball will be changed back to Tennis

#### Scenario 4
{: .no_toc}  
**Context:** There exists an `Event` with an index 1 in _Ez-Schedule_

**Action:**
1. Execute the command: `delete 1`
2. Execute the command: `undo`

**Result:**
* The Response Box will display the message "Action undone: delete"
* The `Event` with index 1 that was deleted is added back into _Ez-Schedule_

<div style="page-break-after: always;"></div>

### **Appendix D: Effort**

**Difficulty Level:** Overall _Medium_ difficulty (averaged total votes by all members)

**Challenges Faced:**
- During the first milestone, we decided to "morph" by creating a parallel package and using AB3 as code reference.
  We would copy over whichever code we deem relevant. Halfway through the milestone, one of us realised that doing this
  possibly violated [`Constraint-Brownfield`][tp constraints brownfield]. After checking with our tutor/prof, we had to
  restart again, effectively wasting our effort and restricting our duration for `milestone 1` by half.
- During morphing, the refactor of `Person` to `Event`, as well as `AddressBook` to `Scheduler` was tedious and
  required a lot of careful checking. Despite using IDE features like refactor and find-and-replace, there were
  still variable name and comments which we had to change, and we had to painstakingly double-check everything.
- Furthermore, the morphing process also invalidated many of the existing test cases (from AB3) resulting in the 
  Java CI failing for a prolonged period. We spend a lot of time in `milestone 2` changing/fixing the broken test 
  cases, which left us with not a lot of time to implement new features in `milestone 2` and `milestone 3`.

**Effort Required:** High effort for the morphing process.

**Achievements of Project:**
- Created a working product
- Experienced using GitHub Issues and PRs
- Experienced making UML diagrams digitally
- Practiced debugging & testing skills taught
- Practiced workflows & design approaches taught
- Collaborated with a group on a huge project (~13kLoC)

[tp constraints brownfield]: https://nus-cs2103-ay2223s2.github.io/website/admin/tp-constraints.html#constraint-brownfield

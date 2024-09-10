# Gitlet Design Document

**Name**: Bai Haozhen


## Classes and Data Structures

### Main
This is the entry point to our program. It takes in arguments from the command 
line and based on the command (the first element of the args array) calls the 
corresponding command in Repository which will actually execute the logic 
of the command. It also validates the number of arguments based on the command to ensure 
that enough arguments were passed in.


### Repository
This is where the main logic of our program will live. This file will handle all of the actual git commands by reading/writing from/to the correct file, setting up persistence, and additional error checking.

It will also be responsible for setting up all persistence within git. This includes creating the .gitlet folder as well as the folder and file where we store all commit and blob objects.

This class defers all Commit specific logic to the Commit class: for example, instead of having the Repository class handle Commit serialization and deserialization, we have the Commit class contain the logic for that.

#### Fields
`static final File CWD = new File(System.getProperty("user.dir"))` The Current Working Directory. Since it has the package-private access modifier (i.e. no access modifier), other classes in the package may use this field. It is useful for the other File objects we need to use.

`static final File CAPERS_FOLDER = Utils.join(CWD, ".gitlet")` The hidden .capers directory. This is where all of the state of the Repository will be stored, including additional things like the Commit objects and blob objects. It is also package private as other classes will use it to store their state.

These fields are both static since we don’t actually instantiate a CapersRepository object: we simply use it to house functions. If we had additional non-static state (like the Commit class), we’d need to serialize it and save it to a file.

### Commit
This class 


## Algorithms

## Persistence


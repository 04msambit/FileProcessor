# FileProcessor
Process Files in a Directory

This helps to process files in the current directory and apply a Lower Case Transformation to the contents of the file.
Once the transformation is applied an output file is created with an .out extension. This is a continuous running process and
new files can be added to the directory at the runtime.

The program exits if there is an Exception or the user exits is forcefully

# Steps to Execute the Program

**Prerequisites**

The code can be executed on any Linux/Mac/Windows OS running JDK 6 and above

```
1 - git clone https://github.com/04msambit/FileProcessor
2 - cd FileProcessor
3 - javac Assignment.java
4 - java Assignment
```

Once the above is executed the .out files will be created in the directory

# Program Logic

Since we have been provided with 3 groups of files, we have created a Thread Pool of size 3 which applies the transformation on each of the groups
We have used 3 separate queues to store the names of the files. Each Thread processes a separate queue.

# Tests Carried Out

```
1 - Add 3 text files of each group in the folder
2 - Start the process
3 - Verify the .out files are created in the same folder
4 - Add new files while the process is running
5 - The .out files corresponding to new files should be created
```


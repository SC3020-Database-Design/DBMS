# DBMS Design
  Note: Given the project requirements has to do with only the Storage and Indexing parts of a DBMS, the design uses a simplified design that tries to inherit some of the best practices that are used in practice for actual RDBMS, and may not handle some of the other requirements of DBMS like concurrency control, transaction isolation, and crash recovery.

  ## Installation Guide
  1. <b>Download the Project:</b> Begin by downloading our project submission from NTULearn. Once downloaded, unzip the project folder to a location of your choice on your computer.
  2. <b>Open the Project in an IDE:</b> Launch your preferred Integrated Development Environment (IDE) such as Eclipse or Visual Studio Code. Navigate to the unzipped project folder and open it within your IDE.
  3. <b>Download Movie Data:</b> Download the movie data file named data.tsv from the provided source. Locate the src folder within the project directory. Move the downloaded data.tsv file into this src folder
  4. <b>Navigate to Source Folder:</b> If you prefer to use the terminal for compilation and execution, open a terminal window. Navigate to the src folder within the project directory using the cd command, <code>cd DBMS/src</code>
  5. <b>Compile the Project:</b> In the terminal, compile the project by running <code>javac App.java</code>. 
  6. <b>Run the Project:</b> Once the project is successfully compiled, execute the program by running <code> java App</code> command in the terminal. 


  ## Specifications
  ### Disk
  - Disk is just a byte[] of 500MB. However we can think of it as having many Blocks
  - On disk we are representing data as an append-only log to utilize sequential writes for better performance. To prevent out-of-memory issues we will run a compaction process when the size of data is 90% of the limit(500mb)

  ### Block
  - A block is a logical unit representing some slice of the Disk.
  - Each block is 200 Bytes
  - Each block has a 4 Byte block header containing an integer = # of records in the block currently
  - Each block can hold a maximum of 9 Records

  ### Record
  - Each Record is 24 Bytes = 2 Byte header + 2 Byte padding + 10 Byte String + 2 Byte padding + 4 Byte Float + 4 Byte Integer
  - Each record header contains a short = 0/1, which indicates whether a record is a tombstone(see below)

  ## Insert/Delete/Update/Read
  - Inserting records are append-only to make use of sequential writes. To ensure that a lookup of a record is log(n) and not O(n) we create a default B tree index on the primary key(which is what real RDBMS do in practice)
  - Deleting records will not be executed immediately. Instead a 'tombstone'(borrowed term from distributed systems) will be appended(and the index's record pointer will point to this tombstone).
  - Updating records are essentially delete + insert. A tombstone displaces the original record. Then the updated record is appended at the end to make use of sequential writes. Finally, the index structure is updated to hold the new (block #, record # in block). 
  - Reading records by primary key involve retrieving the (block #, record # in block) from the default B tree. If it's a miss or the lookup is done on a non-primary column then a sequential O(n) look up is executed.

  ## Crash Recovery
  - Crash recovery is implemented using a (Write-Ahead Log)[https://www.postgresql.org/docs/current/wal-intro.html]
  - To match closer to real DBMS, we write to a binary log file.
  - Only need to log INSERT and DELETE operations, since UPDATE operations are just DELETE + INSERT

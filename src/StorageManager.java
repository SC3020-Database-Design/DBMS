/**
 * Blocks are logical units representing a particular slice of the Disk
 * This class is to encapsulate the logic for interactions between Disk and Block
 */
class StorageManager {
<<<<<<< HEAD
    private Integer numRecords = 0;
    private int occupiedBlocks = 0;
    Disk disk;
=======
  private Integer numRecords = 0;
  private int occupiedBlocks = 0;
  Disk disk;
  StorageConfiguration config;
>>>>>>> e67eb14 (feat(StorageConfiguration): Add data class for storing storage config constants)

  public StorageManager(Disk disk, StorageConfiguration storageConfiguration) {
    this.disk = disk;
    config = storageConfiguration;
  }

  /**
   * We do an append only log which is performant as it makes use of sequential writes.
   * @param record Record to be inserted
   */
  public void insertRecord(Record r) {
    int recordPositionInDisk; 
    if (occupiedBlocks == 0) {
      Block block = new Block();
      occupiedBlocks ++;
      block.insertRecord(r);
      disk.writeBlock(1, block);
      recordPositionInDisk = Record.RECORD_BYTE_SIZE;
    } else {
      Block block = disk.getBlock(occupiedBlocks);
      if (block.isFull()) {
        block = new Block();
        occupiedBlocks ++;
      }
      block.insertRecord(r);
      disk.writeBlock(occupiedBlocks, block);
      recordPositionInDisk = occupiedBlocks * Block.BLOCK_BYTE_SIZE + (block.getRecordCount()-1) * Record.RECORD_BYTE_SIZE;
    }
    numRecords ++;
    this.checkAndRunCompaction();
    // Update b tree with recordPositionInDisk
  }

    /**
     * Instead of deleting on shift, mark the record with RecordTombstone and batch delete and shift when tombstone exceed 20%
     * @param record Record to delete
     */
    public void deleteRecord(Record r) {
      Record recordTombstone = new Record(r.getUuid(), r.getAverageRating(), r.getNumVotes(), (short) 1);
      insertRecord(recordTombstone);
      if (numRecords * Record.RECORD_BYTE_SIZE >= (int) 0.9 * Disk.DISK_BYTE_SIZE) {
        runCompaction();
      }
    }

  public void readRecordByPrimaryKey() {
    // Get (block #, record #) from default B tree
    // If valid block # and record # do constant time lookup. Otherwise do linear lookup.
  }

  public void updateRecordByPrimaryKey() {
    // Call readRecordByPrimaryKey
    // Call deleteRecord
    // Call insertRecord with the updated properties
  }

  public float getDiskUtilization() {
    return (float) (numRecords*Record.RECORD_BYTE_SIZE ) / Disk.DISK_BYTE_SIZE;
  }

<<<<<<< HEAD
    /**
     * Delete tombstones and reclaim space to reduce fragmentation. Tombstones are a result of updates or deletions.
     */
    private void runCompaction() {
      int currentBlockNumber = 1;
      int currentIndexInBlock = 0;  // Index in the current block
  
      // Iterate through blocks and process tombstones
      for (int blockNumber = 1; blockNumber <= occupiedBlocks; blockNumber++) {
          Block currentBlock = disk.getBlock(blockNumber);
          int numRecords = currentBlock.getRecordCount();
  
          // Iterate through records in the current block
          for (int j = 0; j < numRecords; j++) {
              Record record = currentBlock.getRecordAt(j);
              if (record.isTombstone()) {
                numRecords --;
                continue;
              }
              // Move valid records to the current block and index in block
              Block block = disk.getBlock(currentBlockNumber);
              block.insertRecordAt(currentIndexInBlock, record);
              currentIndexInBlock++;
=======
  /**
   * Check if disk utilization is over 90%.
   * If so delete tombstones and reclaim space to reduce fragmentation
   */
  private void checkAndRunCompaction() {
    if (getDiskUtilization() < this.config.getCompactionThreshold()) {
      return;
    }
    int currentBlockNumber = 1;
    int currentIndexInBlock = 0;  // Index in the current block

    // Iterate through blocks and process tombstones
    for (int blockNumber = 1; blockNumber <= occupiedBlocks; blockNumber++) {
        Block currentBlock = disk.getBlock(blockNumber);
        int numRecords = currentBlock.getRecordCount();

        // Iterate through records in the current block
        for (int j = 0; j < numRecords; j++) {
            Record record = currentBlock.getRecordAt(j);
            if (record.isTombstone()) {
              numRecords --;
              continue;
            }
            // Move valid records to the current block and index in block
            Block block = disk.getBlock(currentBlockNumber);
            block.insertRecordAt(currentIndexInBlock, record);
            currentIndexInBlock++;
>>>>>>> e67eb14 (feat(StorageConfiguration): Add data class for storing storage config constants)

            if (block.isFull()) {
                currentBlockNumber++;
                currentIndexInBlock = 0;
            }
        }
    }
  }
    public void printState(Boolean verbose) {
      System.out.println("#####\tPrinting state of Storage\t#####");
      if(verbose) {
        for (int blockNumber = 1; blockNumber <= occupiedBlocks; blockNumber ++) {
          System.out.println(disk.getBlock(blockNumber));
        }
      }
      System.out.println(String.format("Number of records: %d", numRecords));
      System.out.println(String.format("Size of record in bytes: %d", Record.RECORD_BYTE_SIZE));
      System.out.println(String.format("Max number of records in a block: %d", Block.MAX_RECORDS_PER_BLOCK));
      System.out.println(String.format("Current number of occupied blocks: %d", occupiedBlocks));
      System.out.println(String.format("Disk utilization: %f %%", (float) (numRecords*Record.RECORD_BYTE_SIZE ) / Disk.DISK_BYTE_SIZE * 100));
      System.out.println();
    }
}
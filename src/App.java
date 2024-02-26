public class App {
    public static void main(String[] args) throws Exception {
        // Init disk memory
        Disk disk = new Disk(Block.BLOCK_BYTE_SIZE);
        StorageConfiguration storageConfiguration = new StorageConfiguration.Builder().build();
        StorageManager blockManager = new StorageManager(disk, storageConfiguration);

        // seed data
        String filePath = System.getProperty("user.dir") + "/data.tsv";
<<<<<<< HEAD
        DataSeeder.seed(filePath, blockManager);
=======
        RecordManager recordManager = new RecordManager(filePath);
        recordManager.printHead();
        // Init disk memory
        Disk disk = new Disk(Block.BLOCK_BYTE_SIZE);
        // Put seed data into Blocks
        StorageManager blockManager = new StorageManager(recordManager.records, disk);
>>>>>>> e6310b1 (refactor(BlockManager): Rename to StorageManager a Facade class encapsulating logic between Disk and Block)
        blockManager.printState(false);

        // Initialize default B tree using uuid(primary key)
        Bplustree btree = new bplustree(3);
        // Initialize any other indexes the user has created
        
        // answer queries

    }
}

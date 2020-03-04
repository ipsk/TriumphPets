package me.mattstudios.triumphpets.data.database

object Queries {

    /**
     * Create Queries
     */
    const val SQLITE_CREATE_PETS = "CREATE TABLE IF NOT EXISTS `pets`(\n" +
            "  `uuid` VARCHAR(36) PRIMARY KEY,\n" +
            "  `owner_uuid` VARCHAR(36),\n" +
            "  `type` VARCHAR(36),\n" +
            "  `name` VARCHAR(128),\n" +
            "  `experience` INTEGER\n" +
            ");" /*public static final String SQLITE_CREATE_PET_INVENTORY =
            "CREATE TABLE IF NOT EXISTS `tp_pet_inventory`(\n" +
                    "  `inv_id` INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                    "  `pet_id` INTEGER, \n" +
                    "  `inventory` TEXT, \n" +
                    "  FOREIGN KEY (`pet_id`) REFERENCES `tp_pets` (`pet_id`)" +
                    ");";*/
    /**
     * Select queries
     */

    const val SQLITE_SELECT_PETS = "SELECT * FROM `pets`"


    /**
     * Insert queries
     */

    const val SQLITE_INSERT_PET = "INSERT INTO `pets` VALUES(?, ?, ?, ?, ?)"
}
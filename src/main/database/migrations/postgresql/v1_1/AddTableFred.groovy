package migrations.postgresql.v1_1

import com.readytalk.staccato.database.migration.MigrationRuntime
import com.readytalk.staccato.database.migration.annotation.Migration
import com.readytalk.staccato.database.migration.annotation.SchemaUp
import com.readytalk.staccato.database.migration.annotation.DataUp
import java.sql.ResultSet
import com.readytalk.staccato.database.migration.annotation.PostUp
import com.readytalk.staccato.database.DatabaseType

/**
 * Represents a groovy migration script.
 *
 * Migration.scriptDate:
 * The migration script date is used to sort the
 * script for execution. This is a required field
 * and must be unique across the entire set of migration
 * scripts
 *
 * Migration.databaseVersion:
 * This attribute is required and is used for performing incremental
 * upwards or downwards migrations
 *
 * Migration.scriptVersion:
 * This field is used to perform API version compatibility checking prior to script execution.
 * If your annotated value doesn't equal the current template version bundled in
 * the jar, then the system will not execute the script and throw an exception.
 *
 * Migration.description:
 * Optional attribute that provides information about script execution.
 * This field is for informational purposes only but does
 * get outputted to the log file when defined.
 *
 * Migration.databaseType:
 * Optional field that informs the system which database type the script belongs to.
 * If undefined, the system will assume to queue the script for execution.
 *
 * @author jhumphrey
 */
@Migration(
scriptDate = "2010-09-05T10:37:12-06:00",
databaseVersion = "1.1",
scriptVersion = "1.0.0",
databaseType = DatabaseType.POSTGRESQL)
class AddTableFred {

  @SchemaUp
  schemaUp(MigrationRuntime runtime) {
    runtime.executeSQLFile "add-table-fred.sql"
  }

  @DataUp
  moveFromTableBar(MigrationRuntime runtime) {
    ResultSet rs = runtime.executeSQL("select bar from foo");
    while (rs.next()) {
      String fieldToMigrate = rs.getString(1)  // get the bar field value
      fieldToMigrate = fieldToMigrate.replace("bar", "baz")  // do data transformation on it; change 'bar' to 'baz'
      runtime.executeSQL("insert into fred(baz) values('${fieldToMigrate}');")  // insert the the new baz field into fred
    }
  }

  @PostUp
  dropBar(MigrationRuntime runtime) {
    runtime.executeSQL("alter table foo drop column bar");
  }
}
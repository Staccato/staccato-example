package migrations.postgresql.v2_0

import com.readytalk.staccato.database.migration.MigrationRuntime
import com.readytalk.staccato.database.migration.annotation.Migration
import com.readytalk.staccato.database.migration.annotation.SchemaUp

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
scriptDate = "2010-10-01T08:56:40-06:00",
databaseVersion = "2.0",
scriptVersion = "1.0.0")
class AddTableWaldo {

  @SchemaUp
  schemaUp(MigrationRuntime runtime) {

    /**
     * This example creates table waldo using SQL contained in a String rather than delegating to a sql file
     */

    runtime.executeSQL """

      create table waldo (
        id serial,
        baz varchar(50) not null,
        primary key (id)
      );

    """;
  }
}
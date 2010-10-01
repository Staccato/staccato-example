package migrations.postgresql

import com.readytalk.staccato.database.DatabaseContext
import com.readytalk.staccato.database.migration.MigrationRuntime
import com.readytalk.staccato.database.migration.annotation.Create
import com.readytalk.staccato.database.migration.annotation.Migration
import java.sql.SQLException
import org.apache.log4j.Logger
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
scriptDate = "2010-09-04T08:00:40-06:00",
databaseVersion = "0.0",
scriptVersion = "1.0.0",
databaseType = DatabaseType.POSTGRESQL)
class CreateDatabase {

  Logger logger = Logger.getLogger(CreateDatabase.class);

  /**
   * This methods gets run during a CREATE migration.
   *
   * It uses the runtime.executeSQL method to execute SQL
   *
   * @param runtime
   * @return
   */
  @Create
  createDb(MigrationRuntime runtime) {

    // retrieve the database context
    DatabaseContext dbCtx = runtime.databaseContext;
    String dbName = dbCtx.dbName;
    String dbUser = dbCtx.username;
    String dbPwd = dbCtx.password;

    try {
      runtime.executeSQL "CREATE USER ${dbUser} WITH PASSWORD '${dbPwd}';"
    } catch (SQLException e) {
      // An exception gets thrown if the user already exists, just fail gracefully in this case
      logger.info "User ${dbUser} already exists"
    }

    runtime.executeSQL "DROP DATABASE if EXISTS ${dbName};"
    runtime.executeSQL "CREATE DATABASE ${dbName} WITH OWNER ${dbUser} ENCODING 'UTF-8';"
    runtime.executeSQL "GRANT ALL PRIVILEGES ON DATABASE ${dbName} TO ${dbUser};"
  }
}
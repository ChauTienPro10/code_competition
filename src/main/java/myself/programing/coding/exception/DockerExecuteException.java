package myself.programing.coding.exception;

import myself.programing.coding.enums.DOCKER_EXECUTE_TYPE_ERROR;

public class DockerExecuteException extends Throwable {

  /**
   *
   * @param message
   * @param e
   */
  public DockerExecuteException(String message, Throwable e) {
    super(message);
  }

  /**
   *
   * @param errorTyoe
   * @param message
   * @param e
   */
  public DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR errorTyoe, String message, Throwable e) {
    super(message);
  }

  /**
   *
   * @param errorType
   * @param message
   */
  public DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR errorType, String message) {
    super(message);
  }

  public DockerExecuteException(DOCKER_EXECUTE_TYPE_ERROR errorType, Throwable e) {
    super(e.getMessage());
  }
}

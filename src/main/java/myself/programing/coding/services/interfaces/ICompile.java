package myself.programing.coding.services.interfaces;

import myself.programing.coding.exception.DockerExecuteException;

public interface ICompile {
    String doCompile(String pathInContainer) throws DockerExecuteException;
}

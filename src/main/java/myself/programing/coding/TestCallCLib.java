package myself.programing.coding;


public class TestCallCLib {
    static {
        System.loadLibrary("fib");
    }

    public native int fib(int n);

    public native int factorial(int n);

}
package cliet.additional;

public class TestHelper {

    /**
     * Need only for more understandable code in tests.
     * !!! good practice will be use(change System.out by your output): {@code
     *
     *     private ByteArrayOutputStream newOutput;
     *     private PrintStream defaultOutput;
     *
     *     @Before
     *     public void changeOutput() {
     *         newOutput = new ByteArrayOutputStream();
     *         defaultOutput = System.out;
     *         System.setOut(new PrintStream(newOutput));
     *     }
     *
     *     @After
     *     public void returnOutput() {
     *         System.setOut(defaultOutput);
     *     }
     * }
     * @param path       path to save log.
     * @param consoleLog log content.
     */
    public static void saveLog(String path, String consoleLog) {
        IOHelper.writeStringToFie(path, consoleLog);
    }

    public static void addToFile(String path, String content) {
        var temp = IOHelper.readFileToList(path);
        temp.add(content);
        IOHelper.writeListToFile(path, temp, "");
    }

}
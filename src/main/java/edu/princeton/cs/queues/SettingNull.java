public class SettingNull
{
    public static void main(String[] args)
    {
        String[] leArr = new String[2];

        leArr[0] = args[0];
        leArr[1] = null;

        System.out.println(leArr.length);
    }
}

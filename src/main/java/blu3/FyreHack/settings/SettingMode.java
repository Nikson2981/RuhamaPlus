package blu3.FyreHack.settings;

public class SettingMode extends SettingBase
{
    public String[] modes;
    public int mode;
    public String text;

    public SettingMode(String text, String... modes)
    {
        this.modes = modes;
        this.text = text;
    }

    public int getNextMode()
    {
        return this.mode + 1 >= this.modes.length ? 0 : this.mode + 1;
    }
}

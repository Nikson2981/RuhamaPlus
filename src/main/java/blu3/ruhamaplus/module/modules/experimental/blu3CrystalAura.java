package blu3.ruhamaplus.module.modules.experimental;

import blu3.ruhamaplus.module.Category;
import blu3.ruhamaplus.module.Module;
import blu3.ruhamaplus.settings.SettingBase;
import blu3.ruhamaplus.settings.SettingMode;
import blu3.ruhamaplus.settings.SettingSlider;
import blu3.ruhamaplus.settings.SettingToggle;

import java.util.Arrays;
import java.util.List;

public class blu3CrystalAura extends Module {
    private static final List<SettingBase> settings = Arrays.asList(
            new SettingMode("Login: ", "BreakPlace", "PlaceBreak"),
            new SettingMode("Rotate: ", "Never", "Place", "Break", "Always"),
            new SettingSlider(0.0D, 8.0D, 5.0D, 0, "Range: "),
            new SettingSlider(0.0D, 20.0D, 3.0D, 0, "Hit Delay: "),
            new SettingSlider(0.0D, 20.0D, 3.0D, 0, "Place Delay: "),
            new SettingSlider(1.0D, 36.0D, 6.0D, 0, "MinDMG: "),
            new SettingSlider(1.0D, 36.0D, 6.0D, 0, "MaxSelfDMG: "),
            new SettingSlider(1.0D, 36.0D, 10.0D, 0, "FacePlace HP: "),
            new SettingToggle(true, "MultiPlace"),
            new SettingToggle(false, "RenderTarget"),
            new SettingToggle(true, "AutoSwitch")
    );

    public blu3CrystalAura() { super("blu3CA", 0, Category.EXPERIMENTAL, "absolute shit i say", settings); }


    public void onUpdate() {
        this.doStuff();
    }

    public void doStuff(){
        switch (this.getSetting(0).asMode().mode){
            case 0: {
                this.breakCrystal();
                this.placeCrystal();
                break;
            }

            case 1: {
                this.placeCrystal();
                this.breakCrystal();
                break;
            }
        }
    }

    public void placeCrystal(){

    }

    public void breakCrystal(){

    }

    public void switchToCrystal(){

    }
}

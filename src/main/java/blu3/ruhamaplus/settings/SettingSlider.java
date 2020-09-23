package blu3.ruhamaplus.settings;

import net.minecraft.client.Minecraft;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.RoundingMode;
import java.math.BigDecimal;
import java.net.URL;
import java.net.URLConnection;
import java.security.SecureRandom;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

public class SettingSlider extends SettingBase
{
    public double min;
    public double max;
    public double value;
    public int round;

    public String text;

    public SettingSlider(double min, double max, double value, int round, String text)
    {
        this.min = min;
        this.max = max;
        this.value = value;
        this.round = round;
        this.text = text;
    }

    public double getValue()
    {
        return this.round(this.value, this.round);
    }

    public double round(double value, int places)
    {
        BigDecimal bd = new BigDecimal(value);
        bd = bd.setScale(places, RoundingMode.HALF_UP);

        return bd.doubleValue();
    }
}

package io.github.jas34.scheduledwf.utils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.amazonaws.util.EC2MetadataUtils;

/**
 * @author Jasbir Singh
 */
public class CommonUtils {

    private static final String dateFormat = "dd-MM-yyyy HH:mm:ss:SSS";

    public static String resolveNodeAddress() {
        String serverId = null;
        try {
            serverId = InetAddress.getLocalHost().getHostName();

        } catch (UnknownHostException e) {
            serverId = System.getenv("HOSTNAME");
        }

        if (serverId == null) {
            serverId = (EC2MetadataUtils.getInstanceId() == null) ? System.getProperty("user.name")
                    : EC2MetadataUtils.getInstanceId();
        }
        return serverId;
    }

    public static String toFormattedDate(Long timeInMillis) {
        if (timeInMillis == null) {
            return null;
        }
        SimpleDateFormat format = new SimpleDateFormat(dateFormat);
        return format.format(new Date(timeInMillis));
    }
}

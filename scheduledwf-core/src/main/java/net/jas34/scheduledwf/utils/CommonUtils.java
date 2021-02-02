package net.jas34.scheduledwf.utils;

import com.amazonaws.util.EC2MetadataUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author Jasbir Singh
 */
public class CommonUtils {

    public static String resolveNodeAddress() {
        String serverId = null;
        try {
            serverId = InetAddress.getLocalHost().getHostName();

        } catch (UnknownHostException e) {
            serverId = System.getenv("HOSTNAME");
        }

        if (serverId == null) {
            serverId = (EC2MetadataUtils.getInstanceId() == null) ? System.getProperty("user.name") : EC2MetadataUtils.getInstanceId();
        }
        return serverId;
    }
}

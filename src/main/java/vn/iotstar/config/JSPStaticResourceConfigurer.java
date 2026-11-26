package vn.iotstar.config;

import java.net.URI;
import java.net.URL;

import org.apache.catalina.Context;
import org.apache.catalina.Lifecycle;
import org.apache.catalina.LifecycleEvent;
import org.apache.catalina.LifecycleListener;
import org.apache.catalina.WebResourceRoot;
import org.springframework.util.ResourceUtils;

public class JSPStaticResourceConfigurer implements LifecycleListener {

    private final Context context;
    private final String subPath = "/META-INF";

    public JSPStaticResourceConfigurer(Context context) {
        this.context = context;
    }

    @Override
    public void lifecycleEvent(LifecycleEvent event) {
        if (!event.getType().equals(Lifecycle.CONFIGURE_START_EVENT)) {
            return;
        }

        try {
            final URL finalLocation = getUrl();
            if (this.context != null && this.context.getResources() != null) {
                this.context.getResources().createWebResourceSet(
                        WebResourceRoot.ResourceSetType.RESOURCE_JAR,
                        "/",
                        finalLocation,
                        subPath
                );
            }
        } catch (Throwable e) {
            // Gracefully ignore so embedded Tomcat continues standard resource resolution
        }
    }

    private URL getUrl() {
        final URL location = this.getClass().getProtectionDomain().getCodeSource().getLocation();

        if (ResourceUtils.isFileURL(location)) {
            return location;
        } else if (ResourceUtils.isJarURL(location)) {
            try {
                String locationStr = location.getPath()
                        .replaceFirst("^nested:", "")
                        .replaceFirst("/!BOOT-INF/classes/!/$", "!/");

                return new URI("jar:file", locationStr, null).toURL();
            } catch (Exception e) {
                return location;
            }
        } else {
            return location;
        }
    }
}

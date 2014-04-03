package codeOrchestra.colt.core.rpc.discovery;

import javax.jmdns.ServiceInfo;

/**
 * @author Alexander Eliseyev
 */
public class ColtServiceDescriptor {

    private final Key key;

    private String path;
    private String name;
    private long timestamp;

    private String host;
    private int port;

    public ColtServiceDescriptor(ServiceInfo serviceInfo) {
        this.path = serviceInfo.getPropertyString("path");
        this.name = serviceInfo.getPropertyString("name");
        this.timestamp = Long.valueOf(serviceInfo.getPropertyString("timestamp"));

        this.port = serviceInfo.getPort();
        this.host = serviceInfo.getHostAddress();

        this.key = new Key(path, name);
    }

    public String getPath() {
        return path;
    }

    public String getName() {
        return name;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getHost() {
        return host;
    }

    public int getPort() {
        return port;
    }

    public Key getKey() {
        return key;
    }

    @Override
    public String toString() {
        return "ColtServiceDescriptor{" +
                "path='" + path + '\'' +
                ", name='" + name + '\'' +
                ", timestamp=" + timestamp +
                ", host='" + host + '\'' +
                ", port=" + port +
                "} " + super.toString();
    }

    public static class Key {

        private String path;
        private String name;

        public Key(String path, String name) {
            this.path = path;
            this.name = name;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            Key key = (Key) o;

            if (!name.equals(key.name)) return false;
            if (!path.equals(key.path)) return false;

            return true;
        }

        @Override
        public int hashCode() {
            int result = path.hashCode();
            result = 31 * result + name.hashCode();
            return result;
        }
    }

}

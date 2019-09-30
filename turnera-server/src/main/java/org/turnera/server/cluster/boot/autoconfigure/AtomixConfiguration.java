package org.turnera.server.cluster.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

@Validated
@ConfigurationProperties(prefix = "atomix")
public class AtomixConfiguration {
    /**
     * enable/disable.
     */
    private boolean enabled = true;

    /**
     * The cluster id;
     */
    @NotNull
    private String clusterId;

    /**
     * The local member.
     */
    @NotNull
    private Member member;

    /**
     * The cluster member;
     */
    private List<Member> nodes = new ArrayList<>();

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getClusterId() {
        return clusterId;
    }

    public void setClusterId(String clusterId) {
        this.clusterId = clusterId;
    }

    public Member getMember() {
        return member;
    }

    public void setMember(Member member) {
        this.member = member;
    }

    public List<Member> getNodes() {
        return nodes;
    }

    // ************************
    //
    // ************************

    @Validated
    public static class Member {
        /**
         * The member id.
         */
        @NotNull
        private String id;

        /**
         * The member host.
         */
        private String host = "localhost";

        /**
         * The member listening port.
         */
        private Integer port = 5679;


        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }
    }
}

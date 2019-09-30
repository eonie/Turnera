package org.turnera.server.cluster.boot.autoconfigure;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import io.atomix.cluster.Member;


public final class AtomixUtils {
    public static final String META_MEMBER_ID = "atomix.member.id";
    public static final String META_MEMBER_ZONE = "atomix.member.zone";
    public static final String META_MEMBER_RACK = "atomix.member.rack";
    public static final String META_MEMBER_HOST = "atomix.member.host";
    public static final String META_MEMBER_CLUSTER_ID = "atomix.member.cluster.id";
    public static final String META_MEMBER_ADDRESS_HOST = "atomix.member.address.host";
    public static final String META_MEMBER_ADDRESS_PORT = "atomix.member.address.port";

    private AtomixUtils() {
    }

    public static Map<String, String> getMeta(Member member) {
        final Map<String, String> meta = new HashMap<>();

        putIfNotNull(meta, META_MEMBER_ADDRESS_HOST, member.address().host());
        putIfNotNull(meta, META_MEMBER_ADDRESS_PORT, Integer.toString(member.address().port()));
        putIfNotNull(meta, META_MEMBER_HOST, member.host());
        putIfNotNull(meta, META_MEMBER_RACK, member.rack());
        putIfNotNull(meta, META_MEMBER_ZONE, member.zone());
        putIfNotNull(meta, META_MEMBER_ID, member.id().id());

        member.properties().stringPropertyNames().forEach(
                name -> putIfNotNull(meta, name, member.properties().getProperty(name))
        );

        return meta;
    }

    public static Optional<String> getClusterId(Member member) {
        return Optional.ofNullable(
                member.properties().getProperty(META_MEMBER_CLUSTER_ID)
        );
    }

    public static <K, V> void putIfNotNull(Map<K, V> map, K key, V val) {
        if (map != null && key != null && val != null) {
            map.put(key, val);
        }
    }
}

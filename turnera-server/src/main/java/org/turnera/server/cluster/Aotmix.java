/*
 * Alipay.com Inc.
 * Copyright (c) 2004-2017 All Rights Reserved.
 */
package org.turnera.server.cluster;


import io.atomix.cluster.Member;
import io.atomix.cluster.Node;
import io.atomix.cluster.discovery.BootstrapDiscoveryProvider;
import io.atomix.core.Atomix;
import io.atomix.protocols.raft.partition.RaftPartitionGroup;

import java.util.Collection;
import java.util.Set;

/**
 * 集群选举
 *
 * @author gongzuo.zy
 * @version $Id: Study.java, v0.1 2017-04-21 16:42  gongzuo.zy Exp $
 */
public class Aotmix {

    public static void main(String[] args) {

            Atomix atomix = Atomix.builder()
                    .withMemberId("member3")
                    .withHost("localhost")
                    .withPort(5679)
                    .withMembershipProvider(BootstrapDiscoveryProvider.builder()
                            .withNodes(
                                    Node.builder()
                                            .withId("member1")
                                            .withHost("localhost")
                                            .withPort(5677)
                                            .build(),
                                    Node.builder()
                                            .withId("member2")
                                            .withHost("localhost")
                                            .withPort(5678)
                                            .build(),
                                    Node.builder()
                                            .withId("member3")
                                            .withHost("localhost")
                                            .withPort(5679)
                                            .build())
                            .build())
                    .withManagementGroup(RaftPartitionGroup.builder("system")
                            .withNumPartitions(1)
                            .withMembers("member1", "member2", "member3")
                            .build())
                    .withPartitionGroups(RaftPartitionGroup.builder("raft")
                            .withPartitionSize(3)
                            .withNumPartitions(3)
                            .withMembers("member1", "member2", "member3")
                            .build())
                    .build();

            atomix.start().join();
            Set<Member> nodes = atomix.getMembershipService().getMembers();
            atomix.getMembershipService().addListener(event -> {
                switch (event.type()) {
                    case MEMBER_ADDED:
                        System.out.println(event.subject().id() + " joined the cluster");
                        break;
                    case MEMBER_REMOVED:
                        System.out.println(event.subject().id() + " left the cluster");
                        break;
                }
            });

    }
}

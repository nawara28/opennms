/*******************************************************************************
 * This file is part of OpenNMS(R).
 *
 * Copyright (C) 2011-2017 The OpenNMS Group, Inc.
 * OpenNMS(R) is Copyright (C) 1999-2017 The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is a registered trademark of The OpenNMS Group, Inc.
 *
 * OpenNMS(R) is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License,
 * or (at your option) any later version.
 *
 * OpenNMS(R) is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with OpenNMS(R).  If not, see:
 *      http://www.gnu.org/licenses/
 *
 * For more information contact:
 *     OpenNMS(R) Licensing <license@opennms.org>
 *     http://www.opennms.org/
 *     http://www.opennms.com/
 *******************************************************************************/

package org.opennms.jicmp.jna;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assume.assumeTrue;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import java.util.concurrent.TimeUnit;

import org.junit.After;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.opennms.core.utils.InetAddressUtils;

/**
 * NativeSocketTest
 *
 * @author brozow
 */
public class NativeSocketTest {

    private static final Charset UTF_8 = Charset.forName("UTF-8");
    private static final int SERVER_PORT = 7777;

    private static final ExecutorService m_executor = Executors.newCachedThreadPool();

    private InetAddress m_localhostV4 = InetAddressUtils.getLocalHostAddress();
    private InetAddress m_localhostV6 = InetAddressUtils.addr("::1");

    Server m_server;

    @Before
    public void setUp() throws Exception {
        assumeTrue(Boolean.getBoolean("runPingTests"));
        m_server = new Server(SERVER_PORT);
        m_server.start();
        m_server.waitForStart();
    }

    @After
    public void tearDown() throws InterruptedException {
        if (m_server != null) {
            m_server.stop();
        }
    }

    public void printf(final String fmt, final Object... args) {
        System.err.print(String.format(fmt, args));
    }

    @Test
    public void testServer() throws Exception  {
        String[] cmds = new String[] { "echo", "echo2", "quit" };
        DatagramSocket socket = null;
        try {
            socket = new DatagramSocket();

            for(final String cmd : cmds) {
                final DatagramSocket sock = socket;
                final FutureTask<DatagramPacket> task = new FutureTask<DatagramPacket>(new Callable<DatagramPacket>() {
                    @Override public DatagramPacket call() throws Exception {
                        printf("Sending cmd: %s\n", cmd);

                        final byte[] data = cmd.getBytes("UTF-8");
                        final DatagramPacket p = new DatagramPacket(data, data.length, m_localhostV4, 7777);
                        sock.send(p);

                        printf("Receiving...\n");
                        final DatagramPacket r = new DatagramPacket(new byte[128], 128);
                        sock.receive(r);
                        printf("Received\n");

                        return r;
                    }
                });

                m_executor.execute(task);
                final DatagramPacket r = task.get(10, TimeUnit.SECONDS);
                assertNotNull(r);

                final String response = new String(r.getData(), r.getOffset(), r.getLength(), "UTF-8");
                printf("Received Response: %s from %s:%d\n", response, r.getAddress().getHostAddress(), r.getPort());
                assertEquals(cmd, response);
            }

        } finally {
            if (socket != null) socket.close();
        }
    }

    @Test
    public void testNativeV4() throws Exception {
        printf("Testing IPv4(%d)\n", NativeDatagramSocket.PF_INET);
        testNative(NativeDatagramSocket.PF_INET, m_localhostV4);
    }

    @Test
    public void testNativeV6() throws Exception {
        assumeTrue(!Boolean.getBoolean("skipIpv6Tests"));
        printf("Testing IPv6(%d)\n", NativeDatagramSocket.PF_INET6);
        testNative(NativeDatagramSocket.PF_INET6, m_localhostV6);
    }

    private void testNative(final int family, final InetAddress address) throws Exception {
        final String[] cmds = new String[] { "nativeEcho", "nativeEcho2", "quitNative" };
        NativeDatagramSocket socket = null;

        try {
            socket = NativeDatagramSocket.create(family, NativeDatagramSocket.SOCK_DGRAM, NativeDatagramSocket.IPPROTO_UDP);

            for(final String cmd : cmds) {
                final NativeDatagramSocket sock = socket;
                final FutureTask<NativeDatagramPacket> task = new FutureTask<NativeDatagramPacket>(new Callable<NativeDatagramPacket>() {
                    @Override public NativeDatagramPacket call() throws Exception {
                        try {
                            printf("Sending %d cmd: %s\n", family, cmd);
                            final ByteBuffer buf = UTF_8.encode(cmd);
                            final NativeDatagramPacket p = new NativeDatagramPacket(buf, address, 7777);
                            sock.send(p);

                            printf("Receiving %d...\n", family);
                            final NativeDatagramPacket r = new NativeDatagramPacket(128);
                            sock.receive(r);
                            printf("Received %d.\n", family);

                            return r;
                        } catch (final Exception e) {
                            printf("Failed %d cmd: %s\n", family, cmd);
                            e.printStackTrace();
                            throw e;
                        }
                    }

                });

                m_executor.execute(task);
                final NativeDatagramPacket r = task.get(10, TimeUnit.SECONDS);
                assertNotNull(r);

                final String response = UTF_8.decode(r.getContent()).toString();
                printf("Received %d Response: %s from %s:%d\n", family, response, r.getAddress().getHostAddress(), r.getPort());

                assertEquals(cmd, response);
            }

        } finally {
            if (socket != null) socket.close();
        }
    }

    @Test(timeout=10000)
    @Ignore("This is ignored since I haven't found a way to interrupt a socket blocked on recvfrom in linux")
    public void testCloseInReceive() throws Exception {
        NativeDatagramSocket socket = null;
        try {
            socket = NativeDatagramSocket.create(NativeDatagramSocket.PF_INET, NativeDatagramSocket.SOCK_DGRAM, NativeDatagramSocket.IPPROTO_UDP);

            final NativeDatagramSocket sock = socket;
            final FutureTask<NativeDatagramPacket> task = new FutureTask<NativeDatagramPacket>(new Callable<NativeDatagramPacket>() {
                @Override public NativeDatagramPacket call() throws Exception {
                    final ByteBuffer buf = UTF_8.encode("msg1");
                    final NativeDatagramPacket p = new NativeDatagramPacket(buf, m_localhostV4, 7777);
                    sock.send(p);

                    final NativeDatagramPacket r = new NativeDatagramPacket(128);
                    printf("Receiving...\n");
                    sock.receive(r);
                    printf("Received\n");
                    return r;
                }
            });

            m_executor.execute(task);
            final NativeDatagramPacket r = task.get(10, TimeUnit.SECONDS);
            assertNotNull(r);

            final String response = UTF_8.decode(r.getContent()).toString();
            printf("Received Response: %s from %s:%d\n", response, r.getAddress().getHostAddress(), r.getPort());
        } finally {
            if (socket != null) socket.close();
        }
    }

}

package com.dqm.msg.common;

import com.dqm.utils.UInt32;
import lombok.Getter;
import lombok.Setter;

/**
 * Created by dqm on 2018/8/25.
 */
@Setter
@Getter
public class Msg {
    private long magic;//uint32

    private String command;//ASCII码，12字节

    private long length;//uint32_t，payload的长度

    private long checksum;//uint32【version和verack消息不包含checksum】

    private byte[] payload;//实际数据

    public enum NETWORK {
        MAIN(new byte[]{(byte)0xF9, (byte)0xBE, (byte)0xB4, (byte)0xD9}),
        TEST(new byte[]{(byte)0xFA, (byte)0xBF, (byte)0xB5, (byte)0xDA});
        NETWORK(byte[] type) {
            this.type = type;
        }
        private byte[] type;

        public long getType() {
            return UInt32.toUnsignedInt(type);
        }
    }
}

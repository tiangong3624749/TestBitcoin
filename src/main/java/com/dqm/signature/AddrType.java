package com.dqm.signature;

/**
 * Created by dqm on 2018/9/18.
 */
public enum AddrType {

    BitcoinPubkey("00"), BitcoinScriptHash("05"), BitcoinestnetPubkeyHash("111"),
    BitcoinPrivateKeyncompressed ("128"), BitcoinPrivateKeyCompressed ("128"), TestnetScriptHash("196"),
    TestnetPrivatekeyUncompressed("239"), TestnetPrivatekeyCompressed("239");

    AddrType(String type) {
        this.type = type;
    }

    private String type;

    public String getType() {
        return this.type;
    }
}

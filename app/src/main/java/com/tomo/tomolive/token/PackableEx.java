package com.tomo.tomolive.token;

public interface PackableEx extends Packable {
    void unmarshal(ByteBuf in);
}

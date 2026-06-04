package com.tcalik;

import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class FirstTest {

    @Test
    void shouldPass() {
        assertThat(2 + 2).isEqualTo(4);
    }
}
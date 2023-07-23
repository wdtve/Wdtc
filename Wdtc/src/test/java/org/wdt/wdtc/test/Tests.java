package org.wdt.wdtc.test;

import javafx.scene.Node;
import javafx.scene.control.Button;
import org.junit.jupiter.api.Test;

public class Tests {
    @Test
    public void testgetErrorWin() {
        Button jl = new Button();
        node(jl);
    }

    public void node(Node node) {
        System.out.println(node instanceof Button);
    }
}

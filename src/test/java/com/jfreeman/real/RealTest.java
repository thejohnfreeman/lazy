package com.jfreeman.real;

import static junit.framework.TestCase.assertEquals;

import org.junit.Before;
import org.junit.Test;

import com.jfreeman.real.pass.LAttributedValuePass;
import com.jfreeman.real.syntax.ConsProduction;
import com.jfreeman.real.syntax.DigitNode;
import com.jfreeman.real.syntax.FloatingPointProduction;
import com.jfreeman.real.syntax.SingletonProduction;
import com.jfreeman.real.syntax.StartNode;

public class RealTest
{
    StartNode _root;

    @Before
    public void before() {
        _root = new FloatingPointProduction(
            new ConsProduction(
                new ConsProduction(
                    new SingletonProduction(
                        new DigitNode(1)
                    ),
                    new DigitNode(0)
                ),
                new DigitNode(1)
            ),
            new ConsProduction(
                new ConsProduction(
                    new SingletonProduction(
                        new DigitNode(1)
                    ),
                    new DigitNode(0)
                ),
                new DigitNode(1)
            )
        );

    }

    @Test
    public void testString() {
        assertEquals("101.101", _root.toString());
    }

    @Test
    public void testValue() {
        assertEquals(5.625, LAttributedValuePass.evaluate(_root));
    }
}

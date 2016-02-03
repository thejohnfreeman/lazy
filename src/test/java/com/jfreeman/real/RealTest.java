package com.jfreeman.real;

import org.junit.Before;
import org.junit.Test;

import static junit.framework.TestCase.assertEquals;

import com.jfreeman.real.pass.LAttributedValuePass;
import com.jfreeman.real.syntax.*;

/**
 * @author jfreeman
 */
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

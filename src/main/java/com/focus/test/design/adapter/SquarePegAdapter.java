package com.focus.test.design.adapter;


/**
 * Adapter allows fitting square pegs into round holes.
 */
//适配器类让你能够将方钉放入圆孔中。它会对 RoundPeg 类进行扩展，以接收适
//配器对象作为圆钉。
public class SquarePegAdapter extends RoundPeg {
    private SquarePeg peg;

    public SquarePegAdapter(SquarePeg peg) {
        this.peg = peg;
    }

    @Override
    public double getRadius() {
        double result;
        // Calculate a minimum circle radius, which can fit this peg.
        result = (Math.sqrt(Math.pow((peg.getWidth() / 2), 2) * 2));
        return result;
    }
}
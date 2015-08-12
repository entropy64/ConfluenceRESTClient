package com.softwareleaf.confluence.util;

/**
 * An immutable tuple structure, that can hold two
 * different types.
 *
 * @author Jonathon Hope
 * @since 7/07/2015
 */
public class Pair<U, V>
{
    public final U p1;
    public final V p2;

    public Pair( final U p1, final V p2 )
    {
        this.p1 = p1;
        this.p2 = p2;
    }
}

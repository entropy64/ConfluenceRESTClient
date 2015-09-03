package com.softwareleaf.confluence.rest.util;

import java.util.*;

/**
 * A generic recursive, immutable Tree structure, used to implement a
 * {@code "n-ary"} Tree. Contains a {@link #isVisited()} query method
 * useful for depth first search and breadth first search algorithms.
 *
 * @author Jonathon Hope
 * @since 8/07/2015
 */
public class Tree<E>
{

    /**
     * The element stored in this Tree.
     */
    private E myElement;
    /**
     * The child elements of this node.
     */
    private List<Tree<E>> myChildren;
    /**
     * A predicate for this tree node having been visited.
     */
    private boolean myVisited;

    /**
     * Constructor.
     *
     * @param element  the element stored in this {@code Tree}
     * @param children the child nodes of this {@code Tree}
     */
    public Tree( E element, List<Tree<E>> children )
    {
        myVisited = false;
        myElement = element;
        myChildren = children;
    }

    /**
     * Construct a Tree using a TreeBuilder.
     *
     * @param treeBuilder the {@code TreeBuilder} instance to use.
     */
    protected Tree( TreeBuilder<E> treeBuilder )
    {
        myVisited = false;
        myElement = treeBuilder.myRootElement;
        final Map<E, List<Tree<E>>> locator = treeBuilder.myLocator;
        myChildren = locator.get( treeBuilder.myRootElement );

        // add all children that would otherwise not be reached from the parent.
        List<Tree<E>> visitedChildren = myChildren;
        while ( visitedChildren != null )
        {
            for ( Tree<E> subTree : visitedChildren )
            {
                final E root = subTree.myElement;
                final List<Tree<E>> children = locator.get( root );
                subTree.myChildren = children;
                visitedChildren = children;
            }
        }
    }

    /**
     * Attempts to find a {@code Tree<E>} with the {@param parent} element,
     * and subsequently return the children of that {@code Tree<E>}.
     *
     * @param parent the parent node to find.
     * @return the list of children or {@code null}
     */
    public List<Tree<E>> findChildrenOf( E parent )
    {
        List<Tree<E>> visitedChildren = myChildren;
        while ( visitedChildren != null )
        {
            for ( Tree<E> subTree : visitedChildren )
            {
                final E root = subTree.myElement;
                if ( root.equals( parent ) )
                {
                    return subTree.myChildren;
                }
                visitedChildren = subTree.myChildren;
            }
        }
        return Collections.emptyList();
    }

    /**
     * Fetch the element stored in this node.
     *
     * @return the element stored in this node.
     */
    public E getElement()
    {
        return myElement;
    }

    /**
     * @return {@code true} if this {@code Tree} has been
     * visited or not.
     */
    public boolean isVisited()
    {
        return myVisited;
    }

    /**
     * Mark or un-mark this node as visited.
     *
     * @param isVisited the predicate for being visited.
     */
    public void setVisited( boolean isVisited )
    {
        myVisited = isVisited;
    }

    /**
     * @return the children of this {@code Tree}
     */
    public List<Tree<E>> getChildren()
    {
        return myChildren;
    }

    /**
     * Construct a Tree by builder pattern.
     *
     * @param <E>         the type of the resulting Tree.
     * @param rootElement the root element of the Tree.
     * @return the TreeBuilder instance for building the Tree.
     */
    public static <E> TreeBuilder<E> builder( E rootElement )
    {
        return new TreeBuilder<>( rootElement );
    }

    /**
     * The builder of a {@code Tree}. This enables us to make
     * {@code Tree} instances immutable.
     */
    public static class TreeBuilder<T>
    {
        /**
         * Used to track the root element.
         */
        private T myRootElement;
        /**
         * We use a map to to locate {@literal "parent"} {@code Tree}. We rely on
         * the {@code <T>} type overriding {@link Object#hashCode()}.
         */
        private Map<T, List<Tree<T>>> myLocator;

        /**
         * Constructor.
         *
         * @param rootElement the root element to establish a guarantee
         *                    that the tree will not be empty.
         */
        public TreeBuilder( T rootElement )
        {
            if ( rootElement == null )
            {
                throw new NullPointerException( "Root element of the tree cannot be null." );
            }
            myRootElement = rootElement;
            myLocator = new HashMap<>();
            myLocator.put( rootElement, new ArrayList<>() );
        }

        /**
         * Adds a child in the {@code Tree}, given the {@param parent}.
         *
         * @param parent the parent element.
         * @param child  the child of the parent.
         * @return {@code this}.
         */
        public TreeBuilder<T> addChild( T parent, T child )
        {
            if ( Objects.equals( parent, child ) )
            {
                throw new IllegalArgumentException( "A child element cannot be equal to its parent." );
            }
            List<Tree<T>> children = myLocator.get( parent );
            if ( children == null )
            {
                children = new ArrayList<>();
            }
            children.add( new Tree<>( child, null ) );
            myLocator.put( parent, children );
            return this;
        }

        /**
         * Adds all children given in a list, to a {@code Tree}, given the {@param parent}.
         *
         * @param parent   the parent element.
         * @param children the intended list of children of the parent.
         * @return {@code this}.
         */
        public TreeBuilder<T> addAllChildren( T parent, List<T> children )
        {
            children.stream().forEachOrdered( child -> addChild( parent, child ) );
            return this;
        }

        /**
         * @return a new {@code Tree} instance with all the
         * elements added by this {@code Builder}.
         */
        public Tree<T> build()
        {
            return new Tree<>( this );
        }

    }

}

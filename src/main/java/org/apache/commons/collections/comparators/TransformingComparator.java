/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.apache.commons.collections.comparators;

import java.io.Serializable;
import java.util.Comparator;

import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.Transformer;

/**
 * Decorates another Comparator with transformation behavior. That is, the
 * return value from the transform operation will be passed to the decorated
 * {@link Comparator#compare(Object,Object) compare} method.
 * <p>
 * This class is Serializable from Commons Collections 4.0.
 *
 * @since 2.1
 * @version $Id$
 *
 * @see org.apache.commons.collections.Transformer
 * @see org.apache.commons.collections.comparators.ComparableComparator
 */
public class TransformingComparator<E> implements Comparator<E>, Serializable {
    
    /** Serialization version from Collections 4.0. */
    private static final long serialVersionUID = 3456940356043606220L;

    /** The decorated comparator. */
    protected final Comparator<E> decorated;
    /** The transformer being used. */    
    protected final Transformer<? super E, ? extends E> transformer;

    //-----------------------------------------------------------------------
    /**
     * Constructs an instance with the given Transformer and a 
     * {@link ComparableComparator ComparableComparator}.
     * 
     * @param transformer what will transform the arguments to <code>compare</code>
     */
    @SuppressWarnings("unchecked")
    public TransformingComparator(final Transformer<? super E, ? extends E> transformer) {
        this(transformer, ComparatorUtils.NATURAL_COMPARATOR);
    }

    /**
     * Constructs an instance with the given Transformer and Comparator.
     * 
     * @param transformer  what will transform the arguments to <code>compare</code>
     * @param decorated  the decorated Comparator
     */
    public TransformingComparator(final Transformer<? super E, ? extends E> transformer, final Comparator<E> decorated) {
        this.decorated = decorated;
        this.transformer = transformer;
    }

    //-----------------------------------------------------------------------
    /**
     * Returns the result of comparing the values from the transform operation.
     * 
     * @param obj1  the first object to transform then compare
     * @param obj2  the second object to transform then compare
     * @return negative if obj1 is less, positive if greater, zero if equal
     */
    public int compare(final E obj1, final E obj2) {
        final E value1 = this.transformer.transform(obj1);
        final E value2 = this.transformer.transform(obj2);
        return this.decorated.compare(value1, value2);
    }

    //-----------------------------------------------------------------------
    /**
     * Implement a hash code for this comparator that is consistent with
     * {@link #equals(Object) equals}.
     *
     * @return a hash code for this comparator.
     */
    @Override
    public int hashCode() {
        int total = 17;
        total = total*37 + (decorated == null ? 0 : decorated.hashCode());
        total = total*37 + (transformer == null ? 0 : transformer.hashCode());
        return total;
    }

    /**
     * Returns <code>true</code> iff <i>that</i> Object is 
     * is a {@link Comparator} whose ordering is known to be 
     * equivalent to mine.
     * <p>
     * This implementation returns <code>true</code>
     * iff <code><i>that</i></code> is a {@link TransformingComparator} 
     * whose attributes are equal to mine.
     * 
     * @param object  the object to compare to
     * @return true if equal
     */
    @Override
    public boolean equals(final Object object) {
        if (this == object) {
            return true;
        }
        if (null == object) {
            return false;
        }
        if (object.getClass().equals(this.getClass())) {
            final TransformingComparator<?> comp = (TransformingComparator<?>) object;
            return null == decorated ? null == comp.decorated : decorated.equals(comp.decorated) &&
                    null == transformer ? null == comp.transformer : transformer.equals(comp.transformer);
        }
        return false;
    }

}


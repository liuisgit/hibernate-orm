/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2012, Red Hat Inc. or third-party contributors as
 * indicated by the @author tags or express copyright attribution
 * statements applied by the authors.  All third-party contributions are
 * distributed under license by Red Hat Inc.
 *
 * This copyrighted material is made available to anyone wishing to use, modify,
 * copy, or redistribute it subject to the terms and conditions of the GNU
 * Lesser General Public License, as published by the Free Software Foundation.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of MERCHANTABILITY
 * or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with this distribution; if not, write to:
 * Free Software Foundation, Inc.
 * 51 Franklin Street, Fifth Floor
 * Boston, MA  02110-1301  USA
 */
package org.hibernate.metamodel.spi.source;

import org.hibernate.mapping.PropertyGeneration;
import org.hibernate.metamodel.spi.binding.SingularAttributeBinding;

/**
 * Source-agnostic description of information needed to bind a singular attribute.
 *
 * @author Steve Ebersole
 */
public interface SingularAttributeSource extends AttributeSource, RelationalValueSourceContainer {
	/**
	 * Determine whether this is a virtual attribute or whether it physically exists on the users domain model.
	 *
	 * @return {@code true} indicates the attribute is virtual, meaning it does NOT exist on the domain model;
	 *         {@code false} indicates the attribute physically exists.
	 */
	public boolean isVirtualAttribute();

	/**
	 * Obtain the nature of this attribute type.
	 *
	 * @return The attribute type nature
	 */
	public Nature getNature();

	/**
	 * Obtain a description of if/when the attribute value is generated by the database.
	 *
	 * @return The attribute value generation information
	 */
	public PropertyGeneration getGeneration();

	/**
	 * Should the attribute be (bytecode enhancement) lazily loaded?
	 *
	 * @return {@code true} to indicate the attribute should be lazily loaded.
	 */
	public boolean isLazy();

	/**
	 * Retrieve the natural id mutability
	 *
	 * @return The mutability, see enum for meanings
	 */
	public SingularAttributeBinding.NaturalIdMutability getNaturalIdMutability();

	/**
	 * Describes the understood natures of a singular attribute.
	 *
	 * @author Steve Ebersole
	 */
	enum Nature {
		BASIC,
		COMPONENT,
		MANY_TO_ONE,
		ONE_TO_ONE,
		ANY
	}
}

/*
 * Hibernate, Relational Persistence for Idiomatic Java
 *
 * Copyright (c) 2010, Red Hat Inc. or third-party contributors as
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
package org.hibernate.metamodel.spi.binding;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import org.hibernate.AssertionFailure;
import org.hibernate.FetchMode;
import org.hibernate.engine.FetchStyle;
import org.hibernate.engine.FetchTiming;
import org.hibernate.internal.FilterConfiguration;
import org.hibernate.metamodel.spi.domain.PluralAttribute;
import org.hibernate.metamodel.spi.source.MetaAttributeContext;
import org.hibernate.persister.collection.CollectionPersister;

/**
 * TODO : javadoc
 *
 * @author Steve Ebersole
 */
public abstract class AbstractPluralAttributeBinding extends AbstractAttributeBinding implements PluralAttributeBinding {
	private final PluralAttributeKeyBinding pluralAttributeKeyBinding;
	private final AbstractPluralAttributeElementBinding pluralAttributeElementBinding;

	private FetchTiming fetchTiming;
	private FetchStyle fetchStyle;

	private int batchSize = -1;

	private Caching caching;

	private boolean mutable = true;

	private Class<? extends CollectionPersister> explicitPersisterClass;

	private String where;
	private String orderBy;
	private boolean sorted;
	private Comparator< ? > comparator;

	private String customLoaderName;
	private CustomSQL customSqlInsert;
	private CustomSQL customSqlUpdate;
	private CustomSQL customSqlDelete;
	private CustomSQL customSqlDeleteAll;

	private String referencedPropertyName;

	private List<FilterConfiguration> filterConfigurations = new ArrayList<FilterConfiguration>();

	//	private final java.util.Set<String> synchronizedTables = new HashSet<String>();

	protected AbstractPluralAttributeBinding(
			AttributeBindingContainer container,
			PluralAttribute attribute,
			PluralAttributeElementBinding.Nature pluralAttributeElementNature,
			SingularAttributeBinding referencedAttributeBinding,
			String propertyAccessorName,
			boolean includedInOptimisticLocking,
			MetaAttributeContext metaAttributeContext) {
		super(
				container,
				attribute,
				propertyAccessorName,
				includedInOptimisticLocking,
				metaAttributeContext
		);
		this.pluralAttributeKeyBinding = new PluralAttributeKeyBinding( this, referencedAttributeBinding );
		this.pluralAttributeElementBinding = interpretNature( pluralAttributeElementNature );
		this.referencedPropertyName = referencedAttributeBinding.getAttribute().getName();
	}

	private AbstractPluralAttributeElementBinding interpretNature(PluralAttributeElementBinding.Nature nature) {
		switch ( nature ) {
			case BASIC: {
				return new BasicPluralAttributeElementBinding( this );
			}
			case COMPOSITE: {
				return new CompositePluralAttributeElementBinding( this );
			}
			case ONE_TO_MANY: {
				return new OneToManyPluralAttributeElementBinding( this );
			}
			case MANY_TO_MANY: {
				return new ManyToManyPluralAttributeElementBinding( this );
			}
			case MANY_TO_ANY: {
				return new ManyToAnyPluralAttributeElementBinding( this );
			}
			default: {
				throw new AssertionFailure( "Unknown collection element nature : " + nature );
			}
		}
	}

//	protected void initializeBinding(PluralAttributeBindingState state) {
//		super.initialize( state );
//		fetchMode = state.getFetchMode();
//		extraLazy = state.isExtraLazy();
//		pluralAttributeElementBinding.setNodeName( state.getElementNodeName() );
//		pluralAttributeElementBinding.setTypeName( state.getElementTypeName() );
//		mutable = state.isMutable();
//		subselectLoadable = state.isSubselectLoadable();
//		if ( isSubselectLoadable() ) {
//			getEntityBinding().setSubselectLoadableCollections( true );
//		}
//		cacheConcurrencyStrategy = state.getCacheConcurrencyStrategy();
//		cacheRegionName = state.getCacheRegionName();
//		orderBy = state.getOrderBy();
//		where = state.getWhere();
//		referencedPropertyName = state.getReferencedPropertyName();
//		sorted = state.isSorted();
//		comparator = state.getComparator();
//		comparatorClassName = state.getComparatorClassName();
//		orphanDelete = state.isOrphanDelete();
//		batchSize = state.getBatchSize();
//		embedded = state.isEmbedded();
//		optimisticLocked = state.isOptimisticLocked();
//		collectionPersisterClass = state.getCollectionPersisterClass();
//		filters.putAll( state.getFilters() );
//		synchronizedTables.addAll( state.getSynchronizedTables() );
//		customSQLInsert = state.getCustomSQLInsert();
//		customSQLUpdate = state.getCustomSQLUpdate();
//		customSQLDelete = state.getCustomSQLDelete();
//		customSQLDeleteAll = state.getCustomSQLDeleteAll();
//		loaderName = state.getLoaderName();
//	}

	@Override
	public PluralAttribute getAttribute() {
		return (PluralAttribute) super.getAttribute();
	}

	@Override
	public boolean isAssociation() {
		return pluralAttributeElementBinding.getNature().isAssociation();
	}

	@Override
	public PluralAttributeKeyBinding getPluralAttributeKeyBinding() {
		return pluralAttributeKeyBinding;
	}

	@Override
	public AbstractPluralAttributeElementBinding getPluralAttributeElementBinding() {
		return pluralAttributeElementBinding;
	}

	@Override
	public FetchTiming getFetchTiming() {
		return fetchTiming;
	}

	@Override
	public void setFetchTiming(FetchTiming fetchTiming) {
		this.fetchTiming = fetchTiming;
	}

	@Override
	public FetchStyle getFetchStyle() {
		return fetchStyle;
	}

	@Override
	public void setFetchStyle(FetchStyle fetchStyle) {
		this.fetchStyle = fetchStyle;
	}

	@Override
	public String getCustomLoaderName() {
		return customLoaderName;
	}

	public void setCustomLoaderName(String customLoaderName) {
		this.customLoaderName = customLoaderName;
	}

	@Override
	public CustomSQL getCustomSqlInsert() {
		return customSqlInsert;
	}

	public void setCustomSqlInsert(CustomSQL customSqlInsert) {
		this.customSqlInsert = customSqlInsert;
	}

	@Override
	public CustomSQL getCustomSqlUpdate() {
		return customSqlUpdate;
	}

	public void setCustomSqlUpdate(CustomSQL customSqlUpdate) {
		this.customSqlUpdate = customSqlUpdate;
	}

	@Override
	public CustomSQL getCustomSqlDelete() {
		return customSqlDelete;
	}

	public void setCustomSqlDelete(CustomSQL customSqlDelete) {
		this.customSqlDelete = customSqlDelete;
	}

	@Override
	public CustomSQL getCustomSqlDeleteAll() {
		return customSqlDeleteAll;
	}

	public void setCustomSqlDeleteAll(CustomSQL customSqlDeleteAll) {
		this.customSqlDeleteAll = customSqlDeleteAll;
	}

	@Override
	public Class<? extends CollectionPersister> getExplicitPersisterClass() {
		return explicitPersisterClass;
	}

	public void setExplicitPersisterClass(Class<? extends CollectionPersister> explicitPersisterClass) {
		this.explicitPersisterClass = explicitPersisterClass;
	}

	public Caching getCaching() {
		return caching;
	}

	public void setCaching(Caching caching) {
		this.caching = caching;
	}

	@Override
	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	@Override
	public String getWhere() {
		return where;
	}

	public void setWhere(String where) {
		this.where = where;
	}

	@Override
	public boolean isMutable() {
		return mutable;
	}

	public void setMutable(boolean mutable) {
		this.mutable = mutable;
	}

	@Override
	public int getBatchSize() {
		return batchSize;
	}

	public void setBatchSize(int batchSize) {
		this.batchSize = batchSize;
	}

	@Override
	public String getReferencedPropertyName() {
		return referencedPropertyName;
	}

	@Override
	public boolean isSorted() {
		return sorted;
	}

	public void setSorted(boolean sorted) {
		this.sorted = sorted;
	}

	@Override
	public Comparator< ? > getComparator() {
		return comparator;
	}

	public void setComparator( Comparator< ? > comparator ) {
		this.comparator = comparator;
	}

	public void addFilterConfiguration(FilterConfiguration filterConfiguration) {
		filterConfigurations.add( filterConfiguration );
	}

		@Override
	public List<FilterConfiguration> getFilterConfigurations() {
		return filterConfigurations;
	}

	@Override
	public FetchMode getFetchMode() {
		return getFetchStyle() == FetchStyle.JOIN ? FetchMode.JOIN : FetchMode.SELECT;
	}

	@Override
	public boolean isLazy() {
		return fetchTiming != FetchTiming.IMMEDIATE;
	}

}

/*
 * Copyright 2018 (C) Tom Parker <thpr@users.sourceforge.net>
 * 
 * This library is free software; you can redistribute it and/or modify it under the terms
 * of the GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 2.1 of the License, or (at your option) any later version.
 * 
 * This library is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR A
 * PARTICULAR PURPOSE. See the GNU Lesser General Public License for more details.
 * 
 * You should have received a copy of the GNU Lesser General Public License along with
 * this library; if not, write to the Free Software Foundation, Inc., 59 Temple Place,
 * Suite 330, Boston, MA 02111-1307 USA
 */
package pcgen.spring.scope;

import org.springframework.beans.factory.FactoryBean;
import org.springframework.context.annotation.Scope;

import pcgen.spring.inst.DataSet;

/**
 * Provides the active DataSet within the dataSet scope.
 */
@Scope(scopeName = "dataSet")
public class ActiveDataSet implements FactoryBean<DataSet>
{
	@Override
	public DataSet getObject() throws Exception
	{
		return SpringContextHolder.getInstance().currentDataSet();
	}

	@Override
	public Class<DataSet> getObjectType()
	{
		return DataSet.class;
	}
}

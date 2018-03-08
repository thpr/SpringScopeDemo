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

import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.config.Scope;

import pcgen.spring.inst.DataSet;

/**
 * Implements the DataSetScope.
 */
public class DataSetScope implements Scope
{

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory)
	{
		SpringContextHolder context = SpringContextHolder.getInstance();
		if (!context.isDataSetActive())
		{
			throw new IllegalStateException(
				"Was not in DataSetScope when get was called");
		}
		DataSet dataSet = context.currentDataSet();
		Object scopedObject;
		synchronized (dataSet)
		{
			scopedObject = dataSet.getScopedInfo(name);
			if (scopedObject == null)
			{
				scopedObject = objectFactory.getObject();
				dataSet.setScopedInfo(name, scopedObject);
			}
		}
		return scopedObject;
	}

	@Override
	public Object remove(String name)
	{
		SpringContextHolder context = SpringContextHolder.getInstance();
		if (!context.isDataSetActive())
		{
			throw new IllegalStateException(
				"Was not in DataSetScope when get was called");
		}
		DataSet dataSet = context.currentDataSet();
		synchronized (dataSet)
		{
			return dataSet.removeScopedInfo(name);
		}
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback)
	{
		// TODO Auto-generated method stub
	}

	@Override
	public Object resolveContextualObject(String key)
	{
		return null;
	}

	@Override
	public String getConversationId()
	{
		DataSet dataSet = SpringContextHolder.getInstance().currentDataSet();
		return "DataSet: " + dataSet.getName();
	}
}

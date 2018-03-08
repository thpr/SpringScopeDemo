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
package pcgen.spring.users;

import javax.inject.Named;
import javax.inject.Provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pcgen.spring.inst.DataSet;

/**
 * Implements an object which is a Singleton but can be instantiated when the DataSet
 * Scope is inactive, since this uses a Provider.
 */
@Component
public class DataSetSensitive
{
	private Provider<DataSet> dataSet;

	@Autowired
	@Named("activeDataSet")
	public DataSetSensitive(Provider<DataSet> dataSet)
	{
		this.dataSet = dataSet;
	}

	public String getDataSetName()
	{
		return dataSet.get().getName();
	}

}

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
package pcgen.spring.inst;

import java.util.HashMap;
import java.util.Map;

import org.springframework.context.annotation.Scope;

import pcgen.spring.base.Loadable;
import pcgen.spring.base.ScopeRepository;

/**
 * This is an object representing the GameMode from PCGen.
 */
@Scope(scopeName = "prototype")
public class GameMode implements ScopeRepository, Loadable
{

	/**
	 * The map of objects held within the GameModeScope for this GameMode.
	 * 
	 * Note: We synchronize the use of this in the GameModeScope.
	 */
	private final Map<String, Object> map = new HashMap<>();

	/**
	 * The name of this GameMode.
	 */
	private final String name;

	/**
	 * Constructs a new GameMode with the given name.
	 * 
	 * @param name
	 *            The name of the GameMode
	 */
	public GameMode(String name)
	{
		this.name = name;
	}

	@Override
	public Object getScopedInfo(String key)
	{
		return map.get(key);
	}

	@Override
	public void setScopedInfo(String key, Object value)
	{
		map.put(key, value);
	}

	@Override
	public Object removeScopedInfo(String key)
	{
		return map.remove(key);
	}

	@Override
	public String getName()
	{
		return name;
	}

	/**
	 * Debugging Method
	 */
	public void dump()
	{
		System.err.println("Game Mode: " + name + "-> " + map);
	}

}

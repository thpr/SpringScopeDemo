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

import pcgen.spring.inst.GameMode;

/**
 * Implements the GameModeScope
 */
public class GameModeScope implements Scope
{

	@Override
	public Object get(String name, ObjectFactory<?> objectFactory)
	{
		SpringContextHolder context = SpringContextHolder.getInstance();
		if (!context.isGameModeActive())
		{
			throw new IllegalStateException(
				"Was not in GameModeScope when get was called");
		}
		GameMode gameMode = context.currentGameMode();
		Object scopedObject;
		synchronized (gameMode)
		{
			scopedObject = gameMode.getScopedInfo(name);
			if (scopedObject == null)
			{
				scopedObject = objectFactory.getObject();
				gameMode.setScopedInfo(name, scopedObject);
			}
		}
		return scopedObject;
	}

	@Override
	public Object remove(String name)
	{
		SpringContextHolder context = SpringContextHolder.getInstance();
		if (!context.isGameModeActive())
		{
			throw new IllegalStateException(
				"Was not in GameModeScope when get was called");
		}
		GameMode gameMode = context.currentGameMode();
		synchronized (gameMode)
		{
			return gameMode.removeScopedInfo(name);
		}
	}

	@Override
	public void registerDestructionCallback(String name, Runnable callback)
	{
		/*
		 * Ignore for now, since we do not currently destroy objects in the normal part of
		 * the GameMode life-cycle (and the GameMode scope is never destroyed).
		 */
	}

	@Override
	public Object resolveContextualObject(String key)
	{
		return null;
	}

	@Override
	public String getConversationId()
	{
		GameMode gameMode = SpringContextHolder.getInstance().currentGameMode();
		return "GameMode: " + gameMode.getName();
	}

}

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

import java.util.Optional;

import org.springframework.context.ApplicationContext;

import pcgen.spring.inst.DataSet;
import pcgen.spring.inst.GameMode;

/**
 * The SpringContextHolder is the master modification mechanism to shift the Scope
 * behavior of Spring within PCGen.
 */
public class SpringContextHolder
{

	/**
	 * The ThreadLocal instance of SpringContextHolder.
	 */
	private static ThreadLocal<SpringContextHolder> INSTANCE =
			ThreadLocal.withInitial(SpringContextHolder::new);

	/**
	 * The underlying ApplicationContext for the SpringContextHolder.
	 */
	private static ApplicationContext CONTEXT;

	/**
	 * The active GameMode.
	 */
	private Optional<GameMode> activeGameMode = Optional.empty();

	/**
	 * The active DataSet.
	 */
	private Optional<DataSet> activeDataSet = Optional.empty();

	/**
	 * Enters the given GameMode's scope.
	 */
	public void enterGameMode(GameMode gameMode)
	{
		if (CONTEXT == null)
		{
			throw new IllegalStateException(
				"Context must be identified before SpringContextHolder can enter a GameMode");
		}
		exitDataSet();
		activeGameMode = Optional.of(gameMode);
	}

	/**
	 * Exits the GameMode scope.
	 */
	public void exitGameMode()
	{
		exitDataSet();
		activeGameMode = Optional.empty();
	}

	/**
	 * Returns the current GameMode.
	 * 
	 * Note: This should stay package protected. If you think you need this as a public
	 * method, you are doing it wrong - use dependency injection
	 */
	GameMode currentGameMode()
	{
		return activeGameMode.get();
	}

	/**
	 * Returns true if a GameMode Scope is active.
	 * 
	 * Note: This should stay package protected. If you think you need this as a public
	 * method, you are doing it wrong - use dependency injection
	 */
	boolean isGameModeActive()
	{
		return activeGameMode.isPresent();
	}

	/**
	 * Enters the given DataSet's scope.
	 */
	public void enterDataSet(DataSet dataSet)
	{
		if (!isGameModeActive())
		{
			throw new IllegalStateException(
				"GameMode must be active before SpringContextHolder can enter a DataSet");
		}
		if (CONTEXT == null)
		{
			throw new IllegalStateException(
				"Context must be identified before SpringContextHolder can enter a DataSet");
		}
		activeDataSet = Optional.of(dataSet);
	}

	/**
	 * Exits the DataSet scope.
	 */
	public void exitDataSet()
	{
		activeDataSet = Optional.empty();
	}

	/**
	 * Returns the current DataSet.
	 * 
	 * Note: This should stay package protected. If you think you need this as a public
	 * method, you are doing it wrong - use dependency injection
	 */
	DataSet currentDataSet()
	{
		return activeDataSet.get();
	}

	/**
	 * Returns true if a DataSet Scope is active.
	 * 
	 * Note: This should stay package protected. If you think you need this as a public
	 * method, you are doing it wrong - use dependency injection
	 */
	boolean isDataSetActive()
	{
		return activeDataSet.isPresent();
	}

	/**
	 * Sets the Context for this SpringContextHolder.
	 */
	public static void setContext(ApplicationContext ctx)
	{
		//TODO Do we need to check that ActiveGameMode and ActiveDataSet are mapped?
		CONTEXT = ctx;
	}

	/**
	 * Returns the SpringContextHolder instance for the current thread.
	 * 
	 * @return The SpringContextHolder instance for the current thread
	 */
	public static SpringContextHolder getInstance()
	{
		return INSTANCE.get();
	}
}

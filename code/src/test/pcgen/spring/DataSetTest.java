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
package pcgen.spring;

import org.junit.Test;
import org.springframework.beans.factory.BeanCreationException;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.context.annotation.AnnotationConfigApplicationContext;

import junit.framework.TestCase;
import pcgen.spring.inst.DataSet;
import pcgen.spring.inst.GameMode;
import pcgen.spring.scope.ActiveDataSet;
import pcgen.spring.scope.ActiveGameMode;
import pcgen.spring.scope.DataSetScope;
import pcgen.spring.scope.GameModeScope;
import pcgen.spring.scope.SpringContextHolder;
import pcgen.spring.users.DataSetSensitive;
import pcgen.spring.users.GameModePrototype;
import pcgen.spring.users.GameModeRequired;
import pcgen.spring.users.GameModeSensitive;

public class DataSetTest extends TestCase
{

	private SpringContextHolder context = SpringContextHolder.getInstance();

	private AnnotationConfigApplicationContext ctx;

	@Override
	public void setUp()
	{
		DefaultListableBeanFactory factory = new DefaultListableBeanFactory();
		factory.registerScope("gameMode", new GameModeScope());
		factory.registerScope("dataSet", new DataSetScope());
		ctx = new AnnotationConfigApplicationContext(factory);
		SpringContextHolder.setContext(ctx);
		ctx.register(ActiveGameMode.class);
		ctx.register(ActiveDataSet.class);
		ctx.register(GameMode.class);
		ctx.register(DataSet.class);
		ctx.register(GameModeRequired.class);
		ctx.register(GameModeSensitive.class);
		ctx.register(DataSetSensitive.class);
		ctx.register(GameModePrototype.class);
		ctx.refresh();
	}

	@Override
	public void tearDown()
	{
		context.exitGameMode();
		ctx.close();
	}

	@Test
	public void testGameModeRequired()
	{
		GameMode gameMode = (GameMode) ctx.getBean("gameMode", "MyMode");
		GameMode otherMode = (GameMode) ctx.getBean("gameMode", "MyOtherMode");
		context.enterGameMode(gameMode);
		assertEquals("MyMode", ctx.getBean(GameModeRequired.class).getModeName());
		context.enterGameMode(otherMode);
		assertEquals("MyOtherMode", ctx.getBean(GameModeRequired.class).getModeName());
		context.enterGameMode(gameMode);
		assertEquals("MyMode", ctx.getBean(GameModeRequired.class).getModeName());
		context.exitGameMode();
	}

	@Test
	public void testGameModePrototype()
	{
		GameModePrototype a = ctx.getBean(GameModePrototype.class);
		GameModePrototype b = ctx.getBean(GameModePrototype.class);
		assertTrue(a != b);
		GameMode gameMode = (GameMode) ctx.getBean("gameMode", "MyMode");
		GameMode otherMode = (GameMode) ctx.getBean("gameMode", "MyOtherMode");
		context.enterGameMode(gameMode);
		assertEquals("MyMode", a.getModeName());
		assertEquals("MyMode", b.getModeName());
		context.enterGameMode(otherMode);
		assertEquals("MyOtherMode", a.getModeName());
		assertEquals("MyOtherMode", b.getModeName());
		context.enterGameMode(gameMode);
		assertEquals("MyMode", a.getModeName());
		assertEquals("MyMode", b.getModeName());
		context.exitGameMode();
		try
		{
			a.getModeName();
			fail();
		}
		catch (BeanCreationException e)
		{
			//Expected
		}
		try
		{
			b.getModeName();
			fail();
		}
		catch (BeanCreationException e)
		{
			//Expected
		}
	}

	@Test
	public void testGameModeSensitive()
	{
		GameModeSensitive y = ctx.getBean(GameModeSensitive.class);
		GameModeSensitive z = ctx.getBean(GameModeSensitive.class);
		assertTrue(y == z);
		DataSetSensitive a = ctx.getBean(DataSetSensitive.class);
		GameMode gameMode = (GameMode) ctx.getBean("gameMode", "MyMode");
		DataSet dataSet = (DataSet) ctx.getBean("dataSet", "MyData");
		GameMode otherMode = (GameMode) ctx.getBean("gameMode", "MyOtherMode");
		context.enterGameMode(gameMode);
		assertEquals("MyMode", y.getModeName());
		try
		{
			a.getDataSetName();
			fail("Should fail as we haven't entered a dataset");
		}
		catch (BeanCreationException e)
		{
			//Expected
		}
		context.enterDataSet(dataSet);
		assertEquals("MyData", a.getDataSetName());
		context.enterGameMode(otherMode);
		assertEquals("MyOtherMode", y.getModeName());
		try
		{
			a.getDataSetName();
			fail("Enter different Game Mode should exit data set");
		}
		catch (BeanCreationException e)
		{
			//Expected
		}
		context.enterDataSet(dataSet);
		assertEquals("MyData", a.getDataSetName());
		context.exitDataSet();
		try
		{
			a.getDataSetName();
			fail("Shouldn't work when exited dataset");
		}
		catch (BeanCreationException e)
		{
			//Expected
		}
		context.enterGameMode(gameMode);
		assertEquals("MyMode", y.getModeName());
		context.exitGameMode();
		try
		{
			a.getDataSetName();
			fail("Exit Game Mode should have exited data set as well");
		}
		catch (BeanCreationException e)
		{
			//Expected
		}
		try
		{
			y.getModeName();
			fail("Should fail when game mode was exited");
		}
		catch (BeanCreationException e)
		{
			//Expected
		}
	}

}

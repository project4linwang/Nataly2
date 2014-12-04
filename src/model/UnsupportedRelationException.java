/* JayFX - A Fact Extractor Plug-in for Eclipse
 * Copyright (C) 2006  McGill University (http://www.cs.mcgill.ca/~swevo/jayfx)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * $Revision: 1.4 $
 */

package model;
/**
 * Describes a problem creating a relation not supported 
 * by the model.
 */

public class UnsupportedRelationException extends Exception
{
	/**
	 * Constructor for UnsupportedRelationException.
	 */
	public UnsupportedRelationException()
	{
		super();
	}
	/**
	 * Constructor for UnsupportedRelationException.
	 * @param arg0
	 */
	public UnsupportedRelationException(String arg0)
	{
		super(arg0);
	}
	/**
	 * Constructor for UnsupportedRelationException.
	 * @param arg0
	 * @param arg1
	 */
	public UnsupportedRelationException(String arg0, Throwable arg1)
	{
		super(arg0, arg1);
	}
	/**
	 * Constructor for UnsupportedRelationException.
	 * @param arg0
	 */
	public UnsupportedRelationException(Throwable arg0)
	{
		super(arg0);
	}
}


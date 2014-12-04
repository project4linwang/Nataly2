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
 * Abtract class for the various program elements in the
 * model.  
 */ 
public abstract class AbstractElement implements IElement
{
	private String aId;
	
	/**
	 * Builds an abstract element. 
	 * @param pId The id uniquely identifying the element.
	 * This id consists in the fully-qualified name of a class element,
	 * the field name appended to the fully-qualified named of the 
	 * declaring class for fields, and the name and signature appended
	 * to the fully-qualified name of the declaring class for methods.
	 */
	protected AbstractElement( String pId )
	{
		aId = pId;
	}
	
	/**
	 * This method must be redeclared here for compatibility
	 * with the IElement interface.  Returns the category of the element 
	 * within the general model.
	 * @return An int representing the category of the element.
	 * @see model.ubc.cs.javadb.model.IElement#getCategory()
	 */
	public abstract ICategories getCategory();
	
	/**
	 * This method must be redeclared here for compatibility
	 * with the IElement interface.  Returns the unique (fully qualified)
	 * name of the element.
	 * @return A String representing the fully qualified name of the
	 * element.
	 * @see model.ubc.cs.javadb.model.IElement#getId()
	 */
	public String getId()
	{
		return aId;
	}
	
	/** 
	 * Returns a String representation of the element.
	 * @return The element's ID.
	 */
	public String toString()
	{
		return getId();
	}
	
	/** 
	 * @return The id of this element without the package.
	 */
	public abstract String getShortName();
}


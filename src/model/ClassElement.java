/* JayFX - A Fact Extractor Plug-in for Eclipse
 * Copyright (C) 2006  McGill University (http://www.cs.mcgill.ca/~swevo/jayfx)
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * $Revision: 1.5 $
 */

package model;



/**
 * Represents a class program element.
 */
public class ClassElement extends AbstractElement
{
	/** Initialize a class element with its fully qualified name 
	 * Class elements should only be created by a FlyweightElementFactory.
	 * @param pId The fully qualified name of the class.
	 */
	protected ClassElement(String pId)
	{
		super( pId );
	}
	
	/** Returns the category of this element, which always a class.
	 * @return the keyword "class".
	 */
	public ICategories getCategory()
	{
		return ICategories.CLASS;
	}
	
	/**
	 * @param pObject The object to compare the class to.
	 * @return Whether pObject has the same ID as this element.
	 */
	public boolean equals( Object pObject )
	{
		if( !(pObject instanceof ClassElement))
			return false;
		else
			return getId().equals(((ClassElement)pObject).getId() );
	}
	
	/** 
	 * @return A hash code for this element.
	 */
	public int hashCode()
	{
		return getId().hashCode();
	}
	
	/** 
	 * @return The declaring class of this class.  null is the
	 * element is a top-level class.
	 */
	public ClassElement getDeclaringClass()
	{
		return null;
	}
	
	/** 
	 * @return The name of the package in which this class is defined.
	 */
	public String getPackageName()
	{
		int lIndex = getId().lastIndexOf( "." );
		if( lIndex >= 0 )
			return getId().substring(0, getId().lastIndexOf("."));
		else
			return "";
	}
	
	/**
	 * @return The name of the class without the package prefix.
	 */
	public String getShortName()
	{
		String lPackageName = getPackageName();
		if( lPackageName.length() > 0 )
			return getId().substring( lPackageName.length() +1, getId().length() );
		else
			return getId();
	}

	@Override
	public String getType() {
		// TODO Auto-generated method stub
		return null;
	}
}

